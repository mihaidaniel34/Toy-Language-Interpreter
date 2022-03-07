package View.GUI;

import View.GUI.ProgramList.ListController;
import View.GUI.ProgramState.StateController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Gui extends Application {
    public static void main(String [] args){
     launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader listLoader = new FXMLLoader(getClass().getResource("ProgramList/List.fxml"));
        Scene listScene = new Scene(listLoader.load());
        Stage listStage = new Stage();
        ListController listController = listLoader.getController();
        listStage.setTitle("Select program");
        listStage.setScene(listScene);
        listStage.show();


        FXMLLoader stateLoader = new FXMLLoader(getClass().getResource("ProgramState/State.fxml"));
        Scene stateScene = new Scene(stateLoader.load());
        Stage stateStage = new Stage();
        StateController stateController = stateLoader.getController();
        listController.setStateController(stateController);
        stateStage.setTitle("Program");
        stateStage.setScene(stateScene);
        stateStage.show();
    }
}
