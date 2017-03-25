package com.logicaalternativa.monkeyproblem
package actor



import functional.{Region}

sealed trait Protocol

case object CreateNewMonkey extends Protocol
case object MonkeyArrived extends Protocol
case object CheckNewMonkeyInRope extends Protocol
case class WantToCross( from :Region ) extends Protocol
case object InitMonkey extends Protocol
case object CanCross extends Protocol
