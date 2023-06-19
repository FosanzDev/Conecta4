package com.fosanzdev.conecta4Server.ServerStructure.Protocols;

public class Response {

    public ResultCode resultCode;
    public String response;

    public Response(ResultCode resultCode, String response){
        this.resultCode = resultCode;
        this.response = response;
    }

    @Override
    public String toString(){
        return resultCode + " " + response;
    }
}
