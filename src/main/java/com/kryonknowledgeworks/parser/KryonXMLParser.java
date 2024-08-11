package com.kryonknowledgeworks.parser;

import com.kryonknowledgeworks.parser.Exception.HandleException;
import com.kryonknowledgeworks.parser.Exception.JatsException;
import com.kryonknowledgeworks.parser.mapbuilder.MapBuilder;
import com.kryonknowledgeworks.parser.html.HTMLBuilder;
import com.kryonknowledgeworks.parser.util.CustomEntityResolver;
import com.kryonknowledgeworks.parser.util.Util;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KryonXMLParser {

//    public static String filePath = "";


    public static void main(String[] args){

        parse("C:\\Users\\TK-SDK\\Documents\\jobnest\\xml\\ijssis-2023-0001.xml",
                "C:\\Users\\TK-SDK\\Documents\\jobnest\\out\\ijssis-2023-0001.html",true);
//        mapParser("C:\\Users\\TK-SDK\\Documents\\jobnest\\xml\\ijssis-2023-0001.xml",true);
    }

   public static String parse(String relativeFilePath,String outputFilePath,Boolean enableDebugMode){
       String html = "";
        try {
           System.setProperty("file.encoding","UTF-8");
           new HandleException(enableDebugMode);
           File file = new File(relativeFilePath);
           String fileImagePath = file.getParentFile().toString();
           file = new File(outputFilePath);
           Util.copyDirectories(fileImagePath, file.getParentFile().toString());
           File inputFile = new File(relativeFilePath);
           DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
           factory.setNamespaceAware(true);
           factory.setValidating(true);
           DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            documentBuilder.setEntityResolver(new CustomEntityResolver());
           documentBuilder.setErrorHandler(new JatsException());
           Document document = documentBuilder.parse(inputFile);
           String xlinkNamespaceURI = "http://www.w3.org/1999/xlink";
           document.getDocumentElement().setAttribute("xmlns:xlink", xlinkNamespaceURI);
           document.getDocumentElement().normalize();
            html = new HTMLBuilder(document).buildHTML(outputFilePath);
        } catch (Exception e) {
           HandleException.processException(e);
       }
       return html;
   }

   public static Map<String,Object> mapParser(String relativeFilePath,Boolean enableDebugMode){
       Map<String,Object> map = new HashMap<>();
        try {
            System.setProperty("file.encoding","UTF-8");
            new HandleException(enableDebugMode);
            File file = new File(relativeFilePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(true);
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            documentBuilder.setEntityResolver(new CustomEntityResolver());
            documentBuilder.setErrorHandler(new JatsException());
            Document document = documentBuilder.parse(file);
            String xlinkNamespaceURI = "http://www.w3.org/1999/xlink";
            document.getDocumentElement().setAttribute("xmlns:xlink", xlinkNamespaceURI);
            document.getDocumentElement().normalize();
            map = new MapBuilder(document).buildMap();
        }catch (Exception e){
            HandleException.processException(e);
        }
        return map;
   }

    public static List<String> getGraphics(String relativeFilePath,Boolean enableDebugMode){

        try {
            System.setProperty("file.encoding","UTF-8");
            new HandleException(enableDebugMode);
            File file = new File(relativeFilePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(true);
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            documentBuilder.setEntityResolver(new CustomEntityResolver());
            documentBuilder.setErrorHandler(new JatsException());
            Document document = documentBuilder.parse(file);
            String xlinkNamespaceURI = "http://www.w3.org/1999/xlink";
            document.getDocumentElement().setAttribute("xmlns:xlink", xlinkNamespaceURI);
            document.getDocumentElement().normalize();
            return getGraphicHrefAttributes(document);
        }catch (Exception e){
            HandleException.processException(e);
        }
        return new ArrayList<>();
    }

    public static Document encodeData(byte[] xml, String encoding) throws ParserConfigurationException, IOException, SAXException {
        InputSource inputSource = new InputSource(new ByteArrayInputStream(xml));
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(true);
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        inputSource.setEncoding("UTF-8");
        return documentBuilder.parse(inputSource);
    }

    private static List<String> getGraphicHrefAttributes(Document document) throws XPathExpressionException {
        List<String> hrefs = new ArrayList<>();
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();

        XPathExpression expr = xpath.compile("//graphic | //inline-graphic");
        NodeList graphicNodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        for (int i = 0; i < graphicNodes.getLength(); i++) {
            Element graphicElement = (Element) graphicNodes.item(i);
            String href = graphicElement.getAttributeNS("http://www.w3.org/1999/xlink", "href");
            if (href != null && !href.isEmpty()) {
                hrefs.add(href.replace("/", "\\"));
            }
        }
        return hrefs;
    }



}
