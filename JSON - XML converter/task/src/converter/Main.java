package converter;

import java.util.Scanner;
import java.util.regex.Pattern;


public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        if (checkIfIsXML(input)) {
            System.out.println(parseXML(input));
        } else {
            System.out.println(parseJSON(input));
        }
    }

    private static boolean checkIfIsXML(String input) {
        return input.trim().startsWith("<");
    }

    private static String parseXML(String xmlString) {
        // <tagName>content</tagName>
        String tagName = findXMLTagName(xmlString);
        String content = findXMLContent(xmlString);

        if (content == null) {
            return String.format("{\"%s\": null}", tagName);
        }

        return String.format("{\"%s\": \"%s\"}", tagName, content);
    }

    private static String findXMLTagName(String xmlString) {
        // for example, xmlString: <word>hello</word>
        int openTagStart = xmlString.indexOf("<");
        int openTagEnd = xmlString.indexOf(">");

        return xmlString.substring(openTagStart + 1, openTagEnd)
                .replace("/", ""); // replace redundant / character in case like <word/>
    }

    private static String findXMLContent(String xmlString) {
        if (xmlString.contains("/>")) {
            // <mytag/>
            return null;
        }
        // for example, xmlString: <word>hello</word>
        int openTagEnd = xmlString.indexOf(">");
        int closedTagStart = xmlString.indexOf("</");

        return xmlString.substring(openTagEnd + 1, closedTagStart);
    }


    private static String parseJSON(String jsonString) {
        // {"key" : "value"}
        String key = findJSONKey(jsonString);
        String value = findJSONValue(jsonString);

        if (value.equals("null")) {
            // for example, {"word": null}
            return String.format("<%s/>", key);
        }

        return String.format("<%s>%s</%s>", key, value, key);
    }

    private static String findJSONKey(String jsonString) {
        // for example jsonString: {"key" : "value"}
        String stringBeforeColon = jsonString.split(":")[0].trim(); // {"key"

        return stringBeforeColon.replaceAll("\"", "")
                .replaceAll("\\{", ""); // key

    }

    private static String findJSONValue(String jsonString) {
        // for example jsonString: {"key" : "value"}
        String stringAfterColon = jsonString.split(":")[1].trim(); // "value"}

        return stringAfterColon.replaceAll("\"", "")
                .replaceAll("}", ""); // value

    }


}
