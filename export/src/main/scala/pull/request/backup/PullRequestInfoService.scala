package pull.request.backup

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}
import java.time.Instant

import cats.effect.IO
import cats.syntax.traverse._
import pull.request.backup.model.PullRequest


class PullRequestInfoService(client: Client) {

  private val parentDirectory: String = "prs-info"

  private def activitiesUri(pr: PullRequest): String = {
    s"${pr.repo.project}/repos/${pr.repo.name}/pull-requests/${pr.id}/activities?start=0&limit=100&markup=true"
  }

  private def commonInfoUri(pr: PullRequest): String = {
    s"${pr.repo.project}/repos/${pr.repo.name}/pull-requests/${pr.id}?markup=true"
  }

  def downloadAppPrsInfo(prs: List[PullRequest]): IO[Unit] = {
    prs.traverse { pr =>
      for {
//        _ <- IO.println(s"[loadAppPrsInfo] starting with $pr")
        commonInfo <- getPrInfo(pr, commonInfoUri(pr))
        activities <- getPrInfo(pr, activitiesUri(pr))
        _ <- savePrInfo(pr, commonInfo, activities)
//        _ <- IO.println(s"[loadAppPrsInfo] finished with $pr")
      } yield ()
    }.as(())
  }

  private def getPrInfo(pr: PullRequest, path: String): IO[String] = {
    client.sendRequest(path)
      .onError(ex => IO.println(s"[getPrInfo] error for $pr uri: $path $ex"))
      .flatMap { resp =>
        resp.body match {
        case Right(body) => IO.pure(body)
        case Left(body) =>
          IO
            .println(s"[getPrInfo] error for $pr uri: $path code: ${resp.code.code} body: $body")
            .flatMap(_ => IO.raiseError[String](new Exception(s"error code ${resp.code.code}")))
      }
    }
  }

  private def savePrInfo(pr: PullRequest, commonInfo: String, activities: String): IO[Unit] = {
    IO {
      val projectPath = s"$parentDirectory/${pr.repo.project}/${pr.repo.name}"
      val fileNamePrefix = s"$projectPath/${pr.jiraTask}-PR-${pr.id}-${Instant.now().toEpochMilli}"

      Files.createDirectories(Paths.get(projectPath))

      val infoFile = Files.createFile(Paths.get(s"$fileNamePrefix-info.json"))
      Files.write(infoFile, commonInfo.getBytes(StandardCharsets.UTF_8))

      val activitiesFile = Files.createFile(Paths.get(s"$fileNamePrefix-activities.json"))
      Files.write(activitiesFile, activities.getBytes(StandardCharsets.UTF_8))
      activitiesFile
    }
      .as(())
      .recoverWith {
        case ex => IO.println(s"[savePrInfo] error $ex")
      }
  }

}
