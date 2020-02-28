package rockpaperscissor;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

public class RockPaperScissor2_1 {
    
    public static void main(String[] args) throws InterruptedException {
        
        long startTime = System.nanoTime();
        String[] hand = {"Rock", "Paper", "Scissor"};
        
        System.out.print("Please Enter the Number of Players: ");
        Scanner scan = new Scanner(System.in);
        int numPlayers = scan.nextInt();
        
        ArrayList<Player> players = new ArrayList<>(numPlayers);
        ArrayList<Player> Winners = new ArrayList<>();
        ArrayList<String> allPlayers = new ArrayList<>(numPlayers);
        ArrayList<Future> fut = new ArrayList<>(numPlayers/2);
        
        int numProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService threadPool = Executors.newFixedThreadPool(numProcessors);
        
        Player playerThread;
        
        for(int i = 0; i< numPlayers; i++){
            allPlayers.add("Player" + i);
        }
        
        do{
            
            Winners.clear();
            for(int i = 0; i< numPlayers; i++){
                playerThread = new Player(players, Winners, allPlayers, allPlayers.get(i), hand[ThreadLocalRandom.current().nextInt(3)], (numPlayers/2) + i);
                players.add(playerThread);
                System.out.println(playerThread.myName + ' ' + playerThread.myHand);
            }
            
            //if odd numPlayers add the middle player to winners array first
            if(numPlayers % 2 != 0) {
                Winners.add(players.get(numPlayers/2));
                players.remove(numPlayers/2);
                numPlayers--;
            }
            
            //add the first half of players to future queue
            for(int i = 0; i < numPlayers/2; i++){
                Future<?> f = threadPool.submit(players.get(i));
                fut.add(f);
            }
            
            for(Future f : fut){
                try{
                    f.get();
                }catch(ExecutionException e){

                }
            }
            
            players.clear();
            numPlayers = Winners.size();
            fut.clear();
        }while(numPlayers > 1);
        
        System.out.println("Winner is: " + Winners.get(0).myName);

        threadPool.shutdown();
        
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
    String[] hand = {"Rock", "Paper", "Scissor"};
    
    public Player(ArrayList<Player> opponents, ArrayList<Player> Winners, ArrayList<String> allPlayers,  String Name, String hand, int myOpponent){
        this.opponents = opponents;
        this.allPlayers = allPlayers;
        this.myHand = hand;
        this.myName = Name;
        this.Winners = Winners;
        this.myOpponenet = myOpponent;
        
    }
    
    @Override
    public synchronized void run(){
        play();
        
       
    }
    
    public void play(){
        
        System.out.println("Play: " + this.myName + " vs. " + opponents.get(myOpponenet).myName);
        
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
                allPlayers.remove(this.myName);
                Winners.add(opponents.get(myOpponenet));
            }else{
                allPlayers.remove(opponents.get(myOpponenet).myName);
                Winners.add(this);
            }
        } else if (myHand == "Paper"){
            if(opponents.get(myOpponenet).myHand == "Rock"){
                //Win
                allPlayers.remove(opponents.get(myOpponenet).myName);
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
                allPlayers.remove(this.myName);
                Winners.add(opponents.get(myOpponenet));
            }
        }else if (myHand == "Scissor"){
            if(opponents.get(myOpponenet).myHand == "Rock"){
               //Loss
               allPlayers.remove(this.myName);
               Winners.add(opponents.get(myOpponenet));
            }else if(opponents.get(myOpponenet).myHand == "Paper") {
                //Win
                allPlayers.remove(opponents.get(myOpponenet).myName);
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