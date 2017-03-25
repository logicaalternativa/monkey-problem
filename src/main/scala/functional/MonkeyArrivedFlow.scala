package com.logicaalternativa.monkeyproblem
package functional


object MonkeyArrivedFlow {
  
  import scalaz.MonadError
  import scalaz.syntax.monadError._
  
  def monkeyArrived [P[_]] ( stateEvents: StateEvents, data : Data )( implicit ENM : ExecuteEventNewMonkeyInRope[P], E: MonadError[P,Error] ): P[ (StateEvents,Data) ]= {
    
    for {
      
      newNumMonkeys <- data.numMonkeysInRope match {
                        case 0 => (MonkeysInRopeMinusZero: Error) .raiseError
                        case num => ( num - 1 ).pure
                      }
      
      newData       <- data.copy( numMonkeysInRope = newNumMonkeys ).pure
      
      event <- stateEvents match {
        
                  case StateEvents( NotSent ) => ENM.execEventNewMonkeyInRope
                  case StateEvents( e )  => e.pure
        
                }
      
    } yield (StateEvents( event ), newData )
    
  }
  
}
