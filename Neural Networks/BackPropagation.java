package com.ml.neuralnet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class BackPropagation {

	public BackPropagation() {
	}

	public float forwardPass() {

		int nodeId = 0;
		float error = 0;

		for (int i = 0; i < NeuralNetwork.layerCount; i++) {
			for (int j = 0; j < NeuralNetwork.layers.get(i).neurons.size(); j++) {
				Neuron tempNeuron = NeuralNetwork.layers.get(i).neurons.get(j);
				if (i == 0) {
					tempNeuron.net = tempNeuron.value;
					tempNeuron.output = tempNeuron.value;
				} else {
					tempNeuron.net = tempNeuron.bias + findNetValue(NeuralNetwork.layers.get(i - 1), nodeId);
					tempNeuron.output = findOutput(tempNeuron.net);
				}
				
				nodeId++;
			}
			if (i == NeuralNetwork.layerCount - 1) {
				error = NeuralNetwork.layers.get(i).neurons.get(0).classValue - NeuralNetwork.layers.get(i).neurons.get(0).output;
				error = (error * error) / 2;
			}
			 
		}
		// System.out.println("no of nodes:" + NeuralNetwork.noOfNeurons);
		return error;
	}

	private float findOutput(float net) {
		float output = 1 / (1 + (float) Math.exp(-net));
		return output;
	}

	private float findNetValue(Layer layer, int nodeId) {
		float net = 0;
		for (int i = 0; i < layer.neuronCount; i++) {
			if (layer.neurons.get(i).weights.containsKey(nodeId))
				net = net + layer.neurons.get(i).weights.get(nodeId) * layer.neurons.get(i).output;

		}
		return net;
	}

	public void backPropagation() {
		int layerID = NeuralNetwork.layerCount - 1;
		int nodeID = NeuralNetwork.noOfNeurons - 1;
		float delta = 0;
		for (int i = layerID; i >= 0; i--) {
			for (int j = NeuralNetwork.layers.get(i).neuronCount - 1; j >= 0; j--) {
				Neuron temp = NeuralNetwork.layers.get(i).neurons.get(j);
				delta = temp.output * (1 - temp.output);
				if (i == layerID) {
					temp.delta = delta * (temp.classValue - temp.output);

				} else {
					temp.delta = delta * findWeightedDelta(temp, NeuralNetwork.layers.get(i + 1), nodeID);
				}
				if(i != 0)
					temp.bias = temp.bias + NeuralNetwork.learningRate * temp.delta;
				nodeID--;
			}
		}
		updateWeights();
	}

	private void updateWeights() {
		for (int i = 0; i < NeuralNetwork.layerCount; i++) {
			for (int j = 0; j < NeuralNetwork.layers.get(i).neuronCount; j++) {
				HashMap<Integer, Float> updatedWeights = new HashMap<Integer, Float>();
				Neuron tempN = NeuralNetwork.layers.get(i).neurons.get(j);

				Set<Entry<Integer, Float>> entrySet1 = tempN.weights.entrySet();
				Iterator<Entry<Integer, Float>> iter1 = entrySet1.iterator();
				while (iter1.hasNext()) {
					Entry<Integer, Float> element1 = iter1.next();
					float t = element1.getValue() + tempN.weightDeltas.get(element1.getKey());
					updatedWeights.put(element1.getKey(), t);
				}
				NeuralNetwork.layers.get(i).neurons.get(j).weights = updatedWeights;
			}
		}
	}

	private float findWeightedDelta(Neuron neuron, Layer nextLayer, int nodeID) {
		float temp = 0;
		float weightUpdate = 0;
		neuron.weightDeltas = new HashMap<Integer, Float>();
		for (int i = 0; i < nextLayer.neuronCount; i++) {
			Neuron tempNeuron = nextLayer.neurons.get(i);
			temp = temp + tempNeuron.delta * neuron.weights.get(tempNeuron.id);
			weightUpdate = NeuralNetwork.learningRate * neuron.output * tempNeuron.delta;
			neuron.weightDeltas.put(tempNeuron.id, weightUpdate);
		}
		return temp;
	}

}
