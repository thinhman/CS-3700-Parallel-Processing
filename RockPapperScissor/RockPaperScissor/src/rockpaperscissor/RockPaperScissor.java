//package rockpaperscissor;
//
//import java.util.ArrayList;
//import java.util.Scanner;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//import java.util.concurrent.ThreadLocalRandom;
//
//public class RockPaperScissor {
//    
//    public static void main(String[] args) throws InterruptedException {
//        //Start timing
//        long startTime = System.nanoTime();
//        
//        String[] hand = {"Rock", "Paper", "Scissor"};
//        
//        int numProcessors = Runtime.getRuntime().availableProcessors();
//        System.out.println("Number of Avaliable Processors: " + numProcessors);
//        ExecutorService threadPool = Executors.newFixedThreadPool(numProcessors);
//        
//        System.out.print("Please Enter the Number of Players: ");
//        Scanner scan = new Scanner(System.in);
//        int numPlayers = scan.nextInt();
//        
//        ArrayList<String> allPlayerNames = new ArrayList<>(numPlayers);
//        ArrayList<Player> players = new ArrayList<>();
//        ArrayList<Future> fut = new ArrayList<>(numPlayers+1);
//        
//        for(int i = 0; i< numPlayers; i++){
//            allPlayerNames.add("Player" + i);
//        }
//                
//        do{
//            players.clear();
//            for(int i = 0; i < numPlayers; i++){
//                Player playerThread = new Player(players, allPlayerNames.get(i), hand[ThreadLocalRandom.current().nextInt(3)]);
//                players.add(playerThread);
//                System.out.println(playerThread.myName + ' ' + playerThread.myHand);
//                Future<?> f = threadPool.submit(playerThread);
//                fut.add(f);
//            }
//            WinnerThread win = new WinnerThread(players, allPlayerNames);
//            Future<?> ft = threadPool.submit(win);
//            fut.add(ft);
//            
//            for(Future f : fut){
//                try{
//                    f.get();
//                }catch(ExecutionException e){
//
//                }
//            }
//            numPlayers--;
//            fut.clear();
//        }while(numPlayers > 1);
//        
//        for(String winner: allPlayerNames)
//        {
//            System.out.println("Winner is: " + winner);   
//        }
//         
//
//        threadPool.shutdown();
//        long endTime = System.nanoTime();
//        long timeElapsed = endTime - startTime;
//        System.out.format("Time elapsed: %d ns\n", timeElapsed);    
//    }
//    
//}
//
//class WinnerThread implements  Runnable{
//    ArrayList<Player> allPlayers;
//    ArrayList<String> allPlayerNames;
//    ArrayList<Player> losers;
//    int lowest = Integer.MAX_VALUE;
//    
//    public WinnerThread (ArrayList<Player> allPlayers, ArrayList<String> allPlayerNames){
//        this.allPlayers = allPlayers;
//        this.losers = new ArrayList<>();
//        this.allPlayerNames = allPlayerNames;
//    }
//    
//    @Override
//    public void run(){
//        System.out.println("Winner Thread Running...");
//        for(int x = 0; x < allPlayers.size(); x++){
//            if(allPlayers.get(x).myScore < lowest){
//                lowest = allPlayers.get(x).myScore;
//                losers.clear();
//                losers.add(allPlayers.get(x));
//            } 
//        }
//        
//        System.out.println("---------Round Over-----------");
//        for(Player eliminated: losers){
//            System.out.println("Eliminated: " + eliminated.myName + " Score: " + eliminated.myScore);
//            allPlayerNames.remove(eliminated.myName);
//        } 
//            
//        lowest = Integer.MAX_VALUE;
//    }
//}
//class Player implements Runnable{
//    String myName;
//    String myHand;
//    int myScore = 0;
//    ArrayList<Player> opponents;
//    
//    public Player(ArrayList<Player> opponents, String Name, String hand){
//        this.myHand = hand;
//        this.opponents = opponents;
//        this.myHand = hand;
//        this.myName = Name;
//        
//    }
//    
//    @Override
//    public void run(){
//        for(Player op: opponents){
//            if(myHand == "Rock"){
//                if(op.myHand == "Paper") {
//                    myScore--;
//                }else{
//                    myScore++;
//                }
//            } else if (myHand == "Paper"){
//                if(op.myHand == "Rock"){
//                    myScore++;
//                }else if(op.myHand == "Scissor") {
//                    myScore--;
//                }
//            }else if (myHand == "Scissor"){
//                if(op.myHand == "Rock"){
//                    myScore--;
//                }else if(op.myHand == "Paper") {
//                    myScore++;
//                }
//            }
//        }
//    }
//    public void setNewHand(String newHand){
//        this.myHand = newHand;
//    }
//    
//}
