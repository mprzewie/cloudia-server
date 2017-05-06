import akka.actor._

/**
  * Created by marcin on 5/6/17.
  */
class Receiver extends Actor{
  override def receive: PartialFunction[Any, Unit] = {
    case msg: String =>
      println(s"Receiver received '$msg'")
    case manifesto: FileManifesto =>
      println(manifesto.name)
      sender ! "ready"


  }
}

object Main extends App {
  val system = ActorSystem("cloudia-server")
  val receiver = system.actorOf(Props[Receiver], name = "manifesto-receiver")

}