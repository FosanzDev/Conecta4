package com.fosanzdev.conecta4Server.ServerStructure;

import com.fosanzdev.conecta4Server.ServerStructure.Protocols.CommandParser;
import com.fosanzdev.conecta4Server.ServerStructure.Protocols.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ClientThread extends Thread{

    Socket socket;
    CommandParser commandParser;
    PrintStream out;
    BufferedReader in;

    public ClientThread(Socket socket, CommandParser cp){
        super();
        this.socket = socket;
        this.commandParser = cp;
        try {
            out = new PrintStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ioe) {
            System.out.println("Error al crear los canales de E/S");
        }
        start();
    }

    public void destruct(){
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException ioe) {
            System.out.println("Error al cerrar el socket");
        }
    }

    public Response in(String command) {
        return commandParser.in(command);
    }
}
