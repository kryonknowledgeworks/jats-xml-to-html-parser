package com.kryonknowledgeworks.parser.util;

import com.sun.tools.javac.Main;
import org.w3c.dom.Node;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClassNameSingleTon {

    private static ClassNameSingleTon instance;
    public List<String> tagNames;
    public List<String> tagNamesMap;

    private ClassNameSingleTon() {
        tagNames = getTagNamesFromFolder(Constants.ELEMENTS_PATH);
        tagNamesMap = getTagNamesFromFolder(Constants.FRONT_ELEMENTS_PATH);
    }

    public static synchronized ClassNameSingleTon getInstance() {
        if (instance == null) {
            instance = new ClassNameSingleTon();
        }
        return instance;
    }

    private List<String> getTagNamesFromFolder(String folderPath) {
        List<String> classNames = new ArrayList<>();
        File folder = new File(folderPath);

        if (folder.exists() && folder.isDirectory()) {
            List<File> files = listFiles(folder);
            for (File file : files) {
                if (file.getName().endsWith(".java")) {
                    classNames.add(classNameToTag(file.getName().replace(".java", "")));
                }
            }
        } else {
            List<String> files = listFilesFromJar("com/kjms/xmlparser/elements");
            for (String file : files) {
                classNames.add(classNameToTag(file));
            }
        }

        return classNames;
    }

    private List<File> listFiles(File folder) {
        try {
            return Files.walk(folder.toPath())
                    .filter(Files::isRegularFile)
                    .map(java.nio.file.Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static List<String> listFilesFromJarPath(String packageName) {
        String path = ClassNameSingleTon.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        List<String> classNames = new ArrayList<>();
        File jarFile;
        try {
            jarFile = new File(path);
            if (jarFile.isFile() && jarFile.getName().endsWith(".jar")) {
                try (java.util.jar.JarFile jf = new java.util.jar.JarFile(jarFile)) {
                    Enumeration<java.util.jar.JarEntry> entries = jf.entries();
                    while (entries.hasMoreElements()) {
                        String entry = entries.nextElement().getName();
                        if (entry.endsWith(".class") && entry.startsWith(packageName)) {
                            String className = entry.substring(entry.lastIndexOf('/') + 1, entry.lastIndexOf('.'));
                            classNames.add(className);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classNames;
    }

    private static List<String> listFilesFromJar(String path) {
        List<String> files = new ArrayList<>();
        try {
            ClassLoader classLoader = Main.class.getClassLoader();
            Enumeration<URL> resources = classLoader.getResources(path);

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                String protocol = resource.getProtocol();
                if (protocol.equals("jar")) {
                    // If the protocol is jar, the path needs to be parsed differently
                    String jarPath = resource.getPath();
                    String filePath = jarPath.substring(jarPath.indexOf("!") + 1);
                    if (filePath.startsWith("/")) {
                        // Remove leading slash if present
                        filePath = filePath.substring(1);
                    }
                    files.addAll(listFilesFromJarPath(filePath));
                } else if (protocol.equals("file")) {
                    // For file protocol, it can be read directly
                    files.addAll(listResourceFiles(path, resource));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }


    private static List<String> listResourceFiles(String path, URL resource) throws IOException {
        List<String> files = new ArrayList<>();
        try (InputStream inputStream = resource.openStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.endsWith(".class")) {
                    files.add(path + "/" + line);
                }
            }
        }
        return files;
    }

    public static Object createInstanceFromClassName(String className, Object... constructorParams)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class<?> clazz = Class.forName("com.kjms.xmlparser.elements." + className);

        // Find the constructor with matching parameter types
        Class<?>[] parameterTypes = new Class[constructorParams.length];
        for (int i = 0; i < constructorParams.length; i++) {
            if (constructorParams[i].getClass().toString().equals("class com.sun.org.apache.xerces.internal.dom.DeferredElementNSImpl")) {
                parameterTypes[i] = Node.class;
            } else {
                parameterTypes[i] = constructorParams[i].getClass();
            }
        }

        Constructor<?> constructor = clazz.getConstructor(parameterTypes);

        return constructor.newInstance(constructorParams);
    }


    public static Object createInstanceFromClassNameForMap(String className, Object... constructorParams)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class<?> clazz = Class.forName("com.kjms.xmlparser.front.element." + className);

        // Find the constructor with matching parameter types
        Class<?>[] parameterTypes = new Class[constructorParams.length];
        for (int i = 0; i < constructorParams.length; i++) {
            if (constructorParams[i].getClass().toString().equals("class com.sun.org.apache.xerces.internal.dom.DeferredElementNSImpl")) {
                parameterTypes[i] = Node.class;
            } else {
                parameterTypes[i] = constructorParams[i].getClass();
            }
        }

        Constructor<?> constructor = clazz.getConstructor(parameterTypes);

        return constructor.newInstance(constructorParams);
    }


    public static Boolean isImplement(String className) throws ClassNotFoundException {
        Class<?> clazz = Class.forName("com.kjms.xmlparser.elements." + className);

        try {
            Field field = clazz.getDeclaredField("IMPLEMENT");
            return (Boolean) field.get(null);
        } catch (NoSuchFieldException e) {

            e.printStackTrace();
            return false;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Boolean isImplementForMap(String className) throws ClassNotFoundException {
        Class<?> clazz = Class.forName("com.kjms.xmlparser.front.element." + className);

        try {
            Field field = clazz.getDeclaredField("IMPLEMENT");
            return (Boolean) field.get(null);
        } catch (NoSuchFieldException e) {

            e.printStackTrace();
            return false;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getVariable(Object instance, String variableName) {
        try {
            Field field = instance.getClass().getDeclaredField(variableName);
            field.setAccessible(true);
            return field.get(instance);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String invokeMethod(Object instance, String methodName, Object... args) {
        try {
            Class<?> clazz = instance.getClass();
            Method method = findMethod(clazz, methodName, args);
            if (method != null) {
                Object result = method.invoke(instance, args);

                return result != null ? result.toString() : "null";
            } else {
                return "Method " + methodName + " not found in class " + clazz.getName();
            }
        } catch (Exception e) {
            // Handle or wrap the exception accordingly
            e.printStackTrace();
            return "Error invoking method: " + e.getMessage();
        }
    }

    public static Map<String,Object> invokeMethodForMap(Object instance, String methodName, Object... args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

            Class<?> clazz = instance.getClass();
            Method method =clazz.getMethod(methodName);
            Map<String,Object> result = (Map<String, Object>) method.invoke(instance, args);
            return result ;

    }

    private static Method findMethod(Class<?> clazz, String methodName, Object... args) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName) && matchParameterTypes(method, args)) {
                return method;
            }
        }
        return null;
    }

    private static boolean matchParameterTypes(Method method, Object... args) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != args.length) {
            return false;
        }
        for (int i = 0; i < parameterTypes.length; i++) {
            if (!parameterTypes[i].isInstance(args[i]) && !(parameterTypes[i].isPrimitive() && isWrapperTypeOfPrimitive(args[i], parameterTypes[i]))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isWrapperTypeOfPrimitive(Object obj, Class<?> primitiveType) {
        if (obj == null) return false;
        if (primitiveType == boolean.class) return obj instanceof Boolean;
        if (primitiveType == byte.class) return obj instanceof Byte;
        if (primitiveType == char.class) return obj instanceof Character;
        if (primitiveType == double.class) return obj instanceof Double;
        if (primitiveType == float.class) return obj instanceof Float;
        if (primitiveType == int.class) return obj instanceof Integer;
        if (primitiveType == long.class) return obj instanceof Long;
        if (primitiveType == short.class) return obj instanceof Short;
        return false;
    }

    public static String classNameToTag(String className) {

        StringBuilder result = new StringBuilder();
        result.append(Character.toLowerCase(className.charAt(0))); // Convert the first character to lowercase

        for (int i = 1; i < className.length(); i++) {
            char c = className.charAt(i);
            if (Character.isUpperCase(c)) {
                result.append('-'); // Add hyphen before the uppercase letter
                result.append(Character.toLowerCase(c)); // Convert the uppercase letter to lowercase
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    public static String tagToClassName(String tag) {
        if (tag == null || tag.isEmpty()) {
            return tag;
        }
        String[] parts;
        StringBuilder result = new StringBuilder();
        parts = tag.split("-");

        if (tag.contains(":")){
            parts = tag.split(":");
        }

        result.append(Character.toUpperCase(parts[0].charAt(0)));
        result.append(parts[0].substring(1));

        for (int i = 1; i < parts.length; i++) {
            result.append(Character.toUpperCase(parts[i].charAt(0)));
            result.append(parts[i].substring(1));
        }

        return result.toString();
    }
}
