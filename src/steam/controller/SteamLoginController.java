package steam.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import steam.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;

public class SteamLoginController {
    public AnchorPane background;

    @FXML
    TextField name = new TextField();
    @FXML
    PasswordField password = new PasswordField();

    public void getInputs(Event e) throws Exception {
        System.out.println(name.getText());
        validation(name.getText(), password.getText());
    }

    public void validation(String name, String password) throws Exception {
        try{
            File file = new File("src\\steam\\users.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String str;
            while ((str = br.readLine()) != null) {
                String[] exploded=str.split(";");
                System.out.println(name);
                System.out.println(password);
                if (exploded[0].equals(name) && exploded[1].equals(password)){
                    changeScene();
                    break;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeScene()throws Exception{
        URL url = new File("src/steam/view/SteamView.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root);
        Main.stage.setTitle("Steam Catalog");
        Main.stage.setScene(scene);
    }
}
