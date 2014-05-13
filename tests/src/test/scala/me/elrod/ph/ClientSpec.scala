package me.elrod.ph

import me.elrod.ph.{ JavaClient => http }
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary._
import org.specs2.scalaz._
import org.specs2.mutable._
import purefn.bytestring.ByteString
import purefn.bytestring.ByteString._
import purefn.bytestring.io._
import purefn.bytestring.syntax._
import scalaz._, Scalaz._
import scalaz.effect.IO

class JavaClientSpec extends Specification {
  "JavaClient" should {
    "succesfully perform a GET request (to ByteString)" in {
      val ip: IO[ByteString] =
        for {
          ip <- http get "http://ipv4.da.gd/ip?text&strip" // Response[ByteString]
        } yield (ip.body) // IO[ByteString]
      ip.unsafePerformIO.filter(_ === '.').length must_== 3
    }

    "succesfully perform a HEAD request" in {
      val headers: IO[List[Header]] = http head "http://ipv4.da.gd/"
      val unsafe = headers.unsafePerformIO
      unsafe.length must be_> 1
    }
  }
}
