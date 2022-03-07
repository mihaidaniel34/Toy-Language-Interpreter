package View;

import Model.Exceptions.InterpreterException;

public class Interpreter {
    public static void main(String[] args) {
        TextMenu menu = Examples.addExamples();
        menu.show();
    }

}
