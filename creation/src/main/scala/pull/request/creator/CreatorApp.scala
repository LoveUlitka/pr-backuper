package pull.request.creator

import cats.effect.{IO, IOApp}

object CreatorApp extends IOApp.Simple {

  override def run: IO[Unit] = {
      IO.println("Application is started")
  }
}
