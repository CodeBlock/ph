package me.elrod.ph.examples

import scalaz._
import scalaz.effect._
import IO._
import me.elrod.ph.{JavaClient => http}
import me.elrod.ph.ResponseLenses._
import monocle.syntax.lens._

object DisplayIP extends SafeApp {
  override def runc: IO[Unit] =
    for {
      ip <- http get "http://ipv4.da.gd/ip?text&strip" // Response[ByteString]
      _ <- putStrLn((ip |-> body get).toString)
    } yield ()
}
