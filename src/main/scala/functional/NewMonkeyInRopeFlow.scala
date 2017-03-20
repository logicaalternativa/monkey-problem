package com.logicaalternativa.monkeyproblem
package functional


trait NewMonkeyInRopeFlow [P[_]] {
  
  import scalaz.MonadError
  import scalaz.syntax.monadError._
  
  implicit val E: MonadError[P,Error]
  
  def sendMessageCanAcross( monkey : Monkey ) : P[Unit]
  
  def next( data : Data ) : P[(Boolean, Data)] =  {
    
    for {
      
      _ <- checkIfListIsNotEmpty(data.waitingEastToWest, data.waitingWestToEast ) 
      
      set  <- data.to match {
              case East => data.waitingEastToWest.pure
              case West => data.waitingWestToEast.pure 
            } 
            
      newSet <- sendMessageAndUpdateSet( set )
      
      newData <- data.to match {
              case East => data.copy( waitingEastToWest = newSet).pure
              case West => data.copy( waitingWestToEast = newSet).pure 
            } 
      
      
    } yield ( true, newData ) 
      
  }
  
  private def checkIfListIsNotEmpty( waitingEast : Set[Monkey], waitingWest : Set[Monkey]) : P[Unit] = {
    
    val a = checkIfSetIsNotEmpty( waitingEast )
    val b = checkIfSetIsNotEmpty( waitingWest )
    
    for {
      
      validation <- ( a |@| b )( _ && _ )
      
      _ <- validation match {
              case true => ( BothListMonkeyWaitingNotEmpty : Error) raiseError
              case _ => E pure( Unit )
            }
      
      
    } yield ()
    
  }
  
  
   private def checkIfSetIsNotEmpty( set : Set[Monkey] ) : P[Boolean] = {
    
    set.headOption.fold( false )( s => true  ).pure
    
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
