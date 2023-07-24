import play.core.PlayVersion.current
import sbt._

object AppDependencies {

  private val bootstrapPlay28Version = "7.19.0"

  val compile = Seq(
    "uk.gov.hmrc"                  %% "bootstrap-backend-play-28"         % bootstrapPlay28Version
  )

  val test = Seq(
    "org.scalatest"          %% "scalatest"               % "3.2.15"               % "test",
    "com.typesafe.play"      %% "play-test"               % current                % "test",
    "org.scalatestplus.play" %% "scalatestplus-play"      % "5.1.0"                % "test",
    "uk.gov.hmrc"            %% "bootstrap-test-play-28"  % bootstrapPlay28Version % "test",
    "com.vladsch.flexmark"    % "flexmark-all"            % "0.64.4"               % "test"
  )

}
