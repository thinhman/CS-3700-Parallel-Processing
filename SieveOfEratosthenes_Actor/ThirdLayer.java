package SieveOfEratosthenes;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

public class ThirdLayer extends AbstractActor{
    private final int MAX;
    int number = 0;

    public static class checkMsg{
        final int checkPrime;
        //final ActorRef replyTo;

        public checkMsg(int checkPrime){
            this.checkPrime = checkPrime;
            //this.replyTo = replyTo;
        }
    }

    public static Props props(int MAX) {
        return Props.create(ThirdLayer.class, MAX);
    }
    public ThirdLayer(int MAX){
        this.MAX = MAX;

    }
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(checkMsg.class, c->{
                    //2+2+2+2 < 1_000_000
                    //3+3+3+3+ < 1_000_000
                    number = 2 * c.checkPrime;
                    for (; number <= MAX; number+=c.checkPrime ){
                        getSender().tell(new FirstLayer.nonPrime(number), getSelf());
                    }
                    getSender().tell(new FirstLayer.nonPrime(-1), getSelf());
                    getContext().getParent().tell(new SecondLayer.finished(), getSelf());

                })
                .build();
    }
}
