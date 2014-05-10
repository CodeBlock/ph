package me.elrod.ph

import java.net.{ HttpURLConnection, URL }
//import javax.net.ssl.HttpsURLConnection
import purefn.bytestring.ByteString
//import purefn.bytestring.ByteString._
import purefn.bytestring.io._
import scala.collection.JavaConverters._
import scalaz._, Scalaz._
import scalaz.effect.IO

object JavaClient extends Client {
  def get(url: String): IO[Response[ByteString]] = {
    def getConnection(url: URL): IO[HttpURLConnection] = IO {
      val u = url.openConnection.asInstanceOf[HttpURLConnection]
      u.setRequestMethod("GET")
      u.setDoOutput(true)
      u
    }

    for {
      conn <- getConnection(new URL(url))
      contents <- sGetContents(conn.getInputStream)
    } yield (
      Response(
        HTTPStatus(conn.getResponseCode, conn.getResponseMessage),
        conn.getHeaderFields.asScala.mapValues(_.asScala.toList).toMap,
        contents))
  }
}
