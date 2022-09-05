// https://mvnrepository.com/artifact/io.prometheus/simpleclient_httpserver
libraryDependencies += "io.prometheus" % "simpleclient_httpserver" % "0.15.0"
libraryDependencies += "co.fs2" %% "fs2-core" % "3.2.12"

libraryDependencies += "org.typelevel" %% "cats-effect" % "3.3.14"

val http4sVersion = "1.0.0-M36"
resolvers += Resolver.sonatypeRepo("snapshots")
libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion
)
