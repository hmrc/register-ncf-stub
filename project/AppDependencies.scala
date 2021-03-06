import play.core.PlayVersion.current
import sbt._

object AppDependencies {

  val compile = Seq(
    "uk.gov.hmrc" %% "bootstrap-backend-play-26" % "2.24.0"
  )

  val test = Seq(
    "org.scalatest"          %% "scalatest"          % "3.0.8" % "test",
    "com.typesafe.play"      %% "play-test"          % current % "test",
    "org.pegdown"            % "pegdown"             % "1.6.0" % "test, it",
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % "test, it",
    "uk.gov.hmrc"            %% "hmrctest"           % "3.9.0-play-26" % "test, it",
    "com.github.kxbmap"      %% "configs"            % "0.4.4"
  )

}
