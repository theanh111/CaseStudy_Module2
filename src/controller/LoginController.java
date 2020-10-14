package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Optional;

public class LoginController {
    @FXML
    private TextField userNameText;

    @FXML
    private TextField passWordText;

    public void login(ActionEvent event) throws IOException {
        String username = userNameText.getText();
        String password = passWordText.getText();

        if (username.equals("admin") && password.equals("admin")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login Successfully!");
            alert.setContentText("Welcome!");
            alert.showAndWait();

            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("../view/home.fxml"));
            primaryStage.setTitle("Skateshop Management Application");
            primaryStage.setScene(new Scene(root, 1200, 800));
            primaryStage.centerOnScreen();
            primaryStage.show();
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    try {
                        event.consume();
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("WARNING!");
                        alert.setContentText("Are you sure to close this program?");
                        Optional<ButtonType> action = alert.showAndWait();
                        if (action.get() == ButtonType.OK) {
                            primaryStage.close();
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (userNameText.getText().equals("") && passWordText.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Can't Login!");
            alert.setContentText("Please input your account!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Can't Login!");
            alert.setContentText("Please try again!");
            alert.showAndWait();
        }
    }
}
