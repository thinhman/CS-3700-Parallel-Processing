package hwthree;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SockMatching {
    
    public static void main(String [] args) throws InterruptedException{
        //Max amount of socks possible is 400
        BlockingQueue<Socks> unpairedQueue = new ArrayBlockingQueue<>(400);
        BlockingQueue<String> washingQueue = new ArrayBlockingQueue<>(200);
        
        //Each sock thread creates rand#(1-100) of socks
        //Colors: [Red, Green, Blue, Orange]
        Thread thread1 = new socksThread("Red", unpairedQueue, ThreadLocalRandom.current().nextInt(100) + 1);
        Thread thread2 = new socksThread("Blue", unpairedQueue, ThreadLocalRandom.current().nextInt(100) + 1);
        Thread thread3 = new socksThread("Green", unpairedQueue, ThreadLocalRandom.current().nextInt(100) + 1);
        Thread thread4 = new socksThread("Orange", unpairedQueue, ThreadLocalRandom.current().nextInt(100) + 1);
        
        //Matching sock thread
        //Washer thread to destroy socks
        Thread match = new Matching(unpairedQueue, washingQueue);
        Thread washer = new Washer(washingQueue);
        
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        match.start();
        washer.start();
        
        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
        
        //interrupt matching thread after the 4 socks thread is done
        //matching will continue until queue is empty
        match.interrupt();
        match.join();
        
        //After matching is done interrupt washer thread
        //washer will continue until washer queue is done
        washer.interrupt();
    }
    

}

class Matching extends Thread{
    int inQueue = 0;        //count of socks waiting to be matched
    int matchingPairs = 0;  //count of all the matched pairs of socks
    BlockingQueue<Socks> waiting;
    BlockingQueue<String> washerQueue;
    Socks redSock;
    Socks blueSock;
    Socks greenSock;
    Socks orangeSock;
    
    public Matching(BlockingQueue<Socks> socksQueue, BlockingQueue<String> washerQueue){
        this.waiting = socksQueue;
        this.washerQueue = washerQueue;
        this.redSock = null;
        this.blueSock = null;
        this.greenSock = null;
        this.orangeSock = null;
    }
    @Override
    public void run() {
        while(true)
        {
            try {
                check();
            } catch (InterruptedException ex) {
                while(!waiting.isEmpty()) {
                    try{
                        check();
                    }catch (InterruptedException e) {
                        
                    }
                }
                System.out.println("Matching Thread Finished");
                break;
                //Logger.getLogger(Matching.class.getName()).log(Level.SEVERE, null, ex);
                
            }
        }
        
    }
    
    public synchronized void check() throws InterruptedException{
        if(!waiting.isEmpty()){
            Socks current = waiting.take();
            inQueue = 400 - waiting.remainingCapacity();
            if(current.sockColor == "Red"){
                if(redSock == null){
                    redSock = current;
                }else{
                    matchingPairs++;
                    System.out.println("Matching Thread: Send Red socks to washer. Total matched socks: " 
                            + matchingPairs 
                            + ". Total inside queue: " + inQueue);
                    washerQueue.put("Red Socks");
                    //washerQueue.put(redSock);
                    redSock = null;  
                }
            }else if(current.sockColor == "Blue"){
                if(blueSock == null){
                    blueSock = current;
                }else{
                    matchingPairs++;
                    System.out.println("Matching Thread: Send Blue socks to washer. Total matched socks: " 
                            + matchingPairs 
                            + ". Total inside queue: " + inQueue);
                    washerQueue.put("Blue Socks");
                    //washerQueue.put(blueSock);
                    blueSock = null;
                    
                }
            }else if(current.sockColor == "Green"){
                if(greenSock == null){
                    greenSock = current;
                }else{
                    matchingPairs++;
                    System.out.println("Matching Thread: Send Green socks to washer. Total matched socks: " 
                            + matchingPairs 
                            + ". Total inside queue: " + inQueue);
                    washerQueue.put("Green Socks");
                    //washerQueue.put(greenSock);
                    greenSock = null;
                    
                }
            }else if(current.sockColor == "Orange"){
                if(orangeSock == null){
                    orangeSock = current;
                }else{
                    matchingPairs++;
                    System.out.println("Matching Thread: Send Orange socks to washer. Total matched socks: " 
                            + matchingPairs 
                            + ". Total inside queue: " + inQueue);
                    washerQueue.put("Orange Socks");
                    //washerQueue.put(orangeSock);
                    orangeSock = null;
                    
                }
            }
        }
    }
}

class Washer extends Thread {
    BlockingQueue<String> washerQueue;
    String toDestroy;
    
    public Washer(BlockingQueue<String> washerQueue){
        this.washerQueue = washerQueue;
    }
    
    @Override 
    public void run(){
        while(true){
            try {
                toDestroy = washerQueue.take();
                System.out.println("Washer Thread: Destroyed " + toDestroy);
                
            } catch (InterruptedException ex) {
                while(!washerQueue.isEmpty()){
                    try{
                        toDestroy = washerQueue.take();
                        System.out.println("Destroyed " + toDestroy);
                    }catch (InterruptedException e) {
                        
                    }
                }
                System.out.println("Washer Finished");
                break;
                //Logger.getLogger(Washer.class.getName()).log(Level.SEVERE, null, ex);
            }          
        }
    }
    
}

class Socks{
    String sockColor;       //Colors: [Red, Green, Blue, Orange]
    
    public Socks(String color){
        this.sockColor = color;
    }
}

//Thread Class
class socksThread extends Thread{
    String sockColor;       //Colors: [Red, Green, Blue, Orange]
    int currentThread;
    int total;
    Socks sock;
    BlockingQueue<Socks> waiting;
    
    public socksThread(String color, BlockingQueue<Socks> sockQueue, int toProduce){
        this.sockColor = color;
        this.currentThread = 0;
        this.total = toProduce;
        this.waiting = sockQueue;
    }
    
    @Override
    public synchronized void run() {
        
        for (int i = 0; i < total; i++) {
            currentThread++;
            sock = new Socks(sockColor);
            try {
                System.out.println(sockColor + " sock: Produce " + currentThread + '/' + total + ' ' + sockColor +" socks");
                waiting.put(sock);
            } catch (InterruptedException ex) {
                Logger.getLogger(socksThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    }
}
