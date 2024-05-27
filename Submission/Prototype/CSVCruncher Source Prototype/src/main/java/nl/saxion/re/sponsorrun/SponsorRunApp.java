package nl.saxion.re.sponsorrun;

import javafx.application.Application;
import javafx.stage.Stage;
import nl.saxion.re.sponsorrun.data.Data;
import nl.saxion.re.sponsorrun.util.WindowHelper;

import java.io.IOException;

public class SponsorRunApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        // read all data from disk
        Data.updateFromDisk();

        // start the main menu window
        WindowHelper.openWindow("CSVCruncher.fxml", "Sponsor Run App", 960, 540, stage);
    }

    public static void main(String[] args) {
        launch();
    }
}