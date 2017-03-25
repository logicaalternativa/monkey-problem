package com.logicaalternativa.monkeyproblem
package actor

import akka.actor.{ActorLogging,Actor,Props,ActorRef}

import functional.{Monkey}


object RopeActor {
  
  import scalaz.MonadError
  import scala.util.{Either,Right,Left}
  
  type EitherError[T] = Either[Error, T]
  
  val props = Props( new RopeActor )
  
  val EEither: MonadError[EitherError,Error] = new MonadError[EitherError,Error] {
      
      // Members declared in scalaz.Applicative
      def point[A](a: => A): EitherError[A] = Right( a )
      
      // Members declared in scalaz.Bind
      def bind[A, B](fa: EitherError[A])(f: A => EitherError[B]): EitherError[B] = {
        
        fa match {
            case Right( s ) => f( s )
            case Left( e ) => Left( e )
        }
        
        
      }
      
      // Members declared in scalaz.MonadError
      def handleError[A](fa: EitherError[A])(f: Error => EitherError[A]): EitherError[A] = fa
      
      def raiseError[A](e: Error): EitherError[A] = Left( e )
      
    }
  
}


class RopeActor extends Actor with ActorLogging {
  
  import functional._
  import scala.util.Try

  import RopeActor.{EEither, EitherError}
  import functional.MonkeyArrivedFlow.monkeyArrived
  import functional.NewMonkeyInRopeFlow.next
  import functional.MonkeyWantedToOtherSideFlow.wantedOtherSide

  implicit val E = RopeActor.EEither

  implicit val executeEventNewMonkeyInRope =  new ExecuteEventNewMonkeyInRope[EitherError] {
    
    def execEventNewMonkeyInRope: EitherError[EventNewMonkeyInRope] = sendMessageCheckStateEvent 

    def execEventNewMonkeyInRopeWhitDelay : EitherError[EventNewMonkeyInRope] = sendMessageCheckStateEventDelay
    
  }
  
  implicit val executeEventCanAcrossMonkey  =  new ExecuteEventCanAcrossMonkey[EitherError] {
    
    def acrossMonkey( monkey : Monkey ) : EitherError[Unit] = {
      
        sendMessageActorMonkeyCanCross( monkey )
    }
    
  }
 /**
  * */ 
  override def receive = nextBehavior( StateEvents(NotSent), Data( Set[Monkey](), Set[Monkey](), East,0 ) ) 
  
  /**
   * */
  def nextBehavior( stateEvents: StateEvents, data : Data ) : Receive =  {
    
    
    case MonkeyArrived  =>
        log.info( "Received Event 'MonkeyArrived'")
        monkeyArrived[EitherError]( stateEvents, data ) match {
            case Right(( st, da ) ) => 
              //~ log.info( "New data {}" , getLog( da ) )
              context become nextBehavior( st, da )
            case Left( e ) => log.error( "Error to NewMonkeyArrived event {}", e )
        }
        
    case CheckNewMonkeyInRope => 
        log.info( "Received Event 'CheckNewMonkeyInRope'" )
        next[EitherError]( data )  match {
            case Right(( st, da ) ) => 
              log.info( "New data {}" , getLog( da ) )
              context become nextBehavior( st, da )
            case Left( e ) => log.error( "Error to CheckNewMonkeyInRope event {}", e )
        }
        
    case WantToCross( from ) => 
        log.info( "Received Event 'WantToCross' from {} {}", from, getLogWantToCross( sender,from ) )
        wantedOtherSide[EitherError]( Monkey( sender.path.toStringWithoutAddress, from ),stateEvents, data  )  match {
            case Right(( st, da ) ) => context become nextBehavior( st, da )
              //~ log.info( "New data {}" , getLog( da ) )
              context become nextBehavior( stateEvents, da )
            case Left( e ) => log.error( "Error to WantToCross event {}", e )
        }
    case msg => 
      log.info( "Uknown message {}", msg)
      unhandled( msg )
    
  }
  
  def getLog( data : Data ) = {
    
   
    val mE = data.waitingEastToWest.foldLeft( 0 )( ( acc, s ) => acc + 1  )
    val mW = data.waitingWestToEast.foldLeft( 0 )( ( acc, s ) => acc + 1  )
    
    val to = data.to match {
        case East => s">> East ( ${data.numMonkeysInRope} ) >>" 
        case West => s"<< West ( ${data.numMonkeysInRope} ) <<" 
    }
    
    s"""
    
    ****************************************************
      IN THE ROPE                                       
      ===========                                       
                                                 ($mW)    
        O                                         O     
       /|\\ |>---------------------------------<| /|\\    
       / \\                                       / \\    
       ($mE)                                              
                   $to                                   
    ****************************************************
    """
  }
  
  def getLogWantToCross( sender : ActorRef, from : Region ) = {
   
    val name = sender.path.name
    
    if ( from == East ) {
    
      s"""
    
    ****************************************************
      WANT TO CROSS                                    
      =============                                    
                         >> >>
        O/                                             
       /|  |>---------------------------------<|       
       / \\                                            
                                                       
      This is $name. I want to cross to West           
    ****************************************************
    """
    } else {
      
    s"""
    
    ****************************************************
      IN THE ROPE                                       
      ===========                                       
                         << <<                      
                                                \\O     
          |>---------------------------------<|  |\\    
                                                / \\    
                                                                                                 
                                                       
            This is $name. I want to cross to East    
    ****************************************************
    """
    
    }
    
  }
  
  def sendMessageActorMonkeyCanCross( monkey : Monkey ) : EitherError[Unit] =  {
      
      monkey match {
        case Monkey( path, _ ) => 
        context.system.actorSelection( path ) ! CanCross
      }
      
      Right( Unit )
    
  }
  
  /**
   * */
  def sendMessageCheckStateEvent : EitherError[EventNewMonkeyInRope] = {
    
    self ! CheckNewMonkeyInRope
    
    Right( AlreadySentNow )
      
  } 
  
  /**
   * */
  def sendMessageCheckStateEventDelay : EitherError[EventNewMonkeyInRope] = {
    
    import scala.concurrent.duration._
    
    implicit val ec = context.dispatcher
  
    context.system.scheduler.scheduleOnce(1 seconds, self, CheckNewMonkeyInRope)
    
    Right( AlreadySentDelay )
    
  } 
  
}
