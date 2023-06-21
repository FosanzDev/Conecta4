/* Network Protocol Class
 * Author: Esteban Sánchez Llobregat (@FosanzDev)
 * 
 * This class is used to parse the commands related to the connection between the client and the server.
 * It implements the IProtocol interface.
 */

package com.fosanzdev.conecta4Server.ServerStructure.Protocols;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class NetworkProtocol implements IProtocol{

    // Create the variables to store the socket and the input and output streams.
    private Socket clientSocket;
    private PrintStream out;
    private BufferedReader in;

    // Create a list with the commands that this protocol can handle.
    private static ArrayList<String> commands = new ArrayList<>(Arrays.asList(
            "PING",
            "PONG",
            "HELLO",
            "WELCOME",
            "BYE",
            "ERROR"
    ));


    // Constructor
    public NetworkProtocol(Socket socket){
        //Create the input and output streams
        try{
            this.clientSocket = socket;
            out = new PrintStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ioe){
            System.out.println("Error al crear las interfaces de E/S");
        }
    }

    /**
     * Method implemented from the IProtocol interface.
     * It checks if the command is a valid command for this protocol.
     */
    @Override
    public boolean isCommand(String commandName){
        return commands.contains(commandName);
    }

    /**
     * Method implemented from the IProtocol interface.
     * It parses the command and returns a response.
     */
    @Override
    public Response in(String commandName, String command){
        String[] commandParts = command.split(" ");
        switch (commandName){
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
