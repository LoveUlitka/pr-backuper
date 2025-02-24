package pull.request.backup

import cats.effect.IO
import io.circe
import io.circe.Decoder
import sttp.client3._
import sttp.client3.circe._
import sttp.model.Uri


class Client(backend: SttpBackend[IO, Any],
             config: BackupConfig) {

  private val cookies = List("BITBUCKETSESSIONID" -> config.token)

  private val headers: Map[String, String] = Map(
    "Accept" -> "application/json; charset=UTF-8",
    "Content-Type" -> "application/json; charset=UTF-8",
    "Cache-Control" -> "no-cache",
    "Connection" -> "keep-alive",
  )

  private val apiUri: String = config.uri

  private def buildUri(path: String): Uri = {
    val hackedPath = s"$apiUri/$path" // horrible. breaks ref transparency but keeps "/" nor encoded
    uri"$hackedPath"
  }

  def sendRequest(path: String): IO[Response[Either[String, String]]] = {
    val request = basicRequest
      .get(buildUri(path))
      .headers(headers)
      .cookies(cookies:_*)

    request.send(backend).onError { resp =>
      IO.println(s"[sendRequest] error req: $request resp: $resp")
    }
  }

  def sendRequestAndParseResponse[T: Decoder](path: String): IO[Response[Either[ResponseException[String, circe.Error], T]]] = {
    val request = basicRequest
      .get(buildUri(path))
      .headers(headers)
      .cookies(cookies:_*)
      .response(asJson[T])

    request.send(backend).onError { resp =>
      IO.println(s"[sendRequest] error req: $request resp: $resp")
    }
  }
}
