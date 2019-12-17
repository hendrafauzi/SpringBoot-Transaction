package com.hendra.transaction.demo.exception;

public class NoContentException extends RuntimeException
{
    public NoContentException()
    {
        //Do Nothing
    }

    public NoContentException(String message)
    {
        super(message);
    }
}
