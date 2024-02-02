package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.server.Server;

import java.io.IOException;

public class MainFormController {
    @FXML
    private AnchorPane rootNode;

    @FXML
    void btnGetStartOnAction(ActionEvent event) {
        try {
            Server server = Server.getServerSocket();
            Thread thread = new Thread(server);
            thread.start();

            rootNode.getChildren().clear();
            Stage stage = (Stage) rootNode.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/login.fxml"))));
            stage.show();
            stage.setOnCloseRequest(e-> {
                System.exit(0);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnCreateAccountOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/create_account.fxml"));

        Scene scene = new Scene(rootNode);

        Stage primaryStage =(Stage) this.rootNode.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chatterbox");
    }

    @FXML
    void btnSignInOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/login.fxml"));

        Scene scene = new Scene(rootNode);

        Stage primaryStage =(Stage) this.rootNode.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chatterbox");
    }
}
