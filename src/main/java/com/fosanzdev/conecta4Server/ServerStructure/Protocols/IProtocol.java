/* IProtocol Interface
 * Author: Esteban Sánchez Llobregat (@FosanzDev)
 * 
 * Interface used to implement the protocols correctly.
 */

package com.fosanzdev.conecta4Server.ServerStructure.Protocols;

import com.fosanzdev.conecta4Server.ServerStructure.Response;

public interface IProtocol {

    //Checks if the command is a valid command
    boolean isCommand(String commandName);

    //Parses the command and returns a response from the client
    Response in(String commandName, String command);
}
