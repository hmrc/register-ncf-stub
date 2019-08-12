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

import java.io.FileNotFoundException
import javax.inject.{Singleton, Inject}

import play.api.http.MimeTypes
import play.api.libs.json._
import play.api.mvc._
import uk.gov.hmrc.play.bootstrap.controller.BackendController
import uk.gov.hmrc.registerncfstub.config.AppConfig
import uk.gov.hmrc.registerncfstub.model.NcfRequestData

import scala.concurrent.Future
import scala.io.Source


@Singleton
class DataHandlerController @Inject()(appConfig: AppConfig, cc: ControllerComponents)
  extends BackendController(cc) {

  private val basePath = "/resources/data"
  private val UNKNOWN =  "UNKNOWN"

  def receiveNcfData = Action.async(parse.json) {
    implicit request =>
      withJsonBody[NcfRequestData] {
        ncfData =>
        val filePath = basePath  + "/" + ncfData.Office.getOrElse(UNKNOWN) + "/" + ncfData.MRN + "/" +  "response.json"
        val defaultResponse = "{\"MRN\": \""+ ncfData.MRN + "\", \"ResponseCode\":0\"}"
        try {
          val jsonOption = resourceAsString(filePath) map { body =>
            Json.parse(body)
          }
          val json = Json.prettyPrint(jsonOption.getOrElse(throw new FileNotFoundException()))
          Future.successful(Ok(json).as(MimeTypes.JSON))
        } catch {

          case _ : FileNotFoundException => Future.successful(Ok(defaultResponse).as(MimeTypes.JSON))
          case ex : Exception => Future.failed(ex)
        }
      }
  }

  private def resourceAsString(resourcePath: String): Option[String] = {
    Option(getClass.getResourceAsStream(resourcePath)) map { is =>
      Source.fromInputStream(is).getLines.mkString("\n")
    }
  }

}
