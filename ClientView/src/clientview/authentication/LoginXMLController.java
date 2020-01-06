/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientview.authentication;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import Model.entities.UserEntity;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import Model.RequestCreator;
import Model.SocketConnection;
import Model.dao.implementation.NotificationDBOperations;
import Model.dao.implementation.UserDBOperations;
import Model.entities.NotificationEntity;
import Utility.Validation;
import com.jfoenix.controls.JFXButton;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class LoginXMLController implements Initializable {

    @FXML
    JFXTextField userNameTextField;
    @FXML
    JFXPasswordField passwordPasswordField;
    @FXML
    private JFXButton loginButton;
    @FXML
    private JFXButton signUpButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    public void loginAction() {
        if (!SocketConnection.getInstance().isServerClosed()) {
            String username = userNameTextField.getText(), password = passwordPasswordField.getText();
            if (Validation.checkString(username) && Validation.checkString(password)) {
                if (Validation.checkUsernameRegex(username)) {
                    /* UserEntity loginUser= new UserEntity();
                loginUser.setUsername(userNameTextField.getText());
                loginUser.setPassword(passwordPasswordField.getText());
                loginUser.setOnlineFlag(1);
                RequestCreator newRequest = new RequestCreator("UserDBOperations","login",loginUser);
                String newRequestJson= newRequest.getJsonObject();
                System.out.println(newRequestJson);*/
                    UserDBOperations.login(username, password);
                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("The  server is closed!");
            alert.showAndWait();
        }
    }

    @FXML
    private void signUpAction(ActionEvent event) {
     
        
        
        
    }
    
      public void buttonAction(){
             ArrayList<Object> data = new ArrayList<>();
        NotificationEntity notification = new NotificationEntity();
        notification.setHeader("test Header");
        notification.setText("test Text");
        notification.setNotificationType("test Notification");
        notification.setSenderID(2);     
        data.add(notification);
        NotificationDBOperations.sendNotification(data);
    }
}
