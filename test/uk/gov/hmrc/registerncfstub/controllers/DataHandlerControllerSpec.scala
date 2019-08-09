/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.registerncfstub.controllers

import akka.stream.Materializer
import org.scalatest.{Matchers, WordSpec}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.http.Status
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{FakeRequest,FakeHeaders, Helpers}
import play.api.{Mode, Configuration, Environment}
import uk.gov.hmrc.play.bootstrap.config.{RunMode, ServicesConfig}
import uk.gov.hmrc.play.test.UnitSpec
import uk.gov.hmrc.registerncfstub.config.AppConfig


/**
 * Created by akhileshkumar on 08/08/2019.
 */
class DataHandlerControllerSpec extends WordSpec with UnitSpec with Matchers with GuiceOneAppPerSuite {

  implicit lazy val materializer: Materializer = app.materializer
  private val env           = Environment.simple()
  private val configuration = Configuration.load(env)

  private val serviceConfig = new ServicesConfig(configuration, new RunMode(configuration, Mode.Dev))
  private val appConfig     = new AppConfig(configuration, serviceConfig)

  private val controller = new DataHandlerController(appConfig, Helpers.stubControllerComponents())

  "The DataHandlerController" should {
    "POST /ncfdata/submit return 200 with responseCode : 0" in {
      val requestData = """ { "MRN": "18AT320000TVWJYM68", "Office":"GB000011" }"""

      val expectedJson = Json.parse("""{ "MRN": "18AT320000TVWJYM68", "ResponseCode":0 }""")
      val fakeRequest = FakeRequest(method = "POST", uri = "", headers = FakeHeaders(Seq("Content-type" -> "application/json")), body = Json.parse(requestData))
      val result = await(controller.receiveNcfData(fakeRequest))
      status(result) shouldBe Status.OK
      expectedJson shouldBe jsonBodyOf(result)

    }
  }
}