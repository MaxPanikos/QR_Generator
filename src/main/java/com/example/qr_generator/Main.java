package com.example.qr_generator;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Main extends Application {
    private Scene scene;
    private Path qrPath;

    @FXML
    private TextField dataField, nameField;
    @FXML
    private Label responseLabel;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("QR Generator");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("qr_generator.fxml"));
        VBox root = fxmlLoader.load();
        this.scene = new Scene(root, 300, 200);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

    private void generateQR(String data, String title, Path destPath, int size) {
        try {
            File tempFile = QRCode.from(data).to(ImageType.PNG).withSize(size, size).file(title);
            if (destPath.getParent() != null) {
                Files.createDirectories(destPath.getParent());
            }
            Files.move(tempFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("QR kód uložen do: " + destPath.toAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void choosePath (){
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Select save path");
        File chosenDir = dirChooser.showDialog(null);
        if (chosenDir != null) {
            this.qrPath = Paths.get(chosenDir.getAbsolutePath());
        }
    }
    @FXML
    private void generateQRButton() {
        String data = dataField.getText();
        String title = nameField.getText();

        if (data.isBlank() || title.isBlank()) {
            responseLabel.setText("Prosím vyplňte textová pole");
            return;
        } else if (qrPath == null) {
            responseLabel.setText("Prosím vyplňte složku k uložení");
            return;
        }

        Path fullPath = qrPath.resolve(title + ".png");

        generateQR(data, title, fullPath, 1000);
        responseLabel.setText("QR kód byl úspěšně vytvořen");
    }
}
