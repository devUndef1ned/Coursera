package com.devnull;

/**
 * Created by oleg on 24.01.17.
 */
public class IntegerSequence {

    private int array[];

    public IntegerSequence(final int N) {
        array = new int[N];

        for (int i = 0; i < N; i++) {
            array[i] = i;
        }
    }

    public void remove(int x) {
        if (x < 0 || x > array.length)
            return;

        int index = find(x);
        array[index] = 0;
    }

    public int find(int x) {
        if (x == 0)
            return 0;

        return findInPeriod(0, array.length - 1, x);
    }

    public int findSuccessor(int x){
        if (x == 0)
            return 0;
        int successorIndex;
        if (x == array[array.length - 1])
            successorIndex = array.length - 1;
        else
            successorIndex = find(x+1);

        return array[successorIndex];
    }

    private int findInPeriod(int firstIndex, int lastIndex, int x) {
        if (x == array[firstIndex])
            return firstIndex;
        else if (x == array[lastIndex])
            return lastIndex;
        else {
            int middleIndex = firstIndex + (lastIndex - firstIndex)/2;
            int middleInt = array[middleIndex];
            if (x == middleInt)
                return middleIndex;
            else if (x < middleInt)
                return findInPeriod(firstIndex, middleIndex - 1, x);
            else
                return findInPeriod(middleIndex + 1, lastIndex, x);
        }
    }
}
