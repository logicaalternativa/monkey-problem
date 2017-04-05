# Monkey Problem

La solución está programada en **Scala**.

He planteado la solución separando completamete la lógica. De esta forma
se podría escoger cualquier implementación para modelar la cuerda 
(_rope_) y los monos (_monkeys_). 

La implementación de mi solución está realizada en **akka** que me 
permite modelar los diferentes _actores_ sin tener que preocuparme de la
gestión de la concurrencia.

## Lógica
Para definir la lógica del problema he optado por utilizar conceptos de
programación funcional utilizando la librería **scalaz**

He utilizado la mónada `MonadError` y las _Typed Classes_ que permiten
abstraer la lógica incluso del tipo que se debe devolver. Esto permite
tener la potencia de que la implementación final puede ser asíncrona o 
síncrona siendo la lógica la misma. Para demostrar este aspecto he 
realizado los test basandome en una implementación de la `MonadError` 
basada en `Try` y la final en `Either`, pero podría haberlo hecho, por 
ejemplo, con una implementación de `Future[Try]` `Future[Either]`

### Clases
Directorio `functional`

#### MonkeyArrivedFlow.scala 
Contiene toda lógica cuando un mono llega al destino después de pasar
la cuerda

    ****************************************************
      WANT TO CROSS                                   
      =============                                   
                         >> >>                        
        O/                                            
       /|  |>---------------------------------<|      
       / \                                           
                                                      
      This is monkey-monkey-1. I want to cross to West          
    ****************************************************

**Clase de test**: `MonkeyWantedToOtherSideFlow.scala`

#### MonkeyArrivedFlow.scala 
Contiene la lógica cuando un mono llega al otro lado
    
    ****************************************************
      MONKEY ARRIVED                                   
      ============== 
                    
                                                \O/   
          |>---------------------------------<|  |  
                                                / \ 
                                                
      This is monkey-monkey-1. Ey!! I arrived to West           
    ****************************************************

**Clase de test**: `MonkeyWantedToOtherSideFlowSpec.scala`

#### NewMonkeyInRopeFlow.scala
Contiene la lógica que comprueba si se debe dar permiso a un mono para
pasar la cuerda dependiendo de que si ya hay monos en la cuerda y si 
están o no esperando a uno y al otro lado 
    
    ****************************************************
      IN THE ROPE                                       
      ===========                                       
                                                 (0)  
        O                                         O     
       /|\ |>---------------------------------<| /|\  
       / \                                       / \  
       (0)                                            
                   >> East ( 1 ) >>                                  
    ****************************************************



**Clase de test**: `NewMonkeyInRopeFlowSpec.scala`

## Parte akka
La implementación final se ha hecho en **akka**. 
Esto me ha permitido evitar problemas de concurrencia pudiendo mandar 
mensajes fácilmente entre los diferentes _actores_ de la simulación.

## Clases
Directorio `actor`

### RopeActor.scala
Es el actor que modela la cuerda en el problema

### MonkeyActor.scala
Es el actor que modela un mono en el problema

  
    
    ****************************************************
      CAN CROSS                               
      =========                                   
                         >> >>                        
               _O_                                         
           |>-' | '--------------------------------<|      
               / \                                        
                                                      
     This is monkey-monkey-1. I have authorization to cross to West          
    ****************************************************


### SimulationActor.scala
Es el actor crea la simulación creando los diferentes monos de manera
aletoria. Crea los actores _monos_ con un intervalo entre 1 y 8 
segundos. También genera de manera aleatoria desde donde quieren pasar 
(este o oeste)

## Ejecutar la aplicación

Para ejecturar la aplicación se debe ejecutar el siguiente comando

``
sbt run
``

Por defecto la simulación se hace con _**20**_ monos. Se puede modificar 
en la clase `Simulation.scala` 

### Logs
Además de la información que se muestra por consola, la aplicación 
genera dos logs, que se almacenan en el directorio `./log`
 
 + `simulation.log` con la misma información que aparece en la consola
 + `debug.log` con toda la información que genera la aplicación
 






