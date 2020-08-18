# Status
**This project is no longer being updated. Anyone is free to make pull requests and modify as they wish.**

# File Comparer

File Comparer is a Java program that tells you the whole number percentage of shared lines of text between 2 or more text files. This program is built off a custom hash-table that I wrote as part of a college assignment to implement the use of hash-tables.
### Example 
fun1.txt contains: the quick brown fox jumps over the lazy dog. All the words are seperated by newlines  
fun3.txt contains: the super quick brown foxes jumped over the lazy dogs. All the words are seperated by newlines

If we compare these two files, the output will say:  
`fun1.txt:
66% of lines are also in fun3.txt`
  
`fun3.txt:
60% of lines are also in fun1.txt`

## Installation and usage
Java SE Development Kit 11.0.1 was used to build this program, so much sure to have that installed.  

In order to use this, you will pass command line arguments. Make sure you are in the /src folder directory first.  
Then run in the terminal:  
`javac LineCounter.java`  
`java LineCounter fun1.txt fun2.txt` 
This will compare two of the included texts. To compare more than 2 files, write all the file names after `java LineCounter` and seperate them by space.  

If you want to compare custom files, make sure to put the custom files into the "hw9_LC_tests" folder first.
