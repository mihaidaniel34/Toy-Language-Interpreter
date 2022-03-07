package View.GUI.ProgramState;

import Controller.Controller;
import Model.Exceptions.InterpreterException;
import Model.PrgState;
import Model.adt.*;
import Model.stmt.IStmt;
import Model.value.IValue;
import Model.value.StringValue;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Pair;

import javax.swing.*;
import java.io.BufferedReader;
import java.net.URL;
import java.sql.Array;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class StateController implements Initializable {

    @FXML
    private TableView<Pair<Integer, IValue>> heapView;
    @FXML
    private TableColumn<Pair<Integer, IValue>, Integer> heapAddressColumn;
    @FXML
    private TableColumn<Pair<Integer, IValue>, String> heapValueColumn;


    @FXML
    private TableView<Pair<String, IValue>> symbolTableView;

    @FXML
    private TableColumn<Pair<String, IValue>, String> symbolTableVariableColumn;

    @FXML
    private TableColumn<Pair<String, IValue>, String> symbolTableValueColumn;


    @FXML
    private ListView<String> fileTableView;

    @FXML
    private ListView<String> exeStackView, outputView;
    @FXML
    private ListView<Integer> statesView;

    List<String> output;

    @FXML
    private Button runButton;

    private Controller controller;

    @FXML
    private void oneStep(ActionEvent e) {
        if (controller == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No program selected.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        if (getState() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Nothing to execute.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        try{
        controller.oneStepForAllPrg(controller.removeCompletedPrg(controller.getStates()));}
        catch(ExecutionException exception){
            Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
        update();
    }

    private PrgState getState() {
        if (controller.getStates().isEmpty())
            return null;
        int currentState = statesView.getSelectionModel().getSelectedIndex();
        if(currentState == -1)
            return controller.getStates().get(0);
        return controller.getStates().get(currentState);
    }

    private void update() {
        updateSymbolTable();
        updateHeap();
        updateFileTable();
        updateExeStack();
        updateOutput();
        updateStates();
    }

    private void updateStates() {
        List<PrgState> prgStates = controller.getStates();
        List<Integer> idList = prgStates.stream().map(PrgState::getId).collect(Collectors.toList());
        statesView.setItems(FXCollections.observableArrayList(idList));
        statesView.refresh();

    }

    private void updateExeStack() {
        PrgState state = getState();
        if (state != null) {
            List<String> exeStack = state.getStk().getStack().stream().map(IStmt::toString).collect(Collectors.toList());
            Collections.reverse(exeStack);
            exeStackView.setItems(FXCollections.observableArrayList(exeStack));
            exeStackView.refresh();
        }
    }

    private void updateOutput() {
        if (!controller.getStates().isEmpty())
            output = getState().getOutput().getList().stream().map(Object::toString).collect(Collectors.toList());
        outputView.setItems(FXCollections.observableArrayList(output));
        outputView.refresh();
    }

    private void updateFileTable() {
        ArrayList<String> files;
        if (controller.getStates().isEmpty())
            files = new ArrayList<>();
        else
            files = controller.getStates().get(0).getFileTable().getKeys().stream().map(Object::toString).collect(Collectors.toCollection(ArrayList::new));
        fileTableView.setItems(FXCollections.observableArrayList(files));
        fileTableView.refresh();

    }

    private void updateHeap() {
        IHeap heap;
        if (controller.getStates().isEmpty()) {
            heap = new Heap();
        } else
            heap = controller.getStates().get(0).getHeap();
        List<Pair<Integer, IValue>> heapList = new ArrayList<>();
        heap.getContent().forEach((key, value) -> heapList.add(new Pair<>(key, value)));
        heapView.setItems(FXCollections.observableArrayList(heapList));
    }

    private void updateSymbolTable() {
        List<Pair<String, IValue>> symbolTableList = new ArrayList<>();
        if (getState() != null)
            getState().getSymTable().getContent().forEach((key, value) -> symbolTableList.add(new Pair<>(key, value)));
        symbolTableView.setItems(FXCollections.observableArrayList(symbolTableList));
        symbolTableView.refresh();

    }

    public void setController(Controller controller) {
        this.controller = controller;
        update();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        output = new ArrayList<>();
        heapAddressColumn.setCellValueFactory(p->new SimpleIntegerProperty(p.getValue().getKey()).asObject());
        heapValueColumn.setCellValueFactory(p->new SimpleStringProperty(p.getValue().getValue().toString()));
        symbolTableVariableColumn.setCellValueFactory(p->new SimpleStringProperty(p.getValue().getKey()));
        symbolTableValueColumn.setCellValueFactory(p->new SimpleStringProperty(p.getValue().getValue().toString()));
        statesView.setOnMouseClicked(mouseEvent -> update());
    }

}
