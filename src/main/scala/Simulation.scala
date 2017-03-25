package com.logicaalternativa.monkeyproblem

object Simulation extends App {
  
  import akka.actor.{ActorSystem}
  import actor.{SimulationActor,CreateNewMonkey}
  
  
  val numberMonkeys : Int = 5
  
  implicit val system = ActorSystem("monkey-problem")
  
  val simulationActor = system.actorOf( SimulationActor.props( numberMonkeys ), "simulation" )
  
  simulationActor ! CreateNewMonkey
  
}
