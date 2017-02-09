package com.devnull;

/**
 * Created by oleg on 17.01.17.
 */
public class UF {

    int id[];
    int sz[];

    public UF(int N) {

        id = new int[N];
        sz = new int[N];

        for(int i = 0; i < N; i++){
            id[i] = i;
            sz[i] = 1;
        }
    }

    void union(int p, int q) {
        if (p == q || !isValidIndex(p) || !isValidIndex(q))
            return;

        int i = root(p);
        int j = root(q);

        if (i == j)
            return;

        if (sz[i] < sz[j]) {
            id[i] = j;
            sz[j] += sz[j];
        } else {
            id[j] = i;
            sz[i] += sz[j];
        }
    }
    boolean connected(int p, int q) {
        return root(p) == root(q);
    }
    int find(int p) {
        int proot = root(p);
        int result = proot;
        for (int i = 0; i < id.length; i++){
            int j = id[i];
            if (proot == root(j) && result < j)
                result = j;
        }
        return result;
    }
    private boolean isValidIndex(int i) {
        return !(i < 0 || i > id.length);
    }
    private int root(int i) {
        if (!isValidIndex(i))
            return -1;

        while( i != id[i]) {
            id[i] = id[id[i]];
            i = id[i];
        }

        return i;
    }
}
