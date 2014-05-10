package me.elrod.ph

import me.elrod.ph.{ JavaClient => http }
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary._
import org.specs2.scalaz._
import purefn.bytestring.ByteString
import purefn.bytestring.ByteString._
import purefn.bytestring.io._
import purefn.bytestring.syntax._
import scalaz._, Scalaz._
import scalaz.effect.IO

class JavaClientSpec extends Spec {
  "JavaClient" should {
    "succesfully get IP as a bytestring" in {
      val ip: IO[ByteString] =
        for {
          ip <- http get "http://ipv4.da.gd/ip?text&strip" // Response[ByteString]
        } yield (ip.body) // IO[ByteString]
      ip.unsafePerformIO.filter(_ === '.').length must_== (3)
    }
  }
}
