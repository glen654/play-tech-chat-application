package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.bo.BoFactory;
import lk.ijse.bo.custom.LoginBo;
import lk.ijse.dto.UserDto;

import java.io.IOException;
import java.sql.SQLException;

public class LoginFormController {
    @FXML
    private AnchorPane rootNode;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPassword;

    LoginBo loginBo = (LoginBo) BoFactory.getBoFactory().getBo(BoFactory.BoTypes.LOGIN);

    @FXML
    void btnHomeOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/main_form.fxml"));

        Scene scene = new Scene(rootNode);

        Stage primaryStage =(Stage) this.rootNode.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chatterbox");
    }

    @FXML
    void btnLogInOnAction(ActionEvent event) {
        String displayName = txtName.getText();
        String password = txtPassword.getText();

        try {
            UserDto userDto = loginBo.searchUser(displayName,password);

            if(userDto != null){
                clearFields();
                new Alert(Alert.AlertType.CONFIRMATION,"You are successfully logged in").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Login Unsuccessfull").show();
        }
    }

    private void clearFields() {
        txtName.setText("");
        txtPassword.setText("");
    }
}
