package net.panat.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.collections.ObservableList;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;

public class Controller implements Initializable {

	@FXML
	private ProgressBar progressBar;
	
	@FXML
	private WebView webView;
	
	@FXML
	private TextField textField;
	
	@FXML
	private Label zoomDisplay = new Label("100%");
	
	@FXML
	private Button zoomOutButton;
	
	@FXML
	private Button zoomInButton;
	
	@FXML
	private Button refreshBtn;
	
	@FXML
	private Button nextButton;
	@FXML
	private Button backButton;
	@FXML
	private Button SourceBtn;
	
	
	private String currentSite = "https://www.google.com/";
	private WebEngine engine;
	private WebHistory webHistory;
	
	private String mainURL = "https://www.google.com/";
	
	private float screenSize = 1;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		this.engine = this.webView.getEngine();
		this.textField.setText(this.mainURL);
		this.engine.load(this.mainURL);
	}
	
	public WebEngine getWebEngine() {
		return this.engine;
	}
	
	public ProgressBar getProgressBar() {
		return this.progressBar;
	}
	
	public String getCurrentSite() {
		return this.currentSite;
	}
	
	public void setCurrentSite(String currentSite) {
		this.currentSite = currentSite;
	}
	
	public Button getSourceBtn() {
		return this.SourceBtn;
	}
	
	public WebView getWebView() {
		return this.webView;
	}
	
	public TextField getURLBar() {
		return this.textField;
	}
	
	public void loadSite() {
		
		if(this.textField.getText().startsWith("https://") || 
				this.textField.getText().startsWith("http://")) {
			this.engine.load(textField.getText());
			this.textField.setText(textField.getText());
			setCurrentSite(textField.getText());
		} else {
			this.engine.load("http://" + textField.getText());
			this.textField.setText("https://" + textField.getText());
			setCurrentSite(textField.getText());
		}
	}
	
	public Button getZoomOutButton() {
		return this.zoomOutButton;
	}
	
	public Button getZoomInButton() {
		return this.zoomInButton;
	}
	
	public Button getRefreshButton() {
		return this.refreshBtn;
	}
	
	public Button getNextButton() {
		return this.nextButton;
	}
	
	public Button getBackButton() {
		return this.backButton;
	}
	
	public void reload() {
		this.engine.reload();
	}
	
	public void zoomIn() {
		if(this.screenSize >= 2) {
			return;
		}
		
		this.screenSize += 0.25;
		setZoomDisplay(this.screenSize);
		webView.setZoom(this.screenSize);
	}
	
	public void zoomOut() {
		if(this.screenSize <= 0.25) {
			return;
		}
		this.screenSize -= 0.25;
		setZoomDisplay(this.screenSize);
		webView.setZoom(this.screenSize);
	}
	
	public void setZoomDisplay(float value) {
		DecimalFormat df = new DecimalFormat("#");
		
		this.zoomDisplay.setText(String.valueOf(df.format(value*100)) + "%");
	}
	
	public void nextPage() {
		this.webHistory = this.engine.getHistory();
		ObservableList<WebHistory.Entry> entry = this.webHistory.getEntries();
		try {
			this.webHistory.go(1);
			this.textField.setText(entry.get(webHistory.getCurrentIndex()).getUrl());
			setCurrentSite(entry.get(webHistory.getCurrentIndex()).getUrl());
		} catch(IndexOutOfBoundsException ex) {
			return;
		}
		
	}
	
	public void viewSource() {
		try {
			URL yahoo = new URL(this.textField.getText());
			BufferedReader in = new BufferedReader( new InputStreamReader(yahoo.openStream()));
			String inputLine = "";
			String srcList = "";
			
			while ((inputLine = in.readLine()) != null) {
				srcList += inputLine;
			}
			
			srcList = srcList.replace("<", "&lt;").replace(">", "&gt;");
			
			String content = "<body>Source Code<br> " + srcList +"</body>";
			this.engine.loadContent(content);
			
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void backPage() {
		this.webHistory = this.engine.getHistory();
		ObservableList<WebHistory.Entry> entry = this.webHistory.getEntries();
		try {
			this.webHistory.go(-1);
			this.textField.setText(entry.get(webHistory.getCurrentIndex()).getUrl());
			setCurrentSite(entry.get(webHistory.getCurrentIndex()).getUrl());
		} catch(IndexOutOfBoundsException ex) {
			return;
		}
		
	}

}
