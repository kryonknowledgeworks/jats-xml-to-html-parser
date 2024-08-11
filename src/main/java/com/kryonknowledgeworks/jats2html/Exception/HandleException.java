package com.kryonknowledgeworks.jats2html.Exception;

public class HandleException  {
static Boolean errorDissolve;

    public HandleException(Boolean check) {
        this.errorDissolve =check;
    }

    public static void processException (Exception e)
    {
        if(errorDissolve) {
            e.printStackTrace();
        }
        else {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
