package org.funobjects.seeker

import java.net.InetAddress
import java.util.UUID

import org.cybergarage.upnp.{Service => UpnpService, Device => UpnpDevice, ControlPoint}
import org.cybergarage.upnp.device.DeviceChangeListener

import scala.collection.JavaConverters._

/**
 * Find various things via UPnP
 */
class UpnpSeeker {

  import Seeker._

  def seek(seconds: Int): Map[Device,InetAddress] = {
    class Listener extends ControlPoint with DeviceChangeListener {

      //addDeviceChangeListener(this)

      override def deviceAdded(device: UpnpDevice): Unit =
        println(
          s"""dev add
              |  udn ${device.getUDN} ${device.getUUID}
              |  manufacturer ${device.getManufacture}
              |  model name ${device.getModelName}
              |  friendly name ${device.getFriendlyName}
              |  desc ${Option(device.getModelDescription).getOrElse("n/a")}
              |  dev type ${device.getDeviceType}
              |  remote addr ${device.getSSDPPacket.getRemoteInetAddress}
              |  server ${device.getSSDPPacket.getServer}
              |  services: ${device.getServiceList.asScala.map(_.asInstanceOf[UpnpService]).map(svc => svc.getServiceType + " " + svc.getServiceID).mkString(",")}
              |  """.stripMargin)

      override def deviceRemoved(device: UpnpDevice): Unit = println("dev rm")
    }
    val seeker = new Listener
    seeker.start()
    seeker.search()
    Thread.sleep(seconds * 1000)
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
    val devs = seeker.seek(5)
    println(devs.mkString("\n"))
  }
}
