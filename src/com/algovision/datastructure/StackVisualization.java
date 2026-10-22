package com.algovision.datastructure;

import java.util.ArrayList;
import java.util.List;

public class StackVisualization {

    private final List<Integer> stack = new ArrayList<>();
    private final int capacity = 8;

    public String push(int val) {
        if (stack.size() >= capacity) {
            return "Stack Overflow! Maximum capacity is " + capacity;
        }
        stack.add(val);
        return "Pushed value " + val + " onto Stack. Top is now at index " + (stack.size() - 1);
    }

    public String pop() {
        if (stack.isEmpty()) {
            return "Stack Underflow! Stack is empty.";
        }
        int val = stack.remove(stack.size() - 1);
        return "Popped element " + val + " from Stack.";
    }

    public String peek() {
        if (stack.isEmpty()) return "Stack is empty.";
        return "Top element is " + stack.get(stack.size() - 1);
    }

    public List<Integer> getElements() {
        return new ArrayList<>(stack);
    }
}
