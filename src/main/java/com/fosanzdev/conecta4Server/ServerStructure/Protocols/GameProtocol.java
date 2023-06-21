/* Game Protocol Class
 * Author: Esteban Sánchez Llobregat (@FosanzDev)
 * 
 * Class used to implement the Game Protocol. It's used for commands related to the game
 * (like placing a token, etc.).
 * 
 * It implements the IProtocol interface, so it has to implement the isCommand and in methods.
 * This methods are used to check if the command is a valid command and to parse the command.
 * 
 * This class will connect the API of the game with the Server commands.
 */

package com.fosanzdev.conecta4Server.ServerStructure.Protocols;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import com.fosanzdev.conecta4Server.ServerStructure.Response;

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

    public Response in(String commandName, String command) {
            return null;
        }
}
