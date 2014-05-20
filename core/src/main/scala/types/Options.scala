package me.elrod.ph

import monocle.Macro
import scalaz._, Scalaz._

case class RequestOptions(
  _headers: List[Header],
  _basicAuth: Option[(String, String)],
  _redirects: Int
)

sealed trait RequestOptionsInstances {
  implicit def RequestOptionsEqual: Equal[RequestOptions] = new Equal[RequestOptions] {
    def equal(a: RequestOptions, b: RequestOptions) = a == b
  }
}

object RequestOptions extends RequestOptionsInstances {
  def defaultsForAllMethods =
    RequestOptions(
      List(Header("User-Agent", Some(NonEmptyList("scala-ph/0.1-pre")))),
      None,
      10)

  /** Set the defaults for a given request type.
    *
    * Typically you can call [[Method#defaultsForAllMethods]] then use its
    * lenses in [[RequestOptionsLenses]] to change bits and pieces.
    */
  def defaults(m: Method) =
    m match {
      // Any default overrides we want to provide as a nicety can go here.
      case _ => defaultsForAllMethods
    }
}

object RequestOptionsLenses {
  val headers = Macro.mkLens[RequestOptions, List[Header]]("_headers")
  val basicAuth = Macro.mkLens[RequestOptions, Option[(String, String)]]("_basicAuth")
  val redirects = Macro.mkLens[RequestOptions, Int]("_redirects")
}
