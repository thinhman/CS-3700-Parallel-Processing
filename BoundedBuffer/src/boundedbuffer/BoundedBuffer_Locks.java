//package boundedbuffer;
//
//import java.util.concurrent.locks.Condition;
//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReentrantLock;
//
//public class BoundedBuffer_Locks {
//    
//    public static void main(String[] args) throws InterruptedException {
//        long startTime = System.currentTimeMillis()/1000;
//        
//        Buffer buffer = new Buffer();
//        
//        //Consumers
//        Thread consumer1 = new Thread(new Consumer(buffer));
//        consumer1.start();
//        Thread consumer2 = new Thread(new Consumer(buffer));
//        consumer2.start();
//        
//        Thread consumer3 = new Thread(new Consumer(buffer));
//        consumer3.start();
//        Thread consumer4 = new Thread(new Consumer(buffer));
//        consumer4.start();
//        Thread consumer5 = new Thread(new Consumer(buffer));
//        consumer5.start();
//        
//        //Producers
//        Thread producer1 = new Thread(new Producer(buffer));
//        producer1.start();
//        Thread producer2 = new Thread(new Producer(buffer));
//        producer2.start();
//        
////        Thread producer3 = new Thread(new Producer(buffer));
////        producer3.start();
////        Thread producer4 = new Thread(new Producer(buffer));
////        producer4.start();
////        Thread producer5 = new Thread(new Producer(buffer));
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
//}
//
//class Buffer {
//    volatile int count;
//    final Lock lock = new ReentrantLock();
//    final Condition notFull  = lock.newCondition(); 
//    final Condition notEmpty = lock.newCondition(); 
//    
//    public Buffer(){
//        this.count = 0;
//    }
//    
//    void produce() throws InterruptedException{
//        
//        lock.lock();
//        try {
//            while (count == 10)
//                notFull.await();
//            
//            count++;
//            System.out.println("Producer #" + Thread.currentThread().getId() + " producing item: " + count);
//            notEmpty.signalAll();
//        } finally {
//            lock.unlock();
//        }
//
//    }
//
//    void consume() throws InterruptedException {
//        
//       lock.lock();
//       try{
//           while (count == 0)
//                notEmpty.await();
//           count--;
//           System.out.println("Consumer #" + Thread.currentThread().getId() + " remaning in buffer " + count);
//           notFull.signalAll();
//       }finally{
//           lock.unlock();
//       }
//        
//    }
//    
//}
//
//class Producer implements Runnable {
//    Buffer buff;     
//    public Producer (Buffer buff){
//        this.buff = buff;
//    }
//    
//    @Override
//    public void run() {
//        
//        for(int i = 0; i < 100; i++){
//            try {
//                buff.produce();
//                System.out.println("Producer #" + Thread.currentThread().getId() + " producing: " + i);
//            } catch (InterruptedException ex) {
//                
//            }
//        }
//        System.out.println("Producer #" + Thread.currentThread().getId() + " Finished");
//        
//    }
//    
//}
//
//class Consumer implements Runnable {
//    Buffer buff;  
//    public Consumer (Buffer buff){
//        this.buff = buff;
//    }
//    
//    @Override
//    public void run() {
//        while(true){
//            try {
//                buff.consume();
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                while(buff.count != 0)
//                {
//                    try {
//                        buff.consume();
//                        Thread.sleep(1000);
//                    } catch (InterruptedException ex) {
//                        
//                    }
//                    
//                }
//                System.out.println("No more in buffer");
//                break;
//            }
//        }
//    }
//    
//   
//}
