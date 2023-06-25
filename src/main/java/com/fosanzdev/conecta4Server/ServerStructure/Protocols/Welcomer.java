package com.fosanzdev.conecta4Server.ServerStructure.Protocols;

import java.util.Arrays;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

import com.fosanzdev.conecta4Server.ServerStructure.IServer;
import com.fosanzdev.conecta4Server.ServerStructure.Response;
import com.fosanzdev.conecta4Server.ServerStructure.ResultCode;

public class Welcomer {

    private String gameName;
    private String gameVersion;
    private ArrayList<String> supportedClientVersions;

    public Welcomer(IServer server) {
        gameName = server.getGameName();
        gameVersion = server.getGameVersion();
        supportedClientVersions = new ArrayList<String>(Arrays.asList(server.getSupportedClientversions()));
    }

    public Response welcome(Socket clientSocket){
        try {
            //Create the input and output streams
            PrintStream out = new PrintStream(clientSocket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            //Send the wellcome message

            //CHECKING CLIENT VERSION
            out.println("WELCOME" + " " + gameName + " " + gameVersion);
            String clientVersion = in.readLine();
            String[] clientVersionParts = clientVersion.split(" ");
            if (
                clientVersionParts[0].equals("HI") &&
                clientVersionParts[1].equals(gameName) &&
                supportedClientVersions.contains(clientVersionParts[2])
            ){
                out.println("HI");
                System.out.println("User connected");
                return new Response(ResultCode.COMMAND_OK, "User connected");
            }
            else{
                out.println("ERROR");
                System.out.println("User is using an unsupported client version");
                return new Response(ResultCode.COMMAND_ERROR, "User disconnected");
            }

        } catch (IOException ioe){
            return new Response(ResultCode.COMMAND_ERROR, "Error al crear las interfaces de E/S");
        }
        //Send the wellcome message
    }
}
