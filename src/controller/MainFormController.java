package controller;

import com.sun.glass.ui.SystemClipboard;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import javafx.application.Platform;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
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
import java.util.ArrayList;
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
    public TextField txtReplceTxt;
    public Button btnUp;
    private Matcher matcher;
  public  int replaceIndex1;
  public  int replaceIndex2;
  public  int index2=0;
  public ArrayList<Integer> placeReferance = new ArrayList<>();
    public int coun;


    public void initialize(){

        txtFind.textProperty().addListener(observable -> {
            isChanged =true;
            findOption();
            countWords();
            if(txtFind.getLength()==0){lblFoundText.setText("");}

        });

        txtDisplay.textProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue!=newValue){
                Stage stage = (Stage) txtDisplay.getScene().getWindow();
                String a=stage.getTitle().toString();
                if(a.contains("*")){}else {
                    stage.setTitle("*"+a);}

            }
           });
        txtDisplay.textProperty().addListener(observable -> {
            countAllWords();
        });


    }
    public void countWords(){
        coun=0;
        while (matcher.find()){coun++;}
        System.out.println(coun);
        lblFoundText.setText(String.valueOf(coun+1));
        if(lblFoundText.getText().equals(1+"")) {lblFoundText.setText("");}
        matcher.reset();
    }
    public void countAllWords(){
        int count2 =0;
        Matcher matcher1 =Pattern.compile("\\S{1,}").matcher(txtDisplay.getText().trim());
        while (matcher1.find()){
            count2++;

        }
      if (txtDisplay.getLength()==0){
            lblWordCount.setVisible(false);}else {
            lblWordCount.setText(count2+"");
      }
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

        txtDisplay.deselect();
        if (isChanged){

            matcher = Pattern.compile(txtFind.getText(),btnCaseSensitive.isSelected() ? 0: Pattern.CASE_INSENSITIVE).matcher(txtDisplay.getText());
            isChanged=false;
        }

        if(matcher.find()){
            txtDisplay.selectRange(matcher.start(),matcher.end());

            replaceIndex1 = matcher.start();
            replaceIndex2 =matcher.end();

        }else{
            matcher.reset();
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
placeReferance.add(matcher.start());
placeReferance.add(matcher.end());


        if(placeReferance.size()+1>2*(coun)){
           placeReferance.clear();

        }
        System.out.println(placeReferance.toString());

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

    public void btnReplaceTxtOnAction(ActionEvent actionEvent) {
        //System.out.println(replaceIndex1+"  "+replaceIndex2);
txtDisplay.setText(txtDisplay.getText().replaceAll(txtFind.getText(),txtReplceTxt.getText()));

          //  txtDisplay.setText(txtDisplay.getText().substring(0, replaceIndex1) + txtReplceTxt.getText() + txtDisplay.getText(replaceIndex2, txtDisplay.getLength()));

    }

    public void btnUpOnAction(ActionEvent actionEvent) {
        txtDisplay.deselect();
    }
}