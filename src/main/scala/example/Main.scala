package example

import com.amazonaws.services.lambda.runtime.Context
import encoding.Handler
import io.circe.generic.auto._  // Automatic serializer/deserializer creation


case class Request(reqTime: Long, data: String)

case class Response(respTime: Long, data: String)

/**
  * Main handler class. Pattern taken from Yeghishe Piruzyan's blog.
  * http://yeghishe.github.io/2016/10/16/writing-aws-lambdas-in-scala.html
  */
class MainHandler extends Handler[Request, Response] {
  def handler(req: Request, context: Context): Response = {
    val logger = context.getLogger
    logger.log(s"Received request with data $req")
    Response(req.reqTime, s"This is the response for request data: ${req.data}")
  }
}
