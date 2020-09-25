package $package$.http

import cats.arrow.Arrow.ops.toAllArrowOps
import cats.data.Kleisli
import cats.effect.{ ExitCode, Timer }
import cats.implicits._
import $package$.configuration.Configuration.HttpServerConfig
import $package$.environment.Environments.AppEnvironment
import $package$.http.endpoint.{ Accounts, Users }
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.Router
import org.http4s.server.middleware.{ AutoSlash, GZip }
import org.http4s.{ HttpRoutes, Request, Response }
import zio.interop.catz._
import zio.{ RIO, ZIO }
import org.http4s.rho.RhoMiddleware
import org.http4s.rho.swagger._
import org.http4s.rho.swagger.models.Info
import $package$.http.endpoint.Users
import org.http4s.server.middleware.CORS
import com.http4s.rho.swagger.ui.SwaggerUi
import cats.effect.IO
import cats.effect.Blocker
import doobie.util.pretty.Block

object Server {
  type ServerRIO[A] = RIO[AppEnvironment, A]
  type ServerRoutes =
    Kleisli[ServerRIO, Request[ServerRIO], Response[ServerRIO]]

  def runServer: ZIO[AppEnvironment, Throwable, Unit] =
    ZIO
      .runtime[AppEnvironment]
      .flatMap { implicit rts =>
        val cfg = rts.environment.get[HttpServerConfig]
        val ec = rts.platform.executor.asEC
        val timer = Timer
        val blocker = Blocker.liftExecutionContext(rts.platform.executor.asEC)

        BlazeServerBuilder[ServerRIO]
          .bindHttp(cfg.port, cfg.host)
          .withHttpApp(createRoutes(cfg.path, blocker))
          .serve
          .compile[ServerRIO, ServerRIO, ExitCode]
          .drain
      }
      .orDie

  def createRoutes(basePath: String, blocker: Blocker): ServerRoutes = {
    val accountRoutes = Accounts.routes

    import org.http4s.rho.swagger.syntax.io._

    // val swaggerUiRhoMiddleware =
    //   SwaggerUi.createRhoMiddleware(blocker, swaggerMetadata = Swagger.metadata)

    val swaggerMiddleWare: RhoMiddleware[ServerRIO] = SwaggerSupport
      .apply[ServerRIO]
      .createRhoMiddleware(swaggerMetadata = Swagger.metadata)

    val swaggerRoutes = Swagger.api.toRoutes(swaggerMiddleWare);

    val routes = accountRoutes <+> swaggerRoutes

    val corsService = CORS(routes)

    Router[ServerRIO](basePath -> corsService).orNotFound
  }

  private val middleware: HttpRoutes[ServerRIO] => HttpRoutes[ServerRIO] = { http: HttpRoutes[ServerRIO] =>
    AutoSlash(http)
  }.andThen { http: HttpRoutes[ServerRIO] =>
    GZip(http)
  }
}
