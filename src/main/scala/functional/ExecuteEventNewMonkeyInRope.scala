package com.logicaalternativa.monkeyproblem
package functional

trait ExecuteEventNewMonkeyInRope [P[_]] {
  
  //~ import scalaz.MonadError
  //~ import scalaz.syntax.monadError._
  
  //~ implicit val E: MonadError[P,Error]
  
  def execEventNewMonkeyInRope: P[EventNewMonkeyInRope]
  
  def execEventNewMonkeyInRopeWhitDelay: P[EventNewMonkeyInRope]

}
