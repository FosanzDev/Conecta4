package com.fosanzdev.conecta4Server.ServerStructure.Protocols;


public interface IProtocol {

    boolean isCommand(String commandName);
    Response in(String command);
}
