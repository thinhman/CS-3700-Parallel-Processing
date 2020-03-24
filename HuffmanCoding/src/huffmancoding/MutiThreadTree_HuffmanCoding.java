//package huffmancoding;
//
//import java.io.FileNotFoundException;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.util.HashMap;
//import java.util.PriorityQueue;
//import java.nio.channels.*;
//import java.nio.charset.Charset;
//import java.nio.file.Paths;
//import java.nio.file.StandardOpenOption;
//import java.util.ArrayList;
//import java.util.EnumSet;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ForkJoinPool;
//import java.util.concurrent.Future;
//import java.util.concurrent.RecursiveAction;
//
//public class MutiThreadTree_HuffmanCoding {
//     
//    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException, ExecutionException {
//        
//        int numProcessors = Runtime.getRuntime().availableProcessors();
//        System.out.println("Number of Avaliable Processors: " + numProcessors);
//        
//        long startTime = System.nanoTime();
//
//        ExecutorService threadPool = Executors.newFixedThreadPool(numProcessors);
//        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(Paths.get("constitution.txt"), EnumSet.of(StandardOpenOption.READ), threadPool);
//        
//        final long fileSize = fileChannel.size();
//        System.out.println("Constitution file size: " + fileSize+ " bytes");
//        
//        ArrayList<ByteBuffer> buffers = new ArrayList<>();
//        ArrayList<Future> fut = new ArrayList<>();
//        HashMap<Character, Integer> map = new HashMap<>();
//        
//        
//        long position = 0;
//        int work = (int)fileSize/4;
//        Future<Integer> operation;
//        
//        while (position < fileSize){
//            ByteBuffer buffer = ByteBuffer.allocate(work);
//            operation = fileChannel.read(buffer, position);
//            buffers.add(buffer);
//            fut.add(operation);
//            position+=work;
//            
//        }
//        
//        ConcurrentHashMap<Character, String> encode = new ConcurrentHashMap<>();
//        Future encodeFuture;
//        
//        int num = 0;
//        for (Future<Integer> future : fut) {
//            future.get();
//            ByteBuffer buffer = buffers.get(num);
//            buffer.flip();
//            for(char ch: Charset.defaultCharset().decode(buffer).array()){
//                Character character = ch;
//                if (map.containsKey(character)) {
//                    map.put(character, map.get(character) + 1);
//                }
//                else {
//                    map.put(character, 1);
//                }
//            }
//            num++;
//        }
//        //System.out.println(map);
//        
//        PriorityQueue<HuffmanTreeNode> pq = new PriorityQueue<>(map.size(), 
//                (HuffmanTreeNode x, HuffmanTreeNode y) -> Integer.compare(x.freq , y.freq));            
//
//        map.entrySet().forEach((entry) -> {
//            pq.add(new HuffmanTreeNode(entry.getKey(), entry.getValue()));
//        });
//        
//        while(pq.size()>1){
//            HuffmanTreeNode x = pq.poll(); 
//            HuffmanTreeNode y = pq.poll();
//              
//            HuffmanTreeNode node = new HuffmanTreeNode( '_', x.freq + y.freq);
//            node.left = x;
//            node.right = y;
//            
//            pq.add(node);
//        }
//        fileChannel.close();
//        
//        long endTime = System.nanoTime();
//        long timeElapsed = endTime - startTime;
//        System.out.format("-->Create the tree\nTime elasped: %d ns\n", timeElapsed);
//        threadPool.shutdown();
//        startTime = System.nanoTime();
//        
//        ForkJoinPool commonPool = new ForkJoinPool(4);
//        encodeFuture = commonPool.submit(new EncodeThread(pq.peek(), "", encode));
//        
//        FileWriter compressedFile = new FileWriter(Paths.get("MultiCompressed_Constitution.txt").toFile());
//        encodeFuture.get();
//        
//        for(ByteBuffer b: buffers){
//            b.flip();
//            for(char ch: Charset.defaultCharset().decode(b).array()){
//                //System.out.print(ch);
//                compressedFile.write(encode.get(ch));
//            }
//        }
//
//        endTime = System.nanoTime();
//        timeElapsed = endTime - startTime;
//        System.out.format("-->Encode the file using the tree\nTime elapsed: %d ns\n", timeElapsed);
//        
//        compressedFile.close();
//        
//        long outFileSize = Paths.get("MultiCompressed_Constitution.txt").toFile().length();
//        
//        System.out.println("Constitution compressed file size: " + (outFileSize/8.0f) + " bytes");
//        System.out.println("Compressed by " + ((outFileSize / 8.0f)/fileSize * 100) + "%");
//        
//    }
//}
//class EncodeThread extends RecursiveAction {
//    HuffmanTreeNode root;
//    String s; 
//    ConcurrentHashMap<Character, String> encode;
//    
//    public EncodeThread(HuffmanTreeNode root, String s, ConcurrentHashMap<Character, String> encode){
//        this.root = root;
//        this.s = s;
//        this.encode = encode;
//    }
//    
//    @Override
//    protected void compute() {
//        if(root == null){
//            return;
//        }
// 
//        if(root.c != '_') {
//            encode.put(root.c, s);
//        }
//        
//        invokeAll(new EncodeThread(root.left, s + "0", encode), new EncodeThread(root.right, s + "1", encode)); 
//    }
//    
//}
//
//    
//class HuffmanTreeNode{
//    char c;
//    int freq;
//    
//    HuffmanTreeNode right;
//    HuffmanTreeNode left;
//
//    public HuffmanTreeNode(char c, int freq) {
//        this.c = c;
//        this.freq = freq;
//        this.right = null;
//        this.left = null;
//    }
//    
//    
//}
//
//
