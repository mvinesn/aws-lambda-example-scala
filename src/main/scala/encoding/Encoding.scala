package encoding

import java.io.{InputStream, OutputStream}

import scala.io.Source
import scala.util.{Failure, Left, Right, Success, Try}


// Taken from Yeghishe's scala-aws-lambda-utils
// https://github.com/yeghishe/scala-aws-lambda-utils/blob/a93273d08753d6826aaace67126349d28f968c6a/src/main/scala/io/github/yeghishe/lambda/Encoding.scala
object Encoding {

  import io.circe._
  import io.circe.parser._
  import io.circe.syntax._

  // From Either in Scala 2.12
  implicit class EitherExt[A, B](e: Either[A, B]) {
    def toTry(implicit ev: A <:< Throwable): Try[B] = e match {
      case Right(b) => Success(b)
      case Left(a) => Failure(a)
    }
  }

  def in[T](is: InputStream)(implicit decoder: Decoder[T]): Try[T] = {
    val t: Try[T] = Try(Source.fromInputStream(is).mkString).flatMap(decode[T](_).toTry)
    is.close()
    t
  }

  def out[T](value: T, os: OutputStream)(implicit encoder: Encoder[T]): Try[Unit] = {
    val t = Try(os.write(value.asJson.noSpaces.getBytes("UTF-8")))
    os.close()
    t
  }
}
