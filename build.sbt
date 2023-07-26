import play.sbt.PlayImport.PlayKeys.playDefaultPort
import scoverage.ScoverageKeys
import uk.gov.hmrc.DefaultBuildSettings.integrationTestSettings

val appName = "register-ncf-stub"

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .settings(
    majorVersion := 0,
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test,
    ScoverageKeys.coverageExcludedFiles := "<empty>;com.kenshoo.play.metrics.*;.*definition.*;prod.*;testOnlyDoNotUseInAppConf.*;app.*;.*BuildInfo.*;.*Routes.*;.*repositories.*;.*controllers.test.*;.*services.test.*",
    ScoverageKeys.coverageMinimumStmtTotal := 90,
    ScoverageKeys.coverageFailOnMinimum := false,
    ScoverageKeys.coverageHighlighting := true,
    playDefaultPort := 8269
  )
  .settings(scalaVersion := "2.13.10")
  .configs(IntegrationTest)
  .settings(integrationTestSettings(): _*)
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(
    scalacOptions += "-Wconf:src=routes/.*:s", //Silence all warnings in generated routes
    scalacOptions += "-Ymacro-annotations"
  )
  .settings( //fix scaladoc generation in jenkins
    scalacOptions += "-language:postfixOps"
  )

evictionErrorLevel := Level.Warn
