/* Command Parser Class 
 * Author: Esteban Sánchez Llobregat (@FosanzDev)
 * 
 * Class used to parse the commands received from the client and redirect
 * them to the corresponding protocol. It's used to separate the protocols
 * from the client thread.
 */
package com.fosanzdev.conecta4Server.ServerStructure.Protocols;

import java.net.Socket;

import com.fosanzdev.conecta4Server.ServerStructure.IServer;
import com.fosanzdev.conecta4Server.ServerStructure.Response;
import com.fosanzdev.conecta4Server.ServerStructure.ResultCode;

public class CommandParser {

    //Protocols
    private IProtocol np;
    private IProtocol gp;

    //Socket related to the client
    Socket socket;

    //Constructor
    public CommandParser(IServer server, Socket socket){
        np = new NetworkProtocol(server, socket);
        gp = new GameProtocol(server, socket);
    }

    //Method to check if the command is a valid command and redirect it to the corresponding protocol
    public Response in(String command) {
        //Get the command name
        //It'll probably change to a more efficient way to do it
        String commandName = command.split(" ")[0];
        if (np.isCommand(commandName)) {
            //If it is a Network Protocol command, redirect it to the Network Protocol
            return np.in(commandName, command);
        } else if (gp.isCommand(commandName)) {
            //If it is a Game Protocol command, redirect it to the Game Protocol
            return gp.in(commandName, command);
        } else {
            //If it is not a valid command, return an error
            return new Response(ResultCode.COMMAND_NOT_FOUND, "Command not found");
        }
    }
}
