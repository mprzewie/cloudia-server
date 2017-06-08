/**
  * Created by marcin on 5/6/17.
  */

import akka.actor._
import communication.Node


object Main extends App {

  implicit val chunkSize: Int = 10
  val system = ActorSystem("cloudia-server")
  val cloudia = system.actorOf(Node.props("/home/marcin/Documents/Coding/cloudia/test2"), name = "server")

}