package steam.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import steam.Main;
import steam.model.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.*;

public class SteamController implements Initializable {

    public static final Logger LOGGER = Logger.getLogger("steam");

    private boolean nuevoClicked=false;
    private boolean editarClicked=false;

    //Campos de texto e imagen
    @FXML
    private TextField id = new TextField();
    @FXML
    private TextField nombre = new TextField();
    @FXML
    private TextField precio = new TextField();
    @FXML
    private TextArea descripcion = new TextArea();
    @FXML
    private ImageView image = new ImageView();
    @FXML
    private TextField web = new TextField();

    //Botones de ordenación
    @FXML
    private RadioButton orderByID = new RadioButton();
    @FXML
    private RadioButton orderByName = new RadioButton();
    @FXML
    private RadioButton orderByPrice = new RadioButton();

    //Botones de navegación
    @FXML
    private Button verPrimero = new Button();
    @FXML
    private Button verUltimo = new Button();
    @FXML
    private Button verSiguiente = new Button();
    @FXML
    private Button verAnterior = new Button();

    //Botones de acción
    @FXML
    private Button examinar = new Button();
    @FXML
    private Button nuevo = new Button();
    @FXML
    private Button editar = new Button();
    @FXML
    private Button borrar = new Button();
    @FXML
    private Button guardar = new Button();
    @FXML
    private Button cancelar = new Button();

