package Bounded_Buffer;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.ArrayList;
import java.util.Random;

public class ProducerBuffer extends AbstractActor {
    private final Integer MAX = 10;
    private int numberOfItems = 0;
    private int finishedProducer = 0;

    ArrayList<ActorRef> producerActors = new ArrayList<>();
    ArrayList<ActorRef> replyTO = new ArrayList<>();
    ArrayList<ActorRef> consumerActors = new ArrayList<>();

    public static Props props() {
        return Props.create(ProducerBuffer.class);
    }

    static class producer_Message{
        public final ActorRef replyTo;

        public producer_Message(ActorRef replyTo) {
            this.replyTo = replyTo;
        }
    };
    static class producer_Finish{
        public final String replyTo;

        public producer_Finish(String replyTo) {
            this.replyTo = replyTo;
        }
    };
    static class request{
        public final ActorRef replyTo;

        public request(ActorRef consumer) {
            this.replyTo = consumer;
        }
    };
    @Override
    public void preStart() throws Exception {
        for(int i = 0; i < 2; i++) {
            producerActors.add(getContext().actorOf(Producer.createClass("Producer" + i), "Producer" + i));
        }


    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(producer_Message.class, produce_checking -> {
                    if(!consumerActors.isEmpty()){
                        //Case: when consumer waiting
                        ActorRef reply = consumerActors.get(new Random().nextInt(consumerActors.size()));
                        reply.tell("Item", getSelf());
                        consumerActors.remove(reply);

                        getContext().getSender().tell(new Producer.Successful(), getSelf());

                    }else if(numberOfItems < MAX){
                        numberOfItems++;
                        getContext().getSender().tell(new Producer.Successful(), getSelf());
                    }else {
                        replyTO.add(getContext().getSender());
                    }



                })
                .match(producer_Finish.class, p->{
                    finishedProducer++;
                    getContext().stop(getSender());
                    if(finishedProducer == 2 && numberOfItems == 0)
                        getContext().getParent().tell(new Buffer.Finished(), ActorRef.noSender());
                })
                .match(request.class, r->{
                    if(numberOfItems > 0){
                        if(!replyTO.isEmpty()){
                            ActorRef reply = replyTO.get(new Random().nextInt(replyTO.size()));
                            reply.tell(new Producer.Successful(), getSelf());
                            replyTO.remove(reply);

                        }else {
                            numberOfItems--;
                        }
                        r.replyTo.tell("Item", getSelf());

                    }else {
                        if(!replyTO.isEmpty()){
                            ActorRef reply = replyTO.get(0);
                            reply.tell(new Producer.Successful(), getSelf());
                            replyTO.remove(reply);
                            r.replyTo.tell("Item", getSelf());

                        }else if(finishedProducer == 2 && numberOfItems == 0){
                            getContext().getParent().tell(new Buffer.Finished(), ActorRef.noSender());
                        }else {
                            consumerActors.add(r.replyTo);
                        }

                    }
                })
                .build();
    }

}