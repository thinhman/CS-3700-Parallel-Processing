//package huffmancoding;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.PriorityQueue;
//
//public class HuffmanCoding { 
//    
//    public static void Encode(HuffmanTreeNode root, String s, HashMap<Character, String> encode) { 
//        if(root == null){
//            return;
//        }
//
//        if(root.c != '_') {
//            encode.put(root.c, s);
//        }
//        Encode(root.left, s + "0", encode); 
//        Encode(root.right, s + "1", encode); 
//    } 
//
//    public static void main(String[] args) throws FileNotFoundException, IOException {
//        
//        long startTime = System.nanoTime();
//        
//        File outputFile = new File("compressed_constitution.txt");
//        FileWriter fileWriter = new FileWriter(outputFile);
//        
//        File file = new File("constitution.txt");
//        BufferedReader inputFile = new BufferedReader(new FileReader(file));
//        
//        System.out.println("Constitution file size: " + file.length()+ " bytes");
//        
//        HashMap<Character, Integer> map = new HashMap<>();
//        HashMap<Character, String> encode = new HashMap<>();
//        
//        int c;
//        while ((c = inputFile.read()) != -1){
//            Character character = (char) c;
//            
//            if (map.containsKey(character)) {
//                map.put(character, map.get(character) + 1);
//            }
//            else {
//                map.put(character, 1);
//            }
//        }
//
//        PriorityQueue<HuffmanTreeNode> pq = new PriorityQueue<>(map.size(), 
//                (HuffmanTreeNode x, HuffmanTreeNode y) -> Integer.compare(x.freq , y.freq));
//        
//        
//        map.entrySet().forEach((entry) -> {
//            pq.add(new HuffmanTreeNode(entry.getKey(), entry.getValue()));
//        });
//        
//        while(pq.size()>1){
//            HuffmanTreeNode x = pq.poll();
//            
//            HuffmanTreeNode y = pq.poll();
//              
//            HuffmanTreeNode node = new HuffmanTreeNode( '_', x.freq + y.freq);
//            node.left = x;
//            node.right = y;
//            
//            pq.add(node);
//        }
//        
//        long endTime = System.nanoTime();
//        long timeElapsed = endTime - startTime;
//        System.out.format("-->Create the tree\nTime: %d ns\n", timeElapsed);
//        startTime = System.nanoTime();
//        
//        Encode(pq.peek(), "", encode); 
//        
//        inputFile.close();
//        inputFile = new BufferedReader(new FileReader(file));
//        
//        while((c = inputFile.read()) != -1) {
//            Character character = (char) c;
//            fileWriter.write(encode.get(character));
//        }
//        
//        
//        fileWriter.close();
//        endTime = System.nanoTime();
//        timeElapsed = endTime - startTime;
//        System.out.format("-->Encode the file using the tree\nTime elapsed: %d ns\n", timeElapsed);
//        
//        System.out.println("Constitution compressed file size: " + (outputFile.length()/8.0f) + " bytes");
//        System.out.println("Compressed by " + ((outputFile.length() / 8.0f)/file.length() * 100) + "%");
//    }
//}
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

//        System.out.println("Encoding Key Output");
//        for (Entry<Character, String> entry: encode.entrySet()) {
//            System.out.println(entry.getKey() + ": " + entry.getValue());
//        }