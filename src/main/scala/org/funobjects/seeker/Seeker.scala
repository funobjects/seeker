
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

