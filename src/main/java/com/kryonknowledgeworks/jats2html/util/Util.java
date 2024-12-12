package com.kryonknowledgeworks.jats2html.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static Node getCurrentNode(List<Node> nodeList, String element) {
        if(nodeList.size()>0) {
            for (Node node : nodeList) {

                if (node.getNodeName().equals(element)) {

                    return node;
                }

            }
        }
        return (nodeList.size()>0)?nodeList.get(0):null;
    }

    public static Node getCurrentNodeIsExists(List<Node> nodeList, String element) {

        for (Node node : nodeList) {

            if (node.getNodeName().equals(element)) {

                return node;
            }

        }
        return null;
    }

    public static List<Node> getCurrentNodes(List<Node> nodeList, String element) {

        List<Node> nodes = new ArrayList<>();

        for (Node node : nodeList) {
            if (node.getNodeName().equals(element)) {
                nodes.add(node);
            }

        }

        return nodes;
    }


    public static String htmlTagBinder(String tag, String data) {

        return "<" + tag + ">" + data + "</" + tag + ">";

    }

    public static String htmlTagBinderWithId(String tag,String id,String classAttribute ,String tagId, String data) {

        return "<" + tag + " "+id +(tagId!=null&&!tagId.isEmpty()?"=":"")+tagId +" "+ classAttribute+ ">" + data + "</" + tag + ">";

    }

    public static String htmlTagInject(String tag, List<String> injectDatas, String sourceData) {

        StringBuilder data = new StringBuilder(sourceData.replaceAll(tag, ""));

        for (String injectData : injectDatas){

            data.append(injectData);

        }

        return data + tag;

    }

    public static String htmlHrefBinder(String data, String tagInside, String insideData) {

        String insideHtml = "";

        if (tagInside != null) {
            insideHtml = "<" + tagInside + ">" + insideData + "</" + tagInside + ">";
        }

        return "<a target=\"blank\" href="+data+"> " + insideHtml + "</a>";

    }

    public static List<Node> elementFilter(NodeList childNodes) {

        List<Node> nodeList = new ArrayList<>();

        for (int i = 0; i < childNodes.getLength(); i++) {

            Node firstChild = childNodes.item(i);

            if (firstChild != null && !firstChild.getNodeName().equals("#text")) {
                nodeList.add(firstChild);
            }
        }
        return nodeList;
    }

    public static String getHtmlEscapeData(String data)
    {
          return StringEscapeUtils.escapeHtml4(data);
    }
    public static String htmlImageBinder(String url,String path,String id)
    {
        String per="100%";
        return "<div class='image-area' id='"+id+"'><a target=\"blank\" href="+((url.isEmpty())?"javascript:void(0);":url)+"> <img src="+path+" alt=\"image\"style=max-width:"+per+";height:auto;></a><br></div>";
    }

    public static String htmlImageBinder(String url,String path)
    {
        String per="100%";
        return "<a target=\"blank\" href="+ ((url.isEmpty())?"javascript:void(0);":url) +"> <img src="+path+" alt=\"image\"style=max-width:"+per+";height:auto;></a><br>";
    }
    public static String getNestedNodeValue(Node node)
    {
        if(node.getNodeName().equals("p"))
        {
            return node.getTextContent();
        }
        if(node.getNodeName().equals("title")) {
            return node.getTextContent();
        }
        if(node.getNodeName().equals("label"))
            return node.getTextContent();

        return node.getTextContent();
    }
    public static String labelheadingBinder(String label,String heading)
    {
        return "<H3>"+label+" "+heading+"</H3>";
    }
    public static String generateTableHtmlElement(String th,String td,int iterator)
    {
        String tableHtml="";
        if(!th.equals(""))
        {
            String id="T"+String.valueOf(iterator);
            tableHtml="<div id="+id+"><table><thead><tr>"+th+"</tr></thead><tbody>"+td+"</tbody></table></div>";
        }
        return tableHtml;
    }
    public static List<Node> iterateThroughNestedTags(Node nodeData,List<Node> nestedNode) {
        NodeList children = nodeData.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Node child = (Node) node;
                nestedNode.add(child);
                iterateThroughNestedTags(child,nestedNode);
            }
        }
        return nestedNode;
    }
    public static List<Node> getChildNode(Node node)
    {
        List<Node> listnode = new ArrayList<>();
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            listnode.add(childNode);

        }
        return listnode;
    }

    public static List<String> getChildNodeName(Node node)
    {
        List<String> listnode = new ArrayList<>();
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            if (childNodes.item(i).getNodeType() == Node.ELEMENT_NODE){
                String childNode = childNodes.item(i).getNodeName();
                listnode.add(childNode);
            }
        }
        return listnode;
    }

    public static String htmlRefBinder(String data, String insideData, String nodeName) {
        return "<a class=\"blank\" href=" + "#" + data + ">" + insideData + "</a>";
    }
public static String divGenerator(String id,String data)
{
    if(id==null)
    {
        return"<div>"+data+"</div>";
    }
    return"<div id="+id+">"+data+"</div>";
}
    public static String convertToString(Node node) {
        try {
            StringWriter sw = new StringWriter();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(new DOMSource(node), new StreamResult(sw));
            return sw.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toConvertNodeIntoDateString(Node node){
        char[] dateFormat = {'d','m','y'};
        List<Node> tags = Util.getChildNode(node);
        String dateString = "";
        for (char date : dateFormat){
            for (Node n : tags){
                if (date == n.getNodeName().toLowerCase().charAt(0)){
                    dateString +=  " " + n.getTextContent();
                }
            }
        }
        return dateString;
    }

    public static Object toConvertNodeIntoArrayString(Node node){
        ArrayList<String> keywords = new ArrayList<String>();
        List<Node> tags = Util.getChildNode(node);

            for (Node n : tags){
                if (n.getNodeName().equals("kwd")){
                    keywords.add(n.getTextContent());
                }
            }

        return keywords;
    }

    public static String[] tokenize(String text, String start, String end) {
        Pattern p = Pattern.compile("(?sm)^"+Pattern.quote(start)+".*?"+Pattern.quote(end)+"$");

        Matcher m = p.matcher(text);
        List<String> tokens = new ArrayList<String>();
        while(m.find()) {
            tokens.add(m.group());
        }
        return tokens.toArray(new String[]{});
    }

    public static String convertStringArrayToString(String[] strArr, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (String str : strArr)
            sb.append(str).append(delimiter);
        return sb.substring(0, sb.length() - 1);
    }
    public static void copyDirectories(String src, String dest)
    {
        File source = new File(src);
        File destination = new File(dest);
        try {
            FileUtils.copyDirectory(source, destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPublisherId(Node node) {
        Node parentNode = node;

        while (parentNode.getParentNode() != null) {
            parentNode = parentNode.getParentNode();
            if (parentNode.getNodeName().equals("article")){
                break;
            }
        }

        List<Node> childNodes = getChildNode(parentNode);

        Node front = getCurrentNode(childNodes, "front");

        List<Node> frontChildNodes = getChildNode(front);

        Node journalMeta = getCurrentNode(frontChildNodes, "journal-meta");

        List<Node> journalMetaChildNodes = getChildNode(journalMeta);

        for (Node node1: journalMetaChildNodes){

            if (node1.getNodeName().equals("journal-id")){
                String type = node1.getAttributes().getNamedItem("journal-id-type").getNodeValue();

                if (type.equals("publisher-id")){

                    return node1.getFirstChild().getNodeValue();
                }
            }

        }

        return null;
    }
}
