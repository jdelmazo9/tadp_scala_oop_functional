object CasiTests extends App {

  //  def isInt(algo: Any): Boolean = {
  //    algo.getClass.isInstanceOf[Int]
  //  }

  val isString: Any => Boolean = {
    algo => algo.isInstanceOf[String]//algo.getClass.equals("String".getClass)
  }


    println("Tests de Parsers individuales")
      println("\tChar: ")
        print("\t\t1. "); println(char('c').parse("chau")) //Success
        print("\t\t2. "); println(char('c').parse("hau"))  //Failure
        print("\t\t3. "); println(char('c').parse(""))  //Failure
      println("\tAnyChar: ")
        print("\t\t1. "); println(anyChar().parse("hola")) //Success
        print("\t\t2. "); println(anyChar().parse("")) //Failure
      println("\tDigit: ")
        print("\t\t1. "); println(digit().parse("12asdf")) //Success
        print("\t\t2. "); println(digit().parse("a123")) //Failure
        print("\t\t3. "); println(digit().parse("327589")) //Success
        print("\t\t4. "); println(digit().parse("4")) //Success
      println("\tString: ")
        print("\t\t1. "); println(string("hola").parse("hola mundo!")) //Success
        print("\t\t2. "); println(string("hola").parse("holgado mundo!")) //Failure
      println("\tInteger: ")
        print("\t\t1. "); println(integer().parse("-")) //Failure
        print("\t\t1. "); println(integer().parse("hau")) //Failure
        print("\t\t2. "); println(integer().parse("123.asd")) //Success
        print("\t\t3. "); println(integer().parse("123-456")) //Success
        print("\t\t4. "); println(integer().parse("-43534543")) //Success
      println("\tDouble: ")
        print("\t\t1. "); println(double().parse("hau")) //Failure
        print("\t\t2. "); println(double().parse("123.asd")) //Success
        print("\t\t3. "); println(double().parse("-4353.4543")) //Success
    println("")
    println("Tests de Parsers Combinators")
      println("\tOR combinator: ")
        print("\t\t1. "); println((char('c') <|> char('o')).parse("cola")) //Parsea con el primero
        print("\t\t2. "); println((char('c') <|> char('h')).parse("hau")) //Parsea con el segundo
        print("\t\t3. "); println(orCombinator(char('c'), char('h')).parse("hau")) //Parsea con el segundo
        print("\t\t4. "); println(orCombinator(char('c'), char('h')).parse("cau")) //Parsea con el primero
        print("\t\t5. "); println(orCombinator(char('c'), char('h')).parse("au")) //Falla
  println("\tConcat combinator: ")
  print("\t\t1. ");
  println((string("hola") <> string("mundo")).parse("holamundo")) //Parsea el primero y el segundo
  print("\t\t1. ");
  println((string("hola") <> string("chau")).parse("holamundo")) //Falla: Parsea el primero y no el segundo
  print("\t\t1. ");
  println((string("caca") <> string("mundo")).parse("holamundo")) //Falla: No parsea el primero
  print("\t\t1. ");
  println((opt(string("caca")) <> string("hola")).parse("holamundo")) //Falla: No parsea el primero

    println("\tRightmost combinator: ")
      print("\t\t1. "); println((string("hola") ~> string("mundo")).parse("holamundo")) //Funciona, devuelve el de la derecha
      print("\t\t2. "); println((string("caca") ~> string("mundo")).parse("holamundo")) //Falla: no parsea el de la izquierda
      print("\t\t3. "); println((string("hola") ~> string("mudo")).parse("holamundo")) //Falla: no parsea el de la derecha
    println("\tLeftmost combinator: ")
      print("\t\t1. "); println((string("hola") <~ string("mundo")).parse("holamundo")) //Funciona, devuelve el de la derecha
      print("\t\t2. "); println((string("caca") <~ string("mundo")).parse("holamundo")) //Falla: no parsea el de la izquierda
      print("\t\t3. "); println((string("hola") <~ string("mudo")).parse("holamundo")) //Falla: no parsea el de la derecha
    println("\tSeparated-by combinator: ")
      print("\t\t1. "); println(sepByCombinator(integer(),char('-')).parse("123-abc"))
      print("\t\t2. "); println(sepByCombinator(integer(),char('-')).parse(""))
      print("\t\t3. "); println(sepByCombinator(integer(),char('-')).parse("123"))
      print("\t\t4. "); println(sepByCombinator(integer(),char('-')).parse("123-456"))
      print("\t\t5. "); println(sepByCombinator(integer(),char('-')).parse("123-456-789"))
      print("\t\t6. "); println(sepByCombinator(integer(),char('-')).parse("123-456-789-000"))
      print("\t\t7. "); println(sepByCombinator(integer(),char('-')).parse("123-456-789-aaa"))
      print("\t\t8. "); println(sepByCombinator(integer(),char('-')).parse("123-456-789-"))
      print("\t\t9. "); println(sepByCombinator(integer(),char('-')).parse("123-456-789--"))
      print("\t\t10. "); println(sepByCombinator(integer(),char('-')).parse("123-456-789-789-95"))
      print("\t\t10. "); println(sepByCombinator(integer(),char('-')).parse("123-456-789-789-95---1--2--0-0-0-0-0-0-"))
    println("\tSatisfies combinator: ")
    print("\t\t1. "); println(satisfies(string("hola"), isString).parse("hola mundo!"))
    print("\t\t2. "); println(satisfies(anyChar(), isString).parse("hola mundo!"))
    print("\t\t3. "); println(satisfies(string("hola"), isString).parse("hol mundo!"))
    println("\tOpt combinator: ")
    print("\t\t1. "); println(opt(satisfies(string("hola"), isString)).parse("hola mundo!"))
    print("\t\t2. "); println(opt(satisfies(anyChar(), isString)).parse("hola mundo!"))
  println("\tMap combinator: ")
  print("\t\t1. ");
  println(mapCombinator(integer(), (x: Int) => x * x).parse("12 mundo!"))
  print("\t\t2. ");
  println(mapCombinator(integer(), (x: Int) => x * x).parse("hola mundo!"))

    println("\tClausura de Kleene combinator: ")
    print("\t\t1. "); println(clausuraDeKleene(anyChar()).parse("12 mundo!"))
    print("\t\t2. "); println(clausuraDeKleene(string("hola")).parse("holaholahola mundo!"))
    print("\t\t3. "); println(clausuraDeKleene(string("hola")).parse("chau mundo!"))
    print("\t\t1. "); println(anyChar().*.parse("12 mundo!"))
    print("\t\t2. "); println(string("hola").*.parse("holaholahola mundo!"))
    print("\t\t3. "); println(string("hola").*.parse("chau mundo!"))

    println("\tClausura de Kleene Positiva combinator: ")
    print("\t\t1. "); println(clausuraDeKleenePositiva(anyChar()).parse("12 mundo!"))
    print("\t\t2. "); println(clausuraDeKleenePositiva(string("hola")).parse("holaholahola mundo!"))
    print("\t\t3. "); println(clausuraDeKleenePositiva(string("hola")).parse("chau mundo!"))
    print("\t\t1. "); println(anyChar().+.parse("12 mundo!"))
    print("\t\t2. "); println(string("hola").+.parse("holaholahola mundo!"))
    print("\t\t3. "); println(string("hola").+.parse("chau mundo!"))


    println(parserPunto.parse("1 @ 2"))

  //    val alphaNum = anyChar()
  //    println((alphaNum.* ).parse("Sergi Roberti"))
  //    println((alphaNum.* <> (char(' ') ~> alphaNum.*)).parse("Sergi Roberti"))
  //  case class Persona(nombre: Any, apellido: Any)
  //  val personaParser = mapCombinator(alphaNum.* <> (char(' ') ~> alphaNum.*),
  //    (resultado: Any) => {resultado match{ case (nombre, apellido) => Persona(nombre, apellido) }})
  //
  //  println(personaParser.parse("Sergi Roberti"))

}
