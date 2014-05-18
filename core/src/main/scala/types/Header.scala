package me.elrod.ph

import monocle.Macro
import scalaz._, Scalaz._

case class Header(_name: String, _value: Option[NonEmptyList[String]])

object Header extends HeaderInstances {
  def fromMap(a: Map[String, List[String]]) =
    a.toList.filterNot(x => Option(x._1) == None).map(x => Header(x._1, x._2.toNel))
}

sealed trait HeaderInstances {
  import HeaderLenses._
  implicit def HeaderEqual: Equal[Header] =
    new Equal[Header] {
      def equal(a: Header, b: Header) =
        name.get(a) === name.get(b) && value.get(a) === value.get(b)
    }

  implicit def HeaderShow: Show[Header] =
    new Show[Header] {
      override def shows(a: Header) = s"${name.get(a)}: ${value.get(a)}"
    }
}

object HeaderLenses {
  val name = Macro.mkLens[Header, String]("_name")
  val value = Macro.mkLens[Header, Option[NonEmptyList[String]]]("_value")
}
