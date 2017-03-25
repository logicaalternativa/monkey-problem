package com.logicaalternativa.monkeyproblem

object Simulation extends App {
  
  import akka.actor.{ActorSystem}
  import actor.{SimulationActor,CreateNewMonkey, RopeActor}
  
  
  val numberMonkeys : Int = 20
  
  implicit val system = ActorSystem("monkey-problem")
  
  val ropeActor = system.actorOf ( RopeActor.props, "rope" )
  
  val simulationActor = system.actorOf( SimulationActor.props( numberMonkeys, ropeActor ), "simulation" )
  
  simulationActor ! CreateNewMonkey
  
}
