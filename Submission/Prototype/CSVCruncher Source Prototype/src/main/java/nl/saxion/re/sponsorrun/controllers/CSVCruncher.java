package nl.saxion.re.sponsorrun.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import nl.saxion.re.sponsorrun.data.Data;
import nl.saxion.re.sponsorrun.data.Sponsor;
import nl.saxion.re.sponsorrun.util.TableViewHelper;
import nl.saxion.re.sponsorrun.util.WindowHelper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

public class CSVCruncher {

    @FXML
    private Label label1;
    @FXML
    private CheckBox unpaidCheck;
    @FXML
    private CheckBox paidCheck;
    @FXML
    private Label currentDate;
    @FXML
    public Button closeButton;
    @FXML
    private TableView table1;
    @FXML
    private ChoiceBox eventChoiceBox;
    @FXML
    private TextField searchInput;

    private boolean dataFetched = false;
    private boolean hasPaid = true;
    private boolean hasUnpaid = true;
    private String currentEvent = "ALL";
    private String currentSearchInput = "";
    private ArrayList<Sponsor> currentList = new ArrayList<>();
    private ArrayList<Sponsor> filteredList = new ArrayList<>();
    private boolean filterVisible = true;

    private static CSVCruncher instance;

    public CSVCruncher() {
        instance = this;
    }

    public static CSVCruncher getInstance() {
        return instance;
    }

    @FXML
    public void handleCloseButtonAction() {
        /*
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
        */

        WindowHelper.closeWindow(label1);
    }


    public void initialize() {

        // Set date at the bottom
        String formattedDate = LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
        currentDate.setText(formattedDate);

        // prepare the columns of the table
        String[] columns = {"Outstanding", "Paid", "Name", "Email", "Date", "Ref. Num.", "Event"};
        TableViewHelper.prepareTable(table1, columns);


        addInitialData();

        eventChoiceBox.getItems().addAll("ALL", "911 Memorial", "RunForYaLife");
        eventChoiceBox.setValue("ALL");

        toggleFilterOptions();
    }

    @FXML
    protected void onButtonOkClick() throws IOException {
        // find which item was selected
        int selectedIndex = table1.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
            // no item was selected, show an "error" to the user
            WindowHelper.showAlert("You have to select one item");
        } else {
            // find the selected politician in the data
            Sponsor selectedPolitician = filteredList.get(selectedIndex);
            // set it in the data as public "selectedPolitician", so the edit screen knows which data to edit
            Data.selectedPolitician = selectedPolitician;
            // open the other screen.
            WindowHelper.openWindow("detail-screen.fxml", Data.selectedPolitician.name, 600, 300);
        }
    }

    private void addInitialData() {
        for(int i = 0; i < 2; i++) {
            Sponsor s = Data.sponsors.get(i);
            currentList.add(s);
        }
        filterData(currentEvent, hasPaid, hasUnpaid, currentSearchInput);
    }

    @FXML
    private void fetchDataButtonAction() {
        if(!dataFetched) {
            currentList = Data.sponsors;

            dataFetched = true;
            filterData(currentEvent, hasPaid, hasUnpaid, currentSearchInput);
        }
    }

    @FXML
    private void clearData() {
        table1.getItems().clear();
    }

    private void displayList(ArrayList<Sponsor> sponsors) {
        clearData();
        for(Sponsor s: sponsors) {
            String[] rowValues = {s.outstanding, s.paid, s.name, s.email, s.date, s.refNum, s.event};
            table1.getItems().add(rowValues);
        }

        table1.setRowFactory(tv -> new TableRow<String[]>() {
            protected void updateItem(String[] item, boolean empty) {
                super.updateItem(item, empty);
                if(item == null || item == null)
                    setStyle("");
                else if(Integer.parseInt(item[0]) <= Integer.parseInt(item[1]))
                    setStyle("-fx-background-color: #A5C7A7;");
                else
                    setStyle("");
            }
        });
    }

    public void displayData() {
        filterData(currentEvent, hasPaid, hasUnpaid, currentSearchInput);
    }

    private void filterData(String event, boolean paid, boolean notPaid, String searchInput) {
        ArrayList<Sponsor> filteredArray = new ArrayList<>();

        for(Sponsor s: currentList) {
            int paidValue = Integer.parseInt(s.paid);
            int outstandingValue = Integer.parseInt(s.outstanding);
            String sponsorEvent = s.event;

            if(event.equals("ALL")) {
                if(paid && outstandingValue <= paidValue) {
                    filteredArray.add(s);
                } else if(notPaid && outstandingValue > paidValue) {
                    filteredArray.add(s);
                }
            } else if(event.equals(sponsorEvent)) {
                if(paid && outstandingValue <= paidValue) {
                    filteredArray.add(s);
                } else if(notPaid && outstandingValue > paidValue) {
                    filteredArray.add(s);
                }
            }

        }

        filterSearchData(filteredArray, searchInput.toLowerCase());

    }

    private void filterSearchData(ArrayList<Sponsor> unsearchedArray, String searchInput) {
        ArrayList<Sponsor> searchedArray = new ArrayList<>();

        for(Sponsor s: unsearchedArray) {
            String refNum = s.refNum.toLowerCase();
            String name = s.name.toLowerCase();
            String email = s.email.toLowerCase();

            if(refNum.contains(searchInput) || name.contains(searchInput) || email.contains(searchInput)) {
                searchedArray.add(s);
            }
        }

        displayList(searchedArray);
        filteredList = searchedArray;
    }

    @FXML
    public void paidCheckBox() {
        hasPaid = !hasPaid;
        filterData(currentEvent, hasPaid, hasUnpaid, currentSearchInput);
    }

    @FXML
    public void unpaidCheckBox() {
        hasUnpaid = !hasUnpaid;
        filterData(currentEvent, hasPaid, hasUnpaid, currentSearchInput);
    }

    @FXML
    public void changeToChoiceBox() {
        currentEvent = (String) eventChoiceBox.getValue();
        filterData(currentEvent, hasPaid, hasUnpaid, currentSearchInput);
    }

    @FXML
    public void changeSearchInputText() {
        currentSearchInput = searchInput.getText();
        filterData(currentEvent, hasPaid, hasUnpaid, currentSearchInput);
    }

    @FXML
    public void toggleFilterOptions() {
        filterVisible = !filterVisible;

        unpaidCheck.setVisible(filterVisible);
        paidCheck.setVisible(filterVisible);
        eventChoiceBox.setVisible(filterVisible);
    }
}
