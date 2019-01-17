package com.ml.decisiontree;

import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.IOException;



public class Instance {
	public String classValue;
	public int index;
	public String[] attributes;
	public static int attributeCount;
	public static ArrayList<String> attributeLabels = new ArrayList<String>();
	

	public ArrayList<Instance> FetchData(String dataSet){
		String line = "";
		int count = 0, x= 0;
		ArrayList<Instance> data = new ArrayList<Instance>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(dataSet));
			while ((line = br.readLine()) != null) {
				 if(x==0){
					 String[] attrs = line.split(",");
					 for(int i=0;i<attrs.length-1; i++){
						 if(!attributeLabels.contains(attrs[i]))
								 attributeLabels.add(attrs[i]);
					 }
					 x++;
				 }
				 else{
				 Instance inst = new Instance();				 
				 inst.attributes = new String[line.split(",").length-1];
				 attributeCount = inst.attributes.length-1;
				 for(int i=0;i<line.split(",").length-1;i++){
					 inst.attributes[i]=line.split(",")[i];
				 }
				 inst.classValue= line.split(",")[attributeCount+1];
				 inst.index = count++;
				 data.add(inst);
				 }
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

}
