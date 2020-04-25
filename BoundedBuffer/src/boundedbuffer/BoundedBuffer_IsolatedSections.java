//package boundedbuffer;
//
//public class BoundedBuffer_IsolatedSections {
//    
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
//        
//        consumer1.interrupt();
//        consumer2.interrupt();
//        consumer3.interrupt();
//        consumer4.interrupt();
//        consumer5.interrupt();
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
//
//}
//
//class Buffer {
//    volatile int count;
//    private Object consumeLock = new Object();
//    private Object produceLock = new Object();
//    
//    public Buffer(){
//        this.count = 0;
//    }
//    
//    synchronized void produce() throws InterruptedException{
//        
//        while (count == 10){
//            wait();
//        }
//        count++;
//        notifyAll();
//    }
//
//    synchronized boolean consume() throws InterruptedException {
//        
//        while (count == 0)
//            wait();
//        count--;
//        
//        notifyAll();
//        return true;
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
//                buff.produce();
//                System.out.println(name + " producing item: " + i);
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
//    public Consumer (String name, Buffer buff){
//        this.name = name;
//        this.buff = buff;
//    }
//    
//    @Override
//    public void run() {
//        while(true){
//            try {
//                if (buff.consume()){
//                    System.out.println(name + " consumed item, remaning in buffer: " + buff.count);
//                    System.out.println(name + " Sleeping");
//                    Thread.yield();
//                    Thread.sleep(1000);
//                }
//                    
//                
//                
//            } catch (InterruptedException e) {
//                while(buff.count != 0)
//                {
//                    try {
//                         if (buff.consume()){
//                            System.out.println(name + " consumed item, remaning in buffer: " + buff.count);
//                            System.out.println(name + " Sleeping");
//                            Thread.yield();
//                            Thread.sleep(1000);
//                        }
//                    } catch (InterruptedException ex) {
//                        
//                    }  
//                }
//                System.out.println(name + ": No more in buffer");
//                break;
//            }
//        }
//    }
//    
//   
//}
