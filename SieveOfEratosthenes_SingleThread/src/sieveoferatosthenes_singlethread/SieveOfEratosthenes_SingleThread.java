//package sieveoferatosthenes_singlethread;
//
//import java.util.Arrays;
//import java.util.Objects;
//
//public class SieveOfEratosthenes_SingleThread {
//
//    public static void main(String[] args) {
//        long startTime = System.nanoTime();
//        int n = 1_000_000; //1_000_000;
//        Boolean[] primeNumbers = new Boolean[n+1];
//        Arrays.fill(primeNumbers, Boolean.TRUE);
//        
//        int newLine = 0;
//        for(int p = 2; p*p <= n; p++){
//            if(Objects.equals(primeNumbers[p], Boolean.TRUE)){
//                for(int j = p*p; j <= n; j+= p){
//                    primeNumbers[j] = Boolean.FALSE;
//                }
//            }
//        }
//        
//        for(int i = 2; i <= n; i++) 
//        {   
//            if(Objects.equals(primeNumbers[i], Boolean.TRUE)) {
//                System.out.print(i + ", ");
//                newLine++;
//                if(newLine == 10){
//                    System.out.println();
//                    newLine = 0;
//                }
//                
//            }
//                
//        } 
//        System.out.println();
//        long endTime = System.nanoTime();
//        long timeElapsed = endTime - startTime;
//        System.out.format("Time elasped: %d ns\n", timeElapsed);
//    }
//    
//}
