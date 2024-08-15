package com.kryonknowledgeworks.jats2html;

import com.kryonknowledgeworks.jats2html.Exception.HandleException;
import com.kryonknowledgeworks.jats2html.Exception.JatsException;
import com.kryonknowledgeworks.jats2html.constants.ParserConstants;
import com.kryonknowledgeworks.jats2html.html.HTMLBuilder;
import com.kryonknowledgeworks.jats2html.mapbuilder.MapBuilder;
import com.kryonknowledgeworks.jats2html.util.CustomEntityResolver;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The `KryonXMLParser` class provides methods to parse XML files and extract
 * relevant data, particularly in scenarios where specific XML structures
 * or attributes need to be handled. This class may include various utility
 * methods to load, parse, and convert XML data into different formats, such as
 * `Document` objects or Java collections.
 *
 * <p>This class is designed to be used in applications where XML data processing
 * is required, such as in configuration management, data interchange, or
 * any scenario where XML is used to store structured information.</p>
 *
 * <p>The class is designed to be easily extendable and reusable, offering a range
 * of methods to handle various XML parsing tasks with optional debugging support.</p>
 */
public class JATS2HTMLParser {

    /**
     * Parses the input file located at the specified input file path and writes the
     * processed data to the specified output file path. The method can operate in
     * debug mode, providing additional logging if enabled.
     *
     * @param inputFilePath   the path to the input file that needs to be parsed
     * @param outputFilePath  the path to the output file where the processed data will be written
     * @param enableDebugMode a boolean flag indicating whether to enable debug mode
     *                        (true for enabling debug mode, false otherwise)
     * @return a string message indicating the result of the parsing process
     */
    public static String parse(String inputFilePath, String outputFilePath, Boolean enableDebugMode) {
        try {
            System.setProperty("file.encoding", "UTF-8");
            File inputFile = new File(inputFilePath);
            File outputFile = new File(outputFilePath);
            Util.copyDirectories(inputFile.getParentFile().toString(), outputFile.getParentFile().toString());
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(true);
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            documentBuilder.setEntityResolver(new CustomEntityResolver());
            documentBuilder.setErrorHandler(new JatsException());
            Document document = documentBuilder.parse(inputFile);
            document.getDocumentElement().setAttribute(ParserConstants.XMLNS_XLINK, ParserConstants.X_LINK_NAMESPACE_URI);
            document.getDocumentElement().normalize();
            return new HTMLBuilder(document).buildHTML(outputFilePath);
        } catch (Exception e) {
            new HandleException(enableDebugMode);
            HandleException.processException(e);
        }
        return StringUtils.EMPTY;
    }

    /**
     * Loads and parses an XML file from the specified input file path, creating a
     * map object that represents the structure and data within the XML. The method
     * can optionally operate in debug mode, providing additional logging information.
     *
     * @param inputFilePath the path to the XML file that needs to be loaded and parsed
     * @param enableDebugMode a boolean flag indicating whether to enable debug mode
     *                        (true for enabling debug mode, false otherwise)
     * @return a map containing key-value pairs representing the data and structure of the XML file
     */
    public static Map<String, Object> loadMapFromXml(String inputFilePath, Boolean enableDebugMode) {
        try {
            System.setProperty("file.encoding", "UTF-8");
            File inputFile = new File(inputFilePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(true);
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            documentBuilder.setEntityResolver(new CustomEntityResolver());
            documentBuilder.setErrorHandler(new JatsException());
            Document document = documentBuilder.parse(inputFile);
            document.getDocumentElement().setAttribute(ParserConstants.XMLNS_XLINK, ParserConstants.X_LINK_NAMESPACE_URI);
            document.getDocumentElement().normalize();
            return new MapBuilder(document).buildMap();
        } catch (Exception e) {
            new HandleException(enableDebugMode);
            HandleException.processException(e);
        }
        return new HashMap<>();
    }

    /**
     * Loads and parses an XML file from the specified input file path, extracting
     * attributes related to graphics from the XML. The method returns a list of
     * strings representing these graphic attributes. Optionally, debug mode can be
     * enabled to provide additional logging information.
     *
     * @param inputFilePath the path to the XML file that needs to be loaded and parsed
     * @param enableDebugMode a boolean flag indicating whether to enable debug mode
     *                        (true for enabling debug mode, false otherwise)
     * @return a list of strings containing the graphic-related attributes extracted from the XML file
     */
    public static List<String> loadGraphicsAttrFromXml(String inputFilePath, Boolean enableDebugMode) {
        try {
            System.setProperty("file.encoding", "UTF-8");
            File file = new File(inputFilePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(true);
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            documentBuilder.setEntityResolver(new CustomEntityResolver());
            documentBuilder.setErrorHandler(new JatsException());
            Document document = documentBuilder.parse(file);
            document.getDocumentElement().setAttribute(ParserConstants.XMLNS_XLINK, ParserConstants.X_LINK_NAMESPACE_URI);
            document.getDocumentElement().normalize();
            return getGraphicHrefAttributes(document);
        } catch (Exception e) {
            new HandleException(enableDebugMode);
            HandleException.processException(e);
        }
        return new ArrayList<>();
    }


    private static List<String> getGraphicHrefAttributes(Document document) throws XPathExpressionException {
        List<String> hRefs = new ArrayList<>();
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();

        XPathExpression expr = xpath.compile("//graphic | //inline-graphic");
        NodeList graphicNodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        for (int i = 0; i < graphicNodes.getLength(); i++) {
            Element graphicElement = (Element) graphicNodes.item(i);
            String href = graphicElement.getAttributeNS(ParserConstants.X_LINK_NAMESPACE_URI, "href");
            if (StringUtils.isNotEmpty(href)) {
                hRefs.add(href.replace("/", "\\"));
            }
        }
        return hRefs;
    }
}
