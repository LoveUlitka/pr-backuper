package pull.request.backup

import cats.effect.{IO, IOApp}
import cats.syntax.traverse._
import sttp.client3.http4s.Http4sBackend


object Application extends IOApp.Simple {

  override def run: IO[Unit] = {
    for {
      _ <- IO.println("Application is started")
      backendR = Http4sBackend.usingDefaultEmberClientBuilder[IO]()
      _ <- backendR.use { backend =>
        val config = new BackupConfig()
        val httpClient = new Client(backend, config)
        val repositoryListService = new RepositoryListService(httpClient, config)
        val pullRequestListService = new PullRequestListService(httpClient)
        val pullRequestInfoService = new PullRequestInfoService(httpClient)

        for {
          repos <- repositoryListService.getAllRepositories()
          _ <- repos.traverse { repo =>
            for {
              prs <- pullRequestListService.getPullRequestsList(repo)
//              _ <- IO.println(s"found prs for repo $repo: $prs")
              _ <- pullRequestInfoService.downloadAppPrsInfo(prs)
              _ <- IO.println(s"finished for repo $repo")
            } yield ()
          }
        } yield ()

      }
    } yield ()
  }
}
