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

package akka.http.client

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model._
import akka.stream.Materializer
import com.typesafe.config.Config

import scala.concurrent.{ExecutionContext, Future}

object HttpClient {
  /**
   * Creates a new HttpClient and uses the configuration name to look up the connection configuration
   */
  def apply(name: String)(implicit system: ActorSystem, mat: Materializer, ec: ExecutionContext, log: LoggingAdapter): HttpClient =
    new HttpClient(HttpClientConfig(system.settings.config.getConfig(s"webservices.$name")))

  /**
   * Creates a new HttpClient based on typesafe configuration
   */
  def apply(config: Config)(implicit system: ActorSystem, mat: Materializer, ec: ExecutionContext, log: LoggingAdapter): HttpClient =
    new HttpClient(HttpClientConfig(config))

  /**
   * Creates a new HttpClient based on the configuration given in the constructor
   * s
   */
  def apply(host: String, port: Int, tls: Boolean, username: Option[String] = None, password: Option[String] = None, consumerKey: Option[String] = None, consumerSecret: Option[String] = None)(implicit system: ActorSystem, mat: Materializer, ec: ExecutionContext): HttpClient =
    new HttpClient(HttpClientConfig(host, port, tls, username, password, consumerKey, consumerSecret))
}

class HttpClient(val config: HttpClientConfig)(implicit system: ActorSystem, ec: ExecutionContext, mat: Materializer) extends RequestBuilding {
  def get(url: String, body: String = "", queryParamsMap: Map[String, String] = Map.empty, headersMap: Map[String, String] = Map.empty): Future[HttpResponse] =
    singleRequestPipeline(mkRequest(RequestBuilding.Get, url, body, queryParamsMap, headersMap), config)

  def post(url: String, body: String = "", queryParamsMap: Map[String, String] = Map.empty, headersMap: Map[String, String] = Map.empty): Future[HttpResponse] =
    singleRequestPipeline(mkRequest(RequestBuilding.Post, url, body, queryParamsMap, headersMap), config)

  def put(url: String, body: String = "", queryParamsMap: Map[String, String] = Map.empty, headersMap: Map[String, String] = Map.empty): Future[HttpResponse] =
    singleRequestPipeline(mkRequest(RequestBuilding.Put, url, body, queryParamsMap, headersMap), config)

  def patch(url: String, body: String = "", queryParamsMap: Map[String, String] = Map.empty, headersMap: Map[String, String] = Map.empty): Future[HttpResponse] =
    singleRequestPipeline(mkRequest(RequestBuilding.Patch, url, body, queryParamsMap, headersMap), config)

  def delete(url: String, body: String = "", queryParamsMap: Map[String, String] = Map.empty, headersMap: Map[String, String] = Map.empty): Future[HttpResponse] =
    singleRequestPipeline(mkRequest(RequestBuilding.Delete, url, body, queryParamsMap, headersMap), config)

  def options(url: String, body: String = "", queryParamsMap: Map[String, String] = Map.empty, headersMap: Map[String, String] = Map.empty): Future[HttpResponse] =
    singleRequestPipeline(mkRequest(RequestBuilding.Options, url, body, queryParamsMap, headersMap), config)

  def head(url: String, body: String = "", queryParamsMap: Map[String, String] = Map.empty, headersMap: Map[String, String] = Map.empty): Future[HttpResponse] =
    singleRequestPipeline(mkRequest(RequestBuilding.Head, url, body, queryParamsMap, headersMap), config)
}
