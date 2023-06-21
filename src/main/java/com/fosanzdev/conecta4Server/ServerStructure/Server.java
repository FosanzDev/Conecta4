/* Server class
 * Author: Esteban Sánchez Llobregat (@FosanzDev)
 * 
 * Class used to create a Server object.
 */


package com.fosanzdev.conecta4Server.ServerStructure;

import com.fosanzdev.conecta4Server.ServerStructure.Protocols.CommandParserFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread implements IServer{

    //Attributes
    private final CommandParserFactory cpf;
    private final ArrayList<ClientThread> players;

    //Constructor
    public Server(CommandParserFactory cpf) {
        players = new ArrayList<>();
        this.cpf = cpf;
    }

    //Starts the server
    @Override
    public void run() {
        System.out.println("Server is running...");
        try (ServerSocket server = new ServerSocket(35427, 10)) {
            while (true) {
                //Receives a new player, creates a new thread for him and adds it to the list of players
                Socket player = server.accept();
                ClientThread ct = new ClientThread(player, cpf.createCommandParser(player), this);
                players.add(ct);
                System.out.println("Player connected: " + player.getInetAddress().getHostAddress() + ":" + player.getPort());
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    //Remove a client from the list
    public void removeClient(ClientThread ct){
        players.remove(ct);
    }

    //Returns true if the server is up
    public boolean isUp(){
        //Comprobar si el servidor está activo
        return true;
    }
}
