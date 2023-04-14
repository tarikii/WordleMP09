package wordle.mp09.communication;

import wordle.mp09.model.WordleLogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorWordleTCP {
    static final int PORT = 5555;
    private boolean end = false;
    private WordleLogic wordleLogic;
    private String currentWordPlay;
    private List<Character> guessedLetters;

    public ServidorWordleTCP() {
        this.wordleLogic = new WordleLogic();
        this.currentWordPlay = "";
        this.guessedLetters = new ArrayList<>();
    }

    public void listen() {
        ServerSocket serverSocket=null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            while(!end){
                clientSocket = serverSocket.accept();
                System.out.println("Connection with " + clientSocket.getInetAddress() + " dealt correctly!");

                currentWordPlay = wordleLogic.getRandomWord();
                guessedLetters.clear();

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintStream out = new PrintStream(clientSocket.getOutputStream());

                String clientWord;
                boolean gameOver = false;
                int attemptsGuessing = 0;
                while (!gameOver && (clientWord = in.readLine()) != null) {
                    attemptsGuessing++;
                    System.out.println("Client send the word: " + clientWord);

                    if(attemptsGuessing == 10){
                        out.println("\033[31mYou have reached the maximum number of attempts. You lost!\033[0m");
                        gameOver = true;
                    }

                    else if(clientWord.length() != 5){
                        out.println("\033[36mError: word must have exactly 5 letters!\033[0m");
                    }
                    else if (clientWord.equals(currentWordPlay)) {
                        out.println("Congratulations! You guessed it! :D");
                        gameOver = true;
                    } else {
                        out.println(wordleLogic.checkGuess(clientWord, currentWordPlay));
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServidorWordleTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        ServidorWordleTCP tcpSocketServer = new ServidorWordleTCP();
        tcpSocketServer.listen();
    }
}