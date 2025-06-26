/*
 * Copyright 2025 HM Revenue & Customs
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

package uk.gov.hmrc.registerncfstub.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers.{should, shouldBe}
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.Json

class NcfResponseSpec extends AnyWordSpec with Matchers {

  "NcfResponse" should {
    "serialize and deserialize to/from JSON" in {
      val response = NcfResponse("123456789", 200, Some("No error"))
      val json     = Json.toJson(response)
      json.as[NcfResponse] shouldBe response
    }
  }
}
