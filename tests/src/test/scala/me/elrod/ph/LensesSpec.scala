package me.elrod.ph

import me.elrod.ph.{ JavaClient => http }
import monocle.function.Each._
import monocle.syntax.lens._
import monocle.syntax.traversal._
import org.specs2.mutable._
import purefn.bytestring.ByteString
import scalaz.effect.IO
import scalaz.syntax.functor._

class LensesSpec extends Specification {
  "HeaderLenses" should {
    import me.elrod.ph.HeaderLenses._

    "succesfully give us header names" in {
      val headers =
        List(
          Header("Content-Length", Option(scalaz.NonEmptyList("1024"))),
          Header("Content-Encoding", Option(scalaz.NonEmptyList("UTF-8")))
        )
      (headers |->> each |->> name getAll) must_== List("Content-Length", "Content-Encoding")
    }
  }

  "ResponseLenses" should {
    import me.elrod.ph.ResponseLenses._
    import me.elrod.ph.HTTPStatusLenses._

    "succesfully lets us focus in on parts of a Response[ByteString]" in {
      val r: Response[ByteString] =
        Response(
          HTTPStatus(200, "OK"),
          Map("Content-Encoding" -> List("UTF-8")),
          ByteString.packs("Hello world"))
      (r |-> status |-> code get) must_== 200
      (r |-> status |-> message get) must_== "OK"
      (r |-> body get).toString must_== "Hello world"
    }
  }
}
