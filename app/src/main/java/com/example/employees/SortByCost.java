package com.example.employees;

import java.util.Comparator;

public class SortByCost implements Comparator<Mask> {
    @Override
    public int compare(Mask message, Mask t1) {
        return message.getCost().compareTo(t1.getCost());
    }
}
