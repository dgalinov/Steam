package steam.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import steam.Main;
import steam.model.LocatedImage;
import steam.model.Steam;
import steam.model.SteamModel;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class SteamImageController implements Initializable {
    @FXML
    private ImageView image = new ImageView();

    private void mostrar(Steam steam){
        Image foto = new LocatedImage("file:catalog/"+ steam.getImage(),350,200,true,true);
        image.setImage(foto);
    }

    public void changeScene()throws Exception{
        URL url = new File("src/steam/view/SteamView.fxml").toURI().toURL();
        System.out.println("ChangedScene");
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root);
        Main.stage.setTitle("Steam Catalog");
        Main.stage.setScene(scene);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Steam steam = SteamModel.getActual();
        mostrar(steam);
    }
}
