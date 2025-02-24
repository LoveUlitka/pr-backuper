package pull.request.backup.model

import io.circe.generic.auto._
import io.circe.{Decoder, HCursor}


case class PullRequestListResponse(values: List[(Int, String)])

object PullRequestListResponse {

  private case class PullRequestListResponseValue(id: Int, title: String)

  implicit val decodePullRequestListResponse: Decoder[PullRequestListResponse] = (c: HCursor) => for {
    values <- c.downField("values").as[List[PullRequestListResponseValue]]
  } yield {
    PullRequestListResponse(
      values.map {
        case PullRequestListResponseValue(id, title) =>
          val jiraTask = title.takeWhile(c => c != ' ' && c != ':').replace('/', '-')
          id -> jiraTask
      }
    )
  }

}
