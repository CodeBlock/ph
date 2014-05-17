package me.elrod.ph

import monocle.Macro

case class Response[T](
  _status: HTTPStatus,
  _headers: Map[String, List[String]],
  _body: T
)

object ResponseLenses {
  def status[T]  = Macro.mkLens[Response[T], HTTPStatus]("_status")
  def headers[T] = Macro.mkLens[Response[T], Map[String, List[String]]]("_headers")
  def body[T]    = Macro.mkLens[Response[T], T]("_body")
}
