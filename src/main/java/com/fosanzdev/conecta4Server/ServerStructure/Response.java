/* Response class
 * Author: Esteban Sánchez Llobregat (@FosanzDev)
 * 
 * Class used to create a response object. This object is used to identify the
 * response from the client or the server, so communication can be done.
 */

package com.fosanzdev.conecta4Server.ServerStructure;

public class Response {

    //Attributes
    public ResultCode resultCode;
    public String response;

    //Constructor
    public Response(ResultCode resultCode, String response){
        this.resultCode = resultCode;
        this.response = response;
    }

    //ToString
    @Override
    public String toString(){
        return resultCode + " " + response;
    }
}
