//package hwthree;
//
//import java.util.List;
//import java.util.Random;
//import java.util.Vector;
//import java.util.concurrent.ThreadLocalRandom;
//
//public class OrderedLeaderElection {
//    
//    public static void main(String[] args) throws InterruptedException {
//        Random rand = new Random();
//        int N = rand.nextInt(5)+1;
//        Resource threadResource = new Resource();
//        Object mainWait = new Object();             //sync with main and rank thread
//        List<electedOffical> list = new Vector<>(); //holds all elected officals threads
//        
//        System.out.println("N: " + N);
//        
//        Thread rankedThread = new Thread (() -> {
//            while(true){
//                try{
//                    threadResource.checkRank();
//                    
//                    synchronized(mainWait){
//                        mainWait.notify();  //create next offical
//                    }
//                }catch(InterruptedException e){
//                    threadResource.officalLeader(); //get the final leader
//                    break;
//                }
//            }
//        }); 
//        rankedThread.start();
//
//        synchronized(mainWait){
//            for(int i = 1; i <= N; i++){
//                electedOffical t = new electedOffical("Thread"+i, ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE), threadResource);
//                list.add(t);
//                t.start();
//                mainWait.wait();
//            }
//        }
//        rankedThread.interrupt();
//        rankedThread.join();
//        list.forEach( Thread::interrupt);//(t) -> { //System.out::println
//            //t.interrupt();
//        //}); 
//        
//    }
//
//}
//
//class Resource{
//    String currentLeader = "";
//    int highestRanked = Integer.MIN_VALUE;
//    private int toCheckRank;
//    private String toCheckName;
//    private Object lock = new Object();         //sync rank and officals thread
//    private Object threadPool = new Object();   //lets all the created threads to update their leader
//    
//    public void sendRank(String newOfficalName, int newOfficalRank){
//        synchronized(lock){
//            this.toCheckName = newOfficalName;
//            this.toCheckRank = newOfficalRank;
//            lock.notify();
//        } 
//    }
//    public String getLeaderRank()throws InterruptedException{
//        
//        synchronized(threadPool){
//            threadPool.wait();
//            return currentLeader;
//        } 
//    }
//    public void checkRank() throws InterruptedException{
//                   
//        synchronized(lock){
//            while(toCheckName == null){
//                lock.wait();
//            }
//
//            synchronized(threadPool){
//                if(toCheckRank > highestRanked){
//                    System.out.println("--> New Leader: " + toCheckName + '\n');
//                    this.currentLeader = toCheckName;
//                    this.highestRanked = toCheckRank;
//                    threadPool.notifyAll();
//                }
//            }
//            toCheckName = null;
//        }
//    }
//    
//    public void officalLeader(){
//        System.out.println("-----------------------------------------\n"
//                        +"The Highest Ranking Elected Offical is: " + currentLeader
//                        + " \nWith a Rank of: " + highestRanked);
//    }
//}
//class electedOffical extends Thread{
//    String name;
//    int rank_val;
//    String leader;
//    Resource rankResource;
//    
//    public electedOffical(String name, int rank, Resource threadResource){
//        this.name = name;
//        this.rank_val = rank;
//        leader = this.name;
//        this.rankResource = threadResource;
//        
//        System.out.println("New Elected Offical's Name: " + this.name);
//        System.out.println(this.name + " Rank: " + this.rank_val);
//        System.out.println(this.name + " Leader: " + this.leader);
//        
//    }
//    
//    @Override
//    public void run(){
//        rankResource.sendRank(name, rank_val);
//        
//        while(true){
//            try {
//                leader = rankResource.getLeaderRank();
//            } catch (InterruptedException ex) {
//                break;
//            }
//        }
//        
//    }
//}
//
