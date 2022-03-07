package View.GUI.ProgramList;

import Controller.Controller;
import Model.Exceptions.InterpreterException;
import Model.adt.IDict;
import Model.adt.MyDict;
import Model.stmt.IStmt;
import Model.types.IType;
import View.Examples;
import View.GUI.ProgramState.StateController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class ListController implements Initializable {
    private StateController stateController;

    public void setStateController(StateController stateController){
        this.stateController = stateController;
    }

    @FXML
    private ListView<String> myListView;

    @FXML
    private Button loadButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        myListView.getItems().addAll(Examples.getStatements().stream().map(Object::toString).collect(Collectors.toList()));
    }

    public void loadProgram(ActionEvent e){
        int index = myListView.getSelectionModel().getSelectedIndex();
        if(index < 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No program selected");
            alert.showAndWait();
            return;
        }
        try {
            IStmt statement = Examples.getStatements().get(index);
            IDict<String, IType> typeEnv = new MyDict<>();
            statement.typeCheck(typeEnv);
            stateController.setController(Examples.createController(statement));
        }
        catch(InterpreterException ie){
            Alert alert = new Alert(Alert.AlertType.ERROR, ie.getMessage());
            alert.showAndWait();
        }
    }

}
