package me.elrod.ph

import me.elrod.ph.{ JavaClient => http }
import monocle.function.Each._
import monocle.syntax.lens._
import monocle.syntax.traversal._
import org.specs2.mutable._
import purefn.bytestring.ByteString
import scalaz.effect.IO
import scalaz.syntax.functor._


import me.elrod.ph.ResponseLenses._

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
}
