package steam.model;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class SteamModel {
    private static LinkedList<Steam> steams = new LinkedList<>();
    private static int index = 0;


    /**
     * Método que llama al método readDirectory para iniciar el catálogo.
     *
     * @return int, 1 si ha ido bien, -1, -2, -3 según los fallos que ha encontrado el método readDiretory
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static int initSteams() throws ParserConfigurationException, SAXException, IOException {
        return readDirectory();
    }

    /**
     * Retorna el primer registro del catálogo
     *
     * @return objeto Steam
     */
    public static Steam getFirst() {
        index = 0;
        return steams.getFirst();
    }

    /**
     * Retorna el últmo registro del catálogo
     *
     * @return objeto Steam
     */
    public static Steam getLast() {
        index = steams.size() - 1;
        return steams.getLast();
    }

    /**
     * Retorna el siguiente registro basándose en el varlor de index.
     *
     * @return objeto Steam.
     */
    public static Steam getNext() {
        if (index == steams.size() - 1) index = 0;
        else index++;
        return steams.get(index);
    }

    /**
     * Retorna el registro anterior basándose en el varlor de index.
     *
     * @return objeto Steam.
     */
    public static Steam getPrev() {
        if (index == 0) index = steams.size() - 1;
        else index--;
        return steams.get(index);
    }

    /**
     * Retorna el registro actual basándose en el varlor de index.
     *
     * @return objeto Steam.
     */
    public static Steam getActual() {
        return steams.get(index);
    }

    /**
     * Retorna el id más alto de los objetos de la lista, en un principio cabría pensar que el id más alto es steams.size()+1
     * pero si se ha creado algún archivo .xml y luego se ha borrado alguno con un id intermedio es posible que al tomar el id
     * basándose en el tamño de la lista se acaben creando ids duplicados.
     *
     * @return id, int
     */
    public static int getLastId() {
        int id = 0;
        for (Steam steam : steams) {
            if (steam.getId() > id) id = steam.getId();
        }
        return id;
    }

    /**
     * Ordena el catálogo por id
     */
    public static void orderByID() {
        Collections.sort(steams);
    }

    /**
     * Ordena el catálogo por nombre
     */
    public static void orderByName() {
        steams.sort(new Comparator<Steam>() {
            @Override
            public int compare(Steam o1, Steam o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    /**
     * Ordena el catáloo por precio
     */
    public static void orderByPrice() {
        steams.sort(new Comparator<Steam>() {
            @Override
            public int compare(Steam o1, Steam o2) {
                return Integer.compare(o1.getPrice(), o2.getPrice());
            }
        });
    }

    /**
     * Retorna el atributo updatable del objeto Steam que corresponde al index actual en el catálogo
     *
     * @return boolean, true o false, indicando si es updatable o no
     */
    public static boolean getUpdatable() {
        return steams.get(index).isUpdatable();
    }

    /**
     * Retorna el tamaño de la lista
     *
     * @return int, tamaño de la lista
     */
    public static int getSteamSize() {
        return steams.size();
    }

    /**
     * Guarda, si se cumplen las condiciones, un registro editado o un nuevo registro.
     *
     * @param id,          String
     * @param name,        String
     * @param description, String
     * @param web,         String
     * @param price,       int
     * @param imageUrl,    String
     * @return boolean, true o false segun se haya podido guardar o no.
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws IOException
     */
    public static boolean saveSteam(String id, String name, String description, String web, int price, String imageUrl) throws ParserConfigurationException, TransformerException, IOException {
        /*
          Consideramos todos los campos obligatorios, así que si hay alguno vacío se mostrará una pantalla de error y no se
          guardarán cambios. El id nunca podrá estar vacío porque el usuario no lo ha podido modificar al editar o lo hemos
          generado automáticamente si ha elegido crear un registro nuevo.
         */
        if (name.isEmpty() || description.isEmpty() || web.isEmpty() || price == -1 || imageUrl == null) {
            return false;
        } else {
            int idInt = Integer.parseInt(id);

            //localizamos el nombre de la imagen para guardarla en el objeto
            String[] imageUrlSplited = imageUrl.split("/");
            int imageNamePosition = imageUrlSplited.length - 1;
            String imageName = imageUrlSplited[imageNamePosition];

            //copiamos la imagen al directorio catalog
            Path origenImagen = Paths.get(imageUrl.replace("file:", ""));
            Path destinoImagen = Paths.get("catalog/" + imageName);
            Files.copy(origenImagen, destinoImagen, REPLACE_EXISTING);

            //Si el catálogo tiene registros
            if (getSteamSize() != 0) {
                //si se cumple es que es una modificación.
                if (idInt == steams.get(index).getId()) {
                    //seleccionamos el objeto y lo actualizamos
                    Steam steam = steams.get(index);
                    steam.setName(name);
                    steam.setDescription(description);
                    steam.setPrice(price);
                    steam.setWeb(web);
                    steam.setImage(imageName);
                    //guardamos el archivo
                    saveFile(steam);
                    return true;
                }
                //si no se cumple es un registro  nuevo
                else {
                    return crearNuevo(name, idInt, price, web, imageName, description);
                }
            }
            //Si no tiene registros se crea un nuevo registro
            else return crearNuevo(name, idInt, price, web, imageName, description);
        }
    }

    private static boolean crearNuevo(String name, int idInt, int price, String web, String imageName, String description) throws TransformerException, ParserConfigurationException {
        //le asignamos un nombre, por ejemplo cambiando los espacios por guiones y le definimos la ruta del fichero
        String fileRoute = "catalog\\" + name.replace(" ", "_") + ".xml";
        //creamos el objeto
        Steam steam = new Steam(fileRoute, idInt, true, name, price, web, imageName, description);
        //lo agregamos a la lista
        steams.add(steam);
        index = steams.size() - 1;
        //guardamos el archivo
        saveFile(steam);
        return true;
    }

    /**
     * Borra el registro en la lista y los archivos correspondientes y modifica la variable index.
     *
     * @throws IOException
     */
    public static void deleteSteam() throws IOException {
        Steam steam = steams.get(index);
        Path path = Paths.get(steam.getFileRoute());
        Files.delete(path);
        Path imagePah = Paths.get("catalog/" + steam.getImage());
        Files.delete(imagePah);
        steams.remove(steam);
        if (index == 0) index = steams.size() - 1;
        else index--;
    }

    /**
     * Crea un archivo XML con los datos del objeto Steam que se le pasa como argumento.
     * @param steam objeto Steam
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    private static void saveFile(Steam steam) throws ParserConfigurationException, TransformerException {
        Path path = Paths.get(steam.getFileRoute());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement("steam");
        document.appendChild(root);

        Attr id = document.createAttribute("id");
        id.setValue(Integer.toString(steam.getId()));
        root.setAttributeNode(id);

        Attr updatable = document.createAttribute("updatable");
        updatable.setValue(Boolean.toString(steam.isUpdatable()));
        root.setAttributeNode(updatable);

        Element name = document.createElement("name");
        name.setTextContent(steam.getName());
        root.appendChild(name);

        String[] parrafs = steam.getDescription().split("\n");

        for (String s : parrafs) {
            Element parraf = document.createElement("p");
            parraf.setTextContent(s);
            root.appendChild(parraf);
        }

        Element price = document.createElement("price");
        price.setTextContent(Integer.toString(steam.getPrice()));
        root.appendChild(price);

        Element web = document.createElement("web");
        web.setTextContent(steam.getWeb());
        root.appendChild(web);

        Element image = document.createElement("image");
        image.setTextContent(steam.getImage());
        root.appendChild(image);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);

        StreamResult streamResult = new StreamResult(path.toFile());
        transformer.transform(domSource, streamResult);
    }

    /**
     * Lee el directorio catalog buscando archivos xml, los cuales luego se procesan con el método readFromFile.
     * @return int, 1 si la lectura es correcta y ha encontrado xml, -1 si el "catalog" no existe, -2 si no es un directorio,
     *         -3 si no ha encontrado archivos xml.
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    private static int readDirectory() throws IOException, SAXException, ParserConfigurationException {
        File dir = new File("catalog");
        if (dir.exists()) {
            if (dir.isDirectory()) {
                File[] listOfFiles = dir.listFiles();
                assert listOfFiles != null;
                for (File file : listOfFiles) {
                    if (file.isFile() && (file.getName().endsWith(".xml") || file.getName().endsWith(".XML"))) {
                        readFromFile(Paths.get(file.getPath()));
                    }
                }
                if (steams.size() > 0) return 1;
                else return -3;
            } else return -2;
        } else return -1;
    }

    /**
     * Lee un archivo xml, toma sus nodos y crea un objeto Steam, luego lo guarda en una lista.
     * @param path, ruta del archivo.
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    private static void readFromFile(Path path) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(path.toFile());
        String fileRute = path.toString();
        NodeList nList = document.getElementsByTagName("steam");

        Node node = nList.item(0);
        Element eElement = (Element) node;
        int id = Integer.parseInt(eElement.getAttribute("id"));
        boolean updatable = false;
        if (eElement.getAttribute("updatable").equals("true")) updatable = true;
        String name = eElement.getElementsByTagName("name").item(0).getTextContent();
        int price = Integer.parseInt(eElement.getElementsByTagName("price").item(0).getTextContent());
        String web = eElement.getElementsByTagName("web").item(0).getTextContent();
        String image = eElement.getElementsByTagName("image").item(0).getTextContent();
        int numParraf = document.getElementsByTagName("p").getLength();
        StringBuilder desciption = new StringBuilder();
        for (int i = 0; i < numParraf; i++) {
            if (desciption.length() == 0)
                desciption = new StringBuilder(document.getElementsByTagName("p").item(i).getTextContent());
            else desciption.append("\n").append(document.getElementsByTagName("p").item(i).getTextContent());
        }

        Steam steam = new Steam(fileRute, id, updatable, name, price, web, image, desciption.toString());

        steams.add(steam);
    }
}
