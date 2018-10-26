package filenamefix;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ferhat
 */
public class FixUIController implements Initializable {

    @FXML private TextField sourceCharacters;
    @FXML private TextField targetCharacters;
    @FXML private TextField removedCharacters;
    @FXML private TextField rootFile;
    
    @FXML private TextArea outputArea;
    
    private ArrayList<Pair> _pairs = new ArrayList();
    private StringBuilder _sb = new StringBuilder();
    private String[] _sourceChrs, _targetChrs, _removeChrs;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML private void listAction(){
        
        outputArea.clear();
        refreshPairs();
        int directoryCount = 0;
        int fileCount = 0;
        for(int i = 0; i < _pairs.size(); i++){
            Pair p = _pairs.get(i);
            if(p.isFolder){
                directoryCount++;
            }
            else{
                fileCount++;
            }
            outputArea.appendText(p.isFolder ? "folder ":"file ");
            outputArea.appendText(p.directory.getPath());
            outputArea.appendText(" ---- ");
            outputArea.appendText(p.targetName);
            outputArea.appendText("\r\n");
        }
        outputArea.appendText("folder count: ");
        outputArea.appendText("" + directoryCount);
        outputArea.appendText("\r\n");
        outputArea.appendText("file count: ");
        outputArea.appendText("" + fileCount);
        outputArea.appendText("\r\n");
        
    }
    @FXML private void convertAction(){
        listAction();
        int failCount = 0;
        
        outputArea.appendText("fail list");
        outputArea.appendText("\r\n");
        
        for(int i = 0; i < _pairs.size(); i++){
            Pair p = _pairs.get(i);
            boolean success = p.rename();
            if(!success){
                failCount++;
                outputArea.appendText(p.directory.getPath());
                outputArea.appendText(" ---- ");
                outputArea.appendText(p.targetName);
                outputArea.appendText("\r\n");
            }
        }
        outputArea.appendText("" + failCount);
        outputArea.appendText(" number of fail detected\r\n");
    }
    
    private void refreshPairs() {
        _sourceChrs = sourceCharacters.getText().split(":");
        _targetChrs = targetCharacters.getText().split(":");
        _removeChrs = removedCharacters.getText().split(":");
        _pairs.clear();
        java.io.File f = new File(rootFile.getText());
        addToPairs(f.getParentFile(), f);
    }
    private int indexOf(String[] target, String search, int startIndex){
        for(int i = 0; i < target.length; i++){
            String str = target[i];
            if(str.length() > 0 && search.startsWith(str, startIndex)){
                return i;
            }
        }
        return -1;
    }
    private void addToPairs(File directory, File f){
        String name = f.getName();
        _sb.setLength(0);
        boolean change = false;
        for(int i = 0; i < name.length(); i++){
            int sourceIndex = indexOf(_sourceChrs, name, i);
            if(sourceIndex >= 0){
                change = true;
                i+=_sourceChrs[sourceIndex].length()-1;
                _sb.append(_targetChrs[sourceIndex]);
            }
            else if((sourceIndex = indexOf(_removeChrs, name, i)) >= 0) {
                i+=_removeChrs[sourceIndex].length()-1;
                change = true;
            }
            else {
                _sb.append(name.charAt(i));
            }
        }
        if(change){
            Pair p = new Pair();
            p.isFolder = f.isDirectory();
            p.sourceName = name;
            p.targetName = _sb.toString();
            p.directory = directory;
            _pairs.add(p);
        }
        if(f.isDirectory()){
            File[] children = f.listFiles();
            for(int i = 0; i < children.length; i++){
                addToPairs(f, children[i]);
            }
        }
    }
    
    private static class Pair{
        public boolean isFolder;
        public String sourceName;
        public String targetName;
        public File directory;

        private boolean rename() {
            File f = new File(directory, sourceName);
            return f.renameTo(new File(directory, targetName));
        }
    }
}
