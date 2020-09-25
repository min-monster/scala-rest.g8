package $package$

import $package$.configuration.Configuration.{ DbConfig, HttpServerConfig }
import zio.Has

package object configuration {
  type Configuration = Has[DbConfig] with Has[HttpServerConfig]
}
