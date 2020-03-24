package SieveOfEratosthenes;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        long startTime = System.nanoTime();
        //#actor-system
        ActorSystem system = ActorSystem.create("test-system");
        //Add Buffer Actor to System
        try{
            ActorRef Sieve = system.actorOf(FirstLayer.props(startTime), "Sieve_of_Eratosthenes");
            //ActorRef BoundedBuffer = system.actorOf(, "BoundedBuffer");
            //ActorRef actor = system.actorOf(Props.create(BufferActor.class), "Buffer");
            //actor.tell("Start", actor);
            System.in.read();
        }finally {
            system.terminate();
        }

    }
}
