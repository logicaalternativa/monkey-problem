package com.logicaalternativa.monkeyproblem
package actor

import akka.actor.{ActorLogging,Actor,Props, ActorRef}

import functional.{Region}


object MonkeyActor {
  
  def props( ropeActor : ActorRef, from : Region ) = Props( classOf[MonkeyActor], ropeActor, from )
  
}


class MonkeyActor( val ropeActor : ActorRef, from : Region ) extends Actor with ActorLogging {
  
 /**
  * */ 
  override def receive = {
      case InitMonkey => 
        // Traza
        println( s">>>>>> ropeActor $ropeActor" )
        
        // Fin de traza
        ropeActor ! WantToCross( from )
      case CanCross => 
        Thread.sleep( 4000 )
        sender ! MonkeyArrived
        context stop ( self )
      case msg =>
        log.info( "Unknown message {}", msg )
        unhandled( msg )
    
  }
  
  
  
}
