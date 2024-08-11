package com.kryonknowledgeworks.parser.html;

import com.kryonknowledgeworks.parser.elements.Article;
import com.sun.tools.javac.Main;
import org.w3c.dom.Document;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class HTMLBuilder {

    Document document;

    public HTMLBuilder(Document document) {
        this.document = document;
    }

    public String buildHTML(String outputPath) throws IOException {
        String doctype = "<!DOCTYPE html>";
        Article article = new Article(document.getDocumentElement());
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("style.css");

        Path tempFile = Files.createTempFile("style", ".css");
        Files.copy(inputStream, tempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        // Read the content of the file into a string
        byte[] bytes = Files.readAllBytes(tempFile);
        String style = new String(bytes, StandardCharsets.UTF_8);

        InputStream scriptStream = Main.class.getClassLoader().getResourceAsStream("script.js");

        Path scriptTempFile = Files.createTempFile("script", ".js");
        Files.copy(scriptStream, scriptTempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        // Read the content of the file into a string
        byte[] scriptBytes = Files.readAllBytes(scriptTempFile);
        String script = new String(scriptBytes, StandardCharsets.UTF_8);

        String html = doctype + "<html><head>" +
                "<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH\" crossorigin=\"anonymous\">" +
                "<style>" + style + "</style></head>" + article.element() + "<script>"+  script +"</script>" + "</html>";
//        File theDir = new File(outputPath);
//        FileWriter myWriter = new FileWriter(outputPath);
//        myWriter.write(html.replaceAll("italic", "i").replaceAll("<italic", "<i").replaceAll("/italic>", "/i>")
//                .replaceAll("/bold>", "/b>").replaceAll("<bold", "<b").replaceAll("/underline>", "/u>").replaceAll("<underline", "<u")
//                .replaceAll("<small_cap", "<span class=\"smallcaps\"").replaceAll("/small_cap>", "/span>"));
//        myWriter.close();

        return html;
    }
}
