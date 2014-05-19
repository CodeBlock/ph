package me.elrod.ph

import monocle.Macro
import scalaz._, Scalaz._

case class Options(
  _headers: List[Header],
  _basicAuth: Option[(String, String)],
  _redirects: Int
)

sealed trait OptionsInstances {
  implicit def OptionsEqual: Equal[Options] = new Equal[Options] {
    def equal(a: Options, b: Options) = a == b
  }
}

object Options extends OptionsInstances {
  def defaultsForAllMethods =
    Options(
      List(Header("User-Agent", Some(NonEmptyList("scala-ph/0.1-pre")))),
      None,
      10)

  /** Set the defaults for a given request type.
    *
    * Typically you can call [[Method#defaultsForAllMethods]] then use its
    * lenses in [[OptionsLenses]] to change bits and pieces.
    */
  def defaults(m: Method) =
    m match {
      // Any default overrides we want to provide as a nicety can go here.
      case _ => defaultsForAllMethods
    }
}

object OptionsLenses {
  val headers = Macro.mkLens[Options, List[Header]]("_headers")
  val basicAuth = Macro.mkLens[Options, Option[(String, String)]]("_basicAuth")
  val redirects = Macro.mkLens[Options, Int]("_redirects")
}
