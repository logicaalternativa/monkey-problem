package com.logicaalternativa.monkeyproblem
package actor

import akka.actor.{ActorLogging,Actor,Props,ActorRef}

import functional.{Monkey,East, West}


object SimulationActor {
  
  def props( numMonkeys : Int, ropeActor : ActorRef ) = Props( classOf[SimulationActor], numMonkeys, ropeActor )
  
}


class SimulationActor( numMonkeys : Int, ropeActor : ActorRef ) extends Actor with ActorLogging {
  
  override def receive = nextBehavior( numMonkeys, ropeActor )
 
  /**
   * */
  def nextBehavior( numM : Int, rActor : ActorRef ) : Receive =  {
    
    case CreateNewMonkey => 
    
      val newNum = numM - 1
      
      newNum match {
        case -1 =>         
          log.info( "END of create monkeys")
          context stop( self )
        case _ => 
          sendMessageCreateMonkey( numM, rActor )
          context become nextBehavior( newNum , rActor )
      }
      
    case msg => 
      log.info( "Unknown message {}" , msg  )
      unhandled( msg )
    
  }
  
  def sendMessageCreateMonkey( numM : Int, rActor : ActorRef ) = {
    
    import scala.concurrent.duration._
    import scala.util.Random.nextInt
   
    val nextTime = nextInt( 8 ) + 1
      
    val fromRegion = nextInt(2) match {
      case 0 => East
      case _ => West      
    }
    
    val monkeyActor = context.system.actorOf( MonkeyActor.props( rActor, fromRegion ), "monkey-" + (numMonkeys- numM + 1) )
    
    monkeyActor ! InitMonkey
    
    log.info( "Next time for create a new Monkey: {} seconds" , nextTime )
    
    context.system.scheduler.scheduleOnce(nextTime seconds, self, CreateNewMonkey)( context.dispatcher ) 
    
  }
  
  
}