    /**
     * Método para iniciar el catálogo llama al método estático initSteams de la clase SteamModel
     */
    private void cargarCatalogo(){
        try {
            switch (SteamModel.initSteams()){
                case 1:
                    //Se ha iniciado bien el catálogo, se muestra el primer registro y escribe en el log
                    LOGGER.log(Level.INFO, "Catálogo iniciado correctamente.");
                    mostarPrimero();
                    break;
                case -1:
                    //No se ha iniciado el catálogo porque no existe el fichero, se cierra el programa y escribe en el log
                    LOGGER.log(Level.SEVERE, "ERROR. No se ha podido iniciar el catálogo. No existe el fichero \"catalog\".\nPrograma cerrado.");
                    System.exit(-1);
                    break;
                case -2:
                    //No se ha iniciado el catálogo porque existe el fichero pero no es un directorio, se cierra el programa y escribe en el log
                    LOGGER.log(Level.SEVERE, "ERROR. No se ha podido iniciar el catálogo, el fichero \"catalog\" no es un directorio.\nPrograma cerrado.");
                    System.exit(-2);
                    break;
                case -3:
                    //No se ha iniciado el catálogo porque no existen archivos xml dentro, se cierra el programa y escribe en el log
                    LOGGER.log(Level.SEVERE,"ERROR. No se ha podido iniciar el catálogo, no hay archivos xml en el directorio.\nPrograma cerrado.");
                    System.exit(-3);
                    break;
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            LOGGER.log(Level.SEVERE, "ERROR al procesar los archivos xml: "+Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * Método que muestra el primer registro de la colección, llama al método estático getFirst de la clase SteamModel
     * y al método mostrar del controlador, es a su vez llamado por otros métodos del controlador.
     * Es accionado por el botón Primero
     */
    @FXML
    public void mostarPrimero(){
        Steam firstSteam = SteamModel.getFirst();
        mostrar(firstSteam);
        LOGGER.log(Level.INFO, "READ, Steam_ID:["+ SteamModel.getActual().getId()+"]-> Mostrado el primer registro.");
    }

    /**
     * Método para mostrar el último registro de la colección
     * Es accionado por el botón Último
     */
    @FXML
    public void mostrarUltimo(){
        Steam lastSteam = SteamModel.getLast();
        mostrar(lastSteam);
        LOGGER.log(Level.INFO, "READ, Steam_ID:["+ SteamModel.getActual().getId()+"]-> Mostrado el último registro.");
    }

    /**
     * Método para mostrar el siguiente registro de la colección
     * Es accionado por el botón Siguiente
     */
    @FXML
    public void mostrarSiguiente(){
        Steam nextSteam = SteamModel.getNext();
        mostrar(nextSteam);
        LOGGER.log(Level.INFO, "READ, Steam_ID:["+ SteamModel.getActual().getId()+"]-> Mostrado el siguiente registro.");
    }

    /**
     * Método para mostrar el registro anterior de la colección
     * Es accionado por el botón Anterior
     */
    @FXML
    public void mostrarAnterior(){
        Steam prevSteam = SteamModel.getPrev();
        mostrar(prevSteam);
        LOGGER.log(Level.INFO, "READ, Steam_ID:["+ SteamModel.getActual().getId()+"]-> Mostrado el anterior registro.");
    }

    /**
     * Método accionado por el radio button ID, llama al método orderByID de la clase SteamModel
     * llama al método mostrarPrimero de éste controlador y al método mostrarMensaje
     */
    @FXML
    public void setOrderByID(){
        SteamModel.orderByID();
        LOGGER.log(Level.INFO, "ORDENACIÓN, Steam_ID:["+ SteamModel.getActual().getId()+"]-> Se ha ordenado el catálogo por ID.");
        mostarPrimero();
        mostrarMensaje(orderByID, "Ordenado por ID", "Se ha ordenado el catálogo por ID correctamente");
    }

    /**
     * Método accionado por el radio button Nombre, llama al método orderByName de la clase SteamModel
     * llama a los métodos mostrarPrimero y mostarMensaje de éste controlador
     */
    @FXML
    public void setOrderByName(){
        SteamModel.orderByName();
        LOGGER.log(Level.INFO, "ORDENACIÓN, Steam_ID:["+ SteamModel.getActual().getId()+"]-> Se ha ordenado el catálogo por Nombre.");
        mostarPrimero();
        mostrarMensaje(orderByID, "Ordenado por Nombre", "Se ha ordenado el catálogo por Nombre correctamente");
    }

    /**
     * Método accionado por el radio button Precio, llama al método orderByPrice de la clase SteamModel
     * llama a los métodos mostrarPrimero y mostarMensaje de éste controlador
     */
    @FXML
    public void setOrderByPrice(){
        SteamModel.orderByPrice();
        LOGGER.log(Level.INFO, "ORDENACIÓN, Steam_ID:["+ SteamModel.getActual().getId()+"]-> Se ha ordenado el catálogo por Precio.");
        mostarPrimero();
        mostrarMensaje(orderByID, "Ordenado por Precio", "Se ha ordenado el catálogo por Precio correctamente");
    }

    /**
     * Al pulsar el botón nuevo se cambia el texto del texview id al siguiente id y se ponen en blanco los demás
     * campos. Se indica que se ha marcado el botón Nuevo y se llama a la función onClickNuevoOrEditar que hará
     * más cambios en la UI
     */
    @FXML
    public void onClickNuevo(){
        if (SteamModel.getSteamSize()!=0) {
            id.setText(Integer.toString(SteamModel.getLastId() + 1));
            LOGGER.log(Level.INFO, "CREATE, Steam_ID:["+ SteamModel.getActual().getId()+"]");
        }else{
            id.setText("1");
            LOGGER.log(Level.INFO, "CREATE, Steam_ID:[1]");
        }
        initFiels();
        nuevoClicked=true;
        onClickNuevoOrEditar();
        disableButtons();
    }

    private void initFiels(){
        nombre.setText("");
        descripcion.setText("");
        precio.setText("");
        web.setText("");
        image.setImage(null);
    }

    /**
     * Al pulsar sobre editar se comprobará si el registro es updatable, si lo es se incará que se ha pulsado el boton editar
     * y se llamará al método onClickNuevoOrEditar que hará cambios en la UI. Si salta la excepción se indica el error y se
     * vuelve a iniciar el estado de los elementos de la UI con initUIStatus
     */
    @FXML
    public void onClickEditar(){
        try {
            LOGGER.log(Level.INFO, "UPDATE, Steam_ID:["+ SteamModel.getActual().getId()+"]");
            checkUpdatable();
            editarClicked=true;
            onClickNuevoOrEditar();
            disableButtons();
        }
        catch (NoUpdatableException e){
            mostrarMensaje(editar,Alert.AlertType.WARNING,"Registro no editable",e.getMessage());
            LOGGER.log(Level.WARNING, "ERROR, Steam_ID:["+ SteamModel.getActual().getId()+"]-> registro no editable");
            initUIStatus();
        }
    }

    //Comprueba la excepción que se pedía en la práctica
    private void checkUpdatable(){
        if (!SteamModel.getUpdatable()) throw new NoUpdatableException("Registro no editable. Éste registro está marcado como no editable.");
    }

    /**
     * Al pulsar el botón Borrar se llama al método deleteSteam de modelo SteamModel y si sale bien se muestra el anterior
     * registro y un mensaje informando del éxito. Si ocurre algun error se notifica.
     */
    @FXML
    public void onClickBorrar(){
        try {
            SteamModel.deleteSteam();
            if (SteamModel.getSteamSize()!=0) {
                mostrarAnterior();
                mostrarMensaje(borrar, Alert.AlertType.INFORMATION, "Registro borrado", "Registro borrado correctamente." +
                        "\nSe recomienda reordenar el catálogo.");
                LOGGER.log(Level.INFO, "DELETE, Steam_ID:["+ SteamModel.getActual().getId()+"]");
            }else{
                id.setText("");
                initFiels();
                disableButtons();
                mostrarMensaje(borrar, Alert.AlertType.WARNING, "Todos los registros borrados", "ATENCIÓN, se han borrado " +
                        "todos los registros del catálogo, cree uno nuevo o cierre el programa y cargue archivos xml e imágenes en la carpeta \"catalog\"");
                LOGGER.log(Level.WARNING, "DELETE, se han borrado todos los registros del catálogo.");
            }
        } catch (IOException e) {
            mostrarMensaje(borrar, Alert.AlertType.ERROR, "Error al borrar registro", "Se ha producido un error " +
                    "al borrar el archivo del catálogo.\nMás Información:\n"+e.getMessage());
            LOGGER.log(Level.SEVERE, "ERROR, Steam_ID:["+ SteamModel.getActual().getId()+"]-> Error al borrar: "+e.getMessage());
        }
    }

    /**
     * Al pulsar sobre el botón Guardar guardan en variables los datos introducidos por el usuario, se comprueba y si son correctos
     * se llama al método saveSteam para guardar el archivo xml y la imagen.
     */
    @FXML
    public void onClickGuardar(){
        String idText = id.getText();
        String nombreText = nombre.getText();
        String precioText = precio.getText();
        int precioInt = -1;
        //Comprobamos que se ha introduciso un precio numérico (entero) si no se lanza un error
        try{
            if (!precioText.isEmpty()){
                precioInt = Integer.parseInt(precioText);
            }
        }catch (NumberFormatException e){
            mostrarMensaje(guardar, Alert.AlertType.WARNING, "Precio incorrecto", "No se ha podido guardar, no ha introducido un valor " +
                    "numérico entero en el precio");
            LOGGER.log(Level.WARNING, "ERROR, Steam_ID:["+ SteamModel.getActual().getId()+"]-> No se ha podido guardar, no ha introducido un valor numérico en el precio");
            return;
        }
        String webText = web.getText();
        String descripcionText = descripcion.getText();
        Image imageImage = image.getImage();
        /*
         * En Java 10 hay un método (getUrl()) de la clase Image que te devuelve la url de un objeto Image, pero en Java 8
         * que es el que estamos usando parece que no existe ese método, hay uno que es (impl_getUrl()) que da la url, pero
         * IntelliJ lo marca como deprecated, funcionar funciona, pero al ser deprecated puede que no exista en la versión
         * del java que tenga instalado el cliente y falle. He encontrado ésta solución por Internet, consiste en crear una
         * clase que extienda de Image y que se guarde la url al crear el objeto y con un método get específico para esa url.
         */
        String imageUrl = (imageImage instanceof LocatedImage)
                ? ((LocatedImage) imageImage).getURL()
                : null;

        //Se llama al método saveSteam, si existe algún campo vacío se notifica al usuario, si hay algún error se notifica
        try {
            if(!SteamModel.saveSteam(idText, nombreText, descripcionText,webText, precioInt, imageUrl)){
                mostrarMensaje(guardar, Alert.AlertType.WARNING, "Error al guardar", "No se ha podido guardar, " +
                        "ha dejado algún campo vacío.");
                LOGGER.log(Level.WARNING, "ERROR, Steam_ID:["+ SteamModel.getActual().getId()+"]-> No se ha podido guardar, ha dejado algún campo vacío.");
            }
            else {
                if (nuevoClicked){
                    mostrarMensaje(guardar, Alert.AlertType.INFORMATION, "Registro creado", "Se ha creado el " +
                            "registro correctamente.\nSe recomienda reordenar el catálogo");
                    LOGGER.log(Level.INFO, "FINE, Steam_ID:["+ SteamModel.getActual().getId()+"]-> Registro creado correctamente");
                }
                if (editarClicked){
                    mostrarMensaje(guardar, Alert.AlertType.INFORMATION,"Registro actualizado", "Se ha acutalizado " +
                            "el registro correctamente.\nSe recomienda reordenar el catálogo.");
                    LOGGER.log(Level.INFO, "FINE, Steam_ID:["+ SteamModel.getActual().getId()+"]-> Registro actualizado correctamente");
                }
            }
        } catch (ParserConfigurationException | TransformerException e) {
            mostrarMensaje(guardar, Alert.AlertType.ERROR, "Error al guardar", "Se ha producido un error al " +
                    "guardar el archivo .xml.\nMás inforación:\n."+e.getMessage());
            LOGGER.log(Level.SEVERE, "ERROR: Steam_ID:["+ SteamModel.getActual().getId()+"]-> Error al guardar xml: "+e.getMessage());
        } catch (IOException e){
            mostrarMensaje(guardar, Alert.AlertType.ERROR, "Error al guardar", "se ha producido un error al " +
                    "guardar la imagen.\nMás información:\n"+e.getMessage());
            LOGGER.log(Level.SEVERE, "ERROR: Steam_ID:["+ SteamModel.getActual().getId()+"]-> Error al guardar imagen: "+e.getMessage());
        }

        //se reinicia el stado de la UI y se muestra el registro editado o nuevo
        initUIStatus();
        showOnGuardarOrCancelar();
    }

    //Al pulsar Cancelar se reinicia el estado de la UI y se muestra el registro actual.
    @FXML
    public void onClickCancelar(){
        LOGGER.log(Level.INFO, "CANCELED, Steam_ID:["+ SteamModel.getActual().getId()+"]-> Cancelado por el usuario.");
        initUIStatus();
        showOnGuardarOrCancelar();
    }

    private void showOnGuardarOrCancelar(){
        Steam steam = SteamModel.getActual();
        mostrar(steam);
        LOGGER.log(Level.INFO, "READ, Steam_ID:["+ SteamModel.getActual().getId()+"]");
    }

    //Modificaciones de los componentes de la vista al pulsar Nuevo o Editar.
    private void onClickNuevoOrEditar(){
        nombre.setEditable(true);
        descripcion.setEditable(true);
        precio.setEditable(true);
        web.setEditable(true);
        examinar.setVisible(true);
        guardar.setVisible(true);
        cancelar.setVisible(true);
        nuevo.setDisable(true);
    }

    private void disableButtons(){
        editar.setDisable(true);
        borrar.setDisable(true);
        verPrimero.setDisable(true);
        verAnterior.setDisable(true);
        verSiguiente.setDisable(true);
        verUltimo.setDisable(true);
        orderByID.setDisable(true);
        orderByName.setDisable(true);
        orderByPrice.setDisable(true);
    }

    //Modificadores de los componentes de la vista al pulsar Guardar o Cancelar.
    private void initUIStatus(){
        nombre.setEditable(false);
        descripcion.setEditable(false);
        precio.setEditable(false);
        web.setEditable(false);
        examinar.setVisible(false);
        guardar.setVisible(false);
        cancelar.setVisible(false);
        verPrimero.setDisable(false);
        verAnterior.setDisable(false);
        verSiguiente.setDisable(false);
        verUltimo.setDisable(false);
        orderByID.setDisable(false);
        orderByName.setDisable(false);
        orderByPrice.setDisable(false);
        borrar.setDisable(false);
        nuevo.setDisable(false);
        editar.setDisable(false);
        nuevoClicked=false;
        editarClicked=false;
    }

    /**
     * Al pulsar el botón Examinar se lanza un filechooser para elegir la imagen, si el usuario elige una se cambia el contenido
     * de la Imageview image
     */
    @FXML
    public void onClickExaminar(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("IMG files (*.bmp, *.gif, *.jpeg, *.png)",
                "*.bmp", "*.gif", "*.jpeg", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(examinar.getScene().getWindow());
        if(file!=null){
            String ruta = file.getPath();
            Image foto = new LocatedImage("file:"+ruta,350,200,true,true);
            image.setImage(foto);
        }
    }

    private void mostrar(Steam steam){
        id.setText(Integer.toString(steam.getId()));
        nombre.setText(steam.getName());
        descripcion.setText(steam.getDescription());
        precio.setText(Integer.toString(steam.getPrice()));
        web.setText(steam.getWeb());
        Image foto = new LocatedImage("file:catalog/"+ steam.getImage(),350,200,true,true);
        image.setImage(foto);
    }

    private void mostrarMensaje(RadioButton button, String title, String message){
        Window owner = button.getScene().getWindow();

        AlertMenssage.showAlert(Alert.AlertType.INFORMATION,owner, title, message);
    }

    private void mostrarMensaje(Button button, Alert.AlertType type, String title, String message){
        Window owner = button.getScene().getWindow();

        AlertMenssage.showAlert(type,owner, title, message);
    }

    public void logout()throws Exception{
        URL url = new File("src/steam/view/SteamLoginView.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root);
        Main.stage.setTitle("Steam Login");
        Main.stage.setScene(scene);
    }

    public void changeScene()throws Exception{
        URL url = new File("src/steam/view/SteamImageView.fxml").toURI().toURL();
        System.out.println("ChangedScene");
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root);
        Main.stage.setTitle("Steam Image Preview");
        Main.stage.setScene(scene);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            LogManager.getLogManager().reset();
            Handler fileHandler = new FileHandler("steamLog.log", true);
            SimpleFormatter simpleFormatter = new SimpleFormatter();
            fileHandler.setFormatter(simpleFormatter);
            LOGGER.addHandler(fileHandler);
            fileHandler.setLevel(Level.ALL);
            LOGGER.log(Level.INFO, "Programa iniciado.");
            cargarCatalogo();
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
