package com.logicaalternativa.monkeyproblem
package functional

import org.scalatest.{FlatSpec,Matchers,GivenWhenThen}



class NewMonkeyInRopeFlowSpec extends FlatSpec with Matchers with GivenWhenThen {
  
  import scala.util.{Try,Success,Failure}
  import UtilTest.ETry
  import scala.concurrent.duration._
  
  implicit val E  = UtilTest.ETry
  
  
  class ExecuteEventCanAcrossMonkeyImpTry( monkeySend : Monkey ) extends ExecuteEventCanAcrossMonkey[Try] {
    
    def acrossMonkey( monkey : Monkey ) : Try[Unit] = {
      
        if ( monkey == monkeySend ) {
            
            Success( Unit )
          
        } else {
         
          Failure( new Exception( "Test exception" )  )
          
        }
      
    }
    
  }
  
  implicit val executeEventNewMonkeyInRope =  new ExecuteEventNewMonkeyInRope[Try] {
    
    def execEventNewMonkeyInRope: Try[EventNewMonkeyInRope] = Success( AlreadySentNow )
  
    def execEventNewMonkeyInRopeWhitDelay : Try[EventNewMonkeyInRope] = Success( AlreadySentDelay )
    
  }
  
  
  "1.- If there is not any monkeys  waiting " +
  "to cross the canyon and there is not monkeys in the rope" should  "send " +
  "a message that to the first monkey that it is waiting in East and "+
  "remove it from East list " in {
          
     Given("a flow and data with no monkeys waitinga and no monkeys in rope")
    
     implicit val executeEvCanAcrossTry = new ExecuteEventCanAcrossMonkeyImpTry( Monkey( "1", East ) )
    
     val setEmpty = Set[Monkey]()
     val to = East
     
     val data = Data( setEmpty, setEmpty, to, 0 )   
     
     When("it is called to next")
     val newData  =  NewMonkeyInRopeFlow.next[Try]( data )
     
     Then( "The data should be empty and it is not send an event 'check envent rope'" )      
     newData match {
        case Success( ( StateEvents(eventInRope), data @ Data( _, _, t, _) ) ) => 
          eventInRope should be (NotSent )
          t should be ( to )
          data.isEmpty should be ( true )
        case Failure( e ) => 
          fail( s"It must not be a error : ${e.getMessage}" )
     
    }
    
  }
  
