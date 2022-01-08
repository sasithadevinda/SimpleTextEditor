package controller;

import com.sun.glass.ui.SystemClipboard;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public ToggleButton btnCaseSensitive;
    public ToggleButton btnRegEx;
    public TextField txtFind;
    public Button btnSelectAll1;

    public boolean isChanged;
    public Button btnDown;
    public Label lblFoundText;
    public Button btnCut;
    public Button btnPrint;
    private Matcher matcher;
  public  int countAll;

    public void initialize(){

        txtFind.textProperty().addListener(observable -> {
            isChanged =true;
            findOption();
            countWords();
            if (txtFind.getText().trim().length()==0){lblFoundText.setText("");}
        });

        txtDisplay.textProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue!=newValue){
                Stage stage = (Stage) txtDisplay.getScene().getWindow();
                String a=stage.getTitle().toString();
                if(a.contains("*")){}else {
                    stage.setTitle("*"+a);}

            }});
        txtDisplay.textProperty().addListener(observable -> {


            int count = txtDisplay.getText().length()-txtDisplay.getText().replaceAll("\\b[ ]+\\b","").length();
            lblWordCount.setText(String.valueOf(count+1));
            if (txtDisplay.getLength()==0){lblWordCount.setText("0");}
            countAll =count;
        });

    }
    public void countWords(){
        int coun=0;
        while (matcher.find()){coun++;}
        lblFoundText.setText(String.valueOf(coun));
    }

    public Path selectFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open a file");
        File file = fileChooser.showOpenDialog(null);
        Path path = Paths.get(file.getAbsolutePath());
        return path;

    }


    public void taskNew() throws IOException {
        //txtDisplay.clear();
        AnchorPane newPane =FXMLLoader.load(getClass().getResource("../view/MainForm.fxml"));
        Scene newScene = new Scene(newPane);
        Stage newStage =new Stage();
        newStage.setTitle("Untitled");
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


    }

    public void findOption(){
        if (isChanged){
            /*System.out.println(isChanged);*/
            matcher = Pattern.compile(txtFind.getText().trim(),btnCaseSensitive.isSelected() ? 0: Pattern.CASE_INSENSITIVE).matcher(txtDisplay.getText());

        }



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

    public void btnDownOnAction(ActionEvent actionEvent) {
        findOption();
        if(matcher.find()){


            txtDisplay.selectRange(matcher.start(),matcher.end());

        }



    }

    public void lblFoundText(MouseEvent mouseEvent) {
    }

    public void lblFoundTextOnAction(MouseEvent mouseEvent) {
    }

    public void btnCutOnAction(ActionEvent actionEvent) {
        mnCut.fire();
    }

    public void btnPrint(ActionEvent actionEvent) {

    }
}