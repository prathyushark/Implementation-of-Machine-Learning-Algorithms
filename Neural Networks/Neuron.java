package com.ml.neuralnet;

import java.util.HashMap;

public class Neuron {
	public HashMap<Integer, Float> weights;
	public HashMap<Integer, Float> weightDeltas;
	public int id;
	public float net;
	public float bias;
	public float output;
	public float value;
	public float delta;
	public float classValue;
	
	Neuron(int id, int isNotInputLayer){
		this.id = id;
		this.weights = new HashMap<Integer, Float>();
		if(isNotInputLayer == 1){
			this.bias = NeuralNetwork.getRandomWeight();//setting up the bias by default if it is not input layer
		}
	}
	
}
