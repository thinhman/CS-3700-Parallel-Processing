package DeclarationOfIndependence;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultiThread_DecOfInd {
    
    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException, ExecutionException {
        
        
        int numProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println("Number of Avaliable Processors: " + numProcessors);
        for(int i = 1; i <= numProcessors; i++){
            Instant startTime = Instant.now();
            
            ExecutorService threadPool = Executors.newFixedThreadPool(i);
            System.out.println("Number of Threads used: " + i);
            
            PrintWriter outFile = new PrintWriter("MultiThread_DecOfIndReverse.txt");
            FileReader file = new FileReader("DeclarationOfIndependence.txt");
            BufferedReader inputFile = new BufferedReader(file);
            ArrayList<String[]> row = new ArrayList<>();
            ArrayList<Future> fut = new ArrayList<>();
            Future<String[]> f; 
            String currentLine;
            while ((currentLine = inputFile.readLine()) != null){
                ReverseThread reverse = new ReverseThread(currentLine);
                f = threadPool.submit(reverse);
                fut.add(f);
            }
            
            for(int y = fut.size()-1; y >= 0; y-- ){
                String[] line = (String[]) fut.get(y).get();
                for(String word: line)
                {
                    outFile.print(word + " ");
                }
                outFile.println();
            }
            outFile.close();
            inputFile.close();
            threadPool.shutdown();
            Instant finish = Instant.now();
            long timeElapsed = Duration.between(startTime, finish).toNanos();
            System.out.format("Time elapsed: %d ns\n", timeElapsed); 
        }
        
    }
}

class ReverseThread implements Callable<String[]> {
    String line;
    public ReverseThread(String line){
        this.line = line;
    }
    @Override
    public String[] call() throws Exception {
        line = line.replaceAll("\\p{Punct}","");
        String[] col = line.split(" ");
        String[] reverse = new String[col.length];
        
        int i = col.length - 1;
        for(String word: col){
            reverse[i] = word;
            i--;
        }
        return reverse;
    }
    
}