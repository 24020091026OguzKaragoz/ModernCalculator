package org.t3tracon.moderncalculator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class HelloController {

    //importing txtFields from the .fxml file
    @FXML
    private TextField txtFieldHistory;
    @FXML
    private TextField txtFieldCurrent;

    //CREATING VARIABLES AND CALCULATOR ENGINE
    private String historyData = "";
    private String currentData = "0";
    private boolean isNewInput = true;
    private ScriptEngine engine;

    //WE USE INITIALIZE INSTEAD OF A CONSTRUCTOR BECAUSE FLOW OF THE PROGRAM REQUIRES INSTANT IMPORT OF FXML VALUES
    @FXML
    public void initialize() {
        ScriptEngineManager mgr = new ScriptEngineManager();
        this.engine = mgr.getEngineByName("nashorn");

        if (this.engine == null) {
            System.err.println("Still no engine! Check your dependencies.");
        }
    }

    //CATCHES EVENT, TRANSLATES IT AS A STRING AND SENDS IT TO handleInput METHOD
    @FXML
    private void onButtonClick(ActionEvent event) {
        String value = ((Button) event.getSource()).getText();
        handleInput(value);
    }

    //HANDLES AND CATEGORIES THE ENTERED VALUE
    public void handleInput(String value) {
        if (value.matches("[0-9]") || value.equals(".")) {
            processNumber(value);
        } else if (isOperator(value)) {
            processOperator(value);
        } else if (value.equals("=")) {
            calculateResult(value);
        } else if (value.equals("AC")) {
            clearAll();
        }
        updateDisplay();
    }

    //PROCESSES NUMBER
    private void processNumber(String number) {
        if (isNewInput || currentData.equals("0")) {
            currentData = number;
            isNewInput = false;
        } else {
            currentData += number;
        }
    }

    //PROCESSES OPERATOR
    private void processOperator(String op) {
        historyData += currentData + " " + op + " ";
        isNewInput = true;
    }

    //THE CORE OF CALCULATION
    private void calculateResult(String op) {
        try {
            String mathExpression = (historyData + currentData)
                    .replace("x", "*")
                    .replace("÷", "/");

            Object result = engine.eval(mathExpression);

            historyData = historyData + currentData + " =";
            currentData = result.toString();
            isNewInput = true;
        } catch (ScriptException e) {
            currentData = "Error1";
            historyData = "";
            isNewInput = true;
        } catch (NullPointerException e) {
            currentData = "Error2";
            historyData = "";
            isNewInput = true;
        }
    }

    //UPDATES DISPLAY AS CURRENT VALUES OF historyData AND currentData
    private void updateDisplay() {
        txtFieldHistory.setText(historyData);
        txtFieldCurrent.setText(currentData);
    }

    //FOR FUTURE UPDATE THAT ADDS SUPPORT FOR PROCESSING DECIMAL
    private void processDecimal() {
        if (isNewInput) {
            currentData = "0.";
            isNewInput = false;
        } else if (!currentData.contains(".")) {
            currentData += ".";
        }
    }

    //FOR FUTURE UPDATE THAT ADDS SUPPORT FOR BACKSPACE
    private void processBackspace() {
        if (currentData.isEmpty()) {
            currentData = currentData.substring(0, currentData.length() - 1);
        } else {
            currentData = "0";
        }
    }

    //RESETS TEXT AREAS TO THE DEFAULT VALUES
    private void clearAll() {
        historyData = "";
        currentData = "0";
        isNewInput = true;
    }

    //RETURNS TRUE IF ENTERED VALUE IS AN OPERATOR
    private boolean isOperator(String s) {
        return s.equals("+") || s.equals("-") || s.equals("x") || s.equals("÷");
    }

    //GETTER
    public String gethistoryData() {
        return historyData;
    }

    //GETTER
    public String getcurrentData() {
        return currentData;
    }
}
