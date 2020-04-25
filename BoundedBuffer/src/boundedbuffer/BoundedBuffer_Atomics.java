//package boundedbuffer;
//
//import java.util.concurrent.atomic.AtomicInteger;
//public class BoundedBuffer_Atomics {
//    public static void main(String[] args) throws InterruptedException {
//        long startTime = System.currentTimeMillis()/1000;
//        Buffer buffer = new Buffer();
//        
//        //Consumers
//        Thread consumer1 = new Thread(new Consumer("Consumer1", buffer));
//        consumer1.start();
//        Thread consumer2 = new Thread(new Consumer("Consumer2", buffer));
//        consumer2.start();
//        
//        Thread consumer3 = new Thread(new Consumer("Consumer3", buffer));
//        consumer3.start();
//        Thread consumer4 = new Thread(new Consumer("Consumer4", buffer));
//        consumer4.start();
//        Thread consumer5 = new Thread(new Consumer("Consumer5", buffer));
//        consumer5.start();
//        
//        //Producers
//        Thread producer1 = new Thread(new Producer("Producer1", buffer));
//        producer1.start();
//        Thread producer2 = new Thread(new Producer("Producer2", buffer));
//        producer2.start();
//        
////        Thread producer3 = new Thread(new Producer("Producer3", buffer));
////        producer3.start();
////        Thread producer4 = new Thread(new Producer("Producer4", buffer));
////        producer4.start();
////        Thread producer5 = new Thread(new Producer("Producer5", buffer));
////        producer5.start();
//        
//        producer1.join();
//        producer2.join();
//        
////        producer3.join();
////        producer4.join();
////        producer5.join();
//        buffer.producerFinished = true;
//        
//        consumer1.join();
//        consumer2.join();
//        consumer3.join();
//        consumer4.join();
//        consumer5.join();
//        
//        long endTime = System.currentTimeMillis()/1000;
//        long timeElapsed = endTime - startTime;
//        System.out.println("Time: " + timeElapsed + " sec");
//    }
//}
//
//class Buffer {
//    volatile AtomicInteger count; 
//    boolean producerFinished = false;
//    boolean consumerFinished = false;
//    int consumeCount;
//    public Buffer(){
//        this.count = new AtomicInteger(0);
//        this.consumeCount = 0;
//    }
//    
//    int produce() throws InterruptedException{
//        int current = count.get();
//        
//        if(current < 10){
//            int next = current + 1;
//            if (count.compareAndSet(current, next))
//                return 1;
//            else
//                return 0;
//        }else
//            return 0;
//        
//    }
//
//    boolean consume(String name) throws InterruptedException {
//        
//        if(!producerFinished){
//            int current = count.get();
//            int next = current - 1;
//            if(next >= 0 && count.compareAndSet(current, next)){
//                consumeCount++;
//            
//                System.out.println(name + " consumed item, remaning in buffer: " + current);
//                return true;
//            }else
//                return false;
//        }else{
//            int current = count.get();
//            int next = current - 1;
//            if(next >= 0 ){
//                if(count.compareAndSet(current, next)){
//                    System.out.println(name + " consumed item, remaning in buffer: " + current);
//                    return true;
//                }else{
//                    return false;
//                }
//            }else{
//                consumerFinished = true;
//                return false;
//            }
//                
//        }
//        
//        
//        
//            
//        
//    }
//    
//}
//
//class Producer implements Runnable {
//    String name;
//    Buffer buff;     
//    public Producer (String name, Buffer buff){
//        this.name = name;
//        this.buff = buff;
//    }
//    
//    @Override
//    public void run() {
//        
//        for(int i = 1; i <= 100; i++){
//            try {
//                if(buff.produce() == 1){
//                    System.out.println(name + " producing item: " + i);
//                }else
//                    i--;
//                
//                Thread.yield();
//            } catch (InterruptedException ex) {
//                
//            }
//        }
//        System.out.println(name + " Finished");
//        
//    }
//    
//}
//
//class Consumer implements Runnable {
//    String name;
//    Buffer buff;  
//    boolean notInterrupted = true;
//    int consumeCount = 0;
//
//    public Consumer() {
//    }
//    public Consumer (String name, Buffer buff){
//        this.name = name;
//        this.buff = buff;
//    }
//    
//    @Override
//    public void run() {
//        while(!buff.producerFinished){
//            try {
//                if(buff.consume(name)){
//                    consumeCount++;
//                    Thread.yield();
//                    System.out.println(name + " Sleeping");
//                    Thread.sleep(1000);
//                } 
//            } catch (InterruptedException e) {
//                System.out.println("INTERRUPTED");
//            }
//        }
//        
//        while(!buff.consumerFinished){
//            try {
//                if(buff.consume(name)){
//                    consumeCount++;
//                    Thread.yield();
//                    System.out.println(name + " Sleeping");
//                    Thread.sleep(1000);
//                }                
//            } catch (InterruptedException e) {
//                System.out.println("INTERRUPTED");
//            }
//        }
//        System.out.println(name + " Finished! Count: " + consumeCount);
//    }
//    
//   
//}