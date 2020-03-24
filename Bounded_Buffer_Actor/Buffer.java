package Bounded_Buffer;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

public class Buffer extends AbstractActor {
    long startTime;
    long endTime;
    private ActorRef producerBuffer;
    private ActorRef consumer1;
    private ActorRef consumer2;
    private ActorRef consumer3;
    private ActorRef consumer4;
    private ActorRef consumer5;

    static class consumer_Message{
        public final ActorRef replyTo;

        public consumer_Message(ActorRef consumer) {
            this.replyTo = consumer;
        }
    };
    static class Finished { }
    public Buffer(long startTime){
        this.startTime = startTime;
        consumer1 = getContext().actorOf(Consumer.props("Consumer1"), "Consumer1");
        consumer2 = getContext().actorOf(Consumer.props("Consumer2"), "Consumer2");
        consumer3 = getContext().actorOf(Consumer.props("Consumer3"), "Consumer3");
        consumer4 = getContext().actorOf(Consumer.props("Consumer4"), "Consumer4");
        consumer5 = getContext().actorOf(Consumer.props("Consumer5"), "Consumer5");
        producerBuffer = getContext().actorOf(ProducerBuffer.props(), "ProducerBuffer");
    }

    public static Props create(long startTime ){
        return Props.create(Buffer.class, startTime);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(consumer_Message.class, c->{
                    producerBuffer.forward(new ProducerBuffer.request(c.replyTo), getContext());
                }).match(Finished.class, f->{
                    getContext().getSystem().terminate();
                })
                .build();
        //.match(producer_Buffer.getClass(),  ).build();
    }

    @Override
    public void postStop(){
        System.out.println("PROGRAM SHUTTING DOWN");
        endTime = System.currentTimeMillis()/1000;
        long timeElapsed = endTime - startTime;
        System.out.format("Time elapsed: %d sec\n", timeElapsed);
        System.out.println("Press ENTER to exit the system");
    }
}