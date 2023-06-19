package com.fosanzdev.conecta4Server.ServerStructure;

import com.fosanzdev.conecta4Server.ServerStructure.Protocols.CommandParserFactory;
import com.fosanzdev.conecta4Server.ServerStructure.Protocols.Response;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread {

    private final CommandParserFactory cpf;
    private final ArrayList<ClientThread> players;

    public Server(CommandParserFactory cpf) {
        players = new ArrayList<>();
        this.cpf = cpf;
    }

    @Override
    public void run() {
        System.out.println("Server is running...");
        startPingPong();
        try (ServerSocket server = new ServerSocket(35427, 10)) {
            while (true) {
                Socket player = server.accept();
                ClientThread ct = new ClientThread(player, cpf.createCommandParser(player));
                players.add(ct);
                System.out.println("Player connected: " + player.getInetAddress().getHostAddress());
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public boolean isUp(){
        //Comprobar si el servidor está activo
        return true;
    }

    private void startPingPong() {
        Thread pingPong = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try{
                        Thread.sleep(5000);
                        ping();
                    } catch (InterruptedException ie){
                        ie.printStackTrace();
                    }
                }
            }
        });
        pingPong.start();
    }

    private void ping() {
        ArrayList<ClientThread> toRemove = new ArrayList<ClientThread>();

        for (ClientThread ct : players) {
            System.out.println("Pinging " + ct.socket.getInetAddress().getHostAddress());
            Response r = ct.in("PING");
            if (r.response.equals("PONG"))
                System.out.println("Pong from " + ct.socket.getInetAddress().getHostAddress());
            else {
                ct.destruct();
                toRemove.add(ct);
            }
        }

        for (ClientThread ct : toRemove) {
            players.remove(ct);
        }
    }
}
