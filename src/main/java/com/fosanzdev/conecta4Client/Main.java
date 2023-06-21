package com.fosanzdev.conecta4Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        Socket socket;
        try {
            socket = new Socket("localhost", 35427);
            PrintStream out = new PrintStream(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true){
                String userInput = in.readLine();
                System.out.println("echo: " + userInput);
                if (userInput.equals("PING"))
                    out.println("PONG");
            }

        } catch (IOException ioe) {
            System.out.println("Server is down");
        }




    }
}
