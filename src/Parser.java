import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Parser {

    private final String odem;
    private final String mdg;
    private final List<Dependencies> dependencyList;

    public Parser(String odemFile, String mdgFile) {
        this.odem = odemFile;
        this.mdg = mdgFile;

        dependencyList = new ArrayList<Dependencies>();
    }

    public void createMDGFile() {
        File mdgFile = new File(mdg);

        try {
            if (mdgFile.exists()) {
                mdgFile.delete();
            }

            mdgFile.createNewFile();

            BufferedWriter mdgWriter = new BufferedWriter(new FileWriter(mdgFile.getAbsoluteFile()));

            for (Dependencies dependency : dependencyList) {
                String className = dependency.getName();
                for (String depend : dependency.getDependencies()) {
                    mdgWriter.write(className + " " + depend + "\n");
                }
            }

            mdgWriter.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void parse() {
        File odemFile = new File(odem);

        if (!odemFile.exists()) {
            System.out.println("ODEM file doesn't exist!");
            return;
        }

        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(odemFile);

            NodeList typeList = doc.getElementsByTagName("type");
            for (int typeCounter = 0; typeCounter < typeList.getLength(); typeCounter++) {
                Node typeNode = typeList.item(typeCounter);

                if (Node.ELEMENT_NODE == typeNode.getNodeType()) {
                    Element typeElement = (Element) typeNode;

                    if (!typeElement.getAttribute("classification").equals("class")) {
                        continue;
                    }

                    Dependencies dependency = new Dependencies(typeElement.getAttribute("name"));

                    NodeList dependencies = typeElement.getElementsByTagName("dependencies");
                    for (int dependenciesCounter = 0; dependenciesCounter < dependencies.getLength(); dependenciesCounter++) {
                        Node dependencyNode = dependencies.item(dependenciesCounter);

                        if (Node.ELEMENT_NODE == dependencyNode.getNodeType()) {
                            Element dependencyElement = (Element) dependencyNode;
                            NodeList dependsOn = dependencyElement.getElementsByTagName("depends-on");

                            for (int dependsOnCounter = 0; dependsOnCounter < dependsOn.getLength(); dependsOnCounter++) {
                                Node dependsOnNode = dependsOn.item(dependsOnCounter);

                                if (Node.ELEMENT_NODE == dependsOnNode.getNodeType()) {
                                    Element dependsOnElement = (Element) dependsOnNode;

                                    if (!dependsOnElement.getAttribute("classification").equals("uses")) {
                                        continue;
                                    }

                                    dependency.addDependency(dependsOnElement.getAttribute("name"));
                                }
                            }
                        }
                    }

                    dependencyList.add(dependency);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
