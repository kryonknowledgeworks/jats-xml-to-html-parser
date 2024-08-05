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


public class PageCount {
    public static Boolean IMPLEMENT = true;

    String parentKeyName = this.getClass().getSimpleName();

    Node node = null;

    NodeList childNodes = null;

    List<Node> nodeList;

    Map<String,Object> map = new HashMap<>();



    public PageCount(Node node){
        try{

            Element e = (Element) node;
            String count = e.getAttribute("count");



            map.put(parentKeyName,count);




        }catch (Exception e){
            HandleException.processException(e);
        }
    }

    public Map<String,Object> getMapXML(){
        return this.map;
    }
}
