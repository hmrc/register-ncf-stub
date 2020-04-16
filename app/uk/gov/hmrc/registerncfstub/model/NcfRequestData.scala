/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.registerncfstub.model

import play.api.libs.json.Json

case class NcfRequestData(MRN: String, Office: String)

object NcfRequestData {
  implicit val format = Json.format[NcfRequestData]
}
