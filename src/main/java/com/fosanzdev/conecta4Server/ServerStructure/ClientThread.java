package com.fosanzdev.conecta4Server.ServerStructure;

import com.fosanzdev.Utils.TimeoutCaller;
import com.fosanzdev.conecta4Server.ServerStructure.Protocols.CommandParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ClientThread extends Thread{

    //Constants
    private final int PING_INTERVAL = 30000;
    private final int PING_TIMEOUT = 5000;

    //Object based variables
    private Socket socket;
    private IServer server;
    private CommandParser commandParser;
    private PrintStream out;
    private BufferedReader in;
    //private long lastResponseTime = System.currentTimeMillis();
    private TimeoutCaller tc;

    //Constructor
    public ClientThread(Socket socket, CommandParser cp, IServer server){
        //Call the constructor of the superclass
        super();
        //Initialize the variables
        this.socket = socket;
        this.commandParser = cp;
        this.server = server;

        //Setup the I/O channels
        try {
            out = new PrintStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ioe) {
            System.out.println("Error al crear los canales de E/S");
        }
        //Start the thread
        start();
    }

    // Run method. It will be executed when the thread starts
    @Override
    public void run(){
        // ------------------------------------------
        // ------------------ PING ------------------
        // ------------------------------------------

        //Runnable to be executed for the ping
        tc = new TimeoutCaller(PING_INTERVAL, new Runnable() {
            @Override
            public void run() {
                //Inside the interval timeout caller there's another timeout caller for the ping response
                //If PING_TIMEOUT time passes without a response, the client is disconnected
                //This TimeoutCaller executes only once, so it's automatically cancelled after the first execution
                //TimeoutCaller is cancelled once the response is received
                TimeoutCaller pingTimeoutCaller = new TimeoutCaller(PING_TIMEOUT, new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("PING TIMEOUT");
                        destruct();
                    }
                }, true);
                //Start the ping timeout caller
                pingTimeoutCaller.start();
                Response response = ping();
                //Cancel the ping timeout caller once the response is received
                //If the response is not received, the ping timeout caller will disconnect the client 
                pingTimeoutCaller.cancel();
                if (response.resultCode != ResultCode.COMMAND_OK) {
                    System.out.println("PING RESPONSE ERROR");
                    destruct();
                }
            }
        });
        //Start the ping timeout caller
        tc.start();
    }

    /**
     * Method to close all the connections and remove the client from the server
     * It's called when the client disconnects or when the ping timeout is reached
     */
    public void destruct(){
        try {
            tc.cancel(); //Cancel the ping timeout caller
            in.close(); //Close the input stream
            out.close(); //Close the output stream
            socket.close(); //Close the socket
            server.removeClient(this); //Remove the client from the server
        } catch (IOException ioe) {
            System.out.println("Error al cerrar el socket");
        }
    }

    /**
     * Sends a command to the client
     * @param command Command to be sent
     * @return Response from the client
     */
    public Response in(String command) {
        return commandParser.in(command);
    }

    /**
     * Sends a ping command to the client
     * @return Response from the client
     */
    public Response ping(){
        return in("PING");
    }
}
