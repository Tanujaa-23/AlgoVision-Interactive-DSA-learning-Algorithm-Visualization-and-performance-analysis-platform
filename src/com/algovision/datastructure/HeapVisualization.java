package com.algovision.datastructure;

import java.util.ArrayList;
import java.util.List;

public class HeapVisualization {

    private final List<Integer> heap = new ArrayList<>();

    public String insert(int val) {
        heap.add(val);
        heapifyUp(heap.size() - 1);
        return "Inserted value " + val + " into Max Heap and restored heap property.";
    }

    public String extractMax() {
        if (heap.isEmpty()) return "Heap Underflow! Heap is empty.";
        int max = heap.get(0);
        int last = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, last);
            heapifyDown(0);
        }
        return "Extracted maximum value " + max + " from Max Heap.";
    }

    private void heapifyUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (heap.get(i) > heap.get(parent)) {
                int temp = heap.get(i);
                heap.set(i, heap.get(parent));
                heap.set(parent, temp);
                i = parent;
            } else break;
        }
    }

    private void heapifyDown(int i) {
        int max = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < heap.size() && heap.get(left) > heap.get(max)) max = left;
        if (right < heap.size() && heap.get(right) > heap.get(max)) max = right;

        if (max != i) {
            int temp = heap.get(i);
            heap.set(i, heap.get(max));
            heap.set(max, temp);
            heapifyDown(max);
        }
    }

    public List<Integer> getElements() {
        return new ArrayList<>(heap);
    }
}
