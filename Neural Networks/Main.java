package com.ml.neuralnet;

import java.util.ArrayList;

public class Main {
	public static String datasetPath;
	public static ArrayList<Instance> dataset;
	public static int trainPercent;
	public static float errorTolerance;
	public static int noOfHiddenLayers;
	public static float learningRate;
	
	public static void main(String args[]) {
		
		//input
		datasetPath = args[0];
		String preprocessedPath = Preprocessing.getPreprocessedPath(datasetPath);
		
		//fetchingData and pre process
		dataset = Preprocessing.FetchAndPreprocessInput(datasetPath, preprocessedPath);

		 learningRate = (float)0.3;
		//write pre-processed data into file
		
		trainPercent = Integer.parseInt(args[1]);
		errorTolerance =  Float.parseFloat(args[2]);
		noOfHiddenLayers = Integer.parseInt(args[3]);

		NeuralNetwork neuralnet = new NeuralNetwork();
		
		int[] layersSizes = new int[noOfHiddenLayers];
		for (int i = 0; i < noOfHiddenLayers; i++) {
			layersSizes[i] = Integer.parseInt(args[4 + i]);
		}
		
		//train	
		System.out.println("Training\n***********************************");
		neuralnet.BuildNeuralNet(dataset, layersSizes, trainPercent, learningRate, errorTolerance);
		
		//display
		neuralnet.DisplayTree();
		System.out.println("\nTotal Training error: " +Instance.trainError);
		//test
		System.out.println("\nTesting\n***********************************");
		neuralnet.testNeuralNet(dataset, trainPercent);
		System.out.println("\nTotal Testing error: " + Instance.testError);	
		
		
	}

}
