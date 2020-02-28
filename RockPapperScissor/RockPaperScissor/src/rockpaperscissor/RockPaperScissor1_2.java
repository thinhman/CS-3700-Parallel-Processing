package rockpaperscissor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class RockPaperScissor1_2 {
    public static void main(String[] args) throws InterruptedException {
        long startTime = System.nanoTime();
        String[] hand = {"Rock", "Paper", "Scissor"};
        
        System.out.print("Please Enter the Number of Players: ");
        Scanner scan = new Scanner(System.in);
        int numPlayers = scan.nextInt();
        
        int numProcessors = Runtime.getRuntime().availableProcessors();
        ArrayList<String> allPlayers = new ArrayList<>(numPlayers);
        ArrayList<String> allPlayersHand = new ArrayList<>(numPlayers);
        
        for(int i = 0; i< numPlayers; i++){
            allPlayers.add("Player" + i);
        }
        
        do{
            ArrayList<Player> players = new ArrayList<>(numPlayers);
            AtomicReference<ThreadPoolExecutor> threadpool = new AtomicReference<>(new ThreadPoolExecutor(numProcessors, numProcessors, 0L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(numPlayers)));
            for(int i = 0; i< numPlayers; i++){
                allPlayersHand.add(hand[ThreadLocalRandom.current().nextInt(3)]);
            }    
            for(int i = 0; i< numPlayers; i++){
                Player playerThread = new Player(allPlayersHand, allPlayers.get(i), allPlayersHand.get(i));
                players.add(playerThread);
                System.out.println(playerThread.myName + ' ' + playerThread.myHand);
            }

            players.forEach((player) -> {
                threadpool.get().execute(player);
            });

            WinnerThread win = new WinnerThread(players, allPlayers);
            
            while(threadpool.get().getCompletedTaskCount() != numPlayers){

            }
            
            threadpool.get().execute(win);
            
            while(threadpool.get().getCompletedTaskCount() != numPlayers+1){

            }
            numPlayers--;
            allPlayersHand.clear();
            threadpool.get().shutdown();
        }while(numPlayers > 1);
        
        for(String winner: allPlayers)
        {
            System.out.println("Winner is: " + winner);   
        }
        
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        System.out.format("Time elapsed: %d ns\n", timeElapsed); 
    }
}



class WinnerThread implements  Runnable{
    ArrayList<Player> allPlayers;
    ArrayList<String> allPlayersName;
    Player losers;
    int lowest = Integer.MAX_VALUE;
    String[] hand = {"Rock", "Paper", "Scissor"};
    
    public WinnerThread (ArrayList<Player> allPlayers, ArrayList<String> allPlayersName){
        this.allPlayers = allPlayers;
        this.losers = null;
        this.allPlayersName = allPlayersName;
    }
    
    @Override
    public void run(){
        System.out.println("Winner Thread Running...");
        for(int x = 0; x < allPlayers.size(); x++){
            if(allPlayers.get(x).myScore < lowest){
                
                lowest = allPlayers.get(x).myScore;
                losers = allPlayers.get(x);
            }else if (allPlayers.size() == 2 && allPlayers.get(x).myScore == lowest){
                System.out.println("Draw, Replay");
                int hand1 = ThreadLocalRandom.current().nextInt(3);
                int hand2 = ThreadLocalRandom.current().nextInt(3);
                while(hand1 == hand2){
                    hand2 = ThreadLocalRandom.current().nextInt(3);
                }
                System.out.println(allPlayers.get(0).myName + " new hand: "+ hand[hand1]);
                System.out.println(allPlayers.get(1).myName + " new hand: "+ hand[hand2]);
                if(hand[hand1] == "Rock"){
                    if(hand[hand2] == "Paper") {
                        losers = allPlayers.get(0);
                    }else{
                        losers = allPlayers.get(1);
                    }
                } else if (hand[hand1] == "Paper"){
                    if(hand[hand2] == "Rock"){
                        losers = allPlayers.get(1);
                    }else{
                        losers = allPlayers.get(0);
                    }
                }else if (hand[hand1] == "Scissor"){
                    if(hand[hand2] == "Rock"){
                        losers = allPlayers.get(0);
                    }else if(hand[hand2] == "Paper") {
                        losers = allPlayers.get(1);
                    }
                }
            } 

        }
        
        System.out.println("---------Round Over-----------");

        System.out.println("Eliminated: " + losers.myName + " Score: " + losers.myScore);
        allPlayers.remove(losers);
        allPlayersName.remove(losers.myName);

    }
}

class Player implements Runnable{
    String myName;
    String myHand;
    int myScore = 0;
    ArrayList<String> allPlayersHand;
    
    public Player(ArrayList<String> allPlayersHand, String Name, String hand){
        this.myHand = hand;
        this.allPlayersHand = allPlayersHand;
        this.myHand = hand;
        this.myName = Name;
        
    }
    
    @Override
    public synchronized void run(){
        for(String op: allPlayersHand){
            if(myHand == "Rock"){
                if(op == "Rock"){
                    
                }else if(op == "Paper") {
                    myScore--;
                }else{
                    myScore++;
                }
            } else if (myHand == "Paper"){
                if(op == "Rock"){
                    myScore++;
                }else if(op == "Paper") {
                    
                }else{
                    myScore--;
                }
            }else if (myHand == "Scissor"){
                if(op == "Rock"){
                    myScore--;
                }else if(op == "Paper") {
                    myScore++;
                }else{
                    
                }
            }
        }
        System.out.println(myName + " Score: " + myScore);
    }
    public void setNewHand(String newHand){
        this.myHand = newHand;
    }
    
}

class GameExecutor implements Executor{
    final Queue<Runnable> tasks = new ArrayDeque<>();
    final Executor executor;
    Runnable active;
    
    public GameExecutor(Executor executor) {
        this.executor = executor;
    }
    
    @Override
    public synchronized  void execute(Runnable newTask) {
        tasks.offer(new Runnable() {
            @Override
            public void run() {
                try {
                    newTask.run();
                } finally{
                    scheduleNext();
                }
                
            }
        });
    }
    
    public synchronized void scheduleNext(){
        if((active = tasks.poll()) != null){
            executor.execute(active);
        }
    }
}
