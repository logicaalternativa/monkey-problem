package com.logicaalternativa.monkeyproblem
package actor

import akka.actor.{ActorLogging,Actor,Props, ActorRef}

import functional.{Region}


object MonkeyActor {
  
  def props( ropeActor : ActorRef, from : Region ) = Props( classOf[MonkeyActor], ropeActor, from )
  
}


class MonkeyActor( val ropeActor : ActorRef, from : Region ) extends Actor with ActorLogging {
  
  import UtilLogSimulation._
  import akka.event.{Logging}
  
  implicit val logSimulation = Logging( context system, UtilLogSimulation.nameLoger)
  
 /**
  * */ 
  override def receive = {
      case InitMonkey => 
        ropeActor ! WantToCross( from )
      case CanCross =>
        logCanCross( self.path.name, from )
        Thread.sleep( 4000 )
        sender ! MonkeyArrived( from )
        context stop ( self )
      case msg =>
        log.info( "Unknown message {}", msg )
        unhandled( msg )
    
  }
  
  
  
}
