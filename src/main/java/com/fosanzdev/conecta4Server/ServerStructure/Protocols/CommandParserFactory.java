/* Command Parser Factory Class
 * Author: Esteban Sánchez Llobregat (@FosanzDev)
 * 
 * Class used to create a Command Parser object. This class is used to create
 * the Command Parser object in the Server class, so it can be used to parse
 * the commands received from the client (given the socket)
 */

package com.fosanzdev.conecta4Server.ServerStructure.Protocols;

import java.net.Socket;

import com.fosanzdev.conecta4Server.ServerStructure.IServer;

public class CommandParserFactory {

    //Constructor
    public CommandParser createCommandParser(IServer server, Socket socket) {
        return new CommandParser(server, socket);
    }
}
