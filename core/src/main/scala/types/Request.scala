package me.elrod.ph

case class Request(
  method: Method,
  secure: Boolean,
  host: String,
  port: Int, // TODO: smart constructor or shapeless to verify valid port.
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

object Request {
  def defaultGet(
    method: Method,
    secure: Boolean,
    host: String,
    port: Int,
    path: String,
    queryString: Option[String] = None): Request =
      Request(method, secure, host, port, path, queryString.getOrElse(""), None, "", None, None, false, 5, None)
}
