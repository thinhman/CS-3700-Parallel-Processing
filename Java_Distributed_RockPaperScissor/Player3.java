import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class Player3 {
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
        System.out.println("I am Player3, number of games: " + numGames);

        Socket player1Connection = new Socket("localhost", 7066);
        Socket player2Connection = new Socket("localhost", 8066);

        for(int game = 1; game <= numGames; game++){
            int gameScore = 0;
            System.out.println("Game " + game + " My Hand: " + myHand);

            DataInputStream fromPlayer1 = new DataInputStream(player1Connection.getInputStream());
            String player1Hand = fromPlayer1.readUTF();
            gameScore += getScore(myHand, player1Hand);
            System.out.println("Player1 hand: " + player1Hand);

            DataInputStream fromPlayer2 = new DataInputStream(player2Connection.getInputStream());
            String player2Hand = fromPlayer2.readUTF();
            gameScore += getScore(myHand, player2Hand);
            System.out.println("Player2 hand: " + player2Hand);

            DataOutputStream sendToPlayer1 = new DataOutputStream(player1Connection.getOutputStream());
            sendToPlayer1.writeUTF(myHand);

            DataOutputStream sendToPlayer2 = new DataOutputStream(player2Connection.getOutputStream());
            sendToPlayer2.writeUTF(myHand);

            System.out.println("Game Score: " + gameScore);
            score += gameScore;
            System.out.println("My Total Score: " + score + '\n');
            myHand = hands[new Random().nextInt(3)];
        }

        player1Connection.close();
        player2Connection.close();

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