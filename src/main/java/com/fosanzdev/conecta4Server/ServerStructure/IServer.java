/* Server interface
 * Author: Esteban Sánchez Llobregat (@FosanzDev)
 * 
 * Interface used to implement the server correctly.
 * Every server must implement this interface, so it can be used by 
 * other classes without knowing the implementation of the server.
 */

package com.fosanzdev.conecta4Server.ServerStructure;

public interface IServer {

    void removeClient(ClientThread clientThread);
}
