package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Exception.HandleException;
import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.mapbuilder.MetaDataBuilder;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class P implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_P_FULL = "<p>";
    public static String ELEMENT_P = "p";

    public static String HTML_TAG = "p";

    Node node = null;
    NodeList childNodes = null;
    List<Node> nodeList = new ArrayList<>();
    String html = "";

    public P(Node node, MetaDataBuilder metaDataBuilder) {
        try {
            this.node = node;

            elementFilter();

            if (!node.getParentNode().getNodeName().equals("fig") && !node.getParentNode().getNodeName().equals("caption") && !node.getParentNode().getNodeName().equals("list-item")){
                this.html += "<p>";
            }

            List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

            Node paragraph = node.getFirstChild();

            if (paragraph.getNodeValue() != null){
                this.html += paragraph.getNodeValue();
            } else {

                if (tagNames.contains(paragraph.getNodeName())) {

                    if (this.html.endsWith("</div>") || this.html.endsWith("</div>")){
                        this.html += "<p>";
                    }

                    String className = ClassNameSingleTon.tagToClassName(paragraph.getNodeName());
                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
                        Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, paragraph, metaDataBuilder);
                        this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                    }
                } else if (!paragraph.getNodeName().equals("#text")){

                    this.html += "<pre style='color:red'>'''" + Util.convertToString(paragraph).replace("<","&lt;").replace(">","&gt;") + "'''</pre>";
                }

            }
            Node sibling = paragraph.getNextSibling();

            while (sibling != null){

                if (sibling.getNodeName().equals("#text")){
                    this.html += sibling.getNodeValue().replace("<","&lt;");
                }

                if (tagNames.contains(sibling.getNodeName())) {

                    if (this.html.endsWith("</div>") || this.html.endsWith("</p>")){
                        this.html += "<p>";
                    }

                    String className = ClassNameSingleTon.tagToClassName(sibling.getNodeName());
                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
                        Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, sibling, metaDataBuilder);
                        this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                    }
                } else if (!sibling.getNodeName().equals("#text")){

                    this.html += "<pre style='color:red'>'''" + Util.convertToString(sibling).replace("<","&lt;").replace(">","&gt;") + "'''</pre>";
                }

                sibling = sibling.getNextSibling();

            }

            if (!node.getParentNode().getNodeName().equals("fig") && !node.getParentNode().getNodeName().equals("caption") && !node.getParentNode().getNodeName().equals("list-item")){
                this.html += "</p>";
            }


