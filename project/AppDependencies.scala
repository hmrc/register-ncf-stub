import sbt._

object AppDependencies {

  private val bootstrapVersion = "8.4.0"
  private val playVersion = "play-30"

  val compile = Seq(
    "uk.gov.hmrc"            %% s"bootstrap-backend-$playVersion" % bootstrapVersion
  )

  val test = Seq(
    "uk.gov.hmrc"            %% s"bootstrap-test-$playVersion"  % bootstrapVersion       % "test"
  )

}
