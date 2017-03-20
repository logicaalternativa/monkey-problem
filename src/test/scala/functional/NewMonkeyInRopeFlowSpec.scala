package com.logicaalternativa.monkeyproblem
package functional

import org.scalatest.{FlatSpec,Matchers,GivenWhenThen}

class NewMonkeyInRopeFlowSpec extends FlatSpec with Matchers with GivenWhenThen {
  
  import scala.util.{Try,Success,Failure}
  import UtilTest.ETry
  
  class NewMonkeyInRopeFlowImpTry( monkeySend : Monkey ) extends NewMonkeyInRopeFlow[Try] {
    
    implicit val E = ETry
    
    def sendMessageCanAcross( monkey : Monkey ) : Try[Unit] = {
      
        if ( monkey == monkeySend ) {
            
            Success( Unit )
          
        } else {
         
          Failure( new Exception( "Test exception" )  )
          
        }
      
    }
    
  }
  
    
  "1.- If the direction is to East and there are monkeys  waiting " +
  "to cross the canyon to East and there is not any monkey waiting " +
  "in the West and there is already monkeys in the rope" should  "send " +
  "a message that to the first monkey that it is waiting in East and "+
  "remove it from East list " in {
          
     Given("a flow and data whit monkeys waiting East side and any in the West side")
    
     val firstMonkey = Monkey( "1", East )   
     val secondMonkey = Monkey( "2", East )   
     val monkeyArrivedFlow = new NewMonkeyInRopeFlowImpTry( firstMonkey )
    
     val setEmpty = Set[Monkey]()
     val to = East
     val numMonkeysInRope = 3
     
     val data = Data( Set[Monkey]( firstMonkey, secondMonkey ), setEmpty, to, numMonkeysInRope )   
     
     When("it is called to next")
     val newData  = monkeyArrivedFlow.next( data )
     
     Then( "it should remove the first monkey waiting to cross from east to west" )      
     newData match {
        case Success( (isSentMessage, Data( mew, mwe, t, n) )) => 
          isSentMessage should be ( true )
          mew.head should be ( secondMonkey )
          mwe should be ( setEmpty )
          t should be ( to )
          n should be ( numMonkeysInRope)
        case Failure( e ) => 
          fail( s"It must not be a error : ${e.getMessage}" )
     
    }
    
  }
  
  
  "2.- If the direction is to East and there are monkeys  waiting " +
  "to cross the canyon to East and there are also monkey waiting " +
  "in the West and there is already monkeys in the rope"  should  " throw and " +
  "validation error" in {
          
     Given("a flow and data whit monkeys waiting East side and any the West side")
    
     val firstMonkey = Monkey( "1", East )   
     val secondMonkey = Monkey( "2", East )      
     val monkeyArrivedFlow = new NewMonkeyInRopeFlowImpTry( firstMonkey )
    
     val waitingEast = Set[Monkey]( firstMonkey )
     val waitingWest = Set[Monkey]( secondMonkey )
     val to = East
     val numMonkeysInRope = 3
     
     val data = Data( waitingEast, waitingWest, to, numMonkeysInRope )   
     
     When("it is called to next")
     val newData  = monkeyArrivedFlow.next( data )
     
     Then( "It should and error the type BothListMonkeyWaitingNotEmpty" )      
     newData match {
        case Failure( e ) => 
          e.getMessage should be ( BothListMonkeyWaitingNotEmpty.description )
        case Success( d ) => 
          fail( s"It must be a error : ${d}" )
     
    }
    
  }
  
  
  "3.- If the direction is to West and there are monkeys  waiting " +
  "to cross the canyon to East and there are also monkey waiting " +
  "in the West and there is already monkeys in the rope"  should  " throw and " +
  "validation error" in {
          
     Given("a flow and data whit monkeys waiting East side and any the West side")
    
     val firstMonkey = Monkey( "1", East )   
     val secondMonkey = Monkey( "2", East )      
     val monkeyArrivedFlow = new NewMonkeyInRopeFlowImpTry( firstMonkey )
    
     val waitingEast = Set[Monkey]( firstMonkey )
     val waitingWest = Set[Monkey]( secondMonkey )
     val to = West
     val numMonkeysInRope = 3
     
     val data = Data( waitingEast, waitingWest, to, numMonkeysInRope )   
     
     When("it is called to next")
     val newData  = monkeyArrivedFlow.next( data )
     
     Then( "It should and error the type BothListMonkeyWaitingNotEmpty" )      
     newData match {
        case Failure( e ) => 
          e.getMessage should be ( BothListMonkeyWaitingNotEmpty.description )
        case Success( d ) => 
          fail( s"It must be a validation error : ${d}" )
     
    }
    
  }
   
    
  "4.- If the direction is to West and there are monkeys waiting " +
  "to cross the canyon from west to east and there is not any monkey waiting " +
  "in the east side and there is already monkeys in the rope" should  "send " +
  "a message that to the first monkey that it is waiting in East and "+
  "remove it from East list " in {
          
     Given("a flow and data whit monkeys waiting west side and any in the east side")
    
     val firstMonkey = Monkey( "1", East )   
     val secondMonkey = Monkey( "2", East )   
     val monkeyArrivedFlow = new NewMonkeyInRopeFlowImpTry( firstMonkey )
    
     val setEmpty = Set[Monkey]()
     val to = West
     val numMonkeysInRope = 3
     
     val data = Data( setEmpty, Set[Monkey]( firstMonkey, secondMonkey ), to, numMonkeysInRope )   
     
     When("it is called to next")
     val newData  = monkeyArrivedFlow.next( data )
     
     Then( "it should remove the first monkey waiting to cross from east to west" )      
     newData match {
        case Success( (isSentMessage, Data( mew, mwe, t, n) )) => 
          isSentMessage should be ( true )
          mew should be ( setEmpty )
          mwe.head should be ( secondMonkey )
          t should be ( to )
          n should be ( numMonkeysInRope)
        case Failure( e ) => 
          fail( s"It must not be a error : ${e.getMessage}" )
     
    }
    
  }
  
  
}
