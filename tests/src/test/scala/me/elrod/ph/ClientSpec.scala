package me.elrod.ph

import me.elrod.ph.{ JavaClient => http }
import me.elrod.ph._
import me.elrod.ph.ResponseLenses._
import monocle.syntax.lens._
import org.specs2.mutable._
import purefn.bytestring.ByteString
import scalaz._
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
      val headers: IO[List[Header]] = http head "http://google.com/"
      val unsafe = headers.unsafePerformIO
      unsafe.length must be_> (1)
      unsafe.map(_._name) must contain("Content-Type")

      // Make sure the client strips out the beginning "HTTP/1.1 200 OK"
      // which isn't a header and would give Header(null, ...)
      unsafe.map(x => Option(x._name)) must not contain(None)
    }
  }

    "be able to set headers in a GET request" in {
      val o: RequestOptions =
        RequestOptions.defaults(GET) |-> RequestOptionsLenses.headers modify (x =>
          x ::: List(Header("foo", Some(NonEmptyList("bar")))))
      val h: IO[ByteString] =
        (http.getWith(o)(new java.net.URL("http://da.gd/headers?text"))) ∘ (((_: Response[ByteString]) |-> body) andThen (_.get))
      val unsafe: String = h.unsafePerformIO.toString
      unsafe must contain("foo: bar")
      unsafe must contain("User-Agent: scala-ph")
    }
}
