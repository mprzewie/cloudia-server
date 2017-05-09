/**
  * Created by marcin on 5/6/17.
  */

import akka.actor._
import com.typesafe.config.ConfigFactory
import communication.Cloudia


object Main extends App {

  implicit val host = ConfigFactory.load().getString("akka.remote.netty.tcp.hostname")
  implicit val port = ConfigFactory.load().getString("akka.remote.netty.tcp.port").toInt
  implicit val chunkSize: Long = 1024
  val system = ActorSystem("cloudia-server")
  val cloudia = system.actorOf(Props(new Cloudia()), name = "receiver")

}