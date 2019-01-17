package com.ml.decisiontree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class RandomFunction {

	public static int nodes = 0;
	public static int avgDepth = 0;
	public static Node randomRoot = null;

	public static Node GenerateDecisionTreeRandom(ArrayList<Instance> training_data) {
		Node rootRand = null;
		Random randFn = new Random();
		ArrayList<String> labels = Instance.attributeLabels;
		int count = 0;
		Queue<Node> q = new LinkedList<Node>();
		q.offer(rootRand);

		while (!q.isEmpty()) {
			rootRand = q.poll();
			int randNo = randFn.nextInt(labels.size() - 1);
			count++;
			if (rootRand == null) {
				rootRand = new Node();
				rootRand.attribute = randNo;
				rootRand.rowData = training_data;
				rootRand.node = nodes++;
				rootRand.classLabel = DecisionTree.getClassLabel(rootRand.rowData);
				randomRoot = rootRand;
			}

			rootRand.attribute = randNo;
			rootRand.node = nodes++;
			rootRand.classLabel = DecisionTree.getClassLabel(rootRand.rowData);
			rootRand.deleted_attrs.add(randNo);
			if (randNo != -1) {

				rootRand.left = classifyData(rootRand, "0");
				rootRand.left.node = nodes++;
				rootRand.left.deleted_attrs.addAll(0, rootRand.deleted_attrs);
				if (count< labels.size()-1) {
					q.offer(rootRand.left);
				} else {
					rootRand.left.isLeafNode = true;
					rootRand.left.classLabel = DecisionTree.getClassLabel(rootRand.left.rowData);
				}

				rootRand.right = classifyData(rootRand, "1");
				rootRand.right.node = nodes++;
				rootRand.right.deleted_attrs.addAll(0, rootRand.deleted_attrs);
				if (count< labels.size()-1) {
					q.offer(rootRand.right);
				} else {
					rootRand.right.isLeafNode = true;
					rootRand.right.classLabel = DecisionTree.getClassLabel(rootRand.right.rowData);
				}
			} else {
				rootRand.isLeafNode = true;
				rootRand.node = nodes++;
				rootRand.classLabel = DecisionTree.getClassLabel(rootRand.rowData);
			}
		}
		return randomRoot;
	}

	
	public static Node classifyData(Node root, String attrValue) {

		Node node = new Node();
		Iterator<Instance> iter = root.rowData.iterator();

		while (iter.hasNext()) {
			Instance inst = (Instance) iter.next();
			if (inst.attributes[root.attribute].equals(attrValue)) {
				node.rowData.add(inst);
			}
		}
		return node;
	}

	public static double AverageDepth(Node root) {
		int sumOfNodes = GetSumOfNodes(root, 0);
		return (double)sumOfNodes/(nodes-1);
	}

	private static int GetSumOfNodes(Node root, int i) {
		if(root == null){
			return 0;
		}
		else if(root.left == null && root.right == null){
			return i;//depth
		}
		else{
			return GetSumOfNodes(root.left, i+1)+GetSumOfNodes(root.right, i+1);
		}
	}
}