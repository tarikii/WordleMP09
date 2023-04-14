package wordle.mp09.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ClientWordleTCP {
    private Scanner sc = new Scanner(System.in);

    public void connect(String address, int port) {
        String serverData;
        String request;
        boolean continueConnected=true;
        Socket socket;
        BufferedReader in;
        PrintStream out;
        try {
            socket = new Socket(InetAddress.getByName(address), port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());
            //The client stays in the port and works until it's done
            while(continueConnected){
                // Tells the user to insert a word to guess
                System.out.print("\u001B[35mInsert a word: \u001B[00m");
                request = sc.nextLine();
                // Sends this word to the server, so he can compare it to the word generated from the file
                out.println(request);
                // Gets the response of the server and prints it back to the client
                serverData = in.readLine();
                System.out.println(serverData);
            }

            close(socket);
        } catch (UnknownHostException ex) {
            System.out.printf("Error de connexió. No existeix el host, %s", ex);
        } catch (IOException ex) {
            System.out.printf("Error de connexió indefinit, %s", ex);
        }

    }

    private void close(Socket socket){
        //si falla el tancament no podem fer gaire cosa, només enregistrar
        //el problema
        try {
            //tancament de tots els recursos
            if(socket!=null && !socket.isClosed()){
                if(!socket.isInputShutdown()){
                    socket.shutdownInput();
                }
                if(!socket.isOutputShutdown()){
                    socket.shutdownOutput();
                }
                socket.close();
            }
        } catch (IOException ex) {
            //enregistrem l'error amb un objecte Logger
            Logger.getLogger(ClientWordleTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        ClientWordleTCP client = new ClientWordleTCP();
        client.connect("localhost", 5555);
    }
}