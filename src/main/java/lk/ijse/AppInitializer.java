package lk.ijse;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppInitializer extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("/view/load_form.fxml"))));
        stage.centerOnScreen();
        stage.setTitle("Chatterbox");

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
