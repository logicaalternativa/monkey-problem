package com.logicaalternativa.monkeyproblem
package functional

import org.scalatest.{FlatSpec,Matchers,GivenWhenThen}

class MonkeyArrivedFlowSpec extends FlatSpec with Matchers with GivenWhenThen {
  
  import scala.util.{Try,Success,Failure}
  import UtilTest.ETry
  
  class MonkeyArrivedFlowImpTry extends MonkeyArrivedFlow[Try] {
    
    implicit val E = ETry
    
  }
    
    
  "If a monkey arrived to the other side and there are more than one in the rope " should 
        "the number monkeys is decrement minus one" in {
          
     Given("a flow and data with monkeys in rope")
     val monkeyArrivedFlow = new MonkeyArrivedFlowImpTry
    
     val setEmpty = Set[Monkey]()
     val to = East
     val numMonkeysInRope = 3
     
     val data = Data( setEmpty, setEmpty, to, numMonkeysInRope )      
     
     When("it is called to monkeyArrived")
     val newData  = monkeyArrivedFlow.monkeyArrived( data )
     
     Then( "it should only change the value of the monkeys in rope" )      
     newData match {
        case Success( Data( mw, me, t, newValueMonkeys) ) => 
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
     val monkeyArrivedFlow = new MonkeyArrivedFlowImpTry
    
     val setEmpty = Set[Monkey]()
     val to = East
     val numMonkeysInRope = 0
     
     val data = Data( setEmpty, setEmpty, to, numMonkeysInRope )      
     
     When("it is called to monkeyArrived")
     val newData  = monkeyArrivedFlow.monkeyArrived( data )
     
     Then( "it should throws an MonkeysInRopeMinusZero error" )      
     newData match {
        case Failure( e ) => 
          e.getMessage should be ( MonkeysInRopeMinusZero.description )
        case Success( data ) => 
          fail( s"It must not be a data : $data" )
     
    }
    
  }
  
}
