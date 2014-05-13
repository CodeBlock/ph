package me.elrod.ph.examples

import scalaz._, Scalaz._, effect._, IO._
import me.elrod.ph.{JavaClient => http}

object DisplayIP extends SafeApp {
  override def runc: IO[Unit] =
    for {
      ip <- http get "http://ipv4.da.gd/ip?text&strip" // Response[ByteString]
      _ <- putStrLn(ip.toString)
    } yield ()
}
