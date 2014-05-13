package me.elrod.ph

import scalaz._, Scalaz._
import scalaz.effect.IO

sealed trait Method
case object GET extends Method
case object POST extends Method
case object HEAD extends Method
case object PUT extends Method
case object DELETE extends Method
case object TRACE extends Method
case object CONNECT extends Method
case object OPTIONS extends Method
case object PATCH extends Method
case class NonStandardMethod(method: String) extends Method

sealed trait MethodInstances {
  implicit def MethodShow: Show[Method] = new Show[Method] {
    override def shows(m: Method) = m match {
      case GET     => "GET"
      case POST    => "POST"
      case HEAD    => "HEAD"
      case PUT     => "PUT"
      case DELETE  => "DELETE"
      case TRACE   => "TRACE"
      case CONNECT => "CONNECT"
      case OPTIONS => "OPTIONS"
      case PATCH   => "PATCH"
      case NonStandardMethod(m) => m
    }
  }

  implicit def MethodEqual: Equal[Method] = new Equal[Method] {
    def equal(a: Method, b: Method) = a == b
  }
}

object Method extends MethodInstances
