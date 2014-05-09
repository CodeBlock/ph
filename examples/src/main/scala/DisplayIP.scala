package me.elrod.ph.examples

import scalaz._, Scalaz._, effect._, IO._
import me.elrod.ph.{Ph => http}

object DisplayIP extends SafeApp {
  override def runc: IO[Unit] =
    for {
      ip <- http get "http://da.gd/ip?text&strip"
      _  <- ip match {
        case \/-(ip_) => putStrLn(ip_.body) // TODO: Lens!
        case -\/(err) => putStrLn(err.toString)
      }
    } yield ()
}
