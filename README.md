Scala-based AWS Lambda example
==============================

A small application intended to be used as an example on how to set up an
[AWS Lambda](https://aws.amazon.com/lambda/) written in Scala. AWS Lambda is a 
on-demand serverless platform that hosts a small application
that is run only when a request comes in.

This code is primarily based on 
[Yeghishe Piruzyan's blog](http://yeghishe.github.io/2016/10/16/writing-aws-lambdas-in-scala.html)

Other links with information:
* https://chrislarson.me/blog/tutorial/lambda/2017/03/31/aws-lambda-scala.html
* https://aws.amazon.com/blogs/compute/writing-aws-lambda-functions-in-scala/

## Building the app

AWS Lambda takes either one single JAR or a Zip file with multiple Jars. 


### Creating a Zip File

To build 
a single Zip file,
go to the project's root directory and run
  ```
  $ sbt universal:packageBin
  ```
This is the file that will be uploaded to the AWS Lambda.

If by any reason you need to depoy a JAR file, see the [Deploying a single Jar](#deploying-a-single-jar) section below


## Setting up the AWS Lambda
* Sign up for an [AWS](https://aws.amazon.com/) account
* Go to the [AWS Lambda management](https://console.aws.amazon.com/lambda/home) console and create a new Lambda function.
    * Ensure `Author from scratch` is selected.
    * In runtime set `Java 8`.
    * In Role, select `Create new from template`. The policy necessary to write logs into CloudWatch should
      be added automatically to any other policies you choose.
* Open the [S3 web console](https://s3.console.aws.amazon.com/s3/home). 
    * Create a bucket or select an existing one.
    * Upload the file `target/universal/aws-lambda-example-1.0.zip` to the bucket.
* Go to the the AWS Lambda console for the lambda application:
    * Select the tab `Configuration` (instead of Monitoring)
    * Under `Function Code`:
        * For `Code entry type` select `Upload a file from Amazon S3`
        * Under `S3 link URL` paste the link to the zip package in your S3 bucket.
        * Set the `Handler` in the AWS console as `example.MainHandler`
    * Under `Basic settings`
        * Ensure the memory for the Lambda is **at least** 512MB.
        * Ensure your timeout is around 2 minutes.
    * Hit `Save`

## Test the Lambda
 * In the [AWS Lambda management](https://console.aws.amazon.com/lambda/home) console:
     * Hit `Test`.
     * Select `Create new test event`.
     * Select a name for your test event.
     * Enter a valid JSON for your test.
     * Hit `Create`.
     * Hit `Test`
     * You should see a message indicating execution success.
The first test (cold start) will take a long time. 
Subsequent executions should take significantly less time. 

## Make the Lambda accessible to other web services
 * In the [AWS Lambda management](https://console.aws.amazon.com/lambda/home) console
    * Select the tab `Configuration` (instead of Monitoring)
    * Under `Designer` -> `Add Triggers` select `API Gateway`.
    * Under `Configure Triggers` -> `API` select `Create New API`.
    * Select a uniqie API name and a deployment stage name (e.g. `test`).
    * For testing, under `Security`, select `Open`.
 * Hit `Save`
 
 
 ## Test the API gateway from AWS
 
 * Go to the service list in the Amazon AWS console and under `Networking & Content Delivery` select `API Gateway`.
 * Locate and select the API created in the previous step.
    * Under `Resources` select the path to your lambda.
    * Under `Actions` select `Create Method` and then choose `POST`. Hit Accept.
    * Under `Integration Type` enure that `Lambda Function` is selected and `Use Lambda Proxy integration` is 
    __deselected__.
    * Select the region and name of your lambda function.
    * Hit Save.
    * Click OK if requested permission to access the Lambda. 
    * Hit `Test`
        * Under reques body enter a valid test JSON.
        * You should see a result with a 200 status.

## Test the API gateway

To make sure the Lambda is reachable from anywhere in the internet.

* On the API Gateway menu for your lambda:
    * Select the API method (`POST` as in above steps)
    * Under `Actions` click `Deploy API`
    * Enter your deployment stage name (e.g. `test`) and click `Deploy`.
    * On the Stages meny select your lambda name and the HTTP method (e.g. `POST`)
    * Copy the `Invoke URL`
    * Employ any REST testing tool ([Postman](https://www.getpostman.com/), [cURL](https://curl.haxx.se), [HTTPie](https://httpie.org/)) to perform a POST request to the `Invoke URL` with 
    a test JSON body. 
* Now the Lambda can be accessed from anywhere. It should be possible to secure it 
via tokens or IAM credentials to restrict access.


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
