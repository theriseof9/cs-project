package com.zerui.csproject.Controller;

import com.zerui.csproject.Model.Personal.User;
import com.zerui.csproject.Utils.DEF;
import com.zerui.csproject.Utils.Firebase;
import com.zerui.csproject.SplashScreen;
import com.zerui.csproject.Utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.EmailValidator;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginController {
    static Stage signUpStage;
    @FXML
    TextField email;
    @FXML
    PasswordField password;
//    @FXML
//    protected void initialize() {
//
//    }
    @FXML
    protected void loginButtonPressed() throws IOException {
        login();
    }
    @FXML
    protected void emailTyped(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode()== KeyCode.ENTER) login();
    }
    @FXML
    protected void passwordTyped(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode()== KeyCode.ENTER) login();
    }
    @FXML
    protected void signUp() throws IOException {
        Pane p = FXMLLoader.load(Utils.standard.fxmlPath("signUpView.fxml"), Utils.getBundle());
        Scene scene = new Scene(p, 300, 500);
        signUpStage = new Stage();
        signUpStage.setScene(scene);
        signUpStage.setTitle("Sign Up!");
        signUpStage.setScene(scene);
        signUpStage.show();
    }
    @FXML
    protected void resetPassword() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Reset password");
        dialog.setHeaderText("Enter details");
        dialog.setContentText("Email:");
        dialog.getDialogPane().getStylesheets().add(DEF.dialogCss);
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(email -> {
            if (EmailValidator.getInstance().isValid(email))
            if (Firebase.resetPassword(email)) Utils.standard.addStyleSheet(new Alert(Alert.AlertType.INFORMATION, "Email verification link sent!")).showAndWait();
            else Utils.standard.addStyleSheet(new Alert(Alert.AlertType.ERROR, "Email does not exist!")).showAndWait();
            else Utils.standard.addStyleSheet(new Alert(Alert.AlertType.ERROR, "Invalid email!")).showAndWait();
        });
    }
    @FXML
    protected void switchLanguage() throws IOException {
        DEF.isCN = !DEF.isCN;
        SplashScreen.getStage().setScene(new Scene(FXMLLoader.load(Utils.standard.fxmlPath("loginView.fxml"), Utils.getBundle())));
    }
    private void login() throws IOException {
        int status = User.login(email.getText(), password.getText());
        if (status==2) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Success");
            Utils.standard.addStyleSheet(alert);
            alert.showAndWait();
            Pane p = FXMLLoader.load(Utils.standard.fxmlPath("menuView.fxml"));
            SplashScreen.getStage().setScene(new Scene(p));
        } else if (status == 1) {
            ButtonType ok = new ButtonType("Ok", ButtonBar.ButtonData.CANCEL_CLOSE);
            ButtonType resendEmail = new ButtonType("Resend Email", ButtonBar.ButtonData.OK_DONE);
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please verify your email!", ok, resendEmail);
            Utils.standard.addStyleSheet(alert);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.orElse(ok)==resendEmail) {
                Firebase.sendVerificationEmail(email.getText());
                Utils.standard.addStyleSheet(new Alert(Alert.AlertType.INFORMATION, "Sent email!")).showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Wrong email or password!");
            Utils.standard.addStyleSheet(alert);
            alert.show();
        }
    }

}