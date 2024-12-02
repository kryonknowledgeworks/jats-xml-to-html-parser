package com.kryonknowledgeworks.jats2html.front.element;

import com.kryonknowledgeworks.jats2html.Exception.HandleException;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Aff {
    public static Boolean IMPLEMENT = true;

    String parentKeyName = this.getClass().getSimpleName();

    Node node = null;

    NodeList childNodes = null;

    List<Node> nodeList;

    Map<String,Object> map = new HashMap<>();

    public Aff(Node node){
        try{
            this.node = node;


            nodeList = Util.getChildNode(node);


            List<String> tagNames = ClassNameSingleTon.getInstance().tagNamesMap;
            List<Object> childMap = new ArrayList<>();
            String textContent = "";

            Element e = (Element) node;
            Map<String,String> aff = new HashMap<>();

            if(e.hasAttribute("id")){
                aff.put("id",e.getAttribute("id"));
            }

            for (Node node1 : nodeList) {
                if (tagNames.contains(node1.getNodeName())) {
                    String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplementForMap(className))) {
                        Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassNameForMap(className, node1);
                        childMap.add(ClassNameSingleTon.invokeMethodForMap(instanceFromClassName, "getMapXML"));
                    }
                }else{
                    if (node1.getNodeName().equals("#text")  && !node1.getTextContent().isBlank()){
//                        textContent = node1.getTextContent();
                        if(node1.getTextContent().trim().length()>1){
                            Map<String,String> address = new HashMap<>();
                            address.put("singleLineAddress",node1.getTextContent());
                            childMap.add(address);
                        }
                    }
                }
            }

            if(!aff.isEmpty()){
                childMap.add(aff);
            }

            map.put(parentKeyName,childMap);


//            if (childMap.size() > 0 && textContent == ""){
//                map.put(parentKeyName,childMap);
//            }else if(textContent.length() > 0){
//                map.put(parentKeyName,textContent);
//            }



        }catch (Exception e){
            HandleException.processException(e);
        }
    }

    public Map<String,Object> getMapXML(){
        return this.map;
    }
}
