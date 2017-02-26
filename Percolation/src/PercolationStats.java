package com.devnull;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONF_CONST = 1.96;

    private int mPercolationGridSize;
    private int mTrialsCount;
    private double[] mTrialsResults;
    private double mSampleMean = 0;
    private double mStdDev = 0;
    private double lowConfidence = 0;
    private double highConfidence = 0;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();

        mPercolationGridSize = n;
        mTrialsCount = trials;
        mTrialsResults = new double[trials];

        for (int i = 0; i < mTrialsCount; i++) {
            Percolation mPercolation = new Percolation(mPercolationGridSize);
            while (true) {
                int rndRow = StdRandom.uniform(1, mPercolationGridSize + 1);
                int rndCol = StdRandom.uniform(1, mPercolationGridSize + 1);
                if (!mPercolation.isOpen(rndRow, rndCol)) {
                    mPercolation.open(rndRow, rndCol);
                    if (mPercolation.percolates()) {
                        System.out.println("Number of open sites is " + mPercolation.numberOfOpenSites());
                        mTrialsResults[i] = ((double) mPercolation.numberOfOpenSites()) /
                                (mPercolationGridSize * mPercolationGridSize);
                        break;
                    }
                }
            }
        }

        // System.out.println("Trials results: ");
        // for(double res: mTrialsResults)
        //     System.out.println(res);
    }
    // sample mean of percolation threshold
    public double mean() {
        if (mSampleMean == 0)
            mSampleMean = StdStats.mean(mTrialsResults);
        return mSampleMean;
    }
    // sample standard deviation of percolation threshold
    public double stddev() {
        if (mStdDev == 0)
            mStdDev = StdStats.stddev(mTrialsResults);

        return mStdDev;
    }
    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        if (lowConfidence != 0)
            return lowConfidence;

        lowConfidence = (mean() - (CONF_CONST * stddev())/Math.sqrt(mTrialsCount));
        return lowConfidence;
    }
    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        if (highConfidence != 0)
            return highConfidence;

        highConfidence = (mean() - (CONF_CONST * stddev())/Math.sqrt(mTrialsCount));
        return highConfidence;
    }


    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(n, trials);

        System.out.println("% java PercolationStats " + stats.mPercolationGridSize + " " + stats.mTrialsCount);
        System.out.println("mean                    = " + stats.mean());
        System.out.println("stddev                  = " + stats.stddev());
        System.out.println("95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
        /* Percolation p = new Percolation(57);

        System.out.println("isOpen(1,1) " + p.isOpen(1,1));
        System.out.println("percolates? " + p.percolates());
        System.out.println("numberOfOpenSites " + p.numberOfOpenSites());
        System.out.println("isFull(1,1) " + p.isFull(1,1));

        p.open(1, 1);
        System.out.println("opening the site(1, 1)");

        System.out.println("isOpen(1,1) " + p.isOpen(1,1));
        System.out.println("percolates? " + p.percolates());
        System.out.println("numberOfOpenSites " + p.numberOfOpenSites());
        System.out.println("isFull(1,1) " + p.isFull(1,1)); */
    }
}
