package com.phone.io

import cats.effect.IO

import scala.io.Source

trait LogReader[F[_]] {
  def read(location: String): F[Seq[String]]
}

object LogReader {
  implicit object FileLogReader extends LogReader[IO] {
    override def read(location: String): IO[Seq[String]] =
      IO {
        Source.fromFile(location)
      }.bracket { bufferedSource =>
        IO {
          bufferedSource
            .getLines()
            .filterNot {
              _.isBlank
            }
            .toList
        }
      } { bufferedSource =>
        IO(bufferedSource.close).handleErrorWith(_ => IO.unit)
      }
  }
}
