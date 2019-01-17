package com.ml.decisiontree;

import java.util.ArrayList;


public class Main {
	public static final int num_of_attributes=19;
	public static Node root = null;
	public static Node rootTesting = null;
	public static Node rootValidating = null;
	public static int noOfNodes = 1;
	
	public static void main(String[] args) {
		
		Instance inst = new Instance();	
		DecisionTree dt = new DecisionTree();
		
		String train_data = args[0];
		String validation_data = args[1];
		String test_data = args[2];
		float prune_factor = Float.parseFloat(args[3]);
		
		ArrayList<Instance> training_data = inst.FetchData(train_data);
		ArrayList<Instance> testing_data = inst.FetchData(test_data);
		ArrayList<Instance> validating_data = inst.FetchData(validation_data);
		
		root = DecisionTree.GenerateDecisionTreeUsingID3(training_data,"training");
		System.out.println("Decison Tree :");		
		dt.displayTree(root);
		noOfNodes = DecisionTree.nodes;
		System.out.println("\n\n\nNumber of nodes in the tree = "+(noOfNodes-1));
		System.out.println("Number of Leaf Nodes in the tree = "+(DecisionTree.leafnodes));
		System.out.println("\nAverage Depth:"+DecisionTree.AverageDepth(root));

		System.out.println("*********************************\nPre-pruning Accuracy:");
		System.out.println("Accuracy of	the	model on the Training dataset = " + Pruning.findAccuracy(root,training_data));
		
		rootValidating = DecisionTree.GenerateDecisionTreeUsingID3(validating_data,"validating");
		System.out.println("Accuracy of	the	model on the Validation dataset = " + Pruning.findAccuracy(root,validating_data));
		
		rootTesting = DecisionTree.GenerateDecisionTreeUsingID3(testing_data,"testing");		
		System.out.println("Accuracy of	the	model on the Testing dataset = " + Pruning.findAccuracy(root,testing_data));
		
		Pruning.pruneTree(root, testing_data, validating_data, prune_factor, noOfNodes);
		
		Node rootRand = RandomFunction.GenerateDecisionTreeRandom(training_data);
		System.out.println("\n************************************\n Randomly generated Decision Tree\n");
		dt.displayTree(rootRand);	
		int nodesInRandTree=RandomFunction.nodes;
		System.out.println("\n\n\nNumber of nodes in the tree = "+(nodesInRandTree-1));
		System.out.println("\nAverage Depth:"+RandomFunction.AverageDepth(root));
		System.out.println("Accuracy of	the	Random model on the Training dataset = " + Pruning.findAccuracy(rootRand,training_data));
		System.out.println("Accuracy of	the	Random model on the Validation dataset = " + Pruning.findAccuracy(rootRand,validating_data));
		System.out.println("Accuracy of	the	Random model on the Testing dataset = " + Pruning.findAccuracy(rootRand,testing_data));
			
	}

}
