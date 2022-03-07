package View;

import Controller.Controller;
import Model.Exceptions.InterpreterException;

import java.util.concurrent.ExecutionException;

public class RunExample extends Command {
    private Controller controller;
    public RunExample(String key, String description, Controller ctr){
        super(key, description);
        this.controller = ctr;
    }

    @Override
    public void execute() {
        try{
        controller.allStep();
    }
        catch(ExecutionException e){
            System.out.println(e.getMessage());
        }
    }
}
