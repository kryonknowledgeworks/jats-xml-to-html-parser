package com.kjms.xmlparser.util;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.InputStream;

public class CustomEntityResolver implements EntityResolver {

    @Override
    public InputSource resolveEntity(String publicId, String systemId) {
        // Extract the file name from the systemId
        String fileName = extractFileName(systemId);

        if (!fileName.isEmpty()) {
            InputStream resourceStream = getClass().getResourceAsStream("/jats/" + fileName);
            if (resourceStream != null) {
                return new InputSource(resourceStream);
            }
        }

        // Return null to use the default behavior for unresolved entities
        return null;
    }

    private String extractFileName(String systemId) {
        if (systemId != null && !systemId.isEmpty()) {

            String rootDirectory = System.getProperty("user.dir").replace("\\", "/");

            if (systemId.replace("\\", "/").contains(rootDirectory)){

                int indexOfName = systemId.replace("\\", "/").lastIndexOf(rootDirectory); // Remove the file:// prefix

                return (indexOfName != -1) ? systemId.substring(indexOfName + rootDirectory.length() + 1) : systemId;


            } else {
                int indexOfName = systemId.replace("\\", "/").lastIndexOf(  "jats/"); // Remove the file:// prefix

                return (indexOfName != -1) ? systemId.substring(indexOfName + 5) : systemId;
            }


        }
        return "";
    }
}
