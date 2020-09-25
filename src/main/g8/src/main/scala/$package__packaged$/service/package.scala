package $package$.service

import zio._

object service {
  val live: TaskLayer[HelloService] =
    ZLayer.fromFunction { () =>
      HelloServiceImpl()
    }
}
