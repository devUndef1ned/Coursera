package com.devnull;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private static final int INVALID_INDEX = -1;
    private static final int NOT_INITIALIZED_INDEX = -2;
    private static final int TOP_VIRTUAL_SITE_INDEX = 0;
    private static final int OPEN_SITE_STATUS = 2;

    private final int gridSize;
    private final int unionFindSize;
    private final int bottomVirtualSiteIndex;
    private int countOfOpenSites;
    private WeightedQuickUnionUF mUF;
    private WeightedQuickUnionUF mUFWithoutBottom;
    private boolean[] statusOfSites; // true isOpen, false isClosed.

    /**
     * Creates the system of blocked sites.
     * @param n the grid size.
     */
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();

        gridSize = n;
        unionFindSize = n * n + 2;
        bottomVirtualSiteIndex = unionFindSize - 1;
        countOfOpenSites = 0;
        mUF = new WeightedQuickUnionUF(unionFindSize);
        mUFWithoutBottom = new WeightedQuickUnionUF(unionFindSize -1);
        statusOfSites = new boolean[unionFindSize];

        for (int i = 0; i < unionFindSize; i++) {
                statusOfSites[i] = false;
        }
    }
    /**
     * Opens the site with col, row coordinates.
     * @param row Row number for the current site.
     * @param col Column number for the current site.
     */
    public void open(int row, int col) {
        ensureIndex(row);
        ensureIndex(col);
        if (!isOpen(row, col)) {
            countOfOpenSites += 1;

            int currentIndex = mapTableCoordinatesToArrayIndex(row, col);
            int leftSiteIndex = getLeftSiteIndex(row, col);
            int rightSiteIndex = getRightSiteIndex(row, col);
            int upSiteIndex = getUpSiteIndex(row, col);
            int downSiteIndex = getDownSiteIndex(row, col);

            statusOfSites[currentIndex] = true;

            if (upSiteIndex == TOP_VIRTUAL_SITE_INDEX || isOpen(upSiteIndex)) {
                mUF.union(currentIndex, upSiteIndex);
                mUFWithoutBottom.union(currentIndex, upSiteIndex);
            }
            if (downSiteIndex == bottomVirtualSiteIndex || isOpen(downSiteIndex)) {
                mUF.union(currentIndex, downSiteIndex);
                if (downSiteIndex != bottomVirtualSiteIndex)
                mUFWithoutBottom.union(currentIndex, downSiteIndex);
            }
            if (leftSiteIndex != INVALID_INDEX && isOpen(leftSiteIndex)) {
                mUF.union(currentIndex, leftSiteIndex);
                mUFWithoutBottom.union(currentIndex, leftSiteIndex);
            }
            if (rightSiteIndex != INVALID_INDEX && isOpen(rightSiteIndex)) {
                mUF.union(currentIndex, rightSiteIndex);
                mUFWithoutBottom.union(currentIndex, rightSiteIndex);
            }
        }
    }
    /**
     * Determines is the site with col, row coordinates is opened or not.
     * @param row Row number for the current site.
     * @param col Column number for the current site.
     * @return true if the site is opened.
     */
    public boolean isOpen(int row, int col) {
        ensureIndex(row);
        ensureIndex(col);

        int index = mapTableCoordinatesToArrayIndex(row, col);
        return statusOfSites[index];
    }
    private boolean isOpen(int index) {
        int row = getRowFromArrayIndex(index);
        int col = getColumnFromArrayIndex(index);
        return isOpen(row, col);
    }
    /**
     * Determines is the site with col, row coordinates is full or not.
     * @param row Row number for the current site.
     * @param col Column number for the current site.
     * @return true if the site is full.
     */
    public boolean isFull(int row, int col) {
        ensureIndex(row);
        ensureIndex(col);

        int index = mapTableCoordinatesToArrayIndex(row, col);
        return mUFWithoutBottom.connected(index, TOP_VIRTUAL_SITE_INDEX);
    }
    /**
     * Return the number of open sites of the system.
     * @return the number of open sites of the system.
     */
    public int numberOfOpenSites() {
        return countOfOpenSites;
    }

    /**
     * Defines the system percolates or not.
     * @return true if the system percolates.
     */
    public boolean percolates() {
        return mUF.connected(TOP_VIRTUAL_SITE_INDEX, bottomVirtualSiteIndex);
    }
    /**
     * Ensures that an index is a valid.
     * @throws IndexOutOfBoundsException if an index is invalid.
     * @param i an index which will be checked.
     */
    private void ensureIndex(int i) {
        if (i <= 0 || i > gridSize)
            throw new IndexOutOfBoundsException();
    }
    /**
     * Maps table coordinates of a site to its index of UnionFind data type instance {@link #mUF}.
     * @param row Row number for the current site.
     * @param col Column number for the current site.
     * @return an array index.
     */
    private int mapTableCoordinatesToArrayIndex(int row, int col) {
        return col + gridSize * (row - 1);
    }
    /**
     * Calculates row number for an index of UnionFind data type instance {@link #mUF}.
     * @param arrayIndex an index.
     * @return row number.
     */
    private int getRowFromArrayIndex(int arrayIndex) {
        return (arrayIndex - 1)/gridSize + 1;
    }
    /**
     * Calculates column number for an index of UnionFind data type instance {@link #mUF}.
     * @param arrayIndex an index.
     * @return column number.
     */
    private int getColumnFromArrayIndex(int arrayIndex) {
        return arrayIndex - gridSize * (getRowFromArrayIndex(arrayIndex) - 1);
    }
    /**
     * Calculates the index from UF for the nearest left site.
     * @param row Row number for the current site.
     * @param col Column number for the current site.
     * @return the index from UF for the nearest left site or {@link #INVALID_INDEX} if there is not such site.
     */
    private int getLeftSiteIndex(int row, int col) {
        int leftSiteIndex = NOT_INITIALIZED_INDEX;
        if (col == 1)
            leftSiteIndex = INVALID_INDEX;
        else
            leftSiteIndex = mapTableCoordinatesToArrayIndex(row, col - 1);

        return leftSiteIndex;
    }
    /**
     * Calculates the index from UF for the nearest right site.
     * @param row Row number for the current site.
     * @param col Column number for the current site.
     *
     * @return the index from UF for the nearest right site or {@link #INVALID_INDEX} if there is not such site.
     */
    private int getRightSiteIndex(int row, int col) {
        int rightSiteIndex = NOT_INITIALIZED_INDEX;
        if (col == gridSize)
            rightSiteIndex = INVALID_INDEX;
        else
            rightSiteIndex = mapTableCoordinatesToArrayIndex(row, col + 1);

        return rightSiteIndex;
    }
    /**
     * Calculates the index from UF for the nearest up site.
     * @param row Row number for the current site.
     * @param col Column number for the current site.
     *
     * @return the index from UF for the nearest right site or {@link #TOP_VIRTUAL_SITE_INDEX} if there is not such site.
     */
    private int getUpSiteIndex(int row, int col) {
        int upSiteIndex = NOT_INITIALIZED_INDEX;
        if (row == 1)
            upSiteIndex = TOP_VIRTUAL_SITE_INDEX;
        else
            upSiteIndex = mapTableCoordinatesToArrayIndex(row - 1, col);

        return upSiteIndex;
    }
    /**
     * Calculates the index from UF for the nearest up site.
     * @param row Row number for the current site.
     * @param col Column number for the current site.
     *
     * @return the index from UF for the nearest right site or {@link #bottomVirtualSiteIndex} if there is not such site.
     */
    private int getDownSiteIndex(int row, int col) {
        int downSiteIndex = NOT_INITIALIZED_INDEX;
        if (row == gridSize)
            downSiteIndex = bottomVirtualSiteIndex;
        else
            downSiteIndex = mapTableCoordinatesToArrayIndex(row + 1, col);

        return downSiteIndex;
    }
}
