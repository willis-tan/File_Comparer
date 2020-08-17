/*
 * NAME: Willis Tan
 * PID: A14522499
 */

/**
 * Title: HashTable.java
 * Description: File that can create a hash table.
 *
 * @author Willis Tan
 * @since March 3, 2020
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;
import static java.lang.Math.abs;

/**
 * A class that implements a hash table and its associated methods
 */
public class HashTable implements IHashTable {
    private static final double MAX_LOAD_FACTOR = 2.0 / 3.0;
    private static final int EXPAND_FACTOR = 2;
    private static final DecimalFormat TO_TWO_PLACES = new DecimalFormat("#.##");
    private static final int LEFT_SHIFT = 5;
    private static final int RIGHT_SHIFT = 27;

    private LinkedList<String>[] array;//Array that stores linkedlists
    private int numElem;//Number of element stored in the hash table
    private int expand;//Times that the table is expanded
    private int collision;//Times of collisions occurs
    private String statsFileName;
    private boolean printStats = false;
    private int longestChain;

    /**
     * Constructor for hash table
     * @param size size of of the hash table
     */
    @SuppressWarnings("unchecked")
    public HashTable(int size) {
        this.array = new LinkedList[size];  // the actual table
        this.numElem = 0;
        this.expand = 0;
        this.collision = 0;
        this.longestChain = 0;
    }

    /**
     * Constructor for hash table with the name of the file that stats will be printed to
     * @param size size of the table
     * @param fileName name of the stats file
     */
    @SuppressWarnings( "unchecked" )
    public HashTable(int size, String fileName){
        this.array = new LinkedList[size];  // the actual table
        this.numElem = 0;
        this.expand = 0;
        this.collision = 0;
        this.statsFileName = fileName;
        this.printStats = true;
        this.longestChain = 0;
    }

    /**
     * Insert the string value into the hash table
     * @param value value to insert
     * @return true if the value was inserted, false if the value was already
     * present
     */
    @Override
    public boolean insert(String value) {
        if (value == null) {
            throw new NullPointerException();
        }

        if (loadFactor() > MAX_LOAD_FACTOR) {
            rehash();  // double the table size and rehashes if the load factor is too large
        }

        if (array[hashString(value)] == null) {
            // if the bucket is empty, add the element as normal
            array[hashString(value)] = new LinkedList();
            array[hashString(value)].add(value);
            this.numElem++;
            if (array[hashString(value)].size() >= this.longestChain) {
                this.longestChain = array[hashString(value)].size();
            }
            return true;
        } else {
            // if the bucket is not empty, check collisions and if value is already inserted
            if (!lookup(value)) {
                this.collision++;
                array[hashString(value)].add(value);
                this.numElem++;
                // update length of longest chain
                if (array[hashString(value)].size() >= this.longestChain) {
                    this.longestChain = array[hashString(value)].size();
                }
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Delete the given value from the hash table
     * @param value value to delete
     * @return true if the value was deleted, false if the value was not found
     */
    @Override
    public boolean delete(String value) {
        if (value == null) {
            throw new NullPointerException();
        }
        if (!lookup(value)) {
            return false;
        }
        int index = hashString(value);
        LinkedList bucketList = this.array[index];
        bucketList.remove(value);
        this.numElem--;
        return true;
    }

    /**
     * Check if the given value is present in the hash table
     * @param value value to look up
     * @return true if the value was found, false if the value was not found
     */
    @Override
    public boolean lookup(String value) {
        if (value == null) {
            throw new NullPointerException();
        }

        int index = hashString(value);
        LinkedList list = this.array[index];

        // return false if the bucket is empty
        if (list == null) {
            return false;
        }

        return list.contains(value);
    }

    /**
     * Print the contents of the hash table to stdout.
     */
    @Override
    public void printTable() {
        for (int i = 0; i < this.array.length; i++) {
            if (this.array[i] != null) {
                String arrayString = this.array[i].toString();
                // substring to remove brackets
                System.out.print(i + ": " + arrayString.substring(1, arrayString.length() - 1));
            } else {
                System.out.print(i + ":");
            }
            System.out.println();
        }
    }

    /**
     * Returns the number of elements stored in the table
     */
    @Override
    public int getSize() {
        return this.numElem;
    }


    /**
     * Converts the input string into an int
     * @param value The string that will be added to table
     * @return The hash value of the input string
     */
    private int hashString(String value) {
        int hashValue = 0;
        for (int i = 0; i < value.length(); i++) {
            int leftShiftValue = hashValue << LEFT_SHIFT;
            int rightShiftValue = hashValue >>> RIGHT_SHIFT;
            hashValue = (leftShiftValue | rightShiftValue)^value.charAt(i);
        }
        return abs(hashValue % this.array.length);
    }
    
    /**
     * Expands the array and rehashes all values.
     */
    @SuppressWarnings( "unchecked" )
    private void rehash() {
        this.expand++;

        if (this.printStats) {
            printStatistics();
        }

        // reset instance variables
        this.numElem = 0;
        this.collision = 0;
        this.longestChain = 0;

        LinkedList[] original = this.array;
        LinkedList[] doubleSize = new LinkedList[EXPAND_FACTOR * this.array.length];
        this.array = doubleSize;

        int bucket = 0;
        while (bucket < original.length) {
            // only iterate non-empty buckets
            if (original[bucket] != null) {
                for (Object key : original[bucket]) {
                    insert((String) key);
                }
            }
            bucket++;
        }
    }

    /**
     * Calculates and returns the current load factor of the table
     */
    private double loadFactor() {
        return ((double) getSize()) / ((double) this.array.length);
    }
    
    /**
     * Print statistics to the given file.
     * @return True if successfully printed statistics, false if the file
     *         could not be opened/created.
     */
    //@Override
    public boolean printStatistics(){
        if (!this.printStats) {
            return false;
        }

        try {
            PrintStream stream = new PrintStream(new FileOutputStream(this.statsFileName, true));

            stream.print(this.expand + " resizes, load factor "
                            + TO_TWO_PLACES.format(loadFactor())
                            + ", " + this.collision + " collisions" + ", "
                            + this.longestChain + " longest chain\n");
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }
}
