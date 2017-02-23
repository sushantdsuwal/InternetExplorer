/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internetexplorer;

import com.sun.javafx.property.adapter.PropertyDescriptor;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;

/**
 *
 * @author Nepaze
 */
public class IEMainController implements Initializable {

    private Label label;
    @FXML
    private WebView viewer;
    @FXML
    private TextField addBar;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnNext;
    @FXML
    private Button btnRefresh;
    @FXML
    private Button btnGo;
    @FXML
    private ProgressBar progress = new ProgressBar();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        WebEngine webEngine = viewer.getEngine();
        webEngine.load("http://www.google.com");
//        addBar.setText("http://www.google.com");
        progress.progressProperty().bind(webEngine.getLoadWorker().progressProperty());

        webEngine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {

            if (newValue == Worker.State.SUCCEEDED) {
                addBar.setText(webEngine.getLocation());
            }
        });

    }

    @FXML
    private void doPrevious(ActionEvent event) {
        viewer.getEngine().load(goBack());
    }

    @FXML
    private void doNext(ActionEvent event) {
        viewer.getEngine().load(goForward());
    }

    @FXML
    private void dorefresh(ActionEvent event) {
        viewer.getEngine().reload();
    }

    @FXML
    private void doBtnGo(ActionEvent event) {
//        WebEngine webEngine = viewer.getEngine();
//        webEngine.load(addBar.getText());

        if (addBar.getText(0, 7).equals("http://") || addBar.getText(0, 8).equals("https://")) {
            viewer.getEngine().load(addBar.getText());

        } else if (!addBar.getText(0, 7).equals("http://")) {
            viewer.getEngine().load("http://" + addBar.getText());
        } else {
            viewer.getEngine().load("https://" + addBar.getText());
        }

    }

    public String goBack() {
        final WebHistory history = viewer.getEngine().getHistory();
        ObservableList<WebHistory.Entry> entryList = history.getEntries();
        int currentIndex = history.getCurrentIndex();

        Platform.runLater(new Runnable() {
            public void run() {
                history.go(- 1);
            }
        });

        if (currentIndex > 1) {
            entryList.get(currentIndex - 1);
            btnBack.setDisable(false);
        } else {
            entryList.get(history.getCurrentIndex());
            btnBack.setDisable(true);
            btnNext.setDisable(false);
        }
        return entryList.get(currentIndex).getUrl();
    }

    public String goForward() {
        final WebHistory history = viewer.getEngine().getHistory();
        ObservableList<WebHistory.Entry> entryList = history.getEntries();
        int currentIndex = history.getCurrentIndex();

        Platform.runLater(new Runnable() {
            public void run() {
                history.go(1);
            }
        });
        if (currentIndex < entryList.size() - 2) {
            entryList.get(currentIndex + 1);
            btnNext.setDisable(false);
        } else {
            entryList.get(history.getCurrentIndex());
            btnNext.setDisable(true);
            btnBack.setDisable(false);
        }
        return entryList.get(currentIndex).getUrl();
    }

    @FXML
    private void doThisOnMuse(MouseEvent event) {

        //  addBar.setText(viewer.getEngine().getLocation());
    }

}
