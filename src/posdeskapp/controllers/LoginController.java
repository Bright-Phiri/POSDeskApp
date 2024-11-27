/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posdeskapp.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author biphiri
 */
public class LoginController implements Initializable {

    @FXML
    private BorderPane rootPane;
    @FXML
    private ImageView imageView;
    public static BorderPane root;
    ScheduledService scheduledService;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        root = rootPane;
        try {
            VBox box = (VBox) FXMLLoader.load(getClass().getResource("/posdeskapp/views/LoginForm.fxml"));
            rootPane.setRight(box);
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }

        scheduledService = new ScheduledService() {
            @Override
            protected Task createTask() {
                return new Task() {
                    @Override
                    protected Object call() throws Exception {
                        setImage();
                        return null;
                    }
                };
            }
        };

        scheduledService.setDelay(Duration.ONE);
        scheduledService.setRestartOnFailure(true);
        scheduledService.setPeriod(Duration.seconds(8));
        
        Platform.runLater(() -> {
            scheduledService.start();
        });
    }

    private void setImage() {
        String image1 = "1.jpg";
        String image2 = "2.jpg";
        String image3 = "3.jpg";
        String image4 = "4.jpg";
        String image5 = "5.jpg";
        String image6 = "6.jpg";

        String image = null;
        int number = 1 + (int) (Math.random() * 6);
        
        switch (number) {
            case 1: {
                image = image1;
                break;
            }
            case 2: {
                image = image2;
                break;
            }
            case 3: {
                image = image3;
                break;
            }
            case 4: {
                image = image4;
                break;
            }
            case 5: {
                image = image5;
                break;
            }
            case 6: {
                image = image6;
                break;
            }
        }
        
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3), imageView);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
        
        imageView.setImage(new Image("/posdeskapp/images/" + image));
    }

}
