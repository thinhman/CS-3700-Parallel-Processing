package rockpaperscissor;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class RockPaperScissor2_2 {
    
    public static void main(String[] args) throws InterruptedException {
        long startTime = System.nanoTime();
        String[] hand = {"Rock", "Paper", "Scissor"};
        
        System.out.print("Please Enter the Number of Players: ");
        Scanner scan = new Scanner(System.in);
        int numPlayers = scan.nextInt();

        int numProcessors = Runtime.getRuntime().availableProcessors();
        ArrayList<String> allPlayers = new ArrayList<>(numPlayers);
        ArrayList<String> allPlayersHand;
        ArrayList<Player> Winners = new ArrayList<>();
        ArrayList<Player> Losers = new ArrayList<>();
        Player playerThread;
        
        
        for(int i = 0; i< numPlayers; i++){
            allPlayers.add("Player" + i);
            
        }
        
        do{
            ArrayList<Player> players = new ArrayList<>(numPlayers);
            AtomicReference<ThreadPoolExecutor> threadpool = new AtomicReference<>(new ThreadPoolExecutor(numProcessors, numProcessors, 0L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(numPlayers)));
            Winners.clear();
            Losers.clear();
            allPlayersHand = new ArrayList<>(numPlayers);
            for(int i = 0; i < numPlayers; i++){
                allPlayersHand.add(hand[ThreadLocalRandom.current().nextInt(3)]);
            }
            
            for(int i = 0; i< numPlayers; i++){
                playerThread = new Player(players, Winners, Losers, allPlayers, allPlayers.get(i), allPlayersHand.get(i), (numPlayers/2) + i);
                players.add(playerThread);
                System.out.println(playerThread.myName + ' ' + playerThread.myHand);
            }
            
             //if odd numPlayers add the middle player to winners array first
            if(numPlayers % 2 != 0) {
                Winners.add(players.get(numPlayers/2));
                players.remove(numPlayers/2);
            }
            
            for(int i = 0; i< numPlayers/2; i++){
                threadpool.get().execute(players.get(i));
            }
            
            while (threadpool.get().getCompletedTaskCount() != numPlayers/2) {                
                
            }
            
            for(Player loss: Losers){
                allPlayers.remove(loss.myName);
                numPlayers--;
            }
            players.clear();
            
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

class Player implements Runnable{
    String myName;
    String myHand;
    int myOpponenet;
    ArrayList<Player> opponents;
    ArrayList<String> allPlayers;
    ArrayList<Player> Winners;
    ArrayList<Player> Losers;
    String[] hand = {"Rock", "Paper", "Scissor"};
    
    public Player(ArrayList<Player> opponents, ArrayList<Player> Winners, ArrayList<Player> Losers, ArrayList<String> allPlayers,  String Name, String hand, int myOpponent){
        this.opponents = opponents;
        this.allPlayers = allPlayers;
        this.myHand = hand;
        this.myName = Name;
        this.Winners = Winners;
        this.Losers = Losers;
        this.myOpponenet = myOpponent;
        
    }
    
    @Override
    public synchronized void run(){
        play();
        
       
    }
    
    public void play(){
        
        System.out.println("Play: " + this.myName + " vs. " + allPlayers.get(myOpponenet));
        
        if(myHand == "Rock"){
            if(opponents.get(myOpponenet).myHand == "Rock"){
                //Draw play again
                setNewHand(hand[ThreadLocalRandom.current().nextInt(3)]);
                opponents.get(myOpponenet).setNewHand(hand[ThreadLocalRandom.current().nextInt(3)]);
                System.out.println("Draw Re-play: " + this.myName + ": " + this.myHand);
                System.out.println("Draw Re-play: " + opponents.get(myOpponenet).myName + ": " + opponents.get(myOpponenet).myHand);
                play();
            }else if(opponents.get(myOpponenet).myHand == "Paper") {
                //loss
                //allPlayers.remove(this.myName);
                Losers.add(this);
                Winners.add(opponents.get(myOpponenet));
            }else{
                //allPlayers.remove(opponents.get(myOpponenet).myName);
                Losers.add(opponents.get(myOpponenet));
                Winners.add(this);
            }
        } else if (myHand == "Paper"){
            if(opponents.get(myOpponenet).myHand == "Rock"){
                //Win
                //allPlayers.remove(opponents.get(myOpponenet).myName);
                Losers.add(opponents.get(myOpponenet));
                Winners.add(this);
            }else if(opponents.get(myOpponenet).myHand == "Paper") {
                //Draw play again
                setNewHand(hand[ThreadLocalRandom.current().nextInt(3)]);
                opponents.get(myOpponenet).setNewHand(hand[ThreadLocalRandom.current().nextInt(3)]);
                System.out.println("Draw Re-play: " + this.myName + ": " + this.myHand);
                System.out.println("Draw Re-play: " + opponents.get(myOpponenet).myName + ": " + opponents.get(myOpponenet).myHand);
                play();
            }else{
                //Loss
                //allPlayers.remove(this.myName);
                Losers.add(this);
                Winners.add(opponents.get(myOpponenet));
            }
        }else if (myHand == "Scissor"){
            if(opponents.get(myOpponenet).myHand == "Rock"){
               //Loss
               //allPlayers.remove(this.myName);
               Losers.add(this);
               Winners.add(opponents.get(myOpponenet));
            }else if(opponents.get(myOpponenet).myHand == "Paper") {
                //Win
                //allPlayers.remove(opponents.get(myOpponenet).myName);
                Losers.add(opponents.get(myOpponenet));
                Winners.add(this);
            }else{
                //Draw play again
                setNewHand(hand[ThreadLocalRandom.current().nextInt(3)]);
                opponents.get(myOpponenet).setNewHand(hand[ThreadLocalRandom.current().nextInt(3)]);
                System.out.println("Draw Re-play: " + this.myName + ": " + this.myHand);
                System.out.println("Draw Re-play: " + opponents.get(myOpponenet).myName + ": " + opponents.get(myOpponenet).myHand);
                play();
            }
        }
    }
    
    public void setNewHand(String newHand){
        this.myHand = newHand;
    }
    
}
