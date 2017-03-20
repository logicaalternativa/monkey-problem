package com.logicaalternativa.monkeyproblem
package functional


trait NewMonkeyInRopeFlow [P[_]] {
  
  import scalaz.MonadError
  import scalaz.syntax.monadError._
  
  implicit val E: MonadError[P,Error]
  
  def sendMessageCanAcross( monkey : Monkey ) : P[Unit]
  
  def next( data : Data ) : P[(Boolean, Data)] =  {
    
    for {
      
      newDirection <- newTo( data )
      
      set  <- newDirection match {
              case East => data.waitingEastToWest.pure
              case West => data.waitingWestToEast.pure 
            } 
            
      newSet <- sendMessageAndUpdateSet( set )
      
      newData <- newDirection match {
        
              case East => data.copy(
                                      waitingEastToWest = newSet, 
                                      numMonkeysInRope = data.numMonkeysInRope  + 1,
                                      to = newDirection
                                    ).pure
              case West => data.copy( 
                                      waitingWestToEast = newSet, 
                                      numMonkeysInRope = data.numMonkeysInRope  + 1,
                                      to = newDirection
                                    ).pure 
            } 
      
      
    } yield ( true, newData ) 
      
  }
  
  
  private def newTo( data : Data ) : P[Region] = {
    
    val waitingEastIsNotEmpty = data.waitingEastToWest.headOption.fold( false )( s => true  )
    val waitingWestIsNotEmpty = data.waitingWestToEast.headOption.fold( false )( s => true  )
    
    
    if ( waitingEastIsNotEmpty &&  waitingWestIsNotEmpty ) {
                    
      data.numMonkeysInRope match {
          
          case 0 => data.to match {
                      case East => (West : Region ).pure
                      case West => (East : Region ).pure                 
                    }
          case _ => ( BothListMonkeyWaitingNotEmpty : Error) raiseError
          
       } 
          
    } else if ( waitingEastIsNotEmpty  ) {
     (East : Region ).pure         
    } else {
      (West : Region ).pure
    }
    
  }
  
  
  private def sendMessageAndUpdateSet( set: Set[Monkey] ) : P[Set[Monkey]] = {
    
    for {
      
      monkey <- set.headOption match {
                  case Some( m ) => m.pure
                  case None =>  ( ListMonkeyWaitingEmpty :Error ).raiseError
                }
                
      _  <- sendMessageCanAcross( monkey )
      
    } yield ( set.tail )
    
  }
  
  
}
