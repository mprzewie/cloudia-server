/**
  * Created by marcin on 5/6/17.
  */

import java.util
import scala.collection.JavaConverters._


import akka.actor._
import communication.Node
import com.typesafe.config.ConfigFactory

import scalaj.http.{Http, HttpOptions}


object Main extends App {

  def ping(url: String): Unit = {
    val result = Http(url)
      .postForm(
        Seq(("systemName", systemName),
          ("host", host),
          ("port", port.toString),
          ("nodeName", nodeName)
        ))
      .option(HttpOptions.readTimeout(10000)).asString
    println(s"pinged $url")
  }

  def host: String = ConfigFactory.load().getString("akka.remote.netty.tcp.hostname")

  def port: Int = ConfigFactory.load().getInt("akka.remote.netty.tcp.port")

  def nodeName: String = ConfigFactory.load().getString("cloudia.nodeName")

  def clients: List[String] = ConfigFactory.load().getStringList("cloudia.clients").asScala.toList

  def systemName: String = ConfigFactory.load().getString("cloudia.systemName")

  def directory: String = ConfigFactory.load().getString("cloudia.directory")

  implicit val chunkSize: Int = 10
  val system = ActorSystem(systemName)
  val cloudia = system.actorOf(Node.props(directory), name = nodeName)
  println("started")
  clients.filter(_.startsWith("http://")).foreach(ping)

  do {
    println("Press Enter to ping all clients listed in application.conf")
    println("You can also enter additional url you want  to ping")
    val newUrl = scala.io.StdIn.readLine()
    (newUrl :: clients).filter(_.startsWith("http://")).foreach(ping)

  } while (true)
}