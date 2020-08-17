/*
Name: Willis Tan
ID: A14522499
 */

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * JUnit test cases for HashTable.java
 *
 * @author Willis Tan
 * @since March 11, 2020
 */
public class HashTableTester {

    private HashTable smallTableSize;
    private HashTable bigTableSize;
    private String[] input;
    private String[] duplicates;

    @org.junit.Before
    public void setUp() {
        smallTableSize = new HashTable(3);
        bigTableSize = new HashTable(100);
        input = new String[]{"there", "are", "2", "MANY", "snakes", "on", "this", "$@&%*", "plane"};
        duplicates = new String[]{"abc", "123", "321", "cba", "abc", "123", "cba", "321"};
    }

    @org.junit.Test
    public void testInsert() {
        for (String s : input) {
            assertTrue(bigTableSize.insert(s));
        }

        for (int j = 0; j < 4; j++) {
            assertTrue(smallTableSize.insert(duplicates[j]));
        }
        for (int k = 4; k < duplicates.length; k++) {
            assertFalse(smallTableSize.insert(duplicates[k]));
        }
    }

    @Test
    public void testInsertThrowNPE() {
        try {
            smallTableSize.insert(null);
            fail("did not catch exception");
        } catch (NullPointerException e) {
            // pass
        }
    }

    @org.junit.Test
    public void testDelete() {
        for (String s : input) {
            bigTableSize.insert(s);
        }
        int i = 9;
        for (String key: input) {
            assertTrue(bigTableSize.delete(key));
            assertEquals(i - 1, bigTableSize.getSize());
            i--;
        }
        assertFalse(bigTableSize.delete("dne"));
    }

    @Test
    public void testDeleteThrowsNPE() {
        try {
            bigTableSize.delete(null);
            fail("did not catch exception");
        } catch (NullPointerException e) {
            // pass
        }
    }

    @org.junit.Test
    public void testLookup() {
        for (String s : input) {
            smallTableSize.insert(s);
            assertTrue(smallTableSize.lookup(s));
        }
        assertFalse(smallTableSize.lookup("dne"));

        for (String t : duplicates) {
            bigTableSize.insert(t);
            assertTrue(bigTableSize.lookup(t));
        }
        assertFalse(bigTableSize.lookup("dne"));
    }

    @Test
    public void testLookUpThrowsNPE() {
        try {
            smallTableSize.lookup(null);
            fail("did not catch exception");
        } catch (NullPointerException e) {
            // pass
        }
    }

    @org.junit.Test
    public void testGetSize() {
        for (int i = 0; i < input.length; i++) {
            bigTableSize.insert(input[i]);
            assertEquals(i + 1, bigTableSize.getSize());
        }
    }

    @org.junit.Test
    public void testPrintStatistics() {
        for (String s : input) {
            bigTableSize.insert(s);
        }
        assertFalse(bigTableSize.printStatistics());

        for (String s2 : duplicates) {
            smallTableSize.insert(s2);
        }
        assertFalse(smallTableSize.printStatistics());

        HashTable noFile = new HashTable(16, "");
        assertFalse(noFile.printStatistics());

        //HashTable someFile = new HashTable(20, "./src/hw9_LC_tests/empty_testfile.txt");
        //assertTrue(someFile.printStatistics());
    }
}