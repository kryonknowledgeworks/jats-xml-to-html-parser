package com.kryonknowledgeworks.parser.Exception;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class JatsException implements ErrorHandler {
    @Override
    public void warning(SAXParseException exception) throws SAXException {

    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        System.out.println("Column : " + exception.getColumnNumber());
        System.out.println("Line : " + exception.getLineNumber());
        System.out.println(exception.getMessage());
        System.out.println("XML File Not In Jats Format");
       // System.exit(0);
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        System.out.println(exception.getMessage());
        System.out.println("XML File Not In Jats Format");
      //  System.exit(0);
    }
}
