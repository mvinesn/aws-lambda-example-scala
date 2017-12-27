package example

import com.amazonaws.services.lambda.runtime.Context


object Main extends App {

  def greeting(name: String, context: Context): String = {
    val logger = context.getLogger

    logger.log(s"Received request with name: '$name'")
    s"Hello $name"
  }


}
