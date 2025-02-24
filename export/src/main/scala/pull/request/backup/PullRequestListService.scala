package pull.request.backup

import cats.effect.IO
import pull.request.backup.model.{PullRequest, PullRequestListResponse, Repository}

class PullRequestListService(client: Client) {

  private def prsListUri(repo: Repository): String =
    s"${repo.project}/repos/${repo.name}/pull-requests?start=0&limit=100"

  def getPullRequestsList(repo: Repository): IO[List[PullRequest]] = {
    client.sendRequestAndParseResponse[PullRequestListResponse](prsListUri(repo))
      .onError(ex => IO.println(s"[getPullRequestsList] error for $repo $ex"))
      .flatMap { resp =>
        resp.body match {
          case Right(body) =>
            IO {
              body.values.map(pr => PullRequest(repo, pr._1, pr._2))
            }
          case Left(body) =>
            IO
              .println(s"[getPullRequestsList] error for $repo code: ${resp.code.code} body: $body")
              .flatMap(_ => IO.raiseError[List[PullRequest]](new Exception(s"error code ${resp.code.code}")))
        }
      }
  }

}
