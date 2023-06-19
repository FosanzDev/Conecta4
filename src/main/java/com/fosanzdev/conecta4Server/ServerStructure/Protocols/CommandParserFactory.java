package com.fosanzdev.conecta4Server.ServerStructure.Protocols;

import java.net.Socket;

public class CommandParserFactory {
        public CommandParser createCommandParser(Socket socket){
            return new CommandParser(socket);
        }
}
