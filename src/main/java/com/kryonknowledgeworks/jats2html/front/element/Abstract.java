package com.kryonknowledgeworks.jats2html.front.element;

import com.kryonknowledgeworks.jats2html.Exception.HandleException;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Abstract {
    public static Boolean IMPLEMENT = true;

    String parentKeyName = this.getClass().getSimpleName();

    Node node = null;

    NodeList childNodes = null;

    List<Node> nodeList;

    Map<String,Object> map = new HashMap<>();

    public Abstract(Node node){
        try{
            this.node = node;
            String textContent = "";
            nodeList = Util.getChildNode(node);
            List<String> tagNames = ClassNameSingleTon.getInstance().tagNamesMap;
            List<Object> childMap = new ArrayList<>();
            for (Node nodeParent : nodeList){
                List<Node> childNodes = Util.getChildNode(nodeParent);
                for (Node node1 : childNodes){

                    if (tagNames.contains(node1.getNodeName()) && node1.getNodeType() == Node.ELEMENT_NODE ) {

                        String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());

                        if (Boolean.TRUE.equals(ClassNameSingleTon.isImplementForMap(className)) && !node1.getNodeName().equals("#text") && className.equals("Italic")) {
                            Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassNameForMap(className, node1);
                            textContent += ClassNameSingleTon.invokeMethodForMap(instanceFromClassName, "getMapXML").get("Italic");
                        }
                        if (Boolean.TRUE.equals(ClassNameSingleTon.isImplementForMap(className)) && !node1.getNodeName().equals("#text") && className.equals("Bold")) {
                            Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassNameForMap(className, node1);
                            textContent += ClassNameSingleTon.invokeMethodForMap(instanceFromClassName, "getMapXML").get("Bold");
                        }

                    }else{

                        if (node1.getNodeType() == Node.TEXT_NODE  && !node1.getTextContent().isBlank() && !node1.getParentNode().getNodeName().equals("title")){

                            textContent += node1.getTextContent();

                        }
                    }
                }
            }
            map.put("Abstract",textContent);

//            if (childMap.size() > 0 && textContent == ""){
//                map.put(parentKeyName,childMap);
//            }else if(textContent.length() > 0){
////                map.put(parentKeyName,textContent);
//            }

        }catch (Exception e){
            HandleException.processException(e);
        }
    }

    public Map<String,Object> getMapXML(){
        return this.map;
    }
}
