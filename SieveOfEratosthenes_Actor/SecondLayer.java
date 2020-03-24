package SieveOfEratosthenes;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.ArrayList;

public class SecondLayer extends AbstractActor {
    final int MAX;
    ArrayList<ActorRef> workers = new ArrayList<>();
    ActorRef worker;
    int number = 0;
    public static class finished{

    }
    public static class startCheck{
        final int checkPrime;

        public startCheck(int checkPrime){
            this.checkPrime = checkPrime;
        }
    }

    public static Props props(int MAX) {
        return Props.create(SecondLayer.class, MAX);
    }
    public SecondLayer(int MAX){
        this.MAX = MAX;

    }
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(startCheck.class, i->{
                    number = i.checkPrime;
                    worker = getContext().actorOf(ThirdLayer.props(MAX), "ThirdLayer" +i.checkPrime);
                    worker.forward(new ThirdLayer.checkMsg(number), getContext());
                    workers.add(worker);

                }).match(finished.class, f->{
                    //primeNumbers = f.results;

                    workers.remove(getSender());
                    getContext().stop(getSender());
                    if(workers.isEmpty()){
                        getContext().getParent().tell(new FirstLayer.result(), getSelf());
                    }

                })
                .build();
    }
}
