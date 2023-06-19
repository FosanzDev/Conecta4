package com.fosanzdev.conecta4Server.ServerStructure.Protocols;

import com.fosanzdev.conecta4Server.ServerStructure.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class GameProtocol implements IProtocol{

    Socket clientSocket;
    PrintStream out;
    BufferedReader in;

    public GameProtocol(Socket clientSocket){
        try{
            this.clientSocket = clientSocket;
            out = new PrintStream(clientSocket.getOutputStream());
            in = new BufferedReader(new java.io.InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException ioe){
            System.out.println("Error al crear los canales de E/S");
        }
    }

    @Override
    public boolean isCommand(String commandName) {
        return false;
    }

    public Response in(String command) {
            return null;
        }
}
