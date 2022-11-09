package com.example.employees;

import java.util.Comparator;

public class SortByProduct implements Comparator<Mask> {
    @Override
    public int compare(Mask message, Mask t1) {
        return message.getProduct().compareTo(t1.getProduct());
    }
}
