/*
 * Copyright 2023 HM Revenue & Customs
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

import akka.actor.ActorSystem
import akka.stream.Materializer
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.Json
import play.api.test.{DefaultAwaitTimeout, FakeRequest}
import play.api.test.Helpers._
import play.api.{Configuration, Environment}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.registerncfstub.config.AppConfig
import uk.gov.hmrc.registerncfstub.model.{NcfRequestData, NcfResponse}
import uk.gov.hmrc.registerncfstub.services.RegisterNcfService

class RegisterNcfControllerSpec extends AnyWordSpec with DefaultAwaitTimeout with Matchers with GuiceOneAppPerSuite {

  implicit lazy val materializer: Materializer = app.materializer
  private val env           = Environment.simple()
  private val configuration = Configuration.load(env)

  private val serviceConfig = new ServicesConfig(configuration)
  private val appConfig     = new AppConfig(configuration, serviceConfig)
  private val sCC           = play.api.test.Helpers.stubControllerComponents()
  private val actorSystem: ActorSystem = ActorSystem()
  private val registerNcfService = new RegisterNcfService(appConfig)

  private val controller = new RegisterNcfController(actorSystem: ActorSystem, appConfig: AppConfig, registerNcfService: RegisterNcfService, sCC)

  "receiveNcfData" should {
    "Return a success response if the NCF process is successful" in {
      val requestData      = Json.toJson(NcfRequestData("19GB0000601001F004", "GB000060"))
      val expectedResponse = Json.toJson(NcfResponse("19GB0000601001F004", 0, None))

      val result = controller.receiveNcfData(FakeRequest().withBody(requestData))

      status(result)        shouldBe OK
      contentAsJson(result) shouldBe expectedResponse
    }

    "Return a 400 response with a technical error body" in {
      val requestData      = Json.toJson(NcfRequestData("19GB0000601001F103", "GB000060"))
      val expectedResponse = Json.toJson(NcfResponse("19GB0000601001F103", -1, Some("Technical Error occurred")))

      val result = controller.receiveNcfData(FakeRequest().withBody(requestData))

      status(result)        shouldBe BAD_REQUEST
      contentAsJson(result) shouldBe expectedResponse
    }

    "Return a 400 response with a schema validation body" in {
      val requestData = Json.toJson(NcfRequestData("19GB0000601001F400", "GB000060"))

      val result = controller.receiveNcfData(FakeRequest().withBody(requestData))

      status(result) shouldBe BAD_REQUEST
    }

    "Return a 200 response with a parsing error body" in {
      val requestData      = Json.toJson(NcfRequestData("19GB0000601001F012", "GB000060"))
      val expectedResponse = Json.toJson(NcfResponse("19GB0000601001F012", 1, Some("Parsing Error: Request Message could not be read")))

      val result = controller.receiveNcfData(FakeRequest().withBody(requestData))

      status(result)        shouldBe OK
      contentAsJson(result) shouldBe expectedResponse
    }

    "Return a 200 response with an invalid mrn body" in {
      val requestData      = Json.toJson(NcfRequestData("19GB0000601001F020", "GB000060"))
      val expectedResponse = Json.toJson(NcfResponse("19GB0000601001F020", 2, Some("Invalid MRN")))

      val result = controller.receiveNcfData(FakeRequest().withBody(requestData))

      status(result)        shouldBe OK
      contentAsJson(result) shouldBe expectedResponse
    }

    "Return a 200 response with an unknown mrn body" in {
      val requestData      = Json.toJson(NcfRequestData("19GB0000601001F039", "GB000060"))
      val expectedResponse = Json.toJson(NcfResponse("19GB0000601001F039", 3, Some("Unknown MRN")))

      val result = controller.receiveNcfData(FakeRequest().withBody(requestData))

      status(result)        shouldBe OK
      contentAsJson(result) shouldBe expectedResponse
    }

    "Return a 200 response with an invalid state at ood body" in {
      val requestData      = Json.toJson(NcfRequestData("19GB0000601001F047", "GB000060"))
      val expectedResponse = Json.toJson(NcfResponse("19GB0000601001F047", 4, Some("Invalid State at Office of Destination")))

      val result = controller.receiveNcfData(FakeRequest().withBody(requestData))

      status(result)        shouldBe OK
      contentAsJson(result) shouldBe expectedResponse
    }

    "Return a 200 response with an invalid state at oot body" in {
      val requestData      = Json.toJson(NcfRequestData("19GB0000601001F055", "GB000060"))
      val expectedResponse = Json.toJson(NcfResponse("19GB0000601001F055", 5, Some("Invalid State at Office of Transit")))

      val result = controller.receiveNcfData(FakeRequest().withBody(requestData))

      status(result)        shouldBe OK
      contentAsJson(result) shouldBe expectedResponse
    }

    "Return a 200 response with an invalid customs office body" in {
      val requestData      = Json.toJson(NcfRequestData("19GB0000601001F063", "GB000060"))
      val expectedResponse = Json.toJson(NcfResponse("19GB0000601001F063", 6, Some("Invalid Customs Office")))

      val result = controller.receiveNcfData(FakeRequest().withBody(requestData))

      status(result)        shouldBe OK
      contentAsJson(result) shouldBe expectedResponse
    }

    "Return a 200 response with an oot not for country body" in {
      val requestData      = Json.toJson(NcfRequestData("19GB0000601001F071", "GB000060"))
      val expectedResponse = Json.toJson(NcfResponse("19GB0000601001F071", 7, Some("Office of Transit does not belong to country")))

      val result = controller.receiveNcfData(FakeRequest().withBody(requestData))

      status(result)        shouldBe OK
      contentAsJson(result) shouldBe expectedResponse
    }

    "Return a 5xx response for any other server error from EIS" in {
      val requestData = Json.toJson(NcfRequestData("19GB00006010015500", "GB000060"))

      val result = controller.receiveNcfData(FakeRequest().withBody(requestData))

      status(result) shouldBe INTERNAL_SERVER_ERROR
    }
  }
}
