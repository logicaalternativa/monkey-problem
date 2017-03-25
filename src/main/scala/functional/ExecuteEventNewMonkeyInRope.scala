package com.logicaalternativa.monkeyproblem
package functional

trait ExecuteEventNewMonkeyInRope [P[_]] {
  
  def execEventNewMonkeyInRope: P[EventNewMonkeyInRope]
  
  def execEventNewMonkeyInRopeWhitDelay: P[EventNewMonkeyInRope]

}
