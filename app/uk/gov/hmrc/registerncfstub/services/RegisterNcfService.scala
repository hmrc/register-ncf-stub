/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.registerncfstub.services

import play.api.Logger

import javax.inject.{Inject, Singleton}
import uk.gov.hmrc.registerncfstub.config.AppConfig
import uk.gov.hmrc.registerncfstub.model._

@Singleton
class RegisterNcfService @Inject()(appConfig: AppConfig) {

  def processRegisterNcfRequest(ncfRequestData: NcfRequestData): NcfResult =
    ncfRequestData.MRN.dropRight(1).takeRight(2) match {
      case "00" => CompletedSuccessfully(ncfRequestData.MRN)
      case "10" => TechnicalError(ncfRequestData.MRN)
      case "01" => ParsingError(ncfRequestData.MRN)
      case "02" => InvalidMrn(ncfRequestData.MRN)
      case "03" => UnknownMrn(ncfRequestData.MRN)
      case "04" => InvalidStateOod(ncfRequestData.MRN)
      case "05" => InvalidStateOot(ncfRequestData.MRN)
      case "06" => InvalidCustomsOffice(ncfRequestData.MRN)
      case "07" => OotNotForCountry(ncfRequestData.MRN)
      case "40" => SchemaValidationError
      case "50" => Eis500Error
      case "54" => {
        Logger.info("Request to EIS is due to time out....")
        Thread.sleep(100000000)
        CompletedSuccessfully(ncfRequestData.MRN)
      }
      case _ => CompletedSuccessfully(ncfRequestData.MRN)
    }
}
