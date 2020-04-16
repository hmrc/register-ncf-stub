/*
 * Copyright 2020 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.registerncfstub.util

import akka.actor.Scheduler
import akka.pattern.after
import com.typesafe.config.Config
import configs.syntax._
import uk.gov.hmrc.registerncfstub.util.Delays.DelayConfig

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.FiniteDuration
import scala.util.Random
import scala.concurrent.duration._

trait Delays {

  val scheduler: Scheduler

  def withDelay[A](delayConfig: DelayConfig)(f: () ⇒ A)(implicit ec: ExecutionContext): Future[A] =
    if (delayConfig.enabled) {
      after(nextDelay(delayConfig), scheduler)(Future(f()))
    } else {
      Future(f())
    }

  // return a random delay based on a normal distribution floored with some configured minimum delay
  private def nextDelay(delayConfig: DelayConfig): FiniteDuration = {
    val nanos = (Random.nextGaussian() * delayConfig.standardDeviationNanos).toLong + delayConfig.meanDelayNanos
    print("delay is " + nanos.max(delayConfig.minimumDelayNanos).nanos)
    nanos.max(delayConfig.minimumDelayNanos).nanos
  }

}

object Delays {

  case class DelayConfig(enabled: Boolean, meanDelay: FiniteDuration, standardDeviation: FiniteDuration, minimumDelay: FiniteDuration) {
    val meanDelayNanos:         Long = meanDelay.toNanos
    val standardDeviationNanos: Long = standardDeviation.toNanos
    val minimumDelayNanos:      Long = minimumDelay.toNanos
  }

  def config(name: String, config: Config): DelayConfig =
    config.get[DelayConfig](s"delays.$name").value

}
