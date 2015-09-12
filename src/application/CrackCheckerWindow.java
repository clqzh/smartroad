package application;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CrackCheckerWindow extends Application {

	private TableView tableInputImages = new TableView();
	private TableView tableResults = new TableView();
	private final ObservableList<RoadImage> data = FXCollections
			.observableArrayList();
	private final ObservableList<RoadImage> crackData = FXCollections
			.observableArrayList();
	List<File> images = new ArrayList<File>();
	List<File> crackResults = new ArrayList<File>();
	private long totalTime = 0 ;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("SmartRoad - Crack Checker");

		final FileChooser fileChooser = new FileChooser();

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_LEFT);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text scenetitle = new Text("SmartRoad - Crack Checker");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);

		Label filepath = new Label("File Path :");
		grid.add(filepath, 0, 1);

		TextField filepathField = new TextField();
		filepathField.setEditable(false);
		grid.add(filepathField, 1, 1, 43, 1);

		Button btnBrowse = new Button("Browse");
		HBox hbBtnBrowse = new HBox(10);
		hbBtnBrowse.setAlignment(Pos.TOP_RIGHT);
		hbBtnBrowse.getChildren().add(btnBrowse);
		grid.add(hbBtnBrowse, 1, 1, 50, 1);

		btnBrowse.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				List<File> list = fileChooser
						.showOpenMultipleDialog(primaryStage);
				if (list != null) {
					System.out.println("Getting Images");
					images.clear();
					for (File file : list) {
						getImages(file);
						filepathField.setText(file.getPath());
					}
				}
			}

		});

		Button btnGetImages = new Button("Get Images from the Path ");
		HBox hbBtnGetImages = new HBox(10);
		hbBtnGetImages.setAlignment(Pos.TOP_CENTER);
		hbBtnGetImages.getChildren().add(btnGetImages);
		grid.add(hbBtnGetImages, 0, 5, 20, 1);

		btnGetImages.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				setImages();
			}

		});

		Button btnCheckCrack = new Button("Check For Cracks ");
		HBox hbBtnCheckCrack = new HBox(10);
		hbBtnCheckCrack.setAlignment(Pos.TOP_LEFT);
		hbBtnCheckCrack.getChildren().add(btnCheckCrack);
		grid.add(hbBtnCheckCrack, 20, 5, 25, 1);

		btnCheckCrack.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				checkCracks();
			}

		});
		
		Button BtnGenerateReport = new Button("Generate Report");
		HBox hbBtnGenerateReport = new HBox(10);
		hbBtnGenerateReport.setAlignment(Pos.TOP_LEFT);
		hbBtnGenerateReport.getChildren().add(BtnGenerateReport);
		grid.add(hbBtnGenerateReport, 40, 5, 50, 2);

		BtnGenerateReport.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				generatePDF();
			}

		});

		// Creating the input images table
		tableInputImages.setEditable(true);
		TableColumn inputImages = new TableColumn("Input Images");
		tableInputImages.getColumns().addAll(inputImages);
		// Centering the Table column name.
		tableInputImages
				.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		inputImages
				.setCellValueFactory(new PropertyValueFactory<RoadImage, String>(
						"firstName"));
		// tableInputImages.setItems(data);

		tableResults.setEditable(true);
		TableColumn results = new TableColumn("Crack Images");
		tableResults.getColumns().addAll(results);
		tableResults.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		results.setCellValueFactory(new PropertyValueFactory<RoadImage, String>(
				"firstName"));
		

		grid.add(tableInputImages, 0, 6, 20, 5);
		grid.add(tableResults, 20, 6, 35, 5);
		
		

		Scene scene = new Scene(grid, 700, 600);
		primaryStage.setScene(scene);

		primaryStage.show();

	}

	private void getImages(File file) {

		images.add(file);

	}

	private void setImages() {
		System.out.println("Setting Images");
		if (!images.isEmpty()) {
			System.out.println("Total Number of Images : "+images.size());
			for (File file : images) {
				if (file.getName().endsWith(".jpg")) {
					data.add(new RoadImage(file.getName()));
				}
			}
			tableInputImages.setItems(data);

		}

	}

	private void checkCracks() {
		long startTime = System.currentTimeMillis();
		
		CrackCheck check = new CrackCheck();
		if (!images.isEmpty()) {
			crackResults = check.getResultFromANN(images, false);
			//crackResults = check.getResultsFromDBN(images, false);
		}
		if (!crackResults.isEmpty()) {
			System.out.println("Total Number Identified Cracks :"+crackResults.size());
			for (File file : crackResults) {
				crackData.add(new RoadImage(file.getName()));
			}
			tableResults.setItems(crackData);
		}
		long endTime   = System.currentTimeMillis();
		totalTime = endTime - startTime;
		System.out.println("Total time in millis "+totalTime);

	}
	
	private void generatePDF(){
		System.out.println("Generate PDF");
		PDFCreator report = new PDFCreator();
		if((totalTime != 0)&& (!images.isEmpty()) && (!crackResults.isEmpty())){
			report.createPDF(totalTime ,images,crackResults );
		}
		System.out.println("Generate PDF Done...");
		
		//Opening PDF file.
		String[] command = { "gnome-open", "Crack_Report.pdf" };
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
