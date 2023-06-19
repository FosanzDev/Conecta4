package com.fosanzdev.conecta4Server.ServerStructure.Protocols;

import java.io.IOException;
import java.net.Socket;

public interface IProtocol {

    boolean isCommand(String commandName);
    Response in(String command);
}
