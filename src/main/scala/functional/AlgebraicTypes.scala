package com.logicaalternativa.monkeyproblem
package functional

case class StateEvents( eMonkeyInRope : EventNewMonkeyInRope )

sealed trait EventNewMonkeyInRope
case object AlreadySentNow extends EventNewMonkeyInRope
case object AlreadySentDelay extends EventNewMonkeyInRope
case object NotSent extends EventNewMonkeyInRope


sealed trait Region
case object East extends Region
case object West extends Region

case class Monkey(id : String, in : Region)
case class Data( 
                 waitingEastToWest : Set[Monkey], 
                 waitingWestToEast : Set[Monkey], 
                 to : Region, 
                 numMonkeysInRope : Int ) {
  
  def isEmpty : Boolean = {
      
    waitingEastToWest.headOption.fold( true )( _ => false ) &&
    waitingWestToEast.headOption.fold( true )( _ => false ) &&
    numMonkeysInRope == 0
    
  }
  
}
 
