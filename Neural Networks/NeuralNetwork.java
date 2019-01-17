package com.ml.neuralnet;

import java.util.ArrayList;
import java.util.Random;

public class NeuralNetwork {
	public static int layerCount;
	public static int noOfNeurons;
	public static int[] layerNeuronCount;
	public static ArrayList<Layer> layers;
	public static ArrayList<ArrayList<Float>> Weights;
	public static float errorTolerance;
	public static float learningRate;

	public static void initLayers(int[] layersSizes, Instance instance) {
		int neuronCount = 0;
		layerCount = layersSizes.length + 2;
		layers = new ArrayList<Layer>();
		Layer templayer = new Layer();
		Layer temp;
		int isNotInputLayer = 0; 
		for (int i = 0; i < layerCount; i++) {
			if (i == 0)
				neuronCount = instance.numericValues.length - 1;
			else if (i == layerCount - 1){
				neuronCount = 1;
				isNotInputLayer = 1; 
			}			
			else{
				neuronCount = layersSizes[i - 1];
				isNotInputLayer = 1;
			}
			temp = templayer.initLayer(i, neuronCount, noOfNeurons, isNotInputLayer);
			layers.add(temp);
			noOfNeurons = noOfNeurons + temp.neuronCount;
		}

	}

	public void BuildNeuralNet(ArrayList<Instance> dataset, int[] layersSizes, int trainPercent, float lr, float error) {
		int flag1 = 0;
		int flag2 = 0;
		initLayers(layersSizes, dataset.get(0));
		errorTolerance = error;
		learningRate = lr;
		generateRandomWeights();
		System.out.println("size: " + dataset.size() * trainPercent / 100);
		do {
			for (int i = 0; i < dataset.size() * trainPercent/100; i++) {
				Instance.trainError = trainNeuralNet(dataset.get(i));
				if (flag1 == 1 && Instance.trainError <= errorTolerance) {
					flag2 = 1;
					break;
				}
			}
			flag1 = 1;
		} while (flag2 != 1);
	}

	private float trainNeuralNet(Instance instance) {
		float error = 0;
		BackPropagation bp = new BackPropagation();
		setInputLayer(instance);
		setOutputLayer(instance);

		error = bp.forwardPass();	
//		System.out.println(error);
		bp.backPropagation();
		return error;
	}

	private void setOutputLayer(Instance instance) {
		layers.get(layerCount - 1).neurons.get(0).classValue = instance.numericValues[instance.numericValues.length
				- 1];
	}

	private void generateRandomWeights() {
		for (int i = 0; i < layers.size() - 1; i++) {
			for (Neuron currentNeuron : layers.get(i).neurons) {
				for (Neuron nextLayerNeuron : layers.get(i + 1).neurons) {
					currentNeuron.weights.put(nextLayerNeuron.id, getRandomWeight());
				}
			}
		}

	}

	public static float getRandomWeight() {	
		Random rand = new Random();
		return 5*rand.nextFloat();
	}

	private void setInputLayer(Instance instance) {
		for (int i = 0; i < layers.get(0).neurons.size(); i++) {
			layers.get(0).neurons.get(i).value = instance.numericValues[i];
		}
	}

	public void testNeuralNet(ArrayList<Instance> dataset, int trainPercent) {
		float error=0;
		BackPropagation bp = new BackPropagation();		
		for (int i = (int) (dataset.size() * trainPercent/100); i < dataset.size(); i++) { 
			setInputLayer(dataset.get(i));
			setOutputLayer(dataset.get(i));	
			error = bp.forwardPass();
		}
		Instance.testError = error;
	}

	public void DisplayTree() {
		for(int i=1; i<NeuralNetwork.layerCount; i++){
			Layer layer = layers.get(i);
			if(i==NeuralNetwork.layerCount-1)
				System.out.println("\nOutput Layer :" );
			else
				System.out.println("\nHidden Layer "+i+" :");
			
			for(int j=0; j<layer.neuronCount; j++){
				Neuron neuron = layers.get(i).neurons.get(j);
				System.out.println("    Neuron "+j+" weights: ");
				for(Neuron tempNeuron : layers.get(i-1).neurons)					
					System.out.print("      "+ tempNeuron.weights.get(neuron.id));
				System.out.println();
			}
		}
	}



}
