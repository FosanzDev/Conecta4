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

            //Starting welcome protocol
            if (!welcome(out, in)){
                System.out.println("Error al iniciar el protocolo de bienvenida");
                return;
            }

            String command;
            while ((command = in.readLine()) != null){
                System.out.println("Received command: " + command);
                // CASE: PING
                if (command.equals("PING")){
                    System.out.println("Responding PONG");
                    out.println("PONG");
                    continue;
                }

                // CASE: BYE
                if (command.equals("BYE")){
                    System.out.println("Responding BYE");
                    out.println("BYE");
                    break;
                }
            }

        } catch (IOException ioe) {
            System.out.println("Server is down");
        }
    }

    private static boolean welcome(PrintStream out, BufferedReader in){
        try {

            String[] serverSide = in.readLine().split(" ");
            if (
                serverSide[0].equals("WELCOME") &&
                serverSide[1].equals("Conecta4") &&
                serverSide[2].equals("1.0")
            ) {
                out.println("HI Conecta4 1.0");
                String serverResponse = in.readLine();
                if (serverResponse.equals("HI")){
                    System.out.println("Welcome protocol finished successfully");
                    return true;
                }
            }
        } catch (IOException ioe){
            System.out.println("Error al crear las interfaces de E/S");
        }

        return false;
    }
}
