package me.elrod.ph

case class Response[T](
  status: HTTPStatus,
  headers: Map[String, List[String]],
  body: T
)

// TODO: Lenses
