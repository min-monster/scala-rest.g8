package io.monster.ecomm.account.http

import org.http4s.rho.swagger.SwaggerMetadata
import org.http4s.rho.swagger.models.Info
import io.monster.ecomm.account.http.endpoint.Users

object Swagger {
  val metadata = SwaggerMetadata(apiInfo =
    Info(title = "Employer Account Service", version = "0.0.1", description = Some("This is a PoC service."))
  )

  val api =  HelloService.api.and(Users.api)
}
