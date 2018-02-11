Scala-Based AWS Lambda tests
============================

Tests setting up a Scala written AWS Lambda function.

Primarily based on [Yeghishe Piruzyan's blog](http://yeghishe.github.io/2016/10/16/writing-aws-lambdas-in-scala.html)

Other links with information:
* https://chrislarson.me/blog/tutorial/lambda/2017/03/31/aws-lambda-scala.html
* https://aws.amazon.com/blogs/compute/writing-aws-lambda-functions-in-scala/

## Main Steps
* Sign up for [AWS](https://aws.amazon.com/)
* Go to the AWS Lambda consolea and create a new [Lambda function](http://docs.aws.amazon.com/lambda/latest/dg/lambda-app.html).
  * Create a role that includes **writing logs into AWS CloudWatch** (otherwise no in-app logs will appear in CloudWatch)
  * To set the **handler function** in AWS:
    * Set the _handler_ in the AWS console as `example.MyHandler::handler` if `MyHandler` is an object and `handler` is the function.
    * Set the _handler_ in the AWS console as `example.MyHandler` if `MyHandler` is a a class (It needs to extend some request handler class/interface in `com.amazonaws.services.lambda.runtime`)
  * Use **at least** 512 MB RAM and 5 seconds timeout.
* To create the deployable Zip file, go to the project's root directory and run:
  ```
  $ sbt universal:packageBin
  ```
  * Upload the file `target/universal/aws-lambda-example-1.0.zip` when creating the lambda in AWS.
* When testing your lambda on AWS console:
  * Make sure the test data can be deserialized into the data type of the first parameter of the handler.

* Set up an API Gateway so an arbitrary application can call the Lambda
  * Set up [IAM User, Groups, Roles and Policies](http://docs.aws.amazon.com/apigateway/latest/developerguide/getting-started.html#setting-up-iam)
  * Create the [API Gateway for your Lambda](http://docs.aws.amazon.com/apigateway/latest/developerguide/api-gateway-create-api-from-example.html)
    * Note: Do not use `AWS Lambda Proxy` with this code, otherwise you'll get runtime exceptions when calling the API
* Deploy the API
  * Test with [Postman](https://www.getpostman.com/) or any other REST API tool

## Deploying a single JAR

If by any reason you need to deploy a single jar file, these are the steps to follow:
 * Add the following line to the `project/plugins.sbt`
   ```
   addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.12.0")
   ``` 
 * Add the following to the `build.sbt` file:
   ```
   assemblyMergeStrategy :=
     {
       case PathList("META-INF", xs @ _*) => MergeStrategy.discard
       case x => MergeStrategy.first
     }
   ```
* To produce the deployable Jar file, go to the project's root directory and run:
  ```
  $ sbt assembly
  ```
  * Upload the file `target/scala-2.11/aws-lambda-example-assembly-1.0.jar` when creating the lambda in AWS.


## Future Tasks
* The app uses the default logger in the context. Using an Log4j should be possible, though.
