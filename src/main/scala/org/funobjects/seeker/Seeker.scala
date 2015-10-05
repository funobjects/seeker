
/*
 * Copyright 2015 Functional Objects, Inc.
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

package org.funobjects.seeker

import java.net.InetAddress
import java.util.UUID

import scala.concurrent.duration.Duration

trait Seeker {
  def seek(duration: Duration): Map[Seeker.Device, InetAddress]
}

object Seeker {
  case class Device(id: DeviceId, model: String, server: String, method: String) {
    require(id != null)
    require(model != null)
    require(server != null)
    require(method != null)
  }

  // currently, only the form "uuid:<uuid>" is accepted
  case class DeviceId(id: String) {
    require(id != null && id.length == 41 && id.startsWith("uuid:"))
    val uuid = UUID.fromString(id.substring("uuid:".length))
  }
}

