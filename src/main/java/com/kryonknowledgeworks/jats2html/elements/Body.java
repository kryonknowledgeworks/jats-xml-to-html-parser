package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Exception.HandleException;
import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/body.html
public class Body implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_BODY_FULL = "<body>";
    public static String ELEMENT_BODY = "body";

    Node node = null;
    NodeList childNodes = null;

    List<Node> nodeList = new ArrayList<>();

    String html = "";

    String bodyHtml = "";

    static String thHtml = "";

    static String tdHtml = "";

    String tableContent="";

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

    public Body(Node node) {

        try {
            this.node = node;
            String idValue="";
            elementFilter();

            List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

            for (Node node1 : nodeList) {

                if (tagNames.contains(node1.getNodeName())) {

                    String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
                        Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1);
                        this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                    }
                } else if (!node1.getNodeName().equals("#text")){

                 
                    this.html += "<pre style='color:red'>'''" + Util.convertToString(node1).replace("<","&lt;").replace(">","&gt;") + "'''</pre>";
                }

            }

//            int count = 1;
//            int iterator = 1;
//            for (int index = 0; index < nodeList.size(); index++) {
//                Node node1 = nodeList.get(index);
//                List<Node> nestedNode = new ArrayList<>();
//                nestedNode = Util.iterateThroughNestedTags(node1, nestedNode);
//                String tableVal="";
//                String tableLabel="";
//                String tableId="";
//
//                for (int i = 0; i < nestedNode.size(); i++) {
//
//                    Node nodeListData = nestedNode.get(i);
//                    if(nodeListData.getNodeName().equals("table-wrap"))
//                    {
//                        NamedNodeMap map=nodeListData.getAttributes();
//                        Node idNode=  map.getNamedItem("id");
//                        String xlink="";
//                        if(idNode!=null) {
//                            tableId = nodeListData.getAttributes().getNamedItem("id").getNodeValue();
//                        }
//                    }
//                    if(nodeListData.getNodeName().equals("table"))
//                    {
//                        List<Node> tableWrap =Util.getChildNode(nodeListData);
//                        Node value=Util.getCurrentNode(tableWrap,"caption");
//                        Node label=Util.getCurrentNode(tableWrap,"label");
//                        tableVal=value.getTextContent();
//                        tableLabel=label.getTextContent();
//                        tableContent=Util.convertToString(nodeListData).replaceAll("<xref","<a").replaceAll("/xref>","/a>").replaceAll("rid=\"","href=\"#")
//                                .replaceAll("ref-type","class").replaceAll("<table-wrap-foot>","<tfoot>").replaceAll("</table-wrap-foot>","</tfoot>")
//                                .replaceAll("<table","<table id="+tableId);
//
//
//                        bodyHtml+=tableContent;
//                    }
//                    if ((nodeListData.getNodeName().equals("fig"))) {
//                        NamedNodeMap map=nodeListData.getAttributes();
//                        Node id=  map.getNamedItem("id");
//
//                        if(id!=null) {
//                            idValue = nodeListData.getAttributes().getNamedItem("id").getNodeValue();
//                        }
//                    }
//                    if ((nodeListData.getNodeName().equals("graphic"))) {
//                        Graphic graphic = new Graphic(nodeListData,idValue);
//                        bodyHtml += graphic.element();
//                        count++;
//                    }
//                    if (nodeListData.getNodeName().equals("td") || nodeListData.getNodeName().equals("th") || nodeListData.getNodeName().equals("tr"))
//                        htmlTableContent(nodeListData);
//                    if (nodeListData.getNodeName().equals("p") || nodeListData.getNodeName().equals("title") || nodeListData.getNodeName().equals("label")) {
//                        String tableHtml = "";
//
//                        if (!thHtml.trim().isEmpty()) {
//                            tableHtml = Util.generateTableHtmlElement(thHtml, tdHtml, iterator);
//                            iterator++;
//                        }
//
//
//                        tdHtml = "";
//                        thHtml = "";
//
//                        if (nodeListData.getNodeName().equals("p")&& nestedNode.get(i-1).getNodeName()!="list-item") {
//                            List<Node>paraList=Util.getChildNode(nodeListData);
//                            if (nodeListData.getTextContent().contains(tableVal) && (!tableVal.equals(""))) {
//                                P paragraph = new P(nodeListData);
//                                bodyHtml += paragraph.element().replace(tableVal, "");
//                            } else {
//                                P paragraph = new P(nodeListData);
//                                bodyHtml += paragraph.element();
//                            }
//                        }
//
//                        if (i != nestedNode.size() - 1 && nodeListData.getNodeName() == "label" && nestedNode.get(i + 1).getNodeName().equals("title")) {
//                            Title title = new Title(nodeListData, nestedNode.get(i + 1));
//                            bodyHtml += title.element();
//                            i++;
//                        } else if (nodeListData.getNodeName().equals("label")) {
//
//                            Label label = new Label(nodeListData);
//                            if (!label.element().replaceAll("[^a-zA-Z]", "").trim().equals("")) {
//                                //  System.out.println("label check " + label.element());
//                                bodyHtml += label.element();
//                            }
//                        } else if (nodeListData.getNodeName().equals("title")) {
//                            Title title = new Title(nodeListData);
//                            bodyHtml += Util.htmlTagBinder("h3",title.element());
//                        }
//                    }
//                }
//            }
//            this.html = bodyHtml;
        } catch (Exception e) {
            HandleException.processException(e);
        }
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

    public static void htmlTableContent(Node tableNode) {
        if (tableNode.getNodeName().equals("tr")) {
            thHtml += "</tr><tr>";
            tdHtml += "</tr><tr>";
        }
        if (tableNode.getNodeName().equals("th")) {
            if (tableNode.hasAttributes()) {
                String align = tableNode.getAttributes().getNamedItem("align").getNodeValue();
                String valign = tableNode.getAttributes().getNamedItem("valign").getNodeValue();
                thHtml += "<th align=" + align + " valign=" + valign + ">" + Util.getHtmlEscapeData(tableNode.getTextContent()) + "</th>";
            }
        } else if (tableNode.getNodeName().equals("td")) {
            String align = tableNode.getAttributes().getNamedItem("align").getNodeValue();
            String valign = tableNode.getAttributes().getNamedItem("valign").getNodeValue();
            tdHtml += "<td align=" + align + " valign=" + valign + ">" + tableNode.getTextContent() + "</td>";
        }
    }

}
