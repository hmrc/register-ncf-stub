import sbt._

object AppDependencies {

  private val bootstrapVersion = "9.0.0"
  private val playVersion      = "play-30"

  val compile = Seq(
    "uk.gov.hmrc" %% s"bootstrap-backend-$playVersion" % bootstrapVersion,
    "io.monix"    %% "monix-eval"                      % "3.4.1"
  )

  val test = Seq(
    "uk.gov.hmrc" %% s"bootstrap-test-$playVersion" % bootstrapVersion % "test"
  )

}
