//package tadp

import jdk.nashorn.internal.runtime.JSType.isString
import org.scalatest.funspec.AnyFunSpec
import parser._
import tadp.{ParserErrorException, Resultado}

import scala.util.{Failure, Success}

//import org.scalatest.{FreeSpec, Matchers}



class ParsersScpec extends AnyFunSpec {

  describe("Parsers Individuales") {
    describe("char:") {
      it("Success char(\'c\').parse(\"chau\")") {
        assert(char('c').parse("chau") == Success(Resultado('c', "hau")))
      }

      it("Failure char(\'c\').parse(\"hau\")") {
        assert(char('c').parse("hau").isFailure)
        assertThrows[ParserErrorException[Resultado[Char]]] {
          char('c').parse("hau").get
        }
      }

      it("Failure char(\'c\').parse(\"\")") {
        assert(char('c').parse("").isFailure)
        assertThrows[ParserErrorException[Resultado[Char]]] {
          char('c').parse("").get
        }
      }
    }
    describe("anyChar:") {
      it("Success anyChar().parse(\"hola\")") {
        assert(anyChar().parse("hola") == Success(Resultado('h', "ola")))
      }
      it("Failure anyChar().parse(\"\")") {
        assert(anyChar().parse("").isFailure)
        assertThrows[ParserErrorException[Resultado[Char]]] {
          anyChar().parse("").get
        }
      }
    }
    describe("digit:") {
      it("Success digit().parse(\"4\")") {
        assert(digit().parse("4") == Success(Resultado('4', "")))
      }
      it("Success digit().parse(\"12asdf\")") {
        assert(digit().parse("12asdf") == Success(Resultado('1', "2asdf")))
      }
      it("Success digit().parse(\"327589\")") {
        assert(digit().parse("327589") == Success(Resultado('3', "27589")))
      }
      it("Failure digit().parse(\"a123\")") {
        assert(digit().parse("a123").isFailure)
        assertThrows[ParserErrorException[Resultado[Char]]] {
          digit().parse("a123").get
        }
      }
    }
    describe("string:") {
      it("Success string(\"hola\").parse(\"hola mundo!\")") {
        assert(string("hola").parse("hola mundo!") == Success(Resultado("hola", " mundo!")))
      }
      it("Failure string(\"hola\").parse(\"holgado mundo!\")") {
        assert(string("hola").parse("holgado mundo!").isFailure)
        assertThrows[ParserErrorException[Resultado[String]]] {
          string("hola").parse("holgado mundo!").get
        }
      }
    }
    describe("integer:") {
      it("Success integer().parse(\"-43534543\")") {
        assert(integer().parse("-43534543") === Success(Resultado(-43534543, "")))
      }
      it("Success integer().parse(\"123-456\")") {
        assert(integer().parse("123-456") == Success(Resultado(123, "-456")))
      }
      it("Success integer().parse(\"123.asd\")") {
        assert(integer().parse("123.asd") == Success(Resultado(123, ".asd")))
      }
      it("Failure integer().parse(\"hau\")") {
        assert(integer().parse("hau").isFailure)
        assertThrows[ParserErrorException[Resultado[Int]]] {
          integer().parse("hau").get
        }
      }
      it("Failure integer().parse(\"-\")") {
        assert(integer().parse("-").isFailure)
        assertThrows[ParserErrorException[Resultado[Int]]] {
          integer().parse("-").get
        }
      }
    }
    describe("double: ") {
      it("Success double().parse(\"-4353.4543\")") {
        assert(double().parse("-4353.4543") == Success(Resultado(-4353.4543, "")))
      }
      it("Success double().parse(\"123.asd\")") {
        assert(double().parse("123.asd") == Success(Resultado(123.0, ".asd")))
      }
      it("Failure double().parse(\"hau\")") {
        assert(double().parse("hau").isFailure)
        assertThrows[ParserErrorException[Resultado[Double]]] {
          double().parse("hau").get
        }
      }
    }
  }
  describe("Parser Combinators") {
    describe("<|> combinator:") {
      it("Success: Parsea el primero (char('c') <|> char('o')).parse(\"cola\")") {
        assert((char('c') <|> char('o')).parse("cola") == Success(Resultado('c',"ola")))
      }
      it("Success: Parsea el segundo  (char('c') <|> char('h')).parse(\"hau\")") {
        assert((char('c') <|> char('h')).parse("hau") == Success(Resultado('h',"au")))
      }
      it("Success: Parsea el segundo orCombinator(char('c'), char('h')).parse(\"hau\")") {
        assert(orCombinator(char('c'), char('h')).parse("hau") == Success(Resultado('h',"au")))
      }
      it("Success: Parsea el primero orCombinator(char('c'), char('h')).parse(\"cau\")") {
        assert(orCombinator(char('c'), char('h')).parse("cau")== Success(Resultado('c',"au")))
      }
      it("Failure ") {
        assert(orCombinator(char('c'), char('h')).parse("au").isFailure)
        assertThrows[ParserErrorException[Resultado[String]]] {
          orCombinator(char('c'), char('h')).parse("au").get
        }
      }
    }
    describe("<> combinator:") {
      it("Success: Parsea el primero y el segundo concatCombinator(string(\"hola\"), string(\"mundo\")).parse(\"holamundo\")") {
        assert(concatCombinator(string("hola"), string("mundo")).parse("holamundo") == Success(Resultado(List("hola","mundo"),"")))
      }
      it("Success: Parsea el primero y el segundo (string(\"hola\") <> string(\"mundo\")).parse(\"holamundo\")") {
        assert((string("hola") <> string("mundo")).parse("holamundo") == Success(Resultado(List("hola","mundo"),"")))
      }
      it("Failure: Parsea el primero y no el segundo (string(\"hola\") <> string(\"chau\")).parse(\"holamundo\")") {
        assert((string("hola") <> string("chau")).parse("holamundo").isFailure)
        assertThrows[ParserErrorException[Resultado[List[String]]]] {
          (string("hola") <> string("chau")).parse("holamundo").get
        }
      }
      it("Failure: No parsea el primero y sí el segundo (string(\"caca\") <> string(\"mundo\")).parse(\"holamundo\")") {
        assert((string("caca") <> string("mundo")).parse("holamundo").isFailure)
        assertThrows[ParserErrorException[Resultado[List[String]]]] {
          (string("caca") <> string("mundo")).parse("holamundo").get
        }
      }
      it("Failure: No parsea ni el primero ni el segudo ") {
        assert((string("nii") <> string("chau")).parse("holamundo").isFailure)
        assertThrows[ParserErrorException[Resultado[List[String]]]] {
          (string("nii") <> string("chau")).parse("holamundo").get
        }
      }
    }
    describe("~> combinator:") {
      it("Success: Parsea todo y devuelve el de la derecha (string(\"hola\") ~> string(\"mundo\")).parse(\"holamundo\")") {
        assert((string("hola") ~> string("mundo")).parse("holamundo") == Success(Resultado("mundo","")))
      }
      it("Failure: No parsea el de la izquierda (string(\"caca\") ~> string(\"mundo\")).parse(\"holamundo\")") {
        assert((string("caca") ~> string("mundo")).parse("holamundo").isFailure)
        assertThrows[ParserErrorException[Resultado[String]]] {
          (string("caca") ~> string("mundo")).parse("holamundo").get
        }
      }
      it("Failure: No parsea el de la derecha") {
        assert((string("hola") ~> string("mudo")).parse("holamundo").isFailure)
        assertThrows[ParserErrorException[Resultado[List[String]]]] {
          (string("hola") ~> string("mudo")).parse("holamundo").get
        }
      }
    }
    describe("<~ combinator:") {
      it("Success: Parsea el primero y el segundo, devuelve el de la izquierda (string(\"hola\") <~ string(\"mundo\")).parse(\"holamundo\")") {
        assert((string("hola") <~ string("mundo")).parse("holamundo") == Success(Resultado("hola","")))
      }
      it("Failure: No parsea el de la izquierda (string(\"caca\") <~ string(\"mundo\")).parse(\"holamundo\")") {
        assert((string("caca") <~ string("mundo")).parse("holamundo").isFailure)
        assertThrows[ParserErrorException[Resultado[String]]] {
          (string("caca") <~ string("mundo")).parse("holamundo").get
        }
      }
      it("Failure: No parsea el de la derecha (string(\"hola\") <~ string(\"mudo\")).parse(\"holamundo\")") {
        assert((string("hola") <~ string("mudo")).parse("holamundo").isFailure)
        assertThrows[ParserErrorException[Resultado[String]]] {
          (string("hola") <~ string("mudo")).parse("holamundo").get
        }
      }
    }
    describe("separetedBy combinator:") {
      it("Success: sepByCombinator(integer(),char('-')).parse(\"123-abc\")") {
        assert(sepByCombinator(integer(),char('-')).parse("123-abc") == Success(Resultado(List(123),"-abc")))
      }
      it("Success: sepByCombinator(integer(),char('-')).parse(\"123\")") {
        assert(sepByCombinator(integer(),char('-')).parse("123") == Success(Resultado(List(123),"")))
      }
      it("Success: sepByCombinator(integer(),char('-')).parse(\"123-456\")") {
        assert(sepByCombinator(integer(),char('-')).parse("123-456") == Success(Resultado(List(123,456),"")))
      }
      it("Success: sepByCombinator(integer(),char('-')).parse(\"123-456-789\")") {
        assert(sepByCombinator(integer(),char('-')).parse("123-456-789") == Success(Resultado(List(123,456,789),"")))
      }
      it("Success: sepByCombinator(integer(),char('-')).parse(\"123-456-789-000\")") {
        assert(sepByCombinator(integer(),char('-')).parse("123-456-789-000") == Success(Resultado(List(123,456,789,0),"")))
      }
      it("Success: sepByCombinator(integer(),char('-')).parse(\"123-456-789-aaa\")") {
        assert(sepByCombinator(integer(),char('-')).parse("123-456-789-aaa") == Success(Resultado(List(123,456,789),"-aaa")))
      }
      it("Success: sepByCombinator(integer(),char('-')).parse(\"123-456-789-\")") {
        assert(sepByCombinator(integer(),char('-')).parse("123-456-789-") == Success(Resultado(List(123,456,789),"-")))
      }
      it("Success: sepByCombinator(integer(),char('-')).parse(\"123-456-789--\")") {
        assert(sepByCombinator(integer(),char('-')).parse("123-456-789--") == Success(Resultado(List(123,456,789),"--")))
      }
      it("Success: sepByCombinator(integer(),char('-')).parse(\"123-456-789-789-95\")") {
        assert(sepByCombinator(integer(),char('-')).parse("123-456-789-789-95") == Success(Resultado(List(123,456,789,789,95),"")))
      }
      it("Success: sepByCombinator(integer(),char('-')).parse(\"123-456-789-789-95---1--2--0-0-0-0-0-0-\")") {
        assert(sepByCombinator(integer(),char('-')).parse("123-456-789-789-95---1--2--0-0-0-0-0-0-") == Success(Resultado(List(123,456,789,789,95,-1),"1--2--0-0-0-0-0-0-")))
      }
      it("Failure: sepByCombinator(integer(),char('-')).parse(\"\")") {
        assert(sepByCombinator(integer(),char('-')).parse("").isFailure)
        assertThrows[ParserErrorException[Resultado[List[String]]]] {
          sepByCombinator(integer(),char('-')).parse("").get
        }
      }
    }
    describe("satifies combinator:") {
      it("Success: satisfies(string(\"hola\"))(isString).parse(\"hola mundo!\")") {
        assert( satisfies(string("hola"))(isString).parse("hola mundo!") == Success(Resultado("hola"," mundo!")))
      }
      it("Success: satisfies(anyChar())(_ == 'h').parse(\"hola mundo!\")") {
        assert( satisfies(anyChar())(_ == 'h').parse("hola mundo!") == Success(Resultado('h',"ola mundo!")))
      }
      it("Failure: satisfies(anyChar())(isString).parse(\"hola mundo!\")  Parsea pero no es un string lo parseado") {
        assert(satisfies(anyChar())(isString).parse("hola mundo!").isFailure)
        assertThrows[ParserErrorException[Resultado[String]]] {
          satisfies(anyChar())(isString).parse("hola mundo!").get
        }
      }
      it("Failure: satisfies(string(\"hola\"))(isString).parse(\"hol mundo!\") No parsea") {
        assert(satisfies(string("hola"))(isString).parse("hol mundo!").isFailure)
        assertThrows[ParserErrorException[Resultado[String]]] {
          satisfies(string("hola"))(isString).parse("hol mundo!").get
        }
      }

    }
    describe("opt combinator:") {
      it("Success: opt(satisfies(string(\"hola\"))(isString)).parse(\"hola mundo!\") cuando parser el parser ingresado") {
        assert( opt(satisfies(string("hola"))(isString)).parse("hola mundo!") == Success(Resultado("hola"," mundo!")))
      }
      it("Success: opt(satisfies(anyChar())(isString)).parse(\"hola mundo!\") cuando no parsea el parser ingresado") {
        assert( opt(satisfies(anyChar())(isString)).parse("hola mundo!") == Success(Resultado(None,"hola mundo!")))
      }
      it("Success: (opt(string(\"caca\")) <> string(\"hola\")).parse(\"holamundo\") cuando no parsea el parser ingresado") {
        assert((opt(string("caca")) <> string("hola")).parse("holamundo") == Success(Resultado(List("hola"),"mundo")))
      }
    }
    describe("map combinator:") {
      it("Success: mapCombinator(integer())( (x: Int) => x * x).parse(\"12 mundo!\")") {
        assert( mapCombinator(integer())( (x: Int) => x * x).parse("12 mundo!") == Success(Resultado(144," mundo!")))
      }

      it("Failure: mapCombinator(integer())((x: Int) => x * x).parse(\"hola mundo!\")") {
        assert(mapCombinator(integer())((x: Int) => x * x).parse("hola mundo!").isFailure)
        assertThrows[ParserErrorException[Resultado[String]]] {
          mapCombinator(integer())((x: Int) => x * x).parse("hola mundo!").get
        }
      }
    }
    describe("clausura de Kleene *:") {
      it("Success: clausuraDeKleene(anyChar()).parse(\"12 mundo!\")") {
        assert(clausuraDeKleene(anyChar()).parse("12 mundo!") == Success(Resultado(List('1','2')," mundo!")))
      }
      it("Success: clausuraDeKleene(string(\"hola\")).parse(\"holaholahola mundo!\")") {
        assert( clausuraDeKleene(string("hola")).parse("holaholahola mundo!") == Success(Resultado(List("hola","hola","hola")," mundo!")))
      }
      it("Success: clausuraDeKleene(string(\"hola\")).parse(\"chau mundo!\")") {
        assert( clausuraDeKleene(string("hola")).parse("chau mundo!") == Success(Resultado(List(),"chau mundo!")))
      }
      it("Success: anyChar().*.parse(\"12 mundo!\")") {
        assert( anyChar().*.parse("12 mundo!") == Success(Resultado(List('1','2')," mundo!")))
      }
      it("Success: string(\"hola\").*.parse(\"holaholahola mundo!\")") {
        assert( string("hola").*.parse("holaholahola mundo!") == Success(Resultado(List("hola","hola","hola")," mundo!")))
      }
      it("Success: string(\"hola\").*.parse(\"chau mundo!\")") {
        assert( string("hola").*.parse("chau mundo!") == Success(Resultado(List(),"chau mundo!")))
      }
    }
    describe("clausura de Kleene +:") {
      it("Success: clausuraDeKleenePositiva(anyChar()).parse(\"12 mundo!\")") {
        assert( clausuraDeKleenePositiva(anyChar()).parse("12 mundo!") == Success(Resultado(List('1','2')," mundo!")))
      }
      it("Success: clausuraDeKleenePositiva(string(\"hola\")).parse(\"holaholahola mundo!\")") {
        assert( clausuraDeKleenePositiva(string("hola")).parse("holaholahola mundo!") == Success(Resultado(List("hola","hola","hola")," mundo!")))
      }
      it("Success: anyChar().+.parse(\"12 mundo!\")") {
        assert(anyChar().+.parse("12 mundo!") == Success(Resultado(List('1','2')," mundo!")))
      }
      it("Success: string(\"hola\").+.parse(\"holaholahola mundo!\")") {
        assert( string("hola").+.parse("holaholahola mundo!") == Success(Resultado(List("hola","hola","hola")," mundo!")))
      }
      it("Failure: clausuraDeKleenePositiva(string(\"hola\")).parse(\"chau mundo!\")") {
        assert(clausuraDeKleenePositiva(string("hola")).parse("chau mundo!").isFailure)
        assertThrows[ParserErrorException[Resultado[(List[String],String)]]] {
          clausuraDeKleenePositiva(string("hola")).parse("chau mundo!").get
        }
      }
      it("Failure: string(\"hola\").+.parse(\"chau mundo!\")") {
        assert(string("hola").+.parse("chau mundo!").isFailure)
        assertThrows[ParserErrorException[Resultado[(List[String],String)]]] {
          string("hola").+.parse("chau mundo!").get
        }
      }
    }
  }
}
