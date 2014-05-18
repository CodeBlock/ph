package me.elrod.ph

import monocle.Macro
import scalaz._, Scalaz._

case class HTTPStatus(_code: Int, _message: String)

object HTTPStatus extends HTTPStatusInstances

sealed trait HTTPStatusInstances {
  import HTTPStatusLenses._

  implicit def HTTPStatusEqual: Equal[HTTPStatus] =
    new Equal[HTTPStatus] {
      def equal(a: HTTPStatus, b: HTTPStatus) =
        code.get(a) === code.get(b) && message.get(a) === message.get(b)
    }

  implicit def HTTPStatusOrder: Order[HTTPStatus] =
    new Order[HTTPStatus] {
      def order(a: HTTPStatus, b: HTTPStatus) =
        Order[Int].order(code.get(a), code.get(b))
    }

  implicit def HTTPStatusShow: Show[HTTPStatus] =
    new Show[HTTPStatus] {
      override def shows(a: HTTPStatus) = s"${code.get(a)} ${message.get(a)}"
    }
}

object HTTPStatusLenses {
  val code = Macro.mkLens[HTTPStatus, Int]("_code")
  val message = Macro.mkLens[HTTPStatus, String]("_message")
}
