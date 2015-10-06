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
import java.util.concurrent.TimeUnit

import org.cybergarage.upnp.{Service => UpnpService, Device => UpnpDevice, ControlPoint}
import org.cybergarage.upnp.device.DeviceChangeListener

import scala.collection.JavaConverters._
import scala.concurrent.duration.Duration

/**
 * Find various things via UPnP
 */
class UpnpSeeker extends Seeker {

  import Seeker._

  override def seek(duration: Duration): Map[Device,InetAddress] = {
    val seeker = new ControlPoint()
    seeker.start()
    seeker.search()
    Thread.sleep(duration.toMillis)
    seeker.stop()
    seeker.getDeviceList
      .asScala
      .map(_.asInstanceOf[UpnpDevice])
      .filter (dev => dev.getSSDPPacket != null && dev.getSSDPPacket.getRemoteAddress != null)
      .map (dev => Device(
        DeviceId(dev.getUDN), dev.getModelName, Option(dev.getSSDPPacket.getServer).getOrElse(""), "upnp")
          -> dev.getSSDPPacket.getRemoteInetAddress)
      .toMap
  }
}

object UpnpSeeker {
  def main(args: Array[String]): Unit = {
    val seeker = new UpnpSeeker()
    val devs = seeker.seek(Duration(5, TimeUnit.SECONDS))
    println(devs.mkString("\n"))
  }
}
