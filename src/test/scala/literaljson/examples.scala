package literaljson

import org.scalatest._

class ExampleSuite extends FunSuite {
  import JsonAST._
  import JsonDSL._
  import JsonParser._

  test("Lotto example") {
    parse(lotto) match {
      case Right(lottoAST) =>
        val renderedLotto = compact(render(lottoAST))
        expect (lottoAST) { parse(renderedLotto).right.get }
      case Left(err) => fail(err.message)
    }
  }

  test("Person example") {
    parse(person) match {
      case Right(personAST) =>
        val renderedPerson = pretty(render(personAST))
        expect (personAST) { parse(renderedPerson).right.get }
        expect (render(personAST)) { render(personDSL) }
        expect (compact(render(personAST \\ "name"))) { """{"name":"Joe","name":"Marilyn"}""" }
        expect (compact(render(personAST \ "name"))) { """{"name":"Joe"}""" }
      case Left(err) => fail(err.message)
    }
  }

  test("Quoted example") {
    parse(quoted) match {
      case Right(quotedAST) => expect (List("foo \" \n \t \r bar")) { quotedAST.values }
      case Left(err) => fail(err.message)
    }
  }

  val lotto = """
{
  "lotto":{
    "lotto-id":5,
    "winning-numbers":[2,45,34,23,7,5,3],
    "winners":[ {
      "winner-id":23,
      "numbers":[2,45,34,23,3, 5]
    },{
      "winner-id" : 54 ,
      "numbers":[ 52,3, 12,11,18,22 ]
    }]
  }
}
"""

  val person = """
{ 
  "person": {
    "name": "Joe",
    "age": 35,
    "spouse": {
      "person": {
        "name": "Marilyn"
        "age": 33
      }
    }
  }
}
"""

  val personDSL = 
    ( "person" ->
     ("name" -> "Joe") ~
     ("age" -> 35) ~
     ("spouse" -> 
      ("person" -> 
       ("name" -> "Marilyn") ~
       ("age" -> 33)
     )
    )
   )

  val quoted = """["foo \" \n \t \r bar"]"""
}

