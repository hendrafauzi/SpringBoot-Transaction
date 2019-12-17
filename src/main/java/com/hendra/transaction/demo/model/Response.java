package com.hendra.transaction.demo.model;

public class Response
{
    private String status;
    private String message;
    private long id;

    public Response()
    {
        //Do Nothing
    }

    public Response(String status, String message)
    {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
