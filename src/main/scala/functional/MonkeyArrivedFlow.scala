package com.logicaalternativa.monkeyproblem
package functional


import scala.util.Try


trait MonkeyArrivedFlow [P[_]] {
  
  import scalaz.MonadError
  import scalaz.syntax.monadError._
  
  implicit val E: MonadError[P,Error]
  
  def monkeyArrived( data : Data ) : P[Data] = {
    
    for {
      
      newNumMonkeys <- data.numMonkeysInRope match {
                        case 0 => (MonkeysInRopeMinusZero: Error) .raiseError
                        case num => ( num - 1 ).pure
                      }
      
      newData       <- data.copy( numMonkeysInRope = newNumMonkeys ).pure
      
    } yield newData
    
  }
  
}
