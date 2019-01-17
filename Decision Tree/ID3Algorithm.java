package com.ml.decisiontree;

import java.util.ArrayList;
import java.util.Iterator;

public class ID3Algorithm {
	
	public static int calcIG(ArrayList<Instance> dataset,ArrayList<Integer> attributes)
	{
		double classEntropy = findEntropy(dataset);
		int data_size = dataset.size();
		int maxIGIndex=-1;
		double IG=0,maxIG=0;
		double pos_attr=  0, neg_attr=0;
		double pos_attr_0 = 0, neg_attr_0=0;
		double pos_attr_1 = 0, neg_attr_1=0;
		double log2_val = Math.log10(2);	
		
		if(classEntropy==0)
			return -1;
		
		for(int j=0;j<Instance.attributeCount;j++)
		{
			for(int i=0;i<dataset.size();i++)
			{
				Instance inst= dataset.get(i);
				if(inst.attributes[j].matches("1"))
				{
					pos_attr++;
					if(inst.classValue.matches("1"))
						pos_attr_1++;
					else
						neg_attr_1++;
				}else{
					neg_attr++;
					if(inst.classValue.matches("1"))
						pos_attr_0++;
					else
						neg_attr_0++;
				}
			}
			
			double prob_pos_0 = (double)pos_attr_0/neg_attr;
			double prob_pos_1 = (double)pos_attr_1/pos_attr;
			double prob_neg_0 = (double)neg_attr_0/neg_attr;
			double prob_neg_1 = (double)neg_attr_1/pos_attr;
			double ent_pos = 0, ent_neg = 0;
			if(prob_pos_1==0)
				ent_pos = 0;
			else
				ent_pos = Math.log10(prob_pos_1)/log2_val;
			
			if(prob_neg_1==0)
				ent_neg = 0;
			else 
				ent_neg = Math.log10(prob_neg_1)/log2_val;
			
			double entropy_1 = -((prob_pos_1*ent_pos) + (prob_neg_1*ent_neg));
			
			if(prob_pos_0==0)
				ent_pos = 0;
			else
				ent_pos = Math.log10(prob_pos_0)/log2_val;
			
			if(prob_neg_0==0)
				ent_neg = 0;
			else
				ent_neg = Math.log10(prob_neg_0)/log2_val;
			
			double entropy_0 = -((prob_pos_0*ent_pos) + (prob_neg_0*ent_neg));
			
			IG = classEntropy - (((double)pos_attr/(double)data_size)*entropy_1) - (((double)neg_attr/(double)data_size)*entropy_0);
				if(j==0 && !Double.isNaN(IG) && !attributes.contains(j))
				{
					maxIG = IG;
					maxIGIndex = j;
				}
				else
				{
					if(IG>maxIG && !Double.isNaN(IG) && !attributes.contains(j))
					{
						maxIG=IG;
						maxIGIndex=j;
					}
				}
				IG=0;
				pos_attr_0=0;
				pos_attr_1=0;
				neg_attr_0=0;
				neg_attr_1=0;
				pos_attr=0;
				neg_attr=0;		
		}
		return maxIGIndex;
	}
	
	public static Node classifyData(Node root,String attrValue) {
		
		Node node = new Node(); 
		Iterator<Instance> iter = root.rowData.iterator();
		
		while(iter.hasNext()){
			Instance inst = (Instance)iter.next();
			if(inst.attributes[root.attribute].equals(attrValue)){
				node.rowData.add(inst);
			}
		}
		
		return node;
	}

	
	public static double findEntropy(ArrayList<Instance> dataset){
	
		double entropy = 0.0;
		int size = dataset.size();
		int count1 = 0;
		int count0=0;
		double log2_val = Math.log10(2);
		double ent_pos = 0, ent_neg = 0;
		
		Iterator<Instance> iter = dataset.iterator();
		if(size==0)
			return 0;
		else{
			while(iter.hasNext()){
				String value = iter.next().classValue;
			    if(value.equals("1")){
			    	count1++;
			    } else{
			    	count0++;
			    }
	        }
			double class_1_probability = (double)count1/size;
			double class_0_probability = (double)count0/size;
			if(class_1_probability==class_0_probability)
				entropy = 1;
			else if(class_1_probability==1 || class_0_probability==1)
				entropy = 0;
			else {
				if(class_1_probability==0)
					ent_pos = 0;
				else 
					ent_pos = Math.log10(class_1_probability)/log2_val;
				if(class_0_probability==0)
					ent_neg = 0;
				else
					ent_neg = Math.log10(class_0_probability)/log2_val;
						
				entropy = - ((class_1_probability*ent_pos) + (class_0_probability*ent_neg));
			}
		}
		return entropy;
	}
	
	
}
