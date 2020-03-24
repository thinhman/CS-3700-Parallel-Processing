package Bounded_Buffer;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.ReceiveTimeout;

import java.time.Duration;

public class Consumer extends AbstractActor {
    private int counter;
    final private String consumerId;

    public static Props props(String consumerId) {
        return Props.create(Consumer.class, consumerId);
    }
    private Consumer(String consumerId){
        this.counter = 0;
        this.consumerId = consumerId;
        getContext().getParent().tell(new Buffer.consumer_Message(getSelf()), getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("Item",  Item -> {
                    counter++;
                    System.out.println(consumerId + " consumed Item " + counter);
                    System.out.println(consumerId + " SLEEPING 1sec");
                    getContext().setReceiveTimeout(Duration.ofSeconds(1));
                    //getContext().getParent().tell(new Buffer.consumer_Message(getSelf()), getSelf());
                })
                .match(ReceiveTimeout.class, t -> {
                    // To turn it off
                    getContext().cancelReceiveTimeout();
                    getContext().getParent().tell(new Buffer.consumer_Message(getSelf()), getSelf());
                }).build();
    }
}