module nl.saxion.re.sponsorrun {
    requires javafx.controls;
    requires javafx.fxml;
    requires opencsv;

    requires org.controlsfx.controls;

    opens nl.saxion.re.sponsorrun to javafx.fxml;
    exports nl.saxion.re.sponsorrun;
    exports nl.saxion.re.sponsorrun.controllers;
    opens nl.saxion.re.sponsorrun.controllers to javafx.fxml;
    exports nl.saxion.re.sponsorrun.util;
    opens nl.saxion.re.sponsorrun.util to javafx.fxml;
    exports nl.saxion.re.sponsorrun.data;
    opens nl.saxion.re.sponsorrun.data to javafx.fxml;
}