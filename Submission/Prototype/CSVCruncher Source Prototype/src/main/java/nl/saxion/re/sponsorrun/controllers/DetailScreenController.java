package nl.saxion.re.sponsorrun.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import nl.saxion.re.sponsorrun.data.Data;
import nl.saxion.re.sponsorrun.util.WindowHelper;

public class DetailScreenController {

    @FXML
    private TextField txtOutstanding;
    @FXML
    private TextField txtPaid;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtDate;
    @FXML
    private TextField txtRefNum;
    @FXML
    private TextField txtEvent;

    @FXML
    private Label outstandingErr;
    @FXML
    private Label paidErr;

    @FXML
    protected void initialize() {
        txtOutstanding.setText(Data.selectedPolitician.outstanding);
        txtPaid.setText(Data.selectedPolitician.paid);
        txtName.setText(Data.selectedPolitician.name);
        txtEmail.setText(Data.selectedPolitician.email);
        txtDate.setText(Data.selectedPolitician.date);
        txtRefNum.setText(Data.selectedPolitician.refNum);
        txtEvent.setText(Data.selectedPolitician.event);
    }
    @FXML
    protected void onCancelClick() {
        WindowHelper.closeWindow(txtName);
    }

    @FXML
    protected void onOkClick() {
        boolean textCorrect = true;
        // update fields of the selected politician

        if(txtOutstanding.getText().isBlank()) {
            textCorrect = false;
            outstandingErr.setText("Outstanding amount cannot be empty");
        } else if(!onlyDigits(txtOutstanding.getText())) {
            textCorrect = false;
            outstandingErr.setText("Outstanding amount needs to be a number");
        } else if(Integer.parseInt(txtOutstanding.getText()) < 0) {
            textCorrect = false;
            outstandingErr.setText("Outstanding amount cannot be less than 0");
        } else if(Integer.parseInt(txtOutstanding.getText()) == 0) {
            textCorrect = false;
            outstandingErr.setText("Outstanding amount cannot be 0");
        } else {
            Data.selectedPolitician.outstanding = txtOutstanding.getText();
            outstandingErr.setText("");
        }

        if(txtPaid.getText().isBlank()) {
            textCorrect = false;
            paidErr.setText("Paid amount cannot be empty");
        } else if(!onlyDigits(txtPaid.getText())) {
            textCorrect = false;
            paidErr.setText("Paid amount needs to be a number");
        } else if(Integer.parseInt(txtPaid.getText()) < 0) {
            textCorrect = false;
            paidErr.setText("Paid amount cannot be less than 0");
        } else {
            Data.selectedPolitician.paid = txtPaid.getText();
            paidErr.setText("");
        }

        if(textCorrect) {
            WindowHelper.closeWindow(txtName);
            CSVCruncher.getInstance().displayData();
        }

    }

    private boolean onlyDigits(String s) {
        char[] characters = s.toCharArray();

        for(int i = 1; i < characters.length; i++)
            if(!Character.isDigit(characters[i]))
                return false;

        if(characters[0] != '-' && !Character.isDigit(characters[0]))
            return false;

        return true;
    }
}
