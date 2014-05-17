package me.elrod.ph

import me.elrod.ph.{ JavaClient => http }
import me.elrod.ph.ResponseLenses._
import monocle.syntax.lens._
import org.specs2.mutable._
import purefn.bytestring.ByteString
import scalaz.effect.IO
import scalaz.syntax.functor._

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
