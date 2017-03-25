package com.logicaalternativa.monkeyproblem
package functional

import org.scalatest.{FlatSpec,Matchers,GivenWhenThen}

class MonkeyWantedToOtherSideFlowSpec extends FlatSpec with Matchers with GivenWhenThen {
  
  import scala.util.{Try,Success,Failure}
  implicit val E = UtilTest.ETry
  
  import MonkeyWantedToOtherSideFlow._
  
  implicit val executeEventNewMonkeyInRope =  new ExecuteEventNewMonkeyInRope[Try] {
    
    def execEventNewMonkeyInRope: Try[EventNewMonkeyInRope] = Success( AlreadySentNow )
  
    def execEventNewMonkeyInRopeWhitDelay : Try[EventNewMonkeyInRope] = Success( AlreadySentDelay )
    
  } 
    
  "1.- If a monkey that is in the west and wants to pass east region" +
  "StateEvent is differrent NotSent" should "should be added to list East" in {
          
     Given("a flow and a monkey that wants to pass to east side")
    
     val setEmpty = Set[Monkey]()
     val to = East
     val numMonkeysInRope = 3
     
     val data = Data( setEmpty, setEmpty, to, numMonkeysInRope )   
     
     val monkey = Monkey( "1", West )   
     
     When("it is called to wantedOtherSide")
     val newData  = wantedOtherSide[Try]( monkey, StateEvents(AlreadySentNow), data )
     
     Then( "it should only change the value of the east list and the monkey is added" )      
     newData match {
        case Success( ( StateEvents(eventInRope), Data( mw, me, t, n) ) ) => 
          eventInRope should be ( AlreadySentNow )
          mw should be ( setEmpty )
          me.head should be ( monkey )
          t should be ( to )
          n should be ( numMonkeysInRope)
        case Failure( e ) => 
          fail( s"It must not be a error : ${e.getMessage}" )
     
    }
    
  }
  
   "2.- If a monkey that is in the west and wants to pass east region " +
   "StateEvent is NotSent" should "should be added to list East and " +
   "execute Event check rope without delay " in {
          
     Given("a flow and a monkey that wants to pass to east side")
    
     val setEmpty = Set[Monkey]()
     val to = East
     val numMonkeysInRope = 3
     
     val data = Data( setEmpty, setEmpty, to, numMonkeysInRope )   
     
     val monkey = Monkey( "1", West )   
     
     When("it is called to wantedOtherSide")
     val newData  = wantedOtherSide[Try]( monkey, StateEvents(NotSent), data )
     
     Then( "it should only change the value of the east list and the monkey is added and event is executed" )      
     newData match {
        case Success( ( StateEvents(eventInRope), Data( mw, me, t, n) ) ) => 
          eventInRope should be ( AlreadySentNow )
          mw should be ( setEmpty )
          me.head should be ( monkey )
          t should be ( to )
          n should be ( numMonkeysInRope)
        case Failure( e ) => 
          fail( s"It must not be a error : ${e.getMessage}" )
     
    }
    
  }
  
  "3.- If a monkey that is in the east and wants to pass west region" should 
        "should be added to list East" in {
          
     Given("a flow and a monkey that wants to pass to west side")
    
     val setEmpty = Set[Monkey]()
     val to = East
     val numMonkeysInRope = 3
     
     val data = Data( setEmpty, setEmpty, to, numMonkeysInRope )   
     
     val monkey = Monkey( "1", East )   
     
     When("it is called to wantedOtherSide")
     val newData  = wantedOtherSide[Try]( monkey, StateEvents(AlreadySentNow), data )
     
     Then( "it should only change the value of the west list and the monkey is added" )      
     newData match {
        case Success( ( StateEvents(eventInRope), Data( mw, me, t, n) ) ) => 
          eventInRope should be ( AlreadySentNow )
          mw.head should be ( monkey )
          me should be ( setEmpty )
          t should be ( to )
          n should be ( numMonkeysInRope)
        case Failure( e ) => 
          fail( s"It must not be a error : ${e.getMessage}" )
     
    }
    
  }
  
   "4.- If a monkey that is in the east and wants to pass west region" should 
        "should be added to list East" in {
          
     Given("a flow and a monkey that wants to pass to west side")
    
     val setEmpty = Set[Monkey]()
     val to = East
     val numMonkeysInRope = 3
     
     val data = Data( setEmpty, setEmpty, to, numMonkeysInRope )   
     
     val monkey = Monkey( "1", East )   
     
     When("it is called to wantedOtherSide")
     val newData  = wantedOtherSide[Try]( monkey, StateEvents(NotSent), data )
     
     Then( "it should only change the value of the west list and the monkey is added and event is executed" )      
     newData match {
        case Success( ( StateEvents(eventInRope), Data( mw, me, t, n) ) ) => 
          eventInRope should be ( AlreadySentNow )
          mw.head should be ( monkey )
          me should be ( setEmpty )
          t should be ( to )
          n should be ( numMonkeysInRope)
        case Failure( e ) => 
          fail( s"It must not be a error : ${e.getMessage}" )
     
    }
    
  }
  
}
