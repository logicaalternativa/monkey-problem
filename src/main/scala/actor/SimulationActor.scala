package com.logicaalternativa.monkeyproblem
package actor

import akka.actor.{ActorLogging,Actor,Props,ActorRef}

import functional.{Monkey,East, West}


object SimulationActor {
  
  def props( numMonkeys : Int ) = Props( classOf[SimulationActor], numMonkeys )
  
}


class SimulationActor( numMonkeys : Int ) extends Actor with ActorLogging {
  
 
  
 val ropeActor : ActorRef = context.system.actorOf ( RopeActor.props, "rope" )
  
 
 
  override def receive = nextBehavior( numMonkeys, ropeActor )
  
  /**
   * */
  
  def nextBehavior( numM : Int, rActor : ActorRef ) : Receive =  {
    
    case CreateNewMonkey => 
    
      sendMessageCreateMonkey( numM, rActor )
      
      val newNum = numM - 1
      
      newNum match {
        case 0 =>  context stop ( self )
        case _ => context become nextBehavior( newNum , rActor )
      }
      
    case msg => 
      log.info( "Unknow message {}" , msg  )
      unhandled( msg )
    
  }
  
  def sendMessageCreateMonkey( numM : Int, rActor : ActorRef ) = {
    
    import scala.concurrent.duration._
    import scala.util.Random.nextInt
   
    val nextTime = nextInt( 9 ) + 1
      
    val fromRegion = nextInt(2) match {
      case 0 => East
      case _ => West 
      
    }
    
    val monkeyActor = context.system.actorOf( MonkeyActor.props( rActor, fromRegion ), "monkey-" + (numMonkeys-numM) )
    
    monkeyActor ! InitMonkey
    
    log.info( "Next time for create a new Monkey: {} seconds" , nextTime )
    
    context.system.scheduler.scheduleOnce(nextTime seconds, self, CreateNewMonkey)( context.dispatcher ) 
    
  }
  
  
}
