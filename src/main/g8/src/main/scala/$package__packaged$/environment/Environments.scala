package $package$.environment

import $package$.configuration.Configuration
import $package$.repository.{ AccountRepository, DbTransactor, Repository, UserRepository }
import zio.ULayer
import zio.clock.Clock
import zio.console.Console
import $package$.service

object Environments {
  type HttpServerEnvironment = Configuration with Clock
  type AppEnvironment = HttpServerEnvironment with UserRepository with AccountRepository


  val httpServerEnvironmentLayer: ULayer[HttpServerEnvironment] =
    Configuration.live ++ Clock.live
 
  val appEnvironmentLayer: ULayer[AppEnvironment] =
    httpServerEnvironmentLayer ++ 
}
