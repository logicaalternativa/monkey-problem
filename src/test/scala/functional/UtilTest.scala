package com.logicaalternativa.monkeyproblem
package functional



object UtilTest {
  
  import scalaz.MonadError
  import scala.util.{Try,Success,Failure}
  
  val ETry: MonadError[Try,Error] = new MonadError[Try,Error] {
      
      // Members declared in scalaz.Applicative
      def point[A](a: => A): Try[A] = Success( a )
      
      // Members declared in scalaz.Bind
      def bind[A, B](fa: Try[A])(f: A => Try[B]): Try[B] = {
        
        fa match {
            case Success( s ) => f( s )
            case Failure( e ) => Failure( e )
        }
        
        
      }
      
      // Members declared in scalaz.MonadError
      def handleError[A](fa: Try[A])(f: Error => Try[A]): Try[A] = fa
      
      def raiseError[A](e: Error): Try[A] = Failure( new Exception( e.description ) )
      
    }
  
}
