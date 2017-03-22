package com.logicaalternativa.monkeyproblem
package functional

trait ExecuteEventCanAcrossMonkey [P[_]] {
  
  //~ import scalaz.MonadError
  //~ import scalaz.syntax.monadError._
  
  //~ implicit val E: MonadError[P,Error]
  
  def acrossMonkey( monkey : Monkey ): P[Unit]

}
