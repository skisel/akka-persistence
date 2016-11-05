/*
 * Copyright 2016 Dennis Vriend
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

package akka.persistence.query.extension

import akka.actor.{ActorRef, ActorSystem, PoisonPill}
import akka.persistence.journal.Tagged
import akka.persistence.query.PersistenceQuery
import akka.persistence.query.scaladsl._
import akka.stream.scaladsl.Source
import akka.stream.testkit.TestSubscriber
import akka.stream.testkit.scaladsl.TestSink
import akka.stream.{ActorMaterializer, Materializer}
import akka.testkit.TestProbe
import akka.util.Timeout
import org.scalatest.concurrent.{Eventually, ScalaFutures}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, FlatSpec, Matchers}

import scala.collection.immutable._
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

abstract class TestSpec extends FlatSpec with Matchers with ScalaFutures with BeforeAndAfterAll with BeforeAndAfterEach with InMemoryCleanup with Eventually {
  implicit val system: ActorSystem = ActorSystem()
  implicit val mat: Materializer = ActorMaterializer()
  implicit val ec: ExecutionContext = system.dispatcher
  implicit val pc: PatienceConfig = PatienceConfig(timeout = 3.seconds)
  implicit val timeout = Timeout(30.seconds)

  val journal = PersistenceQuery(system).readJournalFor("inmemory-read-journal")
    .asInstanceOf[ReadJournal with CurrentPersistenceIdsQuery with CurrentEventsByPersistenceIdQuery with CurrentEventsByTagQuery with EventsByTagQuery]

  val mockFailure: Throwable = new RuntimeException("Mock failure")

  implicit class StringImplicits(that: String) {
    def tagged(tags: String*): Tagged =
      Tagged(that, Set(tags.toList: _*))
  }

  def withEnvelopes[A](xs: Seq[A])(f: Source[A, _] => Unit): Unit =
    f(Source(xs))

  def killActors(actors: ActorRef*): Unit = {
    val tp = TestProbe()
    actors.foreach { (actor: ActorRef) =>
      tp watch actor
      actor ! PoisonPill
      tp.expectTerminated(actor)
    }
  }

  implicit class SourceOps[A](that: Source[A, _])(implicit system: ActorSystem) {
    def withTestProbe(f: TestSubscriber.Probe[A] => Unit): Unit =
      f(that.runWith(TestSink.probe(system)))
  }

  implicit class PimpedFuture[T](self: Future[T]) {
    def toTry: Try[T] = Try(self.futureValue)
  }

  override protected def afterAll(): Unit = {
    system.terminate().toTry should be a 'success
  }
}
