/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.registerncfstub.model
import play.api.libs.json.Json

case class NcfResponse(MRN: String, ResponseCode: Int, ErrorDescription: Option[String])

object NcfResponse {
  implicit val format = Json.format[NcfResponse]
}
