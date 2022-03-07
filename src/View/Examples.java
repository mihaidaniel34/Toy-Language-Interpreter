package View;

import Controller.Controller;
import Model.Exceptions.InterpreterException;
import Model.PrgState;
import Model.adt.*;
import Model.exp.*;
import Model.stmt.*;
import Model.types.*;
import Model.value.BoolValue;
import Model.value.IntValue;
import Model.value.StringValue;
import Repo.IRepo;
import Repo.Repo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Examples {
    static int programNumber = 1;

    public static IStmt compound(IStmt... statements){
        if(statements.length == 1)
            return statements[0];
        return new CompStmt(statements[0], compound(Arrays.copyOfRange(statements, 1, statements.length)));
    }
    public static String getFilePath(int progNum) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please input the log file for the program number " + progNum + " (default log" + progNum + ".txt)");
        String filePath = scan.nextLine();
        if (filePath.equals(""))
            filePath = "log" + progNum + ".txt";
        return filePath;
    }

    public static Controller createController(IStmt statement) throws InterpreterException {
        PrgState state = new PrgState(new MyStack<>(), new MyDict<>(), new MyList<>(), new MyDict<>(), new Heap(), statement);
        IRepo repo = new Repo("log" + (programNumber++) + ".txt");
        repo.addPrg(state);
        IDict<String, IType> typeEnv = new MyDict<>();
        statement.typeCheck(typeEnv);
        return new Controller(repo);
    }

    private static void addProgram(TextMenu textMenu, IStmt stmt) {
        try {
            textMenu.addCommand(new RunExample(Integer.toString(programNumber), stmt.toString(), createController(stmt)));
        } catch (InterpreterException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<IStmt> getStatements() {
        List<IStmt> statements = new ArrayList<>();
        IStmt stmt1 = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(2))),
                        new PrintStmt(new VarExp("v"))));
        statements.add(stmt1);

        IStmt stmt2 = new CompStmt(new VarDeclStmt("a", new IntType()), new CompStmt(new VarDeclStmt("b", new IntType()),
                new CompStmt(new AssignStmt("a", new ArithExp('+', new ValueExp(new IntValue(2)), new ArithExp('*',
                        new ValueExp(new IntValue(3)), new ValueExp(new IntValue(5))))), new CompStmt(
                        new AssignStmt("b", new ArithExp('+', new VarExp("a"), new ValueExp(new IntValue(1)))),
                        new PrintStmt(new VarExp("b"))))));
        statements.add(stmt2);

        IStmt stmt3 = new CompStmt(new VarDeclStmt("a", new BoolType()), new CompStmt(new VarDeclStmt("v",
                new IntType()), new CompStmt(new AssignStmt("a", new ValueExp(new BoolValue(true))),
                new CompStmt(new IfStmt(new VarExp("a"), new AssignStmt("v", new ValueExp(new IntValue(2))),
                        new AssignStmt("v", new ValueExp(new IntValue(3)))), new PrintStmt(new
                        VarExp("v"))))));
        statements.add(stmt3);

        IStmt stmt4 = new CompStmt(new VarDeclStmt("varf", new StringType()),
                new CompStmt(new AssignStmt("varf", new ValueExp(new StringValue("test.in"))),
                        new CompStmt(new openRFile(new VarExp("varf")),
                                new CompStmt(new VarDeclStmt("varc", new IntType()),
                                        new CompStmt(new readFile(new VarExp("varf"), "varc"),
                                                new CompStmt(new PrintStmt(new VarExp("varc")),
                                                        new CompStmt(new readFile(new VarExp("varf"), "varc"),
                                                                new CompStmt(new PrintStmt(new VarExp("varc")), new closeFile(new VarExp("varf"))))))))));
        statements.add(stmt4);

        IStmt stmt5 = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(4))),
                        new CompStmt(new WhileStmt(new RelExp(">", new VarExp("v"), new ValueExp(new IntValue(0))),
                                new CompStmt(new PrintStmt(new VarExp("v")),
                                        new AssignStmt("v", new ArithExp('-', new VarExp("v"),
                                                new ValueExp(new IntValue(1)))))), new PrintStmt(new VarExp("v")))));
        statements.add(stmt5);

        IStmt stmt6 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new New("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new New("a", new VarExp("v")),
                                        new CompStmt(new New("v", new ValueExp(new IntValue(30))),
                                                new PrintStmt(new readHeap(new readHeap(new VarExp("a")))))))));
        statements.add(stmt6);

        IStmt stmt7 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new New("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new New("v", new ValueExp(new IntValue(30))),
                                new PrintStmt(new readHeap(new VarExp("v"))))));
        statements.add(stmt7);

        IStmt stmt8 = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new VarDeclStmt("a", new RefType(new IntType())),
                        new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(10))),
                                new CompStmt(new New("a", new ValueExp(new IntValue(22))),
                                        new CompStmt(new Fork(new CompStmt(new writeHeap("a", new ValueExp(new IntValue(30))),
                                                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(32))),
                                                        new CompStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new readHeap(new VarExp("a"))))))),
                                                new CompStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new readHeap(new VarExp("a")))))))));
        statements.add(stmt8);

        IStmt repeatStmt = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new VarDeclStmt("x", new IntType()),
                        new CompStmt(new VarDeclStmt("y", new IntType()),
                                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(0))),
                                        new CompStmt(new RepeatStmt(
                                                new CompStmt(new Fork(
                                                        new CompStmt(new PrintStmt(new VarExp("v")), new AssignStmt("v", new ArithExp('-', new VarExp("v"), new ValueExp(new IntValue(1)))))),
                                                        new AssignStmt("v", new ArithExp('+', new VarExp("v"), new ValueExp(new IntValue(1))))), new RelExp("==", new VarExp("v"), new ValueExp(new IntValue(3)))),
                                                new CompStmt(new AssignStmt("x", new ValueExp(new IntValue(1))),
                                                        new CompStmt(new NopStmt(),
                                                                new CompStmt(new AssignStmt("y", new ValueExp(new IntValue(3))),
                                                                        new CompStmt(new NopStmt(),
                                                                                new PrintStmt(new ArithExp('*', new VarExp("v"), new ValueExp(new IntValue(10)))))))))))));
        statements.add(repeatStmt);

        IStmt forStmt = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new ForStmt("v", new ValueExp(new IntValue(0)), new ValueExp(new IntValue(3)),new ArithExp('+', new VarExp("v"), new ValueExp(new IntValue(1))),
                                new Fork(new CompStmt(new PrintStmt(new VarExp("v")), new AssignStmt("v", new ArithExp('+', new VarExp("v"), new ValueExp(new IntValue(1))))))),
                                new PrintStmt(new ArithExp('*', new VarExp("v"), new ValueExp(new IntValue(10)))))));

        statements.add(forStmt);

        IStmt sleepStmt = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(0))),
                        new CompStmt(new WhileStmt(new RelExp("<", new VarExp("v"), new ValueExp(new IntValue(3))),
                                new CompStmt(new Fork(new CompStmt(new PrintStmt(new VarExp("v")), new AssignStmt("v", new ArithExp('+', new VarExp("v"), new ValueExp(new IntValue(1)))))),
                                        new AssignStmt("v", new ArithExp('+', new VarExp("v"), new ValueExp(new IntValue(1)))))),
                                new CompStmt(new Sleep(5), new PrintStmt(new ArithExp('*', new VarExp("v"), new ValueExp(new IntValue(10))))))));
        statements.add(sleepStmt);

        IStmt switchStmt = new CompStmt(new VarDeclStmt("a", new IntType()),
                new CompStmt(new VarDeclStmt("b", new IntType()),
                        new CompStmt(new VarDeclStmt("c", new IntType()),
                                new CompStmt(new AssignStmt("a", new ValueExp(new IntValue(1))),
                                        new CompStmt(new AssignStmt("b", new ValueExp(new IntValue(2))),
                                                new CompStmt(new AssignStmt("c", new ValueExp(new IntValue(5))),
                                                        new CompStmt(new SwitchStmt(new ArithExp('*', new VarExp("a"), new ValueExp(new IntValue(10))),
                                                                new ArithExp('*', new VarExp("b"), new VarExp("c")),
                                                                        new CompStmt(new PrintStmt(new VarExp("a")), new PrintStmt(new VarExp("b"))),
                                                                new ValueExp(new IntValue(10)), new CompStmt(new PrintStmt(new ValueExp(new IntValue(100))), new PrintStmt(new ValueExp(new IntValue(200)))),
                                                                new PrintStmt(new ValueExp(new IntValue(300)))),
                                                                new PrintStmt(new ValueExp(new IntValue(300))))))))));
        statements.add(switchStmt);


        IStmt conditionalStmt = new CompStmt(new VarDeclStmt("a", new RefType(new IntType())),
                new CompStmt(new VarDeclStmt("b", new RefType(new IntType())),
                        new CompStmt(new VarDeclStmt("v", new IntType()),
                                new CompStmt(new New("a", new ValueExp(new IntValue(0))),
                                        new CompStmt(new New("b", new ValueExp(new IntValue(0))),
                                                new CompStmt(new writeHeap("a", new ValueExp(new IntValue(1))),
                                                        new CompStmt(new writeHeap("b", new ValueExp(new IntValue(2))),
                                                                new CompStmt(new ConditionalStmt("v", new RelExp("<", new readHeap(new VarExp("a")), new readHeap(new VarExp("b"))), new ValueExp(new IntValue(100)), new ValueExp(new IntValue(200))),
                                                                        new CompStmt(new PrintStmt(new VarExp("v")),
                                                                                new CompStmt(new ConditionalStmt("v", new RelExp(">", new ArithExp('-', new readHeap(new VarExp("b")), new ValueExp(new IntValue(2))), new readHeap(new VarExp("a"))),
                                                                                        new ValueExp(new IntValue(100)), new ValueExp(new IntValue(200))),
                                                                                        new PrintStmt(new VarExp("v"))))))))))));
        statements.add(conditionalStmt);

        IStmt mulStmt = new CompStmt(new VarDeclStmt("v1", new IntType()),
                new CompStmt(new VarDeclStmt("v2", new IntType()), new CompStmt(
                        new AssignStmt("v1", new ValueExp(new IntValue(2))),
                                new CompStmt(new AssignStmt("v2", new ValueExp(new IntValue(3))),
                                        new PrintStmt(new MulExp(new VarExp("v1"), new VarExp("v2")))))));
        statements.add(mulStmt);

        IStmt waitStmt = new CompStmt(new VarDeclStmt("v", new IntType()), new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(20))),
                new CompStmt(new WaitStmt(10), new PrintStmt(new ArithExp('*', new VarExp("v"), new ValueExp(new IntValue(10)))))));
        statements.add(waitStmt);
        return statements;
    }

    public static TextMenu addExamples(){

        TextMenu menu = new TextMenu();

        menu.addCommand(new ExitCommand("0", "exit"));
        List<IStmt> statements = getStatements();
        statements.forEach(stmt -> addProgram(menu, stmt));
        return menu;
    }
}
