package com.Query.Columnar.Index;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public final class ColumnTreeIndex<E>
{
    private ListIterator<E> subTreeIterator;

    public ColumnTreeIndex(List<E> lists) {
        subTreeIterator = lists.listIterator();
    }

    public Object previous() {
        return subTreeIterator.previous();
    }

    public Object next() {
        return subTreeIterator.next();
    }

    public boolean hasNext() {
        return subTreeIterator.hasNext();
    }

    public boolean hasPrevious() {
        return subTreeIterator.hasPrevious();
    }

    public int nextIndex() {
        return subTreeIterator.nextIndex();
    }

    public int previousIndex() {
        return subTreeIterator.previousIndex();
    }
}