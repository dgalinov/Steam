package steam;

import steam.controller.SteamController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.logging.Level;


public class Main extends Application {
    public static Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setTitle("Steam Login");
        Parent root = FXMLLoader.load(getClass().getResource("view/SteamLoginView.fxml"));
        Scene scene = new Scene(root,600,400);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                SteamController.LOGGER.log(Level.INFO, "Programa cerrado.");
            }
        });
    }
}
