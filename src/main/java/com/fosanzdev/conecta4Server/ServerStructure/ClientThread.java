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
    private PingPong pingPong = new PingPong();
    //private long lastResponseTime = System.currentTimeMillis();

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
        pingPong.start();

        // ------------------------------------------
        // --------------- WELCOME ------------------
        // ------------------------------------------

        //Send the welcome message to the command parser
        //Doing this we can validate the client version
        in("WELCOME");

        // ------------------------------------------
        // --------------- COMMANDS -----------------
        // ------------------------------------------


        String command;
        try{
            while ((command = in.readLine()) != null) {
                System.out.println("COMMAND: " + command);
                if (command.equals("PONG")) {
                    pingPong.receivedPong();
                    continue;
                }

                //If the command is not null, parse it and send the response to the client
                Response response = commandParser.in(command);
                out.println(response.toString());
                //lastResponseTime = System.currentTimeMillis();
        }
        } catch (IOException ioe) {
                System.out.println("USER DISCONNECTED");
                destruct();
        }
    }

    /**
     * Method to close all the connections and remove the client from the server
     * It's called when the client disconnects or when the ping timeout is reached
     */
    public void destruct(){
        try {
            pingPong.cancel(); //Cancel the ping timeout caller
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
     */
    public void ping(){
        out.println("PING");
    }

    
    /**
     * Class to manage the ping pong protocol
     * It sends a ping command to the client every PING_INTERVAL milliseconds
     * If the client doesn't respond in PING_TIMEOUT milliseconds, the client is disconnected
     * If the client responds, the ping timeout caller is cancelled
     * 
     * The ping timeout/interval caller is a TimeoutCaller that executes a Runnable after a certain time
     * It can be cancelled
     */
    private class PingPong extends Thread{

        private TimeoutCaller pingIntervalCaller;
        private TimeoutCaller pingTimeoutCaller;


        public PingPong(){
            super();
        }

        @Override
        public void run(){
            pingIntervalCaller = new TimeoutCaller(PING_INTERVAL, new Runnable() {
            @Override
            public void run() {
                //Inside the interval timeout caller there's another timeout caller for the ping response
                //If PING_TIMEOUT time passes without a response, the client is disconnected
                //This TimeoutCaller executes only once, so it's automatically cancelled after the first execution
                //TimeoutCaller is cancelled once the response is received
                pingTimeoutCaller = new TimeoutCaller(PING_TIMEOUT, new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("PING TIMEOUT");
                        destruct();
                    }
                }, true);
                //Start the ping timeout caller
                pingTimeoutCaller.start();
                ping();
            }
        });
            //Start the ping interval caller
            pingIntervalCaller.start();
        }

        public void cancel() {
            pingIntervalCaller.cancel();
        }

        public void receivedPong() {
            pingTimeoutCaller.cancel();
        }
    }
}
