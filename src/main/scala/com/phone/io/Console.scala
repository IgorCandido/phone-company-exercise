package com.phone.io

import cats.effect.IO

import scala.io.StdIn

trait Console[F[_]] {
  def print(msg: String): F[Unit]
  def read(): F[String]
}

object Console {
  def apply[F[_]](implicit console: Console[F]): Console[F] = console

  implicit object RealConsole extends Console[IO] {
    override def print(msg: String): IO[Unit] = IO { Predef.print(msg) }

    override def read(): IO[String] = IO { StdIn.readLine() }
  }
}
