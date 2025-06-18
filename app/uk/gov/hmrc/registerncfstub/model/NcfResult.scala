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

package uk.gov.hmrc.registerncfstub.model

sealed trait NcfResult

final case class CompletedSuccessfully(mrn: String, responseCode: Int = 0) extends NcfResult
final case class TechnicalError(mrn: String, responseCode: Int = -1, errorDescription: String = "Technical Error occurred") extends NcfResult
final case class ParsingError(mrn: String, responseCode: Int = 1, errorDescription: String = "Parsing Error: Request Message could not be read")
    extends NcfResult
final case class InvalidMrn(mrn: String, responseCode: Int = 2, errorDescription: String = "Invalid MRN") extends NcfResult
final case class UnknownMrn(mrn: String, responseCode: Int = 3, errorDescription: String = "Unknown MRN") extends NcfResult
final case class InvalidStateOod(mrn: String, responseCode: Int = 4, errorDescription: String = "Invalid State at Office of Destination")
    extends NcfResult
final case class InvalidStateOot(mrn: String, responseCode: Int = 5, errorDescription: String = "Invalid State at Office of Transit")
    extends NcfResult
final case class InvalidCustomsOffice(mrn: String, responseCode: Int = 6, errorDescription: String = "Invalid Customs Office") extends NcfResult
final case class OotNotForCountry(mrn: String, responseCode: Int = 7, errorDescription: String = "Office of Transit does not belong to country")
    extends NcfResult
final case class DocNotReadyForClearance(mrn: String, responseCode: Int = 8, errorDescription: String = "Supporting document not ready for clearance")
    extends NcfResult
case object SchemaValidationError extends NcfResult
case object Eis500Error extends NcfResult
