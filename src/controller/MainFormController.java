package controller;

import com.sun.glass.ui.SystemClipboard;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainFormController {
    public Button btnOpen;
    public MenuItem mnOpen;
    public TextArea txtDisplay;
    public MenuItem mnSave;
    public Button btnSave;
    public MenuItem mnExit;
    public MenuItem mnSelectAll;
    public Button btnSelectAll;
    public MenuItem mnCopy;
    public MenuItem mnPaste;
    public MenuItem mnCut;
    public Label lblWordCount;

    public void initialize(){
        txtDisplay.textProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue!=newValue){
                Stage stage = (Stage) txtDisplay.getScene().getWindow();
                String a=stage.getTitle().toString();
                if(a.contains("*")){}else {
                    stage.setTitle("*"+a);}

            }});
        txtDisplay.textProperty().addListener(observable -> {
            int count = txtDisplay.getText().length()-txtDisplay.getText().replaceAll("\\S[ ]","").length();
            lblWordCount.setText(String.valueOf(count/2));
        });
    }

    public Path selectFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open a file");
        File file = fileChooser.showOpenDialog(null);
        Path path = Paths.get(file.getAbsolutePath());
        return path;

    }
    /* public File selectDirectory(){

         return Files;
     }*/
    public void taskNew() throws IOException {
        //txtDisplay.clear();
        AnchorPane newPane =FXMLLoader.load(getClass().getResource("../view/MainForm.fxml"));
        Scene newScene = new Scene(newPane);
        Stage newStage =new Stage();
        newStage.setScene(newScene);
        newStage.show();
    }

    public void taskOpen(Path path1) throws IOException {
        txtDisplay.clear();
        FileInputStream fis = new FileInputStream(String.valueOf(path1));
        byte[] fileBytes = new byte[fis.available()];
        fis.read(fileBytes);
        String fileContent = new String(fileBytes);

        txtDisplay.setText(fileContent);
        Stage stage = (Stage) txtDisplay.getScene().getWindow();
        stage.setTitle(""+path1.getFileName().toString());

    }
    public void taskSave() throws IOException {
        FileChooser fileChooser =new FileChooser();
        fileChooser.setTitle("Save File");
        File file =fileChooser.showSaveDialog(null);
        String displayContent = new String(txtDisplay.getText());
        byte[] bytes = displayContent.getBytes();
        OutputStream os = Files.newOutputStream(file.toPath());
        os.write(bytes);

    }

    public void btnOpenOnAction(ActionEvent actionEvent) throws IOException {

        taskOpen(selectFile());
    }

    public void mnOpenOnAction(ActionEvent actionEvent) throws IOException {
        taskOpen(selectFile());
    }

    public void mnSaveOnAction(ActionEvent actionEvent) throws IOException {
        taskSave();
    }

    public void btnSaveOnAction(ActionEvent actionEvent) throws IOException {
        taskSave();
    }

    public void mnNewOnAction(ActionEvent actionEvent) throws IOException {
        taskNew();
    }

    public void btnNewOnAction(ActionEvent actionEvent) throws IOException {
        taskNew();
    }

    public void mnExitOnAction(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void mnSelectAllOnAction(ActionEvent actionEvent) {
        txtDisplay.requestFocus();
        txtDisplay.selectAll();
    }

    public void btnSelectAllOnAction(ActionEvent actionEvent) {
        txtDisplay.requestFocus();
        txtDisplay.selectAll();
    }

    public void mnCopyOnAction(ActionEvent actionEvent) {

        Clipboard systemClipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(txtDisplay.getSelectedText());
        systemClipboard.setContent(clipboardContent);


       /* System.out.println(a);
        byte[] bcopy = a.getBytes();*/
    }




    public void mnPasteOnAction(ActionEvent actionEvent) {
        Clipboard paste = Clipboard.getSystemClipboard();
        int caretPosition = txtDisplay.getCaretPosition();
        txtDisplay.insertText(caretPosition,paste.getString());
    }

    public void mnCutOnAction(ActionEvent actionEvent) {
        Clipboard systemClipboard = Clipboard.getSystemClipboard();
        ClipboardContent cutContent =new ClipboardContent();
        cutContent.putString(txtDisplay.getSelectedText());
        systemClipboard.setContent(cutContent);
        //String b=txtDisplay.getSelectedText();
        txtDisplay.setText(txtDisplay.getText().replaceAll(cutContent.getString(),""));


    }
}