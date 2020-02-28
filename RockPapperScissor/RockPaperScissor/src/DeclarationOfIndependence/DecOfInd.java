package DeclarationOfIndependence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class DecOfInd {
    
    public static void main(String[] args) throws FileNotFoundException {
        long startTime = System.nanoTime();
        
        PrintWriter outFile = new PrintWriter("DecOfIndReverse.txt");
        File file = new File("DeclarationOfIndependence.txt");
        Scanner inputFile = new Scanner(file);
        String currentLine;
        String str;
        ArrayList<String[]> row = new ArrayList<>();
        
        while (inputFile.hasNext()){
            currentLine = inputFile.nextLine();
            String[] col = currentLine.split(" ");
            row.add(col);
        }
        
        for(int i = row.size() - 1; i >= 0; i--){
            String[] col = row.get(i);
            for(int y = col.length - 1; y >= 0; y--){
                str = col[y].replaceAll("\\p{Punct}",""); 
                outFile.print(str + " ");
            }
            outFile.println();
        }
        inputFile.close();
        outFile.close();

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        System.out.format("Time elapsed: %d ns\n", timeElapsed);   
        
    }
    
}
