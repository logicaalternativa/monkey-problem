package com.logicaalternativa.monkeyproblem

sealed trait Error {
    def description: String
}

case object MonkeysInRopeMinusZero extends Error{
  override def description : String = "The number in the monkeys rope must not be minus zero"
}

