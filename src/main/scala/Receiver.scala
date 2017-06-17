/**
  * Created by marcin on 5/6/17.
  */

import scala.collection.JavaConverters._
import akka.actor._
import communication.Node
import com.typesafe.config.{Config, ConfigFactory}
import communication.Request



object Main extends App {

  def refPathFromConfig(config: Config): String = {
    val host = config.getString("host")
    val port = config.getInt("port").toString
    val systemName = config.getString("systemName")
    val controllerName = config.getString("controllerName")
    s"akka.tcp://$systemName@$host:$port/user/$controllerName"
  }

  def ping(url: String): Unit = {
    system.actorSelection(url).tell(Request(nodeName), cloudia)
    println(s"pinged $url")
  }

  def nodeName: String = ConfigFactory.load().getString("cloudia.nodeName")

  def clients: List[Config] = ConfigFactory.load().getConfigList("cloudia.clients").asScala.toList

  def systemName: String = ConfigFactory.load().getString("cloudia.systemName")

  def directory: String = ConfigFactory.load().getString("cloudia.directory")

  implicit val chunkSize: Int = 10
  val system = ActorSystem(systemName)
  val cloudia = system.actorOf(Node.props(directory), name = nodeName)
  println("started")
  clients.map(refPathFromConfig).foreach(ping)

  do {
    println("Press Enter to ping all clients listed in application.conf")
    println("You can also enter additional url you want  to ping")
    val newUrl = scala.io.StdIn.readLine()
    (newUrl :: clients.map(refPathFromConfig)).filter(_.startsWith("akka.tcp://")).foreach(ping)

  } while (true)


}