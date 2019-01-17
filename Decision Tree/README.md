Implementation of ID3 decision tree algorithm and Pruning

Language used : JAVA

How to run the program from command line:
javac main.java
java main training_set.csv validation_set.csv test_set.csv 0.2 

Run configurations to be given in IDE: 
"E:\Eclipse Neon Workspace\Machine Learning\data\data_sets1\training_set.csv" (your training data set path) 
"E:\Eclipse Neon Workspace\Machine Learning\data\data_sets1\validation_set.csv" (your validation data set path) 
"E:\Eclipse Neon Workspace\Machine Learning\data\data_sets1\test_set.csv" (your test data set path) 

“0.2” (pruning factor)

Assumptions made:
1. Assuming pruning trails to be upto 30 times.
2. Test data contains only boolean values.
3. There will be no missing data. 
4. First row of the data set will contain column names and each non blank line after that will contain a new data instance. 
5. Last column contins class labels.
