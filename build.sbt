import play.sbt.PlayImport.PlayKeys.playDefaultPort
import scoverage.ScoverageKeys
import uk.gov.hmrc.DefaultBuildSettings.integrationTestSettings

val appName = "register-ncf-stub"

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .settings(
    majorVersion := 0,
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test,
    ScoverageKeys.coverageExcludedFiles :=
      """
        |<empty>;
        |.*metrics.*;
        |.*definition.*;
        |prod.*;
        |testOnlyDoNotUseInAppConf.*;
        |app.*;
        |.*BuildInfo.*;
        |.*Routes.*;
        |.*repositories.*;
        |.*controllers.test.*;
        |.*services.test.*
     """.stripMargin.replaceAll("\n", ""),
    ScoverageKeys.coverageMinimumStmtTotal := 90,
    ScoverageKeys.coverageFailOnMinimum := false,
    ScoverageKeys.coverageHighlighting := true,
    playDefaultPort := 8269
  )
  .settings(ThisBuild / scalaVersion := "3.6.4")
  .configs(IntegrationTest)
  .settings(integrationTestSettings(): _*)
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(scalafmtOnCompile := true)
  .settings(
    scalacOptions += "-language:postfixOps",
    scalacOptions += "-no-indent"
  )

evictionErrorLevel := Level.Warn
