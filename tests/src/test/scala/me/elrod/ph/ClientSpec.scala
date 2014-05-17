package me.elrod.ph

import me.elrod.ph.{ JavaClient => http }
import me.elrod.ph.ResponseLenses._
import monocle.syntax.lens._
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary._
import org.specs2.scalaz._
import org.specs2.mutable._
import purefn.bytestring.ByteString
import purefn.bytestring.ByteString._
import purefn.bytestring.io._
import purefn.bytestring.syntax._
import scalaz.effect.IO
import scalaz.std.AllInstances._
import scalaz.syntax.compose._
import scalaz.syntax.equal._
import scalaz.syntax.functor._
import scalaz.syntax.id._

class JavaClientSpec extends Specification {
  "JavaClient" should {
    "succesfully perform a GET request (to ByteString)" in {
      val ip: IO[ByteString] =
        (http get "http://ipv4.da.gd/ip?text&strip") ∘ (((_: Response[ByteString]) |-> body) andThen (_.get))
      val unsafe = ip.unsafePerformIO
      unsafe.filter(_.toChar == '.').length must_== 3
    }

    "succesfully perform a HEAD request" in {
      val headers: IO[List[Header]] = http head "http://ipv4.da.gd/"
      val unsafe = headers.unsafePerformIO
      unsafe.length must be_> (1)
    }
  }
}
