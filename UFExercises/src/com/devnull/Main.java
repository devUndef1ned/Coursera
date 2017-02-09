package com.devnull;

import java.util.Date;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Date before = new Date();
        IntegerSequence seq = new IntegerSequence(100000000);
        Date after = new Date();
        System.out.println("Spent time for initialization is " + (after.getTime() - before.getTime()));
        int x = 469878;
        before = new Date();
        seq.remove(x);
        int successor = seq.findSuccessor(x);
        after = new Date();
        System.out.println("Spent time for calculation is " + (after.getTime() - before.getTime()));
        System.out.println("successor of " + x + " is " + successor);

    }
}
