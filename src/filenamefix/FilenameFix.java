/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filenamefix;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author ferhat
 */
public class FilenameFix extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        
        StackPane root = new StackPane();
        Parent ui = null;
        try {
            ui = FXMLLoader.load(getClass().getResource("FixUI.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(FilenameFix.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        root.getChildren().add(ui);
        
        Scene scene = new Scene(root, 800, 600);
        
        primaryStage.setTitle("Filename Fix");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
