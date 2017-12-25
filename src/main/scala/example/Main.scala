package example

import scala.collection.JavaConverters._
import java.net.URLDecoder
//import com.amazonaws.services.lambda.runtime.events.S3Event
import java.io.{InputStream, OutputStream, PrintStream}
import com.amazonaws.services.lambda.runtime.Context


object Main extends App {

  def greeting(name: String, context: Context): String = {
    s"Hello $name"
  }


}
