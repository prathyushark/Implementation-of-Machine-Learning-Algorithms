package com.ml.neuralnet;

import java.io.BufferedReader;import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Preprocessing 
{

	static ArrayList<Instance> preProcessedData = new ArrayList<Instance>();

	public static ArrayList<Instance> FetchAndPreprocessInput(String rawDatasetPath, String preProcessedDatasetPath) 
	{
		 int numOfValues=0;
		 int count=0;
		 int count1=0;
		 int total=0;
		 int flag=0;

		//remove incomplete data
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader(rawDatasetPath));
			String line="";
			while ((line = br.readLine()) != null && line.length()>1) 
			{
				total++;
				 Instance instance = new Instance(null);	
				 flag=0;
				 numOfValues=0;
				 line = line.trim().replaceAll(","," ").replaceAll(" +", ",");
	              String[] splitArray = line.split(",");
				 instance.values = new String[splitArray.length];
				 instance.numericValues = new float[splitArray.length];
				 numOfValues = splitArray.length; 
				 for(int i=0;i<splitArray.length;i++)
				 {
					 if(splitArray[i].equals("?"))
					 {
						 count1++;
						 flag=1;
						 break;
					 }
					 else					 
					 {
					 instance.values[i]=splitArray[i];
					 }
					 
				 }
				 //break comes here
				 if(flag==0)
				 {
				 instance.classVal= splitArray[numOfValues-1];
				 instance.index=count++;
				 preProcessedData.add(instance);
				 }
				 
			}//while
			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// preProcessedData now has all rows with complete data
		
			
		System.out.println("\nPreprocessing\n**************************************\n Number of rows with ? mark(removable data): "+count1);
		System.out.println("Number of rows with complete data: "+count);
		System.out.println("Total number of rows: "+total);
		

		//standardization
		for(int i=0;i<numOfValues;i++)
		{
			if(isNumeric(preProcessedData.get(0).values[i]))
			{
			float[] temp=new float[count];
				for(int j=0;j<count;j++)
				{
					temp[j]=Float.parseFloat(preProcessedData.get(j).values[i]);
				}
				//have 1 column
				
				float mean=calculateMean(temp);
				float sd=calculateStandardDeviation(temp,mean);
				if(sd!=0)
					temp=Standardize(temp,mean,sd);

				for(int j=0;j<count;j++)
				{
					preProcessedData.get(j).numericValues[i]=temp[j];
					
				}
			}//if
			else
			{
				//convert strings to numbers
				Set<String> possibleValues=new HashSet<String>();

				for(int j=0;j<count;j++)
				{
				possibleValues.add(preProcessedData.get(j).values[i]);
				}
				
				int index=0;
				for(String s:possibleValues)
				{
				for(int j=0;j<count;j++)
				{
						if(s.equals(preProcessedData.get(j).values[i]))
							preProcessedData.get(j).numericValues[i]=index;	
				}
				index++;
				}
				
			}//else
		}//i for
		//done with standardization and string to number conversion
//		System.out.println("Data after standardization:");
//		for(int i=0;i<count;i++)
//		{
//			for(int j=0;j<numOfValues;j++)
//			{
//				System.out.print(preProcessedData.get(i).numericValues[j]+"  ");
//			}
//			System.out.println("");
//		}
		
		//write preprocessed data into a file
		try {
			FileWriter fileWriter=new FileWriter(preProcessedDatasetPath);
			for(Instance instance : preProcessedData)
			{
				for(int i=0;i<numOfValues;i++)
					fileWriter.write(instance.numericValues[i]+",");
				fileWriter.write("\n");
			}
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return preProcessedData;
	}//fetch and preprocess method
	
	private static float[] Standardize(float[] temp,float mean, float sd) 
	{
		for(int i=0;i<temp.length;i++)
		temp[i]=(temp[i]-mean)/sd;
		return temp;
	}

	private static float calculateStandardDeviation(float[] temp,float mean) 
	{
		float sd=0,deviation=0,variance=0;
		for(int i=0;i<temp.length;i++)
			deviation=deviation+(temp[i]-mean)*(temp[i]-mean);
			
		variance=deviation/temp.length;
		sd=(float) Math.sqrt(variance);
		return sd;
	}

	private static float calculateMean(float[] temp) 
	{
		float sum=0,mean=0;
		for(int i=0;i<temp.length;i++)
		sum=sum+temp[i];
		mean=sum/temp.length;
		return mean;
	}

	public static boolean isNumeric(String s) 
	{
	      boolean isValidNumber = false;
	      try
	      {
	         Float.parseFloat(s);	//works for both float and int 
	         isValidNumber = true;
	      }
	      catch (NumberFormatException ex)
	      {
	      }
	      return isValidNumber;
	   }

	public static String getPreprocessedPath(String datasetPath) {
		String[] temp = datasetPath.split("\\\\");
		String newPath = "";
		for(int i=0;i<temp.length;i++){
			if(i==temp.length-1)
				newPath = newPath.concat("\\PreprocessedData");
			if(i!=0)
				newPath = newPath.concat("\\"+temp[i]);
			else
				newPath = newPath.concat(temp[i]);
		}
		return newPath;
	}

}//class
