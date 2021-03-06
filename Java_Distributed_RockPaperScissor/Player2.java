import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Player2 {

    public static void main(String[] args)throws IOException {
        String[] hands = {"Rock", "Paper", "Scissor"};
        String myHand = hands[new Random(System.currentTimeMillis()).nextInt(3)];
        int score = 0;
        int numGames = 0;

        try {
            numGames = Integer.parseInt(args[0]);
        } catch (Exception e) {
            System.err.println("Command arg error");
            System.exit(1);
        }
        System.out.println("I am Player2, number of games: " + numGames);
        Socket player1Connection = new Socket("localhost", 6066);
        ServerSocket player3Listen = new ServerSocket(8066);
        Socket player3Connection = player3Listen.accept();
        for(int game = 1; game <= numGames; game++){
            int gameScore = 0;
            System.out.println("Game " + game + " My Hand: " + myHand);

            //Connect to Player1 and receive
            DataInputStream fromPlayer1 = new DataInputStream(player1Connection.getInputStream());
            String player1Hand = fromPlayer1.readUTF();
            gameScore += getScore(myHand, player1Hand);
            System.out.println("Player1 hand: " + player1Hand);

            //Connect to Player3 and send

            DataOutputStream sendToPlayer3 = new DataOutputStream(player3Connection.getOutputStream());
            sendToPlayer3.writeUTF(myHand);

            //Send to Player 1
            DataOutputStream sendToPlayer1 = new DataOutputStream(player1Connection.getOutputStream());
            sendToPlayer1.writeUTF(myHand);

            //Receive from Player3
            DataInputStream fromPlayer3 = new DataInputStream(player3Connection.getInputStream());
            String player3Hand = fromPlayer3.readUTF();
            gameScore += getScore(myHand, player3Hand);
            System.out.println("Player3 hand: " + player3Hand);

            System.out.println("Game Score: " + gameScore);
            score += gameScore;
            System.out.println("My Total Score: " + score + '\n');
            myHand = hands[new Random().nextInt(3)];
        }
        player1Connection.close();
        player3Connection.close();
    }

    public static int getScore(String myHand, String opponentsHand){
        int score = 0;
        if(myHand.equals("Rock")){
            if(opponentsHand.equals("Scissor"))
                score++;
        } else if (myHand.equals("Paper")){
            if(opponentsHand.equals("Rock"))
                score++;
        }else if (myHand.equals("Scissor")){
            if(opponentsHand.equals("Paper"))
                score++;
        }

        return score;
    }
}
