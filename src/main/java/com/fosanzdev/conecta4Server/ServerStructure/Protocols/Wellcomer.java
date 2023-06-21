package com.fosanzdev.conecta4Server.ServerStructure.Protocols;

import java.util.Arrays;
import java.util.ArrayList;

import com.fosanzdev.conecta4Server.ServerStructure.ClientThread;
import com.fosanzdev.conecta4Server.ServerStructure.IServer;
import com.fosanzdev.conecta4Server.ServerStructure.Response;
import com.fosanzdev.conecta4Server.ServerStructure.ResultCode;

public class Wellcomer {

    private IServer server;
    private String gameName;
    private String gameVersion;
    private ArrayList<String> supportedClientVersions;

    public Wellcomer(IServer server) {
        this.server = server;
        gameName = server.getGameName();
        gameVersion = server.getGameVersion();
        supportedClientVersions = new ArrayList<String>(Arrays.asList(server.getSupportedClientversions()));
    }

    public boolean wellcome(ClientThread ct){
        //Send the wellcome message
        Response gameCheck = ct.in("WELLCOME " + gameName + " " + gameVersion);

        if (gameCheck.resultCode != ResultCode.COMMAND_OK){
            //Something went wrong
            System.out.println("Client does not support the game");
            ct.destruct();
        } else {
            boolean valid = true;
            String[] gameCheckParts = gameCheck.response.split(" ");
            //Check first attribute
            valid = valid && gameCheckParts[0].equals("WELLCOME");
            //Check second attribute
            valid = valid && gameCheckParts[1].equals(gameName);
            //Check third attribute
            valid = valid && supportedClientVersions.contains(gameCheckParts[2]);
            if (!valid){
                System.out.println("Client does not support the game or game version");
                ct.destruct();
            }
        }

        return true;
    }
}
