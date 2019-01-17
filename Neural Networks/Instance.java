package com.ml.neuralnet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class Instance {
	public static ArrayList<Integer> attributes; // class column at last
	public static Map<Integer, HashSet<String>> attributeInfo; // preprocessing clas is also included
	public static float trainError=10000;
	public static float testError=10000;

	public String[] values;
	public String classVal;
	public int index;
	public float[] numericValues;
	public Instance(String[] values) {
		this.values = values;
	}

}
