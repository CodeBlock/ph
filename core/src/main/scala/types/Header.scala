package me.elrod.ph

import scalaz._, Scalaz._

case class Header(name: String, value: Option[NonEmptyList[String]])

object Header extends HeaderInstances {
  def fromMap(a: Map[String, List[String]]) =
    a.toList.map(x => Header(x._1, x._2.toNel))
}

sealed trait HeaderInstances {
  implicit def HeaderEqual: Equal[Header] =
    new Equal[Header] {
      def equal(a: Header, b: Header) =
        a.name === b.name && a.value === b.value
    }

  implicit def HeaderShow: Show[Header] =
    new Show[Header] {
      override def shows(a: Header) = s"${a.name}: ${a.value}"
    }
}

