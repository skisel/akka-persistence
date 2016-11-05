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

import com.typesafe.config.Config

import scala.util.Try

object HttpClientConfig {
  def apply(config: Config): HttpClientConfig =
    httpClientConfig(config)

  def httpClientConfig(config: Config): HttpClientConfig =
    HttpClientConfig(
      config.getString("host"),
      Try(config.getInt("port")).getOrElse(80),
      Try(config.getBoolean("tls")).toOption.getOrElse(false),
      Try(config.getString("username")).toOption.find(_.nonEmpty),
      Try(config.getString("password")).toOption.find(_.nonEmpty),
      Try(config.getString("consumerKey")).toOption.find(_.nonEmpty),
      Try(config.getString("consumerSecret")).toOption.find(_.nonEmpty)
    )
}

final case class HttpClientConfig(host: String, port: Int, tls: Boolean, username: Option[String], password: Option[String], consumerKey: Option[String], consumerSecret: Option[String])
