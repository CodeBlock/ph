package me.elrod.ph

import monocle.Macro

case class Response[T](
  _status: HTTPStatus,
  _headers: List[Header],
  _body: T
)

object ResponseLenses {
  def status[T]  = Macro.mkLens[Response[T], HTTPStatus]("_status")
  def headers[T] = Macro.mkLens[Response[T], List[Header]]("_headers")
  def body[T]    = Macro.mkLens[Response[T], T]("_body")
}
