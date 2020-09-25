val Http4sVersion = "$http4s_version$"
val CirceVersion = "$circe_version$"
val LogbackVersion = "$logback_version$"
val rhoVersion = "$rho_version$"
val zioVersion = "$zio_version$"
val zioInteropCatsVersion = "$zioInteropCats_version$"
val pureConfigVersion = "$purConfig_version$"
val slf4jVersion = "$slf4j_version$"



lazy val root = (project in file("."))
  .settings(
    organization := "$organization$",
    name := "$name;format="norm"$",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "$scala_version$",
    libraryDependencies ++= Seq(
      "org.http4s"      %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"      %% "http4s-blaze-client" % Http4sVersion,
      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,

      "org.http4s" %% "rho-swagger" % rhoVersion,

      "dev.zio" %% "zio-interop-cats" % zioInteropCatsVersion,
      "dev.zio" %% "zio" % zioVersion
      "dev.zio" %% "zio-test" % zioVersion % "test",
      "dev.zio" %% "zio-test-sbt" % zioVersion % "test",
      "dev.zio" %% "zio-test-magnolia" % zioVersion % "test", // optional

      "com.github.pureconfig" %% "pureconfig" % pureConfigVersion,

      "io.circe"        %% "circe-generic"       % CirceVersion,
      "org.slf4j" % "slf4j-log4j12" % slf4jVersion % "runtime"
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1")
  )

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Xfatal-warnings",
)
