package com.logicaalternativa.monkeyproblem

sealed trait Error {
    def description: String
}

sealed trait ValidationError extends Error

case object MonkeysInRopeMinusZero extends Error{
  override def description : String = "The number in the monkeys rope must not be minus zero"
}

case object BothListMonkeyWaitingNotEmpty extends ValidationError{
  override def description : String = "Both list wainting monkey is not empty"
}

