package Model.adt;

import Model.Exceptions.InterpreterException;

import java.util.Stack;

public interface IStack<T> {

    T pop() throws InterpreterException;
    void push(T v);
    boolean isEmpty();
    Stack<T> getStack();
    T top() throws InterpreterException;
    String toString();
}

