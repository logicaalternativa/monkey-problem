package com.logicaalternativa.monkeyproblem
package functional

import org.scalatest.{FlatSpec,Matchers,GivenWhenThen}

class MonkeyWantedToOtherSideFlowSpec extends FlatSpec with Matchers with GivenWhenThen {
  
  import scala.util.{Try,Success,Failure}
  import UtilTest.ETry
  
  class MonkeyWantedToOtherSideFlowImpTry extends MonkeyWantedToOtherSideFlow[Try] {
    
    implicit val E = ETry
  }
    
    
  "If a monkey that is in the west and wants to pass east region" should 
        "should be added to list East" in {
          
     Given("a flow and a monkey that wants to pass to east side")
     val monkeyArrivedFlow = new MonkeyWantedToOtherSideFlowImpTry
    
     val setEmpty = Set[Monkey]()
     val to = East
     val numMonkeysInRope = 3
     
     val data = Data( setEmpty, setEmpty, to, numMonkeysInRope )   
     
     val monkey = Monkey( "1", West )   
     
     When("it is called to wantedOtherSide")
     val newData  = monkeyArrivedFlow.wantedOtherSide( monkey, data )
     
     Then( "it should only change the value of the east list and the monkey is added" )      
     newData match {
        case Success( Data( mw, me, t, n) ) => 
          mw should be ( setEmpty )
          me.head should be ( monkey )
          t should be ( to )
          n should be ( numMonkeysInRope)
        case Failure( e ) => 
          fail( s"It must not be a error : ${e.getMessage}" )
     
    }
    
  }
  
  "If a monkey that is in the east and wants to pass west region" should 
        "should be added to list East" in {
          
     Given("a flow and a monkey that wants to pass to west side")
     val monkeyArrivedFlow = new MonkeyWantedToOtherSideFlowImpTry
    
     val setEmpty = Set[Monkey]()
     val to = East
     val numMonkeysInRope = 3
     
     val data = Data( setEmpty, setEmpty, to, numMonkeysInRope )   
     
     val monkey = Monkey( "1", East )   
     
     When("it is called to wantedOtherSide")
     val newData  = monkeyArrivedFlow.wantedOtherSide( monkey, data )
     
     Then( "it should only change the value of the west list and the monkey is added" )      
     newData match {
        case Success( Data( mw, me, t, n) ) => 
          mw.head should be ( monkey )
          me should be ( setEmpty )
          t should be ( to )
          n should be ( numMonkeysInRope)
        case Failure( e ) => 
          fail( s"It must not be a error : ${e.getMessage}" )
     
    }
    
  }
  
}
