package me.elrod.ph

import java.io.{ DataOutputStream, InputStream }
import java.net.{ HttpURLConnection, URL, URLEncoder }
import javax.net.ssl.HttpsURLConnection
import monocle.Macro
import monocle.syntax.lens._
import scala.collection.JavaConverters._
import scala.io.{ Codec, Source }
import scalaz._, Scalaz._
import scalaz.effect.IO

case class Request(
  method: Method,
  secure: Boolean,
  host: String,
  port: Int, // TODO: smart constructor to verify valid port.
  path: String,
  queryString: String,
  headers: Option[Map[String, String]], // TODO
  body: String, // TODO
  proxy: Option[String], // TODO
  hostAddress: Option[String], // TODO
  rawBody: Boolean,
  // decompress // TODO
  redirectCount: Int,
  // checkStatus // TODO
  responseTimeout: Option[Int]
  // cookieJar // TODO
)

sealed case class HTTPStatus(code: Int, message: String)

sealed case class Response[T](
  status: HTTPStatus,
  headers: Map[String, List[String]],
  body: T
)

object ResponseLens {
  val status = Macro.mkLens[Response[String], HTTPStatus]("status")
  val headers = Macro.mkLens[Response[String], Map[String, List[String]]]("headers")
  val body = Macro.mkLens[Response[String], String]("body")

  val code = Macro.mkLens[HTTPStatus, Int]("code")
  val message = Macro.mkLens[HTTPStatus, String]("message")
}

object Ph {
  def get(url: String): IO[Throwable \/ Response[String]] = IO {
    def getResponse(url: URL): Throwable \/ Response[String] = {
      // UGH.
      val u =
        \/.fromTryCatch(url.openConnection.asInstanceOf[HttpURLConnection])
      u.map(_.setRequestMethod("GET"))
      u.map(_.setDoOutput(true))
      u >>= (uu =>
        \/.fromTryCatch(Source.fromInputStream(uu.getInputStream)(Codec.UTF8).mkString)
          .map(x => Response(
            HTTPStatus(uu.getResponseCode, uu.getResponseMessage),
            uu.getHeaderFields.asScala.mapValues(_.asScala.toList).toMap,
            x)
          )
      )
    }
    parseURL(url) >>= getResponse
  }

  def getRequest(
    method: Method,
    secure: Boolean,
    host: String,
    port: Int,
    path: String,
    queryString: Option[String] = None): Request =
      Request(method, secure, host, port, path, queryString.getOrElse(""), None, "", None, None, false, 5, None)

  // TODO: We should do better checks to make sure it is HTTP or HTTPS
  // as opposed to ftp:// or telnet:// or something.
  def parseURL(url: String): Throwable \/ URL =
    \/.fromTryCatch(new URL(url))
}
