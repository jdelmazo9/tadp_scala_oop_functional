//package tadp

import org.scalatest.funspec.AnyFunSpec
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
    describe("digit:"){
      it("Success digit().parse(\"4\")") {
        assert(digit().parse("4") == Success(Resultado('4',"")))
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
    describe("string:"){
      it("Success string(\"hola\").parse(\"hola mundo!\")") {
        assert(string("hola").parse("hola mundo!") == Success(Resultado("hola"," mundo!")))
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
        assert(integer().parse("-43534543") === Success(Resultado(-43534543,"")))
      }
      it("Success integer().parse(\"123-456\")") {
        assert(integer().parse("123-456") == Success(Resultado(123,"-456")))
      }
      it("Success integer().parse(\"123.asd\")") {
        assert(integer().parse("123.asd") == Success(Resultado(123,".asd")))
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
        assert( double().parse("-4353.4543") == Success(Resultado(-4353.4543,"")))
      }
      it("Success double().parse(\"123.asd\")") {
        assert( double().parse("123.asd") == Success(Resultado(123.0,".asd")))
      }
      it("Failure double().parse(\"hau\")") {
        assert(double().parse("hau").isFailure)
        assertThrows[ParserErrorException[Resultado[Double]]] {
          double().parse("hau").get
        }
      }
    }

  describe("Parser Combinators") {
    describe("<|> combinator:") {
      it("Success (char('c') <|> char('o')).parse(\"cola\")"){
        (char('c') <|> char('o')).parse("cola")
      }
      it("Success "){
        (char('c') <|> char('h')).parse("hau")
      }
      it("Success "){
        orCombinator(char('c'), char('h')).parse("hau")
      }
      it("Success "){
        orCombinator(char('c'), char('h')).parse("cau")
      }
      it("Failure "){
        orCombinator(char('c'), char('h')).parse("au")
      }
//      print("\t\t1. "); println((char('c') <|> char('o')).parse("cola")) //Parsea con el primero
//      print("\t\t2. "); println((char('c') <|> char('h')).parse("hau")) //Parsea con el segundo
//      print("\t\t3. "); println(orCombinator(char('c'), char('h')).parse("hau")) //Parsea con el segundo
//      print("\t\t4. "); println(orCombinator(char('c'), char('h')).parse("cau")) //Parsea con el primero
//      print("\t\t5. "); println(orCombinator(char('c'), char('h')).parse("au")) //Falla
    }
    describe("<> combinator:") {

    }
    describe("~> combinator:") {

    }
    describe("<~ combinator:") {

    }
    describe("separetedBy combinator:") {

    }
    describe("satifies combinator:") {

    }
    describe("opt combinator:") {

    }
    describe("map combinator:") {

    }
    describe("clausura de Kleene *:") {

    }
    describe("clausura de Kleene +:") {

    }





  }

  }







}
