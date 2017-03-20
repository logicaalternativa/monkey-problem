package com.logicaalternativa.monkeyproblem
package functional


sealed trait Region
case object East extends Region
case object West extends Region

case class Monkey(id : String, in : Region)
case class Data( waitingEastToWest : Set[Monkey], waitingWestToEast : Set[Monkey], to : Region, numMonkeysInRope : Int )
 
