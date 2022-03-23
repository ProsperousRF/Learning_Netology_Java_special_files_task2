import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Stanislav Rakitov in 2022
 */
public class Main {
  public static void main(String[] args) {
    List<Employee> list = parseXML("data.xml");
    String json = listToJson(list);
    String jsonFilename = "data2.json";
    writeString(json, jsonFilename);
  }

  private static List<Employee> parseXML(String fileName) {
    List<Employee> employeeList = new ArrayList<>();

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    //    DocumentBuilder builder = null;
    Document doc = null;
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      doc = builder.parse(new File(fileName));
    } catch (ParserConfigurationException | SAXException | IOException e) {
      e.printStackTrace();
    }

//    Method invocation 'getDocumentElement' may produce 'NullPointerException'
    Node root = doc.getDocumentElement();
    NodeList nodeList = root.getChildNodes();

    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node = nodeList.item(i);

      if ("employee".equals(node.getNodeName())) {
        NodeList employee = node.getChildNodes();
        int employeeLength = employee.getLength();
        ArrayList<String> fields = new ArrayList<>();

        for (int j = 0; j < employeeLength; j++) {
          Node employeeNode = employee.item(j);

          if (Node.ELEMENT_NODE == employeeNode.getNodeType()) {
            //            System.out.println(employeeNode.getTextContent());
            fields.add(employeeNode.getTextContent());
          }
        }

        Employee emp =
            new Employee(
                Long.parseLong(fields.get(0)),
                fields.get(1),
                fields.get(2),
                fields.get(3),
                Integer.parseInt(fields.get(4)));
        employeeList.add(emp);
      }
    }

    return employeeList;
  }

  private static void writeString(String json, String jsonFilename) {
    try (FileWriter file = new FileWriter(jsonFilename)) {
      file.write(json);
      file.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static String listToJson(List<Employee> list) {
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.setPrettyPrinting().create();
    Type listType = new TypeToken<List<Employee>>() {}.getType();
    return gson.toJson(list, listType);
  }

}
