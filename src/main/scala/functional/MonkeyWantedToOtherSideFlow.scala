package com.logicaalternativa.monkeyproblem
package functional


trait MonkeyWantedToOtherSideFlow [P[_]] {
  
  import scalaz.MonadError
  import scalaz.syntax.monadError._
  import scalaz.syntax._
  
  implicit val E: MonadError[P,Error]
  
  def wantedOtherSide( monkey : Monkey, data : Data ) : P[Data] =  {
    
    monkey match {
        case Monkey( _ , East ) =>  data.copy( waitingEastToWest = data.waitingEastToWest + monkey ).pure
        case Monkey( _ , West ) =>  data.copy( waitingWestToEast = data.waitingWestToEast + monkey ).pure
    } 
    
  }
  
  
  
}
