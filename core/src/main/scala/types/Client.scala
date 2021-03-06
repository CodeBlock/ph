package me.elrod.ph

import purefn.bytestring.ByteString
import scalaz._, Scalaz._
import scalaz.effect.IO

/**
  * This is an effort to make ph be client-agnostic. If it ends badly, so be it.
  * It is just an experiment.
  */
trait Client {
  def get(url: String): IO[Response[ByteString]]
  def head(url: String): IO[List[Header]]
}
