package com.kjms.xmlparser.front.element;

import com.kjms.xmlparser.Exception.HandleException;
import com.kjms.xmlparser.mapbuilder.MapBuilder;
import com.kjms.xmlparser.util.ClassNameSingleTon;
import com.kjms.xmlparser.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ArticleMeta {
    public static Boolean IMPLEMENT = true;

    String parentKeyName = this.getClass().getSimpleName();

    Node node = null;

    NodeList childNodes = null;

    List<Node> nodeList;

    Map<String,Object> map =  new HashMap<>();

    public ArticleMeta(Node node){
        try{
            this.node = node;


            nodeList = Util.getChildNode(node);
            List<String> nodeListName = Util.getChildNodeName(node).stream().distinct().collect(Collectors.toList());

            List<String> tagNames = ClassNameSingleTon.getInstance().tagNamesMap;
            String textContent = "";

            boolean toLoadStaticMap = false;

            for (String nodeI : nodeListName) {
                toLoadStaticMap = true;
                List<Object> childMap = new ArrayList<>();
                for (Node node1 :nodeList){
                    if (node1.getNodeName().equals(nodeI) ){
                        if (tagNames.contains(node1.getNodeName()) && node1.getNodeType() == Node.ELEMENT_NODE) {
                            String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());

                            if (Boolean.TRUE.equals(ClassNameSingleTon.isImplementForMap(className)) && !node1.getNodeName().equals("#text")) {
                                Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassNameForMap(className, node1);

                                childMap.add(ClassNameSingleTon.invokeMethodForMap(instanceFromClassName, "getMapXML"));
                            }
                        }else{
                            if (node1.getNodeType() == Node.TEXT_NODE && !node1.getTextContent().isBlank()){
                                textContent = node1.getTextContent();
                            }else{
                                toLoadStaticMap = false;
                                break;
                            }
                        }
                    }
                }


                  if (toLoadStaticMap){
                      Map toGetMap = (Map) childMap.get(0);

                      if (toGetMap.get(ClassNameSingleTon.tagToClassName(nodeI)) != null){
                          MapBuilder.XMLmap.put(ClassNameSingleTon.tagToClassName(nodeI),toGetMap.get(ClassNameSingleTon.tagToClassName(nodeI)));
                      }else{
                          MapBuilder.XMLmap.put(ClassNameSingleTon.tagToClassName(nodeI),childMap);
                      }
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
