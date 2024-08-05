package com.kjms.xmlparser.front.element;

import com.kjms.xmlparser.Exception.HandleException;
import com.kjms.xmlparser.util.ClassNameSingleTon;
import com.kjms.xmlparser.util.Util;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class License {
    public static Boolean IMPLEMENT = true;

    String parentKeyName = this.getClass().getSimpleName();

    Node node = null;

    NodeList childNodes = null;

    List<Node> nodeList;

    Map<String,Object> map = new HashMap<>();

    public License(Node node){
        try{
            this.node = node;


            nodeList = Util.getChildNode(node);

            List<String> tagNames = ClassNameSingleTon.getInstance().tagNamesMap;
            List<Object> childMap = new ArrayList<>();
            Map<String,Object> toAddAccess = new HashMap<>();
            Map<String,Object> toAddHref = new HashMap<>();
            String textContent = "";

             Element e = (Element) node;
             String accessType = e.getAttribute("license-type");
             String xhref = e.getAttribute("xlink:href");


            for (Node node1 : nodeList) {
                if (tagNames.contains(node1.getNodeName()) && !node1.getNodeName().equals("#text")) {
                    String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplementForMap(className)) && !node1.getNodeName().equals("#text")) {
                        Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassNameForMap(className, node1);
                        childMap.add(ClassNameSingleTon.invokeMethodForMap(instanceFromClassName, "getMapXML"));
                    }
                }else{
                    if (node1.getNodeName().equals("#text")  && !node1.getTextContent().isBlank()){
                        textContent = node1.getTextContent();
//                        childMap.add(node1.getTextContent());
                    }
                }
            }

            toAddAccess.put("AccessType",accessType);
            toAddHref.put("AccessProof",xhref);
            childMap.add(toAddAccess);
            childMap.add(toAddHref);

            if (childMap.size() > 0 && textContent == ""){
                map.put(parentKeyName,childMap);
            }else if(textContent.length() > 0){
                map.put(parentKeyName,textContent);
            }

        }catch (Exception e){
            HandleException.processException(e);
        }
    }

    public Map<String,Object> getMapXML(){
        return this.map;
    }
}
