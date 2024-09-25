package com.kryonknowledgeworks.jats2html.util;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CustomEntityResolver implements EntityResolver {

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws FileNotFoundException {
        String fileName = extractFileName(systemId);

        if (systemId.contains("JATS-journalpublishing1.dtd")) {
            InputStream dtdStream = getClass().getClassLoader().getResourceAsStream("jats/JATS-journalpublishing1.dtd");
            if (dtdStream == null) {
                throw new FileNotFoundException("DTD file not found in resources.");
            }
            return new InputSource(dtdStream);
        } else if (!fileName.isEmpty()) {
            InputStream resourceStream = getClass().getClassLoader().getResourceAsStream("jats/" + fileName);
            if (resourceStream != null) {
                return new InputSource(resourceStream);
            }
        }

        return null;
    }

    private String extractFileName(String systemId) {
        if (systemId != null && !systemId.isEmpty()) {
            String normalizedSystemId = systemId.replace("\\", "/");

            if (normalizedSystemId.startsWith("file://")) {
                normalizedSystemId = normalizedSystemId.substring(7);
            }

            String[] pathParts = normalizedSystemId.split("/");

            StringBuilder currentPath = new StringBuilder();
            StringBuilder nonExistingPath = new StringBuilder();

            for (int i = 0; i < pathParts.length - 1; i++) {
                currentPath.append("/").append(pathParts[i]);

                String currentPathString = currentPath.toString();

                File currentFile = new File(currentPathString);

                if (!currentFile.exists()) {
                    for (int j = i; j < pathParts.length; j++) {
                        nonExistingPath.append(pathParts[j]);
                        if (j < pathParts.length - 1) {
                            nonExistingPath.append("/");
                        }
                    }
                    return nonExistingPath.toString();
                }
            }

            return pathParts[pathParts.length - 1];
        }
        return "";
    }


}
