package me.elrod.ph

import java.net.{ HttpURLConnection, URL }
import monocle.syntax.lens._
import purefn.bytestring.ByteString
import purefn.bytestring.io._
import scala.collection.JavaConverters._
import scalaz._, Scalaz._
import scalaz.effect.IO

/**
  * This is a complete mess.
  * Don't look at it.
  * Please.
  *
  * We should implement HTTP ourselves.
  */
object JavaClient extends Client {
  def get(url: String): IO[Response[ByteString]] =
    getWith(Options.defaults(GET))(new URL(url))

  def getWith(o: Options)(url: URL): IO[Response[ByteString]] = {
    def getConnection(url: URL): IO[HttpURLConnection] = IO {
      val u = url.openConnection.asInstanceOf[HttpURLConnection]
      u.setRequestMethod("GET")
      u.setDoOutput(true)
      o._headers.foreach { header =>
        header._value match {
          case None => u.setRequestProperty(header._name, null) // UGH! null.
          case Some(l) => l.foreach(u.addRequestProperty(header._name, _))
        }
      }
      System.setProperty("http.maxRedirects", o._redirects.toString)
      u
    }

    for {
      conn <- getConnection(url)
      contents <- sGetContents(conn.getInputStream)
    } yield (
      Response(
        HTTPStatus(conn.getResponseCode, conn.getResponseMessage),
        Header.fromMap(conn.getHeaderFields.asScala.mapValues(_.asScala.toList).toMap),
        contents))
  }

  def head(url: String): IO[List[Header]] = {
    def getConnection(url: URL): IO[HttpURLConnection] = IO {
      val u = url.openConnection.asInstanceOf[HttpURLConnection]
      u.setRequestMethod("HEAD")
      u
    }

    getConnection(new URL(url)).map(c =>
      Header.fromMap(c.getHeaderFields.asScala.mapValues(_.asScala.toList).toMap))
  }
}
