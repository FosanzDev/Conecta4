package com.fosanzdev.conecta4Server;

import com.fosanzdev.conecta4Server.ServerStructure.Protocols.CommandParser;
import com.fosanzdev.conecta4Server.ServerStructure.Protocols.CommandParserFactory;
import com.fosanzdev.conecta4Server.ServerStructure.Protocols.GameProtocol;
import com.fosanzdev.conecta4Server.ServerStructure.Protocols.NetworkProtocol;
import com.fosanzdev.conecta4Server.ServerStructure.Server;

public class Main {
    public static void main(String[] args) {
        CommandParserFactory cpf = new CommandParserFactory();
        Server server = new Server(cpf);
        server.start();
    }
}
