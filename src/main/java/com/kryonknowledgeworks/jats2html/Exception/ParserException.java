package com.kryonknowledgeworks.jats2html.Exception;

public class ParserException extends Exception {

    static Boolean errorDissolve;

    public ParserException(Boolean check) {
        errorDissolve =check;
    }

    public ParserException(String message) {
        super(message);
    }

    public static void ParserExceptionHandler(Exception e) throws ParserException {
       if (errorDissolve){
           e.printStackTrace();
       } else {
           e.printStackTrace();
           throw new ParserException("Error occurred while parsing XML to HTML");
       }
    }

    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }
}