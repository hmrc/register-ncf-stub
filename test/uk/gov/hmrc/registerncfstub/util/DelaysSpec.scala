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

package uk.gov.hmrc.registerncfstub.util

import org.apache.pekko.actor.{Cancellable, Scheduler}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import uk.gov.hmrc.registerncfstub.util.Delays.DelayConfig

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class DelaysSpec extends AnyWordSpec with Matchers {

  object TestScheduler extends Scheduler {
    override def scheduleOnce(
      delay:    FiniteDuration,
      runnable: Runnable
    )(implicit executor: ExecutionContext): Cancellable = {
      executor.execute(runnable)
      new Cancellable {
        override def cancel():    Boolean = false
        override def isCancelled: Boolean = false
      }
    }

    override def maxFrequency: Double = 1

    @deprecated("Not implemented for this test", "1.0")
    override def schedule(initialDelay: FiniteDuration, interval: FiniteDuration, runnable: Runnable)(implicit
      executor: ExecutionContext
    ): Cancellable = ???
  }

  object TestDelays extends Delays {
    override val scheduler: Scheduler = TestScheduler
  }

  "withDelay" should {
    "apply a delay not less than the minimum when enabled" in {
      val config = DelayConfig(
        enabled = true,
        meanDelay = 100.millis,
        standardDeviation = 50.millis,
        minimumDelay = 80.millis
      )
      implicit val ec: ExecutionContext = ExecutionContext.global

      val start = System.nanoTime()
      TestDelays.withDelay(config)(() => "done").map { result =>
        val elapsed = (System.nanoTime() - start).nanos
        elapsed should be >= config.minimumDelay
        result shouldBe "done"
        succeed
      }
    }
  }
}
