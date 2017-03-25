package com.logicaalternativa.monkeyproblem
package functional

import org.scalatest.{FlatSpec,Matchers,GivenWhenThen}

class MonkeyArrivedFlowSpec extends FlatSpec with Matchers with GivenWhenThen {
  
  import scala.util.{Try,Success,Failure}
  
  implicit val E = UtilTest.ETry
  
  import MonkeyArrivedFlow._
  
  implicit val executeEventNewMonkeyInRope =  new ExecuteEventNewMonkeyInRope[Try] {
    
    def execEventNewMonkeyInRope: Try[EventNewMonkeyInRope] = Success( AlreadySentNow )
  
    def execEventNewMonkeyInRopeWhitDelay : Try[EventNewMonkeyInRope] = Success( AlreadySentDelay )
    
  }
    
    
  "If a monkey arrived to the other side and there are more than one in the rope " + 
  "StateEvent is differrent NotSent" should "the number monkeys is decrement " +
  "minus one and state evend should not change " in {
          
     Given("a flow and data with monkeys in rope")
     
     val setEmpty = Set[Monkey]()
     val to = East
     val numMonkeysInRope = 3
     
     val data = Data( setEmpty, setEmpty, to, numMonkeysInRope )      
     
     When("it is called to monkeyArrived")
     val newData  = monkeyArrived[Try]( StateEvents(AlreadySentNow), data )
     
     Then( "it should only change the value of the monkeys in rope" )      
     newData match {
          
        case Success( ( StateEvents(eventInRope), Data( mw, me, t, newValueMonkeys) ) ) => 
          eventInRope should be ( AlreadySentNow )
          mw should be ( setEmpty )
          me should be ( setEmpty )
          t should be ( to )
          newValueMonkeys should be ( numMonkeysInRope -1 )
        case Failure( e ) => 
          fail( s"It must not be a error : ${e.getMessage}" )
     
    }
    
  }
  
  "If a monkey arrived to the other side and there is one in the rope " + 
  "StateEvent equals NotSent" should "the number monkeys is decrement " +
  "minus one and state event should change AlreadySentNow " in {
          
     Given("a flow and data with monkeys in rope")
     
     val setEmpty = Set[Monkey]()
     val to = East
     val numMonkeysInRope = 3
     
     val data = Data( setEmpty, setEmpty, to, numMonkeysInRope )      
     
     When("it is called to monkeyArrived")
     val newData  = monkeyArrived[Try]( StateEvents(NotSent), data )
     
     Then( "it should only change the value of the monkeys in rope" )      
     newData match {
          
        case Success( ( StateEvents(eventInRope), Data( mw, me, t, newValueMonkeys) ) ) => 
          eventInRope should be ( AlreadySentNow )
          mw should be ( setEmpty )
          me should be ( setEmpty )
          t should be ( to )
          newValueMonkeys should be ( numMonkeysInRope -1 )
        case Failure( e ) => 
          fail( s"It must not be a error : ${e.getMessage}" )
     
    }
    
  }
  
  
  "If a monkey arrived to the other side and there is not any monkey in the rope " should 
        "throws an errot. It is not possible minus zero monkey sin rope " in {
          
     Given("a flow and data with any monkey in rope")
     
     val setEmpty = Set[Monkey]()
     val to = East
     val numMonkeysInRope = 0
     
     val data = Data( setEmpty, setEmpty, to, numMonkeysInRope )      
     
     When("it is called to monkeyArrived")
     val newData  = monkeyArrived[Try](StateEvents(AlreadySentNow), data )
     
     Then( "it should throws an MonkeysInRopeMinusZero error" )      
     newData match {
        case Failure( e ) => 
          e.getMessage should be ( MonkeysInRopeMinusZero.description )
        case Success( data ) => 
          fail( s"It must not be a data : $data" )
     
    }
    
  }
  
}
