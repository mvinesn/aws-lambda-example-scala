package encoding

import java.io.{InputStream, OutputStream}

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import io.circe.{Decoder, Encoder}


// Taken from Yeghishe's scala-aws-lambda-utils
// https://github.com/yeghishe/scala-aws-lambda-utils/blob/a93273d08753d6826aaace67126349d28f968c6a/src/main/scala/io/github/yeghishe/lambda/Handler.scala

abstract class Handler[T, R](implicit decoder: Decoder[T], encoder: Encoder[R]) extends RequestStreamHandler {

  import Encoding._

  protected def handler(input: T, context: Context): R

  def handleRequest(is: InputStream, os: OutputStream, context: Context): Unit =
    in(is).flatMap(i => out(handler(i, context), os)).get
}
