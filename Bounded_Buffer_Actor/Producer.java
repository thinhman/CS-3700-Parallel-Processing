package Bounded_Buffer;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.ActorRef;

public class Producer extends AbstractActor {

    private ActorRef parent;
    private int numberOfItems = 0;
    private final String name;
    public static final class Item { }
    public static final class Successful { }

    public static Props createClass(String ID) {
        return Props.create(Producer.class, ID);
    }

    public Producer(String ID) {
        this.name = ID;
        this.parent = getContext().getParent();
        getContext().getSelf().tell(new Item(), getSelf());
        numberOfItems++;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Item.class, this::produceItem)
                .match(Successful.class, s->{
                    if(numberOfItems < 100){
                        numberOfItems++;
                        System.out.println(name + " ITEM "+ numberOfItems);
                        //getContext().getSender().tell(new ProducerBuffer.producer_Message(getSelf()), getSelf());
                        getContext().getSelf().tell(new Item(), getContext().getSelf());
                    }else{
                        System.out.println(name + " FINISHED");

                        getContext().getSender().tell(new ProducerBuffer.producer_Finish(name), getSelf());
                    }
                }).build();
    }

    void produceItem(Item item){
        parent.tell(new ProducerBuffer.producer_Message(getSelf()), getSelf());
    }
}