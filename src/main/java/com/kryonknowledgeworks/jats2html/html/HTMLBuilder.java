package com.kryonknowledgeworks.jats2html.html;

import com.kryonknowledgeworks.jats2html.JATS2HTMLParser;
import com.kryonknowledgeworks.jats2html.elements.Article;
import com.kryonknowledgeworks.jats2html.mapbuilder.MetaDataBuilder;
import org.w3c.dom.Document;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class HTMLBuilder {

    Document document;

    public HTMLBuilder(Document document) {
        this.document = document;
    }

    public String buildHTML(String outputPath, Map<String,Object> formats) throws IOException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {

        MetaDataBuilder metaDataBuilder = new MetaDataBuilder();
        metaDataBuilder.buildAll(formats);
        String doctype = "<!DOCTYPE html>";
        Article article = new Article(document.getDocumentElement(), metaDataBuilder);
        InputStream inputStream = JATS2HTMLParser.class.getClassLoader().getResourceAsStream("style.css");

        Path tempFile = Files.createTempFile("style", ".css");
        Files.copy(inputStream, tempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        byte[] bytes = Files.readAllBytes(tempFile);
        String style = new String(bytes, StandardCharsets.UTF_8);

        InputStream scriptStream = JATS2HTMLParser.class.getClassLoader().getResourceAsStream("script.js");

        Path scriptTempFile = Files.createTempFile("script", ".js");
        Files.copy(scriptStream, scriptTempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        byte[] scriptBytes = Files.readAllBytes(scriptTempFile);
        String script = new String(scriptBytes, StandardCharsets.UTF_8);

        String html = doctype + "<html><head>" +
                "<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH\" crossorigin=\"anonymous\">" +
                "<style>" + style + "</style></head>" + article.element() + "  <script src=\"https://cdn.jsdelivr.net/npm/mathjax@3/es5/mml-chtml.js\"></script><script>"+  script +"</script>" + "</html>";
        FileWriter myWriter = new FileWriter(outputPath);
        myWriter.write(html);
        myWriter.close();

        return html;
    }
}
