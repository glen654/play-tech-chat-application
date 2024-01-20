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
import lk.ijse.bo.custom.CreateAccBo;
import lk.ijse.dto.UserDto;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;


public class CreateAccountController {

    @FXML
    private AnchorPane rootNode;
    @FXML
    private TextField txtDisplayName;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtPassword;

    CreateAccBo createAccBo = (CreateAccBo) BoFactory.getBoFactory().getBo(BoFactory.BoTypes.CREATE_ACCOUNT);
    public void btnCreateAccOnAction(ActionEvent actionEvent)  {
        if(validateUser()){
            String displayName = txtDisplayName.getText();
            String email = txtEmail.getText();
            String password = txtPassword.getText();

            var dto = new UserDto(displayName,email,password);

            try {
                boolean isSaved = createAccBo.saveUser(dto);

                if(isSaved){
                    clearFields();
                    new Alert(Alert.AlertType.CONFIRMATION,"Your account creation is successfull").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,"Account creation unsuccessfull").show();
            }
        }
    }

    public boolean validateUser(){
        String displayName = txtDisplayName.getText();

        boolean isDisplayNameValidated = Pattern.matches("[A-Z][a-zA-Z\\s]+", displayName);
        if (!isDisplayNameValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Display Name!").show();
            return false;
        }

        String password = txtPassword.getText();

        boolean isPasswordValidated = Pattern.matches("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}", password);
        if (!isPasswordValidated) {
            new Alert(Alert.AlertType.ERROR, "Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters").show();
            return false;
        }

        String email = txtEmail.getText();

        boolean isEmailValidated = Pattern.matches("(^[a-zA-Z0-9_.-]+)@([a-zA-Z]+)([\\\\.])([a-zA-Z]+)$", email);
        if (!isEmailValidated) {
            new Alert(Alert.AlertType.ERROR, "Invalid Email Address!").show();
            return false;
        }

        return true;
    }

    private void clearFields() {
        txtDisplayName.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
    }

    public void btnHomeOnAction(ActionEvent actionEvent) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/main_form.fxml"));

        Scene scene = new Scene(rootNode);

        Stage primaryStage =(Stage) this.rootNode.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chatterbox");
    }
}
