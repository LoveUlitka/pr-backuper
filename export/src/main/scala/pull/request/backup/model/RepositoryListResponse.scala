package pull.request.backup.model

import io.circe.generic.auto._
import io.circe.{Decoder, HCursor}


case class RepositoryListResponse (values: List[String])

object RepositoryListResponse {
  private case class RepositoryListResponseValue(name: String)

  implicit val decodeRepositoryListResponse: Decoder[RepositoryListResponse] = (c: HCursor) => for {
    values <- c.downField("values").as[List[RepositoryListResponseValue]]
  } yield RepositoryListResponse(values.map(_.name))
}
