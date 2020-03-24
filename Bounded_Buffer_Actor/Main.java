package Bounded_Buffer;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis()/1000;
        //#actor-system
        ActorSystem system = ActorSystem.create("test-system");
        //Add Buffer Actor to System
        try{
            ActorRef BoundedBuffer = system.actorOf(Buffer.create(startTime), "BoundedBuffer");
            //ActorRef actor = system.actorOf(Props.create(BufferActor.class), "Buffer");
            //actor.tell("Start", actor);
            System.in.read();
        }finally {
            system.terminate();
        }

    }
}
