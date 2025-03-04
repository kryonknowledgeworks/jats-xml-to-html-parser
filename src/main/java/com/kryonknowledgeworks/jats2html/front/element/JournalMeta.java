package com.kryonknowledgeworks.jats2html.front.element;

import com.kryonknowledgeworks.jats2html.Exception.HandleException;
import com.kryonknowledgeworks.jats2html.mapbuilder.MapBuilder;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JournalMeta {
    public static Boolean IMPLEMENT = true;

    String parentKeyName = this.getClass().getSimpleName();

    Node node = null;

    NodeList childNodes = null;

    List<Node> nodeList;

    Map<String,Object> map =  new HashMap<>();

    public JournalMeta(Node node){
        try{
            this.node = node;


            nodeList = Util.getChildNode(node);
            List<String> nodeListName = Util.getChildNodeName(node).stream().distinct().collect(Collectors.toList());

            List<String> tagNames = ClassNameSingleTon.getInstance().tagNamesMap;
            String textContent = "";


            for (String nodeI : nodeListName) {
                List<Object> childMap = new ArrayList<>();

                for (Node node1 :nodeList){

                   if (node1.getNodeName().equals(nodeI)){

                       if (tagNames.contains(node1.getNodeName()) && !node1.getNodeName().equals("#text")) {

                           String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());

                           if (Boolean.TRUE.equals(ClassNameSingleTon.isImplementForMap(className)) && !node1.getNodeName().equals("#text")) {
                               Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassNameForMap(className, node1);
                               childMap.add(ClassNameSingleTon.invokeMethodForMap(instanceFromClassName, "getMapXML"));
                           }

                       }else{

                           if (node1.getNodeName().equals("#text")  && !node1.getTextContent().isBlank()){
                               textContent = node1.getTextContent();
                           }

                       }
                   }
                }
                  Map toGetMap = (Map) childMap.get(0);
                if (toGetMap.get(ClassNameSingleTon.tagToClassName(nodeI)) != null){
                    map.put(ClassNameSingleTon.tagToClassName(nodeI),toGetMap.get(ClassNameSingleTon.tagToClassName(nodeI)));
                }else{
                    map.put(ClassNameSingleTon.tagToClassName(nodeI),childMap);
                }
            }


        }catch (Exception e){
            HandleException.processException(e);
        }
    }

    public Map<String,Object> getMapXML(){
        return this.map;
    }
}
