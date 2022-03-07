package Model.adt;

import Model.Exceptions.InterpreterException;
import Model.Exceptions.StackException;

import java.util.Stack;

public class MyStack<T> implements IStack<T> {
    Stack<T> stack;
    public MyStack(){
        stack = new Stack<T>();
    }

    @Override
    public T pop() throws InterpreterException {
        if(stack.isEmpty())
            throw new StackException("The stack is empty, nothing to pop.");
        return stack.pop();

    }

    @Override
    public void push(T v) {
        stack.push(v);
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public Stack<T> getStack() {
        return stack;
    }

    @Override
    public T top() throws InterpreterException {
        if(stack.isEmpty())
            throw new StackException("The stack is empty, nothing on top.");
        return stack.peek();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(")");
        Stack<T> stack_copy = (Stack<T>) stack.clone();
        if(stack_copy.isEmpty()){
            return "Stack: (" + stringBuilder;
        }
        stringBuilder = new StringBuilder(stack_copy.pop().toString()).append(stringBuilder);
        while(!stack_copy.isEmpty())
            stringBuilder = new StringBuilder(stack_copy.pop().toString()).append(",").append(stringBuilder);
        return "Stack(" + stringBuilder;
    }
}
