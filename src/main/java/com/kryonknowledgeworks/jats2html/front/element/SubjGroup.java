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


public class SubjGroup {
    public static Boolean IMPLEMENT = true;

    String parentKeyName = this.getClass().getSimpleName();

    Node node = null;

    NodeList childNodes = null;

    List<Node> nodeList;

    Map<String,Object> map = new HashMap<>();

    public SubjGroup(Node node){
        try{
            this.node = node;
            String subjGroupType = "";
            if (node.getAttributes().getNamedItem("subj-group-type") != null){
                subjGroupType = node.getAttributes().getNamedItem("subj-group-type").getNodeValue();
            }

            nodeList = Util.getChildNode(node);

            List<String> tagNames = ClassNameSingleTon.getInstance().tagNamesMap;
            List<Object> childMap = new ArrayList<>();
            String textContent = "";

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

            if (childMap.size() > 0 && textContent == ""){
                map.put((subjGroupType.isEmpty())?parentKeyName:subjGroupType,((HashMap<String,String>) childMap.get(0)).get("Subject"));
            }else if(textContent.length() > 0){
                map.put((subjGroupType.isEmpty())?parentKeyName:subjGroupType,textContent);
            }

        }catch (Exception e){
            HandleException.processException(e);
        }
    }

    public Map<String,Object> getMapXML(){
        return this.map;
    }
}
