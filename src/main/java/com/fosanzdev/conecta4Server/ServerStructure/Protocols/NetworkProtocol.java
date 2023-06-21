package com.fosanzdev.conecta4Server.ServerStructure.Protocols;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class NetworkProtocol implements IProtocol{

    private Socket clientSocket;
    private PrintStream out;
    private BufferedReader in;

    public NetworkProtocol(Socket socket){
        try{
            this.clientSocket = socket;
            out = new PrintStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ioe){
            System.out.println("Error al crear las interfaces de E/S");
        }
    }

    private static ArrayList<String> commands = new ArrayList<>(Arrays.asList(
            "PING",
            "PONG",
            "HELLO",
            "WELCOME",
            "BYE",
            "ERROR"
    ));

    @Override
    public boolean isCommand(String commandName){
        return commands.contains(commandName);
    }

    @Override
    public Response in(String command){
        System.out.println("Socket: " + clientSocket.getInetAddress().getHostAddress());
        System.out.println("Command: " + command);
        String[] commandParts = command.split(" ");
        String commandName = commandParts[0];
        switch (commandName){
            case "PING":
                out.println("PING");
                String response = null;
                try {
                    response = in.readLine();
                    if (response.equals("PONG")){
                        return new Response(ResultCode.COMMAND_OK, "Respuesta correcta al PING");
                    }
                    else
                        return new Response(ResultCode.COMMAND_ERROR, "El cliente no ha respondido correctamente al PING");
                } catch (IOException ioe) {
                    return new Response(ResultCode.COMMAND_ERROR, "Error al leer del canal de entrada");
                }
            case "PONG":
                break;
            case "EXIT":
                break;
            case "WELCOME":
                break;
            case "BYE":
                break;
            case "ERROR":
                break;
            default:
                return new Response(ResultCode.COMMAND_NOT_FOUND, "Command not found");
        }
        return null;
    }
}
