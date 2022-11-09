package com.example.employees;

import java.util.Comparator;

public class SortByQuantity implements Comparator<Mask> {
    @Override
    public int compare(Mask message, Mask t1) {
        return message.getQuantity().compareTo(t1.getQuantity());
    }
}
