package lk.ijse.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoadFormController implements Initializable {
    @FXML
    private ProgressBar progress;

    @FXML
    private AnchorPane rootNode;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                for (int i = 0; i <= 10; i++) {
                    updateProgress(i, 100);
                    Thread.sleep(100);
                }
                Label label = new Label();
                Platform.runLater(() -> {
                    ((AnchorPane) progress.getParent()).getChildren().add(label);
                    label.setText("Loading Chatterbox...");
                });
                updateProgress(10, 100);
                Thread.sleep(500);
                for (int i = 21; i <= 90; i++) {
                    updateProgress(i, 100);
                    Thread.sleep(20);
                }
                Platform.runLater(() -> label.setText("Opening Main Page..."));
                updateProgress(90, 100);
                Thread.sleep(500);
                for (int i = 91; i <= 100; i++) {
                    updateProgress(i, 100);
                    Thread.sleep(30);
                }
                return null;
            }
        };

        progress.progressProperty().bind(task.progressProperty());
        task.setOnSucceeded(event -> {
            try {
                Stage stage1 = (Stage) progress.getScene().getWindow();
                stage1.close();

                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main_form.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Chatterbox");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        new Thread(task).start();
    }
}
