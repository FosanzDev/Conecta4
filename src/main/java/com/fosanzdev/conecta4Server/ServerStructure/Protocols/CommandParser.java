package com.fosanzdev.conecta4Server.ServerStructure.Protocols;

import com.fosanzdev.conecta4Server.ServerStructure.ClientThread;
import com.fosanzdev.conecta4Server.ServerStructure.Server;

import java.io.IOException;
import java.net.Socket;

public class CommandParser {

    private IProtocol np;
    private IProtocol gp;

    Socket socket;

    public CommandParser(Socket socket){
        np = new NetworkProtocol(socket);
        gp = new GameProtocol(socket);
    }

    public Response in(String command) {
        String commandName = command.split(" ")[0];
        if (np.isCommand(commandName)) {
            return np.in(command);
        } else if (gp.isCommand(commandName)) {
            return gp.in(command);
        } else {
            return new Response(ResultCode.COMMAND_NOT_FOUND, "Command not found");
        }
    }
}
