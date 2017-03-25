package com.logicaalternativa.monkeyproblem

import akka.actor.{ActorLogging,Actor,Props,ActorRef}


object PruebaActor {
  
  import scalaz.MonadError
  import scala.util.{Either,Right,Left}
  import functional._
  import actor._
  import functional.MonkeyWantedToOtherSideFlow.wantedOtherSide
  
  type EitherError[T] = Either[Error, T]
  
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
    
    def execEventNewMonkeyInRope( mySelf : ActorRef ) =  new ExecuteEventNewMonkeyInRope[EitherError] {
    
          def execEventNewMonkeyInRope: EitherError[EventNewMonkeyInRope] = {
            
              mySelf ! CheckNewMonkeyInRope
              
              Right(AlreadySentNow)
            } 

          def execEventNewMonkeyInRopeWhitDelay : EitherError[EventNewMonkeyInRope] = {
             
              mySelf ! CheckNewMonkeyInRope
              
              Right(AlreadySentNow)
            
            }
        
        }
    
    def wantedOtherSideInt( mySelf : ActorRef )(monkey: Monkey, stateEvents : StateEvents, data : Data) : EitherError[(StateEvents, Data)]= {
    
     val execEventNewMonkeyInRope =  new ExecuteEventNewMonkeyInRope[EitherError] {
    
          def execEventNewMonkeyInRope: EitherError[EventNewMonkeyInRope] = {
            
              mySelf ! CheckNewMonkeyInRope
              
              Right(AlreadySentNow)
            } 

          def execEventNewMonkeyInRopeWhitDelay : EitherError[EventNewMonkeyInRope] = {
             
              mySelf ! CheckNewMonkeyInRope
              
              Right(AlreadySentNow)
            
            }
        
        }
                  
      wantedOtherSide[EitherError](monkey, stateEvents, data )(execEventNewMonkeyInRope, EEither)
      
    }
  
  
  
}

class PruebaActor extends Actor with ActorLogging {
   
  import functional._
  import actor._
  import PruebaActor.EitherError
  
  val wOtherSide = PruebaActor.wantedOtherSideInt( self ) _
  
  var data =  Data( Set[Monkey](), Set[Monkey](), East,0 )
  
  var stateEvents = StateEvents(NotSent)
  
  implicit val EE = PruebaActor.EEither
  implicit val execEventNewMonkeyInRope = PruebaActor.execEventNewMonkeyInRope( self )
  
  override def receive = {
    
    case CreateNewMonkey => 
        //~ wOtherSide( Monkey( "", East ), stateEvents, data )
        MonkeyWantedToOtherSideFlow.wantedOtherSide[EitherError]( Monkey( "", East ), stateEvents, data )
        self ! "ECCCHO   " + CreateNewMonkey 
    case m : String =>
        println( "Soy capaz " + m )
    case msg => 
      self ! "ECCCHO   " + msg 
    
  }
    
  
  
}



object Prueba extends App {
  
  import akka.actor.{ActorSystem}
  import actor.{SimulationActor,CreateNewMonkey}
  
  
  val numberMonkeys : Int = 1
  
  implicit val system = ActorSystem("monkey-problem")
  
  val simulationActor = system.actorOf( Props( classOf[PruebaActor] ), "pruebaActor" )
  
  simulationActor ! CreateNewMonkey   
  
  Thread.sleep( 5000 )
  
  simulationActor ! CreateNewMonkey   
  
}
