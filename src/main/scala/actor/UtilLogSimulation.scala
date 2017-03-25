package com.logicaalternativa.monkeyproblem
package actor


object UtilLogSimulation {
  
  import functional._
  import akka.event.LoggingAdapter
  
  val nameLoger = "UtilLogSimulation" 
  
  def logData( data : Data )( implicit logRope : LoggingAdapter ) = {
   
    val mE = data.waitingEastToWest.foldLeft( 0 )( ( acc, s ) => acc + 1  )
    val mW = data.waitingWestToEast.foldLeft( 0 )( ( acc, s ) => acc + 1  )
    
    val to = data.to match {
        case East => s">> East ( ${data.numMonkeysInRope} ) >>" 
        case West => s"<< West ( ${data.numMonkeysInRope} ) <<" 
    }
    
    val logS = s"""
    
    ****************************************************
      IN THE ROPE                                       
      ===========                                       
                                                 ($mW)  
        O                                         O     
       /|\\ |>---------------------------------<| /|\\  
       / \\                                       / \\  
       ($mE)                                            
                   $to                                  
    ****************************************************
    """
    
    logRope info( "State of the rope {}", logS )
    
  }
  
  def logWantToCross( name : String, from : Region )( implicit logRope : LoggingAdapter )  = {
   
    val logS = if ( from == East ) {
    
      s"""
    
    ****************************************************
      WANT TO CROSS                                   
      =============                                   
                         >> >>                        
        O/                                            
       /|  |>---------------------------------<|      
       / \\                                           
                                                      
      This is $name. I want to cross to West          
    ****************************************************
    """
    } else {
      
    s"""
    
    ****************************************************
      IN THE ROPE                                     
      ===========                                     
                         << <<                        
                                                \\O   
          |>---------------------------------<|  |\\  
                                                / \\  
                                                      
                                                      
            This is $name. I want to cross to East    
    ****************************************************
    """
    
    }
    
    logRope info ( "Monkey wants to cross {}" , logS )
    
  }
  
  def logArrived( name : String, from : Region )( implicit logRope : LoggingAdapter )  = {
   
    val logS = if ( from == West ) {
    
      s"""
    
    ****************************************************
      MONKEY ARRIVED                                   
      ==============                                  
                                                
       \\O/                                            
        |  |>---------------------------------<|      
       / \\                                           
                                                      
      This is $name. Ey!! I arrived to the West          
    ****************************************************
    """
    } else {
      
    s"""
    
    ****************************************************
      MONKEY ARRIVED                                   
      ============== 
                    
                                                \\O/   
          |>---------------------------------<|  |  
                                                / \\ 
                                                
      This is $name. Ey!! I arrived to the East           
    ****************************************************
    """
    
    }
    
    logRope info ( "Monkey is arrived {}" , logS )
    
  }
  
  
  
  def logCanCross( name : String, from : Region )( implicit logRope : LoggingAdapter )  = {
   
    val logS = if ( from == East ) {
    
      s"""
    
    ****************************************************
      CAN CROSS                               
      =========                                   
                         >> >>                        
               _O_                                         
           |>-' | '--------------------------------<|      
               / \\                                        
                                                      
     This is $name. I have authorization to cross to West          
    ****************************************************
    """
    } else {
      
    s"""
    
    ****************************************************
      CAN CROSS                               
      =========                                   
                         << <<                        
                                            _O_           
          |>-------------------------------' | '-<|       
                                            / \\          
                                                      
                                                     
     This is $name. I have authorization to cross to East    
    ****************************************************
    """
    
    }
    
    logRope info ( "Monkey has authorization  {}" , logS )
    
  }
  
  
}
