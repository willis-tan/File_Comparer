/*
 * NAME: Willis Tan
 * PID: A145224499
 */

import java.io.*;
import java.util.*;

/**
 * LineCounter is used to find percentage of similarities between the input files
 *
 * @author Willis Tan
 * @since March 11, 2020
 */
public class LineCounter {

    private static final int DEFAULT_SIZE = 50;
    private static final double PERCENT_CONV = 100.0;
    private static final int MIN_ARGS = 2;
    
    /**
    * Method to print the filename to console
    */
    public static void printFileName(String filename) {
        System.out.println("\n"+filename+":");
    }

    /**
    * Method to print the statistics to console
    */
    public static void printStatistics(String compareFileName, int percentage) {
        System.out.println(percentage+"% of lines are also in "+compareFileName);
    }

    /**
     * Function that compares, calculates, and prints out the percentages
     * @param args Input files to compare
     */
    public static void main(String[] args) {
        
        if(args.length < MIN_ARGS) {
            System.err.println("Invalid number of arguments passed");
            return;
        }
        
        int numArgs = args.length;
        
        //Create a hash table for every file
        HashTable[] tableList = new HashTable[numArgs];

        //Preprocessing: Read every file and create a HashTable
        
        for(int i = 0; i < numArgs; i++) {
            tableList[i] = new HashTable(DEFAULT_SIZE);

            try {
                File file = new File(args[i]);
                Scanner scanner = new Scanner(file);

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    // insert each line into the hashtable
                    tableList[i].insert(line);
                }
                scanner.close();
            } catch (FileNotFoundException | IllegalStateException | NoSuchElementException e) {
                return;
            }
        }
        
        //Find similarities across files
        
        for(int i = 0; i < numArgs; i++) {

            // print the filename
            printFileName(args[i]);

            // inner loop to compare with other files
            for (int j = 0; j < numArgs; j++) {

                // skip if comparing with self
                if (tableList[j] == tableList[i]) {
                    continue;
                }

                // number of similarities between file i and file j
                // and total number of lines for file i
                double numSimilarities = 0;
                double numLines = 0;

                try {
                    File file = new File(args[i]);
                    Scanner scanner = new Scanner(file);

                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        numLines++;
                        // lookup each line from file i in file j
                        if (tableList[j].lookup(line)) {
                            // if line is found, increment similarities
                            // remove the line after to prevent double counting
                            numSimilarities++;
                        }
                    }
                    scanner.close();
                } catch (FileNotFoundException | NoSuchElementException e) {
                    // pass
                }
                // print statements
                int percentage = (int) Math.floor(PERCENT_CONV * (numSimilarities /  numLines));
                printStatistics(args[j], percentage);
            }
        }
    }

}
