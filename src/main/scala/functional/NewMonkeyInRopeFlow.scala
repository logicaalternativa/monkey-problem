package com.logicaalternativa.monkeyproblem
package functional


object NewMonkeyInRopeFlow {
  
  import scalaz.MonadError
  import scalaz.syntax.monadError._
  
  def next[P[_]]( data : Data )( implicit ECC : ExecuteEventCanAcrossMonkey[P], ENM : ExecuteEventNewMonkeyInRope[P], E: MonadError[P,Error] ) : P[ (StateEvents,Data) ] =  {
    
    val waitingEastIsNotEmpty = data.waitingEastToWest.headOption.fold( false )( _ => true  )
      
    val waitingWestIsNotEmpty = data.waitingWestToEast.headOption.fold( false )( _ => true  )
    
    
    if ( data.isEmpty ) {
        
      ( StateEvents( NotSent ), data ).pure
      
    } else if ( isNotEmptySetsOfBothSides( data ) && data.numMonkeysInRope > 0 ) {
      
      ENM.execEventNewMonkeyInRopeWhitDelay map (s => ( StateEvents( s ), data  )  ) 
      
    } else { 
      
      for {
        
        newData <- nextIfDataIsNotEmpty( data )
        
        stateEvent <- ENM.execEventNewMonkeyInRopeWhitDelay
        
      } yield ( StateEvents( stateEvent ), newData  )     
      
    } 
      
  }
  
  private def nextIfDataIsNotEmpty[P[_]]( data : Data )( implicit ECC : ExecuteEventCanAcrossMonkey[P],  ENM : ExecuteEventNewMonkeyInRope[P], E: MonadError[P,Error] )  :  P[ Data ] = {
    
    for {
      
      newDirection <- newTo( data )
      
      set  <- newDirection match {
              case East => data.waitingEastToWest.pure
              case West => data.waitingWestToEast.pure 
            } 
            
      newMonInRope  <- set.headOption match {
                  case Some( monkey ) => 
                        ECC.acrossMonkey( monkey )
                        ( data.numMonkeysInRope + 1 ).pure
                  case None =>  data.numMonkeysInRope.pure
                }
            
      newSet <- set.headOption match {
                  case _ : Some[Monkey] => set.tail.pure
                  case None =>  Set[Monkey]().pure
                }
                
      newData <- newDirection match {
              case East => data.copy( waitingEastToWest = newSet ).pure
              case West => data.copy( waitingWestToEast = newSet ).pure 
            }
            
    } yield ( 
              newData.copy( 
                             numMonkeysInRope = newMonInRope,
                             to = newDirection
                          )
              
            ) 
    
  }
  
  private def newTo[P[_]]( data : Data )( implicit E: MonadError[P,Error] )  : P[Region] = {
    
    val waitingEastIsNotEmpty = data.waitingEastToWest.headOption.fold( false )( _ => true  )
      
    val waitingWestIsNotEmpty = data.waitingWestToEast.headOption.fold( false )( _ => true  )
    
    if ( isNotEmptySetsOfBothSides( data ) && data.numMonkeysInRope == 0 ) {
          
          data.to match {
                      case East => ( West : Region ).pure
                      case West => ( East : Region ).pure                 
                    }
          
    } else {
      
       data.to.pure
    }
    
  }
  
  
  private def isNotEmptySetsOfBothSides( data: Data )  : Boolean = {
    
    val waitingEastIsNotEmpty = data.waitingEastToWest.headOption.fold( false )( _ => true  )
      
    val waitingWestIsNotEmpty = data.waitingWestToEast.headOption.fold( false )( _ => true  )
    
    waitingEastIsNotEmpty && waitingWestIsNotEmpty
    
  } 
  
  
}
