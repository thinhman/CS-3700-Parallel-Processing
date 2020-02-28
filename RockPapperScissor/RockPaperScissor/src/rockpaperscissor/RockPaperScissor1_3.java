package rockpaperscissor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

public class RockPaperScissor1_3 {
    
    public static void main(String[] args) throws InterruptedException {
        long startTime = System.nanoTime();
        String[] hand = {"Rock", "Paper", "Scissor"};
        
        System.out.print("Please Enter the Number of Players: ");
        Scanner scan = new Scanner(System.in);
        int numPlayers = scan.nextInt();
        
        ArrayList<String> allPlayers = new ArrayList<>();
        ArrayList<String> allPlayersHand = new ArrayList<String>(numPlayers);
        HashMap<String, Integer> scores = new HashMap<>();
        
        ArrayList<Player> players = new ArrayList<>(numPlayers);
        ArrayList<Future> fut = new ArrayList<>();
        int numProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println("Number of Avaliable Processors: " + numProcessors);
        
        ExecutorService threadPool = Executors.newFixedThreadPool(numProcessors);
        
        for(int i = 0; i < numPlayers; i++){
            allPlayers.add("Thread" + i);
        }
        
        do{
            players.clear();
            //give all players their hand
            for(int i = 0; i < numPlayers; i++){
                allPlayersHand.add(hand[ThreadLocalRandom.current().nextInt(3)]);
            }

            //Memoized scores
            for(String s: hand){
                ScoreThread getScore = new ScoreThread(s, scores, allPlayersHand);
                Future<?> f = threadPool.submit(getScore);
                fut.add(f);
            }

            //Player Threads
            for(int i = 0; i < numPlayers; i++){
                Player playerThread = new Player(scores, allPlayers.get(i), allPlayersHand.get(i));
                players.add(playerThread);
                System.out.println(playerThread.myName + ' ' + playerThread.myHand);
                Future<?> f = threadPool.submit(playerThread);
                fut.add(f);
            }
            
            WinnerThread win = new WinnerThread(players, allPlayers);
            Future<?> ft = threadPool.submit(win);
            fut.add(ft);

            for(Future f : fut){
                try{
                    f.get();
                }catch(ExecutionException e){

                }
            }
            numPlayers--;
            fut.clear();
            allPlayersHand.clear();
        }while(numPlayers > 1);
            
        for(Player winner: players)
        {
            System.out.println("Winner is: " + winner.myName);   
        }
         
        threadPool.shutdown();
        
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        System.out.format("Time elapsed: %d ns\n", timeElapsed);    
    }
    
}

class WinnerThread implements  Runnable{
    ArrayList<Player> allPlayers;
    ArrayList<Player> losers;
    int lowest = Integer.MAX_VALUE;
    ArrayList<String> allPlayersName;
    
    public WinnerThread (ArrayList<Player> allPlayers, ArrayList<String> allPlayersName){
        this.allPlayers = allPlayers;
        this.losers = new ArrayList<>();
        this.allPlayersName = allPlayersName;
    }
    
    @Override
    public void run(){
        System.out.println("Winner Thread Running...");
        for(int x = 0; x < allPlayers.size(); x++){
            if(allPlayers.get(x).myScore < lowest){
                lowest = allPlayers.get(x).myScore;
                losers.clear();
                losers.add(allPlayers.get(x));
            } 
        }
        
        System.out.println("---------Round Over-----------");
        
        for(Player eliminated: losers){
            System.out.println("Eliminated: " + eliminated.myName + " Score: " + eliminated.myScore);
            allPlayersName.remove(eliminated.myName);
            allPlayers.remove(eliminated);
        } 
            
        lowest = Integer.MAX_VALUE;
    }
}

class ScoreThread implements Runnable{
    HashMap<String, Integer> scores;
    ArrayList<String> allPlayersHand;
    String hand;
    int score;
    
    public ScoreThread(String hand, HashMap<String, Integer> scores, ArrayList<String> allPlayersHand){
        this.hand = hand;
        this.scores = scores;
        this.allPlayersHand = allPlayersHand;
        score = 0;
    }
    
    @Override
    public void run(){
        for(String s: allPlayersHand){
            if(hand == "Rock"){
                if(s == "Rock"){
                    
                }else if(s == "Paper") {
                    score--;
                }else{
                    score++;
                }
            } else if (hand == "Paper"){
                if(s == "Rock"){
                    score++;
                }else if(s == "Paper") {
                    
                }else{
                    score--;
                }
            }else if (hand == "Scissor"){
                if(s == "Rock"){
                    score--;
                }else if(s == "Paper") {
                    score++;
                }else{
                    
                }
            }
        }
        
        scores.put(hand, score);
    }
    
}
class Player implements Runnable{
    String myName;
    String myHand;
    int myScore = 0;
    HashMap<String, Integer> scores;
    
    public Player(HashMap<String, Integer> scores, String Name, String hand){
        this.scores = scores;
        this.myHand = hand;
        this.myName = Name;
        
    }
    
    @Override
    public void run(){
        myScore = scores.get(myHand);
    }
    public void setNewHand(String newHand){
        this.myHand = newHand;
    }
    
}
