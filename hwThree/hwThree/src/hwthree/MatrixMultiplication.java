package hwthree;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;


public class MatrixMultiplication {
    
    
    public static void main(String [] args) throws InterruptedException{
        final int m = 100;
        final int n = 50;
        final int p = 65;
        
        int[][] a = new int[m][n];
        int[][] b = new int[n][p];
        
        //initalize Matrix A and print
        System.out.println("Matrix A");
        for(int row = 0; row < m; row++){
            System.out.print("|   ");
            for(int col = 0; col < n; col++){
                a[row][col] = ThreadLocalRandom.current().nextInt(2)+1;
                System.out.print(a[row][col] + "   ");
            }
            System.out.println("|");
        }
        
        //initalize Matrix A and print
        System.out.println("Matrix B");
        for(int row = 0; row < n; row++){
            System.out.print("|   ");
            for(int col = 0; col < p; col++){
                b[row][col] = ThreadLocalRandom.current().nextInt(2)+1;
                System.out.print(b[row][col] + "   ");
            }
            System.out.println("|");
        }
        
        //Record Time of Execution
        System.out.println("Matrix Multiplication start...");
	long start = System.currentTimeMillis();
        
        MatrixMulti mm = new MatrixMulti(a, b, m, p);
        mm.multiply();
        
        long finish = System.currentTimeMillis();
	
        //Print Matrix C and check
        System.out.println("Matrix B");
        int[][] res = mm.getResult();
        for(int row = 0; row < m; row++){
            System.out.print("|   ");
            for(int col = 0; col < p; col++){
                System.out.print(res[row][col] + "   ");
            }
            System.out.println("|");
        }
        
        System.out.format("Time elapsed: %d ms\n", finish-start);
    }   
}
class MatrixMultiThread implements Runnable{
    MatrixMulti Matrix;
    int m;
    int work;
    boolean isRow;
    //int p;
    
    public MatrixMultiThread(MatrixMulti M, int start, int work, boolean flag){
        this.Matrix = M;
        this.m = start;
        this.work = work;
        this.isRow = flag;
        //this.p = col;
    }
    
    @Override
    public void run(){
        int sum;
        if(isRow){
            for(int i = 0; i < work; i++){
                for(int p = 0; p < Matrix.C[0].length; p++){
                    sum = 0;
                    for(int k = 0; k < Matrix.A[m].length; k++){
                        sum += Matrix.A[m+i][k] * Matrix.B[k][p];
                    }
                    Matrix.C[m+i][p] = sum;
                }    
            }
        }else{
            for(int i = 0; i < work; i++){
                for(int p = 0; p < Matrix.C.length; p++){
                    sum = 0;
                    for(int k = 0; k < Matrix.B.length; k++){
                        sum += Matrix.A[p][k] * Matrix.B[k][m+i];
                    }
                    Matrix.C[p][m+i] = sum;
                }    
            }
        }
        
        
        
        
    }
}
class MatrixMulti {
    int[][] A;
    int[][] B;
    int[][] C;
    //Vary # of Threads [1, 2, 4, 8]
    int numThreads = 8;
    ExecutorService exec = Executors.newFixedThreadPool(numThreads);
    int work;
    
    public MatrixMulti(int[][] a, int[][] b, int m, int p){
        this.A = a;
        this.B = b;
        this.C = new int[m][p];
    }
    
    public void multiply() throws InterruptedException {
        Future[] fut = new Future[numThreads];
        //Split Thread workload by max(C.row.length, C.col.length)
        if(A.length >= B[0].length)
        {
            
            int split = A.length/numThreads;
            int start = 0;
            int end = start + split;
            work = A.length - end;
            
            for(int x = 0; x < numThreads; x++){
                
                Future f;
                if(!(x+1 < numThreads)){
                    f = exec.submit(new MatrixMultiThread(this, start, work, true));
                }else{
                    f = exec.submit(new MatrixMultiThread(this, start, end, true));
                }
                start += split;
                end += split;
                fut[x] = f;
            }
            //Future f = exec.submit(new WorkerThread(A, B, C, 0, 0, 0, 0, 0, 0, A.length, exec, numThreads));
            
            
        }else{
            int split = C[0].length/numThreads;
            int start = 0;
            int end = start + split;
            work = C[0].length - end;
            for(int x = 0; x < numThreads; x++){
                Future f;
                if(!(x+1 < numThreads)){
                    f = exec.submit(new MatrixMultiThread(this, start, work, false));
                }else{
                    f = exec.submit(new MatrixMultiThread(this, start, end, false));
                }
                start += split;
                end += split;
                fut[x] = f;
            }
        }
        for(Future f : fut){
            try{
                f.get();
            }catch(ExecutionException e){
                
            }
        }
        exec.shutdown();
	
    }
    
    public int[][] getResult() {
	return C;
    }
    
    
}

