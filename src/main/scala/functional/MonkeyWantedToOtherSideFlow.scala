package com.logicaalternativa.monkeyproblem
package functional
import scala.concurrent.duration.Duration


object MonkeyWantedToOtherSideFlow  {
  
  import scalaz.MonadError
  import scalaz.syntax.monadError._
  import scalaz.syntax._
  
  def wantedOtherSide[P[_]]( monkey : Monkey, stateEvents: StateEvents, data : Data )( implicit ENM : ExecuteEventNewMonkeyInRope[P], E: MonadError[P,Error] ): P[(StateEvents, Data)] =  {
   
   for {  
     
      newData <- monkey match {
                  case Monkey( _ , East ) =>  data.copy( waitingEastToWest = data.waitingEastToWest + monkey ).pure
                  case Monkey( _ , West ) =>  data.copy( waitingWestToEast = data.waitingWestToEast + monkey ).pure
                }
      
      event <-  stateEvents match {
                    case StateEvents( NotSent ) => ENM.execEventNewMonkeyInRope
                    case StateEvents( e )  => e.pure
                }
        
    } yield ( StateEvents( event ), newData )
    
  }
  
  
  
}
