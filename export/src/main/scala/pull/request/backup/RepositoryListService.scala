package pull.request.backup

import cats.effect.IO
import cats.syntax.traverse._
import pull.request.backup.model.{Repository, RepositoryListResponse}

class RepositoryListService(client: Client, config: BackupConfig) {

  private def reposListUri(project: String): String = s"$project/repos?start=0&limit=100"

  private def getRepositoryList(project: String): IO[List[Repository]] = {
    client.sendRequestAndParseResponse[RepositoryListResponse](reposListUri(project))
      .onError(ex => IO.println(s"[getRepositoryList] error for $project $ex"))
      .flatMap { resp =>
        resp.body match {
          case Right(body) => IO(body.values.map(Repository(project, _)))
          case Left(body) =>
            IO
              .println(s"[getRepositoryList] error for $project code: ${resp.code.code} body: $body")
              .flatMap(_ => IO.raiseError[List[Repository]](new Exception(s"error code ${resp.code.code}")))
        }
      }
  }

  def getAllRepositories(): IO[List[Repository]] = {
    config.projects.traverse(getRepositoryList).map(_.flatten)
  }

}