  "2.- If there is not any monkeys  waiting " +
  "to cross the canyon and there is monkeys in the rope" should  "send " +
  "a message that to the first monkey that it is waiting in East and "+
  "remove it from East list " in {
          
     Given("a flow and data with no monkeys waiting and no monkeys in rope")
    
     implicit val executeEvCanAcrossTry = new ExecuteEventCanAcrossMonkeyImpTry( Monkey( "1", East ) )
    
     val setEmpty = Set[Monkey]()
     val to = East
     val numMonkeysInRope = 3
     
     val data = Data( setEmpty, setEmpty, to, numMonkeysInRope )   
     
     When("it is called to next")
     val newData  =  NewMonkeyInRopeFlow.next[Try]( data )
     
     Then( "The data should be the same and it should send a check event 'monkeys in rope'" )     
     newData match {
        case Success( ( StateEvents(eventInRope), Data( mew, mwe, t, n) ) ) => 
          eventInRope should be( AlreadySentDelay )
          mew should be ( setEmpty )
          mwe should be ( setEmpty )
          t should be ( to )
          n should be ( numMonkeysInRope )
        case Failure( e ) => 
          fail( s"It must not be a error : ${e.getMessage}" )
     
    }
    
  }
  
    
  "3.- If the direction is to East and there are monkeys  waiting " +
  "to cross the canyon to East and there is not any monkey waiting " +
  "in the West and there is already monkeys in the rope" should  "send " +
  "a message that to the first monkey that it is waiting in East and "+
  "remove it from East list " in {
          
     Given("a flow and data with monkeys waiting East side and any in the West side")
    
     val firstMonkey = Monkey( "1", East )   
     val secondMonkey = Monkey( "2", East )   
     implicit val executeEvCanAcrossTry = new ExecuteEventCanAcrossMonkeyImpTry( firstMonkey )
    
     val setEmpty = Set[Monkey]()
     val to = East
     val numMonkeysInRope = 3
     
     val data = Data( Set[Monkey]( firstMonkey, secondMonkey ), setEmpty, to, numMonkeysInRope )   
     
     When("it is called to next")
     val newData  =  NewMonkeyInRopeFlow.next[Try]( data )
     
     Then( "it should remove the first monkey waiting to cross from east to west" )      
     newData match {
        case Success( ( StateEvents(eventInRope), Data( mew, mwe, t, n) ) ) => 
          eventInRope should be (AlreadySentDelay)
          mew.head should be ( secondMonkey )
          mwe should be ( setEmpty )
          t should be ( to )
          n should be ( numMonkeysInRope + 1)
        case Failure( e ) => 
          fail( s"It must not be a error : ${e.getMessage}" )
     
    }
    
  }
  
  
  "4.- If the direction is to East and there are monkeys  waiting " +
  "to cross the canyon to East and there are also monkey waiting " +
  "in the West and there is already monkeys in the rope"  should  " send " + 
  "event 'check monkey in rope with delay' and the data should be the same " in {
          
     Given("a flow and data with monkeys waiting East side and any the West side")
    
     val firstMonkey = Monkey( "1", East )   
     val secondMonkey = Monkey( "2", East )      
     implicit val executeEvCanAcrossTry = new ExecuteEventCanAcrossMonkeyImpTry( firstMonkey )
    
     val waitingEast = Set[Monkey]( firstMonkey )
     val waitingWest = Set[Monkey]( secondMonkey )
     val to = East
     val numMonkeysInRope = 3
     
     val data = Data( waitingEast, waitingWest, to, numMonkeysInRope )   
     
     When("it is called to next")
     val newData  =  NewMonkeyInRopeFlow.next[Try]( data )
     
     Then( "It should send event 'check monkey in rope with delay' and the data should be the same" )      
     newData match {
       case Success( ( StateEvents(eventInRope), newData ) ) => 
          eventInRope should be (AlreadySentDelay)
          data should be ( newData )
        case Failure( e ) => 
          fail( s"It must not be a error : ${e.getMessage}" )
     
    }
    
  }
  
  "5.- If the direction is to east and there are monkeys  waiting " +
  "to cross the canyon to east and to the west side and there is " + 
  "not any monkeys in the rope"  should  " change direction towest, " +
  "sends a message from first monkey that is waiting in the west side and " + 
  "remove from list waiting monkeys from this side" in {
          
     Given("a flow and data with monkeys waiting East side and any the West side")
    
     val firstMonkey = Monkey( "1", East )   
     val secondMonkey = Monkey( "2", East )      
     implicit val executeEvCanAcrossTry = new ExecuteEventCanAcrossMonkeyImpTry( secondMonkey )
    
     val waitingEast = Set[Monkey]( firstMonkey )
     val waitingWest = Set[Monkey]( secondMonkey )
     val to = East
     val numMonkeysInRope = 0
     
     val data = Data( waitingEast, waitingWest, to, numMonkeysInRope )   
     
     When("it is called to next")
     val newData  =  NewMonkeyInRopeFlow.next[Try]( data )
     
     Then( "it should remove the first monkey waiting to cross from east to west" )      
     newData match {
       case Success( ( StateEvents(eventInRope), Data( mew, mwe, t, n) ) ) => 
          eventInRope should be (AlreadySentDelay)
          mew should be ( waitingEast )
          mwe should be ( Set[Monkey]() )
          t should be ( West )
          n should be ( 1 )
        case Failure( e ) => 
          fail( s"It must not be a error : ${e.getMessage}" )
     
    }
    
  }
  
