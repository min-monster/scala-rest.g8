package $package$.service

case class $name;format="Camel"$ServiceImpl() extends $name;format="Camel"$Service {
 def hello(n: HelloWorld.Name): F[HelloWorld.Greeting] =
        Greeting("Hello, " + n.name).pure[F]
}
