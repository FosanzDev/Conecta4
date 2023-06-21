package com.fosanzdev.conecta4Server.ServerStructure;

import com.fosanzdev.Utils.TimeoutCaller;
import com.fosanzdev.conecta4Server.ServerStructure.Protocols.CommandParser;
import com.fosanzdev.conecta4Server.ServerStructure.Protocols.Response;
import com.fosanzdev.conecta4Server.ServerStructure.Protocols.ResultCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ClientThread extends Thread{

    private Socket socket;
    private IServer server;
    private CommandParser commandParser;
    private PrintStream out;
    private BufferedReader in;
    //private long lastResponseTime = System.currentTimeMillis();
    private final int PING_INTERVAL = 10000;
    TimeoutCaller tc;

    public ClientThread(Socket socket, CommandParser cp, IServer server){
        super();
        this.socket = socket;
        this.commandParser = cp;
        this.server = server;
        try {
            out = new PrintStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ioe) {
            System.out.println("Error al crear los canales de E/S");
        }
        start();
    }

    @Override
    public void run(){
        tc = new TimeoutCaller(PING_INTERVAL, new Runnable() {
            @Override
            public void run() {
                System.out.println("PINGING");
                Response response = ping();
                System.out.println("Response: " + response.resultCode + " " + response.response);
                if (response.resultCode == ResultCode.COMMAND_OK) {
                    System.out.println("PING RESPONSE OK");
                } else {
                    System.out.println("PING RESPONSE ERROR");
                    destruct();
                }
            }
        });
        tc.start();
    }

    public void destruct(){
        try {
            tc.cancel();
            in.close();
            out.close();
            socket.close();
            server.removeClient(this);
        } catch (IOException ioe) {
            System.out.println("Error al cerrar el socket");
        }
    }

    public Response in(String command) {
        return commandParser.in(command);
    }

    public Response ping(){
        return in("PING");
    }
}