  "6.- If the direction is to west and there are monkeys  waiting " +
  "to cross the canyon to east and to the west side and there is " + 
  "not any monkeys in the rope"  should  " change direction to east, " +
  "sends a message from first monkey that is waiting in the west side and " + 
  "remove from list waiting monkeys from this side" in {
          
     Given("a flow and data with monkeys waiting east side and any the West side")
    
     val firstMonkey = Monkey( "1", East )   
     val secondMonkey = Monkey( "2", East )      
     implicit val executeEvCanAcrossTry = new ExecuteEventCanAcrossMonkeyImpTry( firstMonkey )
    
     val waitingEast = Set[Monkey]( firstMonkey )
     val waitingWest = Set[Monkey]( secondMonkey )
     val to = West
     val numMonkeysInRope = 0
     
     val data = Data( waitingEast, waitingWest, to, numMonkeysInRope )   
     
     When("it is called to next")
     val newData  =  NewMonkeyInRopeFlow.next[Try]( data )
     
     Then( "it should remove the first monkey waiting to cross from east to west" )      
     newData match {
        case Success( ( StateEvents(eventInRope), Data( mew, mwe, t, n) ) ) => 
          eventInRope should be (AlreadySentDelay)
          mew should be ( Set[Monkey]())
          mwe should be ( waitingWest )
          t should be ( East )
          n should be ( 1 )
        case Failure( e ) => 
          fail( s"It must not be a error : ${e.getMessage}" )
     
    }
    
  }
  
  
  "7.- If the direction is to West and there are monkeys  waiting " +
  "to cross the canyon to East and there are also monkey waiting " +
  "in the West and there is already monkeys in the rope"  should  " throw and " +
  "validation error" in {
          
     Given("a flow and data with monkeys waiting East side and any the West side")
    
     val firstMonkey = Monkey( "1", East )   
     val secondMonkey = Monkey( "2", East )      
     implicit val executeEvCanAcrossTry = new ExecuteEventCanAcrossMonkeyImpTry( firstMonkey )
    
     val waitingEast = Set[Monkey]( firstMonkey )
     val waitingWest = Set[Monkey]( secondMonkey )
     val to = West
     val numMonkeysInRope = 3
     
     val data = Data( waitingEast, waitingWest, to, numMonkeysInRope )   
     
     When("it is called to next")
     val newData  =  NewMonkeyInRopeFlow.next[Try]( data )
     
     Then( "It should and error the type BothListMonkeyWaitingNotEmpty" )      
     
          
      newData match {
          case Success( ( StateEvents(eventInRope), Data( mew, mwe, t, n) ) ) => 
            eventInRope should be (AlreadySentDelay)
            mew should be ( waitingEast )
            mwe should be ( waitingWest )
            t should be ( to )
            n should be ( numMonkeysInRope )
          case Failure( e ) => 
            fail( s"It must not be a error : ${e.getMessage}" )
       
      }
    
  }
   
    
  "8.- If the direction is to West and there are monkeys waiting " +
  "to cross the canyon from west to east and there is not any monkey waiting " +
  "in the east side and there is already monkeys in the rope" should  "send " +
  "a message that to the first monkey that it is waiting in East and "+
  "remove it from East list " in {
          
     Given("a flow and data with monkeys waiting west side and any in the east side")
    
     val firstMonkey = Monkey( "1", East )   
     val secondMonkey = Monkey( "2", East )   
     implicit val executeEvCanAcrossTry = new ExecuteEventCanAcrossMonkeyImpTry( firstMonkey )
    
     val setEmpty = Set[Monkey]()
     val to = West
     val numMonkeysInRope = 3
     
     val data = Data( setEmpty, Set[Monkey]( firstMonkey, secondMonkey ), to, numMonkeysInRope )   
     
     When("it is called to next")
     val newData  =  NewMonkeyInRopeFlow.next[Try]( data )
     
     Then( "it should remove the first monkey waiting to cross from east to west" )      
     newData match {
        case Success( ( StateEvents(eventInRope), Data( mew, mwe, t, n) ) ) => 
          eventInRope should be (AlreadySentDelay)
          mew should be ( setEmpty )
          mwe.head should be ( secondMonkey )
          t should be ( to )
          n should be ( numMonkeysInRope + 1 )
        case Failure( e ) => 
          fail( s"It must not be a error : ${e.getMessage}" )
     
    }
    
  }
  
  
}