//            if (node.getParentNode().getNodeName().equals("list-item")){
//                this.html += htmlTagBinder("li", node.getTextContent());
//            } else {
//
//                String refType = "", rid = "";
//                String paragraph = Util.getHtmlEscapeData(Util.getNestedNodeValue(node));
//
//                paragraph=convertToStringData(node).replaceAll("<xref","<a").replaceAll("/xref>","/a>").replaceAll("rid=\"","href=\"#").replaceAll("ref-type","class").replaceAll("mml:", "").replaceAll("<disp-formula", "<div")
//                        .replaceAll("</disp-formula>","</div>").replaceAll("italic","i").replaceAll("<inline-graphic", "<img").replaceAll("/inline-graphic>", "/img>").replaceAll("xlink:href", "src");
//
//                if (node.getParentNode().getNodeName().equals("disp-quote")){
//                    int closingTagIndex = paragraph.indexOf("</p>");
//
//                    // If the closing tag is found
//                    if (closingTagIndex != -1) {
//                        // Insert double quotes before '</p>'
//                        StringBuilder modifiedString = new StringBuilder(paragraph);
//                        modifiedString.insert(closingTagIndex, '"');
//
//                        // Insert double quotes after '<p>'
//                        paragraph = modifiedString.insert(3, '"').toString();
//                    }
//                }
//
//                String mathFormula = "";
//                elementFilter();
//                this.html += htmlTagBinder(node.getNodeName(), paragraph) + mathFormula;
//
//            }



        } catch (Exception e) {
            HandleException.processException(e);
        }
    }

    @Override
    public String element() {
        return html;
    }

    @Override
    public List<String> elements() {
        return null;
    }

    @Override
    public Boolean isChildAvailable() {
        return null;
    }

    public List<Node> elementFilter() {

        this.childNodes = this.node.getChildNodes();

        for (int i = 0; i < this.childNodes.getLength(); i++) {

            Node firstChild = this.childNodes.item(i);

            if (firstChild != null && !firstChild.getNodeName().equals("#text")) {
                nodeList.add(firstChild);
            }
        }
        return nodeList;
    }

    public static String frameLinkHref(String paragraph, List<String> hrefData) {
        int count = 0;
        List<String> keyWord = new ArrayList<>();
        String[] arr = paragraph.split(" ");
        if(hrefData.size()>0) {
            for (int i = 0; i < arr.length; i++) {
                if (arr[i].contains("[")&& arr[i].contains("]")) {
                    arr[i] = arr[i].replaceAll(",$", "");
                    if (arr[i].contains(",")) {
                        if (arr[i].split(",").length == 1) {
                            String data = hrefData.get(count);
                            count++;
                            arr[i] = "[" + data + "]";
                        } else if (arr[i].split(",").length > 1) {
                            String htmlVal = "[";
                            String internalData = "";
                            for (int iter = 0; iter < arr[i].split(",").length; iter++) {
                                String data = hrefData.get(count);
                                internalData += data + ",";
                                count++;
                            }
                            internalData = new StringBuffer(internalData).deleteCharAt(internalData.length() - 1).toString();
                            htmlVal += internalData + "]";
                            arr[i] = htmlVal;
                        }
                    } else if (arr[i].contains("&ndash;")) {

                        String data = hrefData.get(count);
                        count++;
                        String data1 = hrefData.get(count);
                        count++;
                        arr[i] = "[" + data + "-" + data1 + "]";
                    } else {
                        arr[i] = "[" + hrefData.get(count) + "]";
                        count++;

                    }
                }
                else if (arr[i].contains("[")|| arr[i].contains("]")) {

                    arr[i] = "[" + hrefData.get(count) + "]";
                    count++;
                }

            }
        }
        paragraph = convertStringArrayToString(arr, " ");
        return paragraph;
    }

    private static String convertStringArrayToString(String[] strArr, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (String str : strArr)
            sb.append(str).append(delimiter);
        return sb.substring(0, sb.length() - 1);
    }

    public static String frameTableHref(String paragraph, List<String> tableList) {
        int count = 0;
        String[] arr = paragraph.split(" ");

        for (int i = 0; i < arr.length; i++) {
            if ( arr[i].contains("Table") && isNumeric(arr[i + 1].replaceAll("\\)",""))) {
                arr[i] = tableList.get(count);
                arr[i + 1] = "";
                count++;
            }
        }
        return convertStringArrayToString(arr, " ");
    }

    public static String frameFigureHref(String paragraph, List<String> figureList) {
        int iterator = 0;
        String[] arr = paragraph.split(" ");
        if(figureList.size()>0) {
            for (int i = 0; i < arr.length; i++) {
                if (arr[i].replaceAll("[^a-zA-Z0-9]", "").equalsIgnoreCase("figure")) {
                    if (arr[i + 1].replaceAll("[^a-zA-Z0-9]", "").trim().split("").length <= 2) {
                        String str = figureList.get(iterator).replaceAll("\\<.*?\\>", "");
                        arr[i] = figureList.get(iterator);
                        arr[i + 1] = "";
                        iterator++;

                    }
                }
            }
        }
        return convertStringArrayToString(arr, " ");

    }

    public static String frameEquationHref(String paragraph, List<String> equationList) {
        int iterator = 0;
        String[] arr = paragraph.split(" ");
        for (int i = 0; i < arr.length; i++) {

            if (arr[i].equalsIgnoreCase("equation")) {
                if (isNumeric(arr[i + 1].replaceAll("[^a-zA-Z0-9]", ""))) {
                    arr[i] = equationList.get(iterator);
                    arr[i + 1] = "";
                    iterator++;
                }
            }
        }

        return convertStringArrayToString(arr, " ");
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            if(strNum.length()>1) {
                double d = Double.parseDouble(strNum);
            }
            else
            {
                int i=Integer.parseInt(strNum);
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static String convertToString(Node node) {
        try {
            StringWriter sw = new StringWriter();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(new DOMSource(node), new StreamResult(sw));

            return sw.toString();

        } catch (Exception e) {
            return node.getTextContent();
        }
    }
    public String convertToStringData(Node node)
    {
        DOMImplementationLS domImplementation = (DOMImplementationLS) node.getOwnerDocument().getImplementation().getFeature("LS", "3.0");
        LSSerializer serializer = domImplementation.createLSSerializer();
        serializer.getDomConfig().setParameter("xml-declaration", false);
        String nodeString = serializer.writeToString(node);
        return nodeString;
    }
}
