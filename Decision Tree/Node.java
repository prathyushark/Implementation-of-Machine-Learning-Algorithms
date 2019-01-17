package com.ml.decisiontree;
import java.util.ArrayList;

public class Node {
	public ArrayList<Instance> rowData;
	public int node;
	public int attribute;
	public char classLabel;
	
	public Node left;
	public Node right;
	public Node parent;
	public boolean isLeftChild;
	public boolean isRightChild;
	public boolean isLeafNode;
	

	public ArrayList<Integer> deleted_attrs;
	
	Node(){
		deleted_attrs = new ArrayList<Integer>();
		attribute = -1;
		classLabel = '2';
		rowData = new ArrayList<Instance>();
		left = null;
		right = null;
		isLeafNode= false;
		
	}

}
