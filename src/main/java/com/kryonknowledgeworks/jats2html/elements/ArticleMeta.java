package com.kryonknowledgeworks.jats2html.elements;

import com.kryonknowledgeworks.jats2html.Exception.HandleException;
import com.kryonknowledgeworks.jats2html.Tag;
import com.kryonknowledgeworks.jats2html.util.ClassNameSingleTon;
import com.kryonknowledgeworks.jats2html.util.Util;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//https://jats.nlm.nih.gov/publishing/tag-library/1.3/element/article-meta.html
public class ArticleMeta implements Tag {

    public static Boolean IMPLEMENT = true;

    public static String ELEMENT_ARTICLEMETA_FULL = "<article-meta>";
    public static String ELEMENT_ARTICLEMETA = "article-meta";

    Node node = null;
    NodeList childNodes = null;

    List<Node> nodeList;

    String html = "";

    public ArticleMeta(Node node) {
        try {

            this.node = node;

            this.nodeList = Util.elementFilter(node.getChildNodes());

            List<String> tagNames = ClassNameSingleTon.getInstance().tagNames;

            String doi = "";

            String volume = "";

            String issue = "";

            String fPage = "";

            String lPage = "";

            String allDate = "";

            String abstractData = "";

            String remaining = "";

            for (Node node1 : nodeList) {

                if (tagNames.contains(node1.getNodeName())) {

                    String className = ClassNameSingleTon.tagToClassName(node1.getNodeName());
                    if (Boolean.TRUE.equals(ClassNameSingleTon.isImplement(className))) {
                        if (node1.getNodeName().equals("article-id")){
                            Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1);
                            doi = ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                        }else if(node1.getNodeName().equals("title-group")) {
                            Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1);
                            this.html += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                        }
                        else if(node1.getNodeName().equals("volume")) {
                            Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1);
                            volume = ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                        }
                        else if(node1.getNodeName().equals("issue")) {
                            Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1);
                            issue = ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                        }
                        else if(node1.getNodeName().equals("pub-date")) {
                            Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1);
                            allDate += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                        }
                        else if(node1.getNodeName().equals("fpage")) {
                            Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1);
                            fPage = ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                        }
                        else if(node1.getNodeName().equals("lpage")) {
                            Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1);
                            lPage = ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                        }
                        else if(node1.getNodeName().equals("abstract")) {
                            Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1);
                            abstractData = ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                        } else {
                            Object instanceFromClassName = ClassNameSingleTon.createInstanceFromClassName(className, node1);
                            remaining += ClassNameSingleTon.invokeMethod(instanceFromClassName, "element");
                        }
                    }
                } else if (!node1.getNodeName().equals("#text")){


                    this.html += "<pre style='color:red'>'''" + Util.convertToString(node1).replace("<","&lt;").replace(">","&gt;") + "'''</pre>";
                }

            }

            String coverDate = extractCoverDate(allDate);

            String volumeDetails = "<p><span style='color:#ee4823'>Volume " + volume + ", Issue " + issue + "</span>, " + coverDate + ", Pages " + fPage + "-" + lPage  + "  </p>";

            this.html += volumeDetails + abstractData + "<div class='card'><div class='card-body'>" + remaining + doi + "</div></div>";

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
        return this.node.hasChildNodes();
    }

    private static String extractCoverDate(String html) {
        String startTag = "<p> cover-date:";
        int startIndex = html.indexOf(startTag);

        if (startIndex == -1) {
            return null;  // cover-date not found
        }

        // Move the start index to the end of the "cover-date" label
        startIndex += startTag.length();

        // Find the end of the <p> tag
        int endIndex = html.indexOf("</p>", startIndex);
        if (endIndex == -1) {
            return null;  // closing </p> not found
        }

        // Extract the content within <span> tags and format it
        String content = html.substring(startIndex, endIndex);

        // Remove all the <span> tags and other HTML tags
        content = content.replaceAll("<span>", "").replaceAll("</span>", "").trim();

        String[] dateParts = content.split("-");
        if (dateParts.length != 3) {
            return null;  // Invalid date format
        }

        // Extract the month and year
        String month = dateParts[1].trim();
        String year = dateParts[2].trim();

        // Convert the month number to the month name
        String monthName = getMonthName(month);

        // Return the formatted cover-date (e.g., "March 2022")
        return monthName + " " + year;
    }

    private static String getMonthName(String monthNumber) {
        // Map of month numbers to month names
        Map<String, String> monthMap = new HashMap<>();
        monthMap.put("01", "January");
        monthMap.put("02", "February");
        monthMap.put("03", "March");
        monthMap.put("04", "April");
        monthMap.put("05", "May");
        monthMap.put("06", "June");
        monthMap.put("07", "July");
        monthMap.put("08", "August");
        monthMap.put("09", "September");
        monthMap.put("10", "October");
        monthMap.put("11", "November");
        monthMap.put("12", "December");

        // Return the corresponding month name
        return monthMap.getOrDefault(monthNumber, "Unknown Month");
    }
}
