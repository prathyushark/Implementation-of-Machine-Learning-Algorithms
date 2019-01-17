package com.ml.neuralnet;
import java.util.ArrayList;

public class Layer {

	public int layerNo;
	public int neuronCount;
	public ArrayList<Neuron> neurons;
	
	Layer(){		
	}
	
	Layer(int layerNo, int neuronCount){
		this.layerNo = layerNo;
		this.neuronCount = neuronCount;
	}

	public Layer initLayer(int id, int neuronCount, int currentNeuronCount,int isNotInputLayer) {
		Layer newLayer = new Layer(id, neuronCount);
		neurons = new ArrayList<Neuron>();
		Neuron neuron;
//		System.out.println("layer "+id+" ");
		for(int i=0; i<neuronCount; i++){
			neuron = new Neuron(currentNeuronCount + i, isNotInputLayer);
//			System.out.println("neuron added with id & bias: "+ neuron.id +"  , "+neuron.bias);
			neurons.add(neuron);
		}
		newLayer.neurons = neurons;
		return newLayer;
	}

}
