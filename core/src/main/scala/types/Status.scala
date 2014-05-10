package me.elrod.ph

import scalaz._, Scalaz._

case class HTTPStatus(code: Int, message: String)

object HTTPStatus extends HTTPStatusInstances

sealed trait HTTPStatusInstances {
  implicit def HTTPStatusEqual: Equal[HTTPStatus] =
    new Equal[HTTPStatus] {
      def equal(a: HTTPStatus, b: HTTPStatus) =
        Equal[Int].equal(a.code, b.code) && Equal[String].equal(a.message, b.message)
    }

  implicit def HTTPStatusOrder: Order[HTTPStatus] =
    new Order[HTTPStatus] {
      def order(a: HTTPStatus, b: HTTPStatus) =
        Order[Int].order(a.code, b.code)
    }

  implicit def HTTPStatusShow: Show[HTTPStatus] =
    new Show[HTTPStatus] {
      override def shows(a: HTTPStatus) = s"${a.code} ${a.message}"
    }
}
