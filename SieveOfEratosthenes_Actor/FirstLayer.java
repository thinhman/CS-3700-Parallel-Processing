package SieveOfEratosthenes;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import java.util.Arrays;

public class FirstLayer extends AbstractActor {
    private Boolean[] primeNumbers;
    private final int MAX;
    Integer block;
    int number = 0;
    final ActorRef child;
    long startTime;
    long endTime;
    public static class result{

    }
    public static class nonPrime{
        final int notPrime;

        public nonPrime(int notPrime){
            this.notPrime = notPrime;
        }
    }

    public static Props props(long startTime) {
        return Props.create(FirstLayer.class, startTime);
    }

    public FirstLayer(long startTime){
        this.startTime = startTime;
        final int n = 1_000_000; //1_000_000;
        this.primeNumbers = new Boolean[n+1];
        Arrays.fill(primeNumbers, Boolean.TRUE);
        this.MAX = primeNumbers.length - 1;
        this.block = 2;
        getContext().getSelf().tell(block, getSelf());
        child = getContext().actorOf(SecondLayer.props(MAX), "SecondLayer");

    }
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Integer.class, i->{
                    number = i;
                    block = i * i;
                    for (; number < block; number++ ){
                        //sends: 2, 3 < 4(2*2)
                        if(primeNumbers[number].equals(Boolean.TRUE)){
                            child.tell(new SecondLayer.startCheck(number), getSelf());
                        }
                    }

                })
                .match(nonPrime.class, n->{
                    if(n.notPrime != -1){
                        primeNumbers[n.notPrime] = Boolean.FALSE;
                    }

                })
                .match(result.class, r->{
                    if(block < Math.sqrt(MAX)){
                        //not done continue to next section
                        getSelf().tell(block, getSelf());
                    }else {
                        //getContext().stop(getSender());
                        //done print all prime sqrt(Max) -> MAX
                        number = 2;
                        int newLine = 1;
                        while(number < MAX){
                            if(primeNumbers[number]){
                                System.out.print(number + ", ");
                                if(newLine == 10 ){
                                    System.out.println();
                                    newLine = 0;
                                }
                                newLine++;
                            }
                            number++;
                        }
                        System.out.println();
                        getContext().stop(getSelf());

                    }
                })
                .build();
    }

    @Override
    public void postStop(){
        System.out.println("FINISHED");
        endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        System.out.format("Time elapsed: %d ns\n", timeElapsed);
        System.out.println("Press ENTER to exit the system");
    }
}
