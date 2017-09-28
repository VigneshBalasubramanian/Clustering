/*
Source code file name: vb0018_rh0059BasicClustering.java"
Date: 02/06/2017
References: 1. CS641 Class Notes, Dr. Ramazan Aygun
			2. Mnemosyne Studio
			2. hub4tech			
			3. Stackoverflow
*/


//Header files

package vb0018_rh0059BasicClustering;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Set;



public class vb0018_rh0059BasicClustering {
	
	public static double[][] fileData_Array;
	public static ArrayList<String> read_Line;
	public static int cluster_count_Flag = 0;
	public static int check = 0;
	public static int Row_Count = 0;
	public static int Column_Count = 0;
	public static int K_Val; 
	public static ArrayList uniqueclusterlists;
	public static double[][] cluster_points;
	public static double[][] basic_Cluster;
	public static double[][] generated_Points;
	public static double[][] bisected_Cluster;
	public static double[][] cluster_for_Bisect;
	public static int New_Column_Count;
	

	
	// K means function//
    
	public static void BasicKmeans(int K_Val)
	{
		cluster_points = new double[K_Val][New_Column_Count+1];
		basic_Cluster = new double[Row_Count][New_Column_Count+1];
		
		generated_Points = new double[K_Val][New_Column_Count+1];
		int[] cluster_random_points = new int[K_Val];
		       
		        
        for(int i=0;i<Row_Count;i++)
        	for(int j=0;j<New_Column_Count;j++)
        		basic_Cluster[i][j] = fileData_Array[i][j];
        
        for(int i=0;i<K_Val;i++)
        {
        	Random r = new Random();
        	cluster_random_points[i] = r.nextInt(read_Line.size()-1);
        	for(int j=0;j<New_Column_Count;j++)
        		cluster_points[i][j] = fileData_Array[cluster_random_points[i]][j];
        }
        
 
        for(int i=0;i<K_Val;i++)
        {
        	int j= New_Column_Count; 
        	cluster_points[i][j] = i+1;
        }
     
     
        boolean enter = false;
        //Comparing arrays//
        while(!Arrays.deepEquals(cluster_points, generated_Points))
        {
        	if(enter)
        	{
        		for(int i=0;i<K_Val;i++)
    	        {
    	        	for(int j=0;j<=New_Column_Count;j++)
    	        	{
    	        		cluster_points[i][j] =generated_Points[i][j];	
    	        	}
    	        }
        	}
        
        	//Eucledian distance calculation//
	        
	        for(int i=0;i<Row_Count;i++)
	        {
	        	double distance_Square = 0;
	        	ArrayList<Double> Eucledian_Values = new ArrayList<Double>();
	        	for(int j=0;j<K_Val;j++)
	        	{
		        	for(int k=0;k<New_Column_Count;k++)
		        	{
		        		distance_Square = distance_Square + Math.pow(basic_Cluster[i][k] - cluster_points[j][k],2);
		        	}
		        	Eucledian_Values.add(Math.sqrt(distance_Square));
		        	distance_Square = 0;
	        	}	
	        	
	        	
	        	int next_Entry = Eucledian_Values.indexOf(Collections.min(Eucledian_Values));
	        	basic_Cluster[i][New_Column_Count] = next_Entry + 1;
	        }
	       
	        //Generating cluster based on new check //
	        for(int k=0; k<K_Val; k++)
	        {
	        	boolean last_Value_Check = false;
	        	for(int i=0;i<Row_Count;i++)
	        	{
	        		for(int j=New_Column_Count;j>=0;j--)
	        		{
	        			if(j==New_Column_Count && basic_Cluster[i][j]==k+1)
	        			{
	        				last_Value_Check = true;
	        			}
	        			if(j==New_Column_Count && basic_Cluster[i][j]!=k+1)
	        			{
	        				last_Value_Check = false;
	        			}
	        			if(last_Value_Check == true)
	        			{
	        				generated_Points[k][j] = generated_Points[k][j] + basic_Cluster[i][j];
	        			}
	        		}
	        	}
	        }
	        
	        ArrayList<Double> New_Cluster = new ArrayList<Double>();
	        
	        for(int i=0;i<K_Val;i++)
	        {
	        	int j=New_Column_Count;
	        	New_Cluster.add((generated_Points[i][j])/(i+1));
	        }
	        
			 for(int i=0;i<K_Val;i++)
		     {
				 for(int j=0;j<=New_Column_Count;j++)
		        	{
					 generated_Points[i][j] = generated_Points[i][j]/New_Cluster.get(i);
		        	}
		     }			 		 
			 enter = true;    
        }
	}
	
	public static void main(String args[]) throws FileNotFoundException, IOException{
		
		Row_Count=0;
		Column_Count =0;
		New_Column_Count = 0;
		String inputFileName = null; 	//Variable to store the location of the file//  
		K_Val = 0; //Cluster number//
		cluster_count_Flag = 0;
		check=0; 
		
		read_Line = new ArrayList<String>();
		
		ArrayList<String> parameters = new ArrayList<String> ();
		
		StringBuilder temp_Output_Write = new StringBuilder();
		StringBuilder Output_Write = new StringBuilder();
		
		//Getting the file name and reading it line by line
		

		//Scraping file path name from arguments//
		for(int i=0; i<args.length; i++){
			if(args[i].equals("-i")){
			  inputFileName = args[i+1];				
			}
		}
		
		//inputFileName = "kmtest.arff";
		
		//Scraping Cluster Count//
		for(int i=0; i<args.length; i++){
			if(args[i].equals("-K")){
				K_Val = Integer.parseInt(args[i+1]);				
			}
		}
		
        try (BufferedReader br = new BufferedReader(new FileReader(inputFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
            	if(check==0)
            	{
            		if(line.trim().split("\\s+")[0].equals("@attribute"))
            		{
            			parameters.add(line.trim().split("\\s+")[1]);
            		}
            		temp_Output_Write.append(line);
            		temp_Output_Write.append(System.getProperty("line.separator"));            		
            	}

            	//Checking for @data//
            	if(line.equals("@data")){
            		check = 1;
            	}
            		
            	//Checking for @data to take input from next lines//
            	if(check==1){
            		read_Line.add(line);
            		}
            	} 		
            }
        
        // If class attribute is found //
        int skipIndex = parameters.indexOf("class");
        
        Row_Count = read_Line.size() - 1; 
    	Column_Count = read_Line.get(1).trim().split("\\s+").length;
    	fileData_Array= new double[Row_Count][Column_Count];
    	
    	int counter = 0;
        for (int i = 1; i < read_Line.size(); i++) {
        	String[] line = read_Line.get(i).trim().split("\\s+");
        	for(int j=0;j<Column_Count;j++)
        	{
        		if(j==skipIndex)  //to check for class attribute//
        			continue;
        		String number = line[j];
            	fileData_Array[i-1][counter] =Double.parseDouble(number);	
            	counter++;
        	}
        	New_Column_Count = counter;
        	counter=0;
		}
        
   
        Output_Write.append(temp_Output_Write);
        
        
        BasicKmeans(K_Val);
        
	    System.out.print('\n');
	    System.out.print('\n');
	    System.out.println("------------------------ For cluster centers -------------------------");
	    System.out.print('\n');
	    System.out.print('\n');
        
	    for(int i=0;i<K_Val;i++)
        {
        	for(int j=0;j<=New_Column_Count;j++)
        	{
        		System.out.print(cluster_points[i][j]);
        		System.out.print('\t');
        	}
        System.out.print('\n');
        }
      	
        int normalize_Flag = 0;
		//Checking if normalization is a parameter//
		for(int i=0; i<args.length; i++){
			if(args[i].equals("-normalize")){
				normalize_Flag = 1;				
			}
		}
	
		
	  if(normalize_Flag == 1){	
      double Add1 = 0.0;  
      double Add2 = 0.0;
      DecimalFormat decimal_number = new DecimalFormat("##.000000");
      Double[] Average = new Double[New_Column_Count];
      Double[] SD =new Double[New_Column_Count];
      String Average_Display = "\t";
      String SD_Display = "\t";
      
      //Average Calculation//
      for (int i =0; i < New_Column_Count; i++) {
      	for(int j=0; j < Row_Count;j++){
      		Add1 += fileData_Array[j][i];
          	Add2 += fileData_Array[j][i] * fileData_Array[j][i];
      	}
      	Average[i] = Add1/Row_Count;
      	
      	//Variance calculation//
          double variance = (Row_Count * Add2 - Add1 * Add1) / (Row_Count * Row_Count);
          SD[i] = Math.sqrt(variance);
          Average_Display += decimal_number.format(Add1/Row_Count) +" ";
          SD_Display += decimal_number.format(Math.sqrt(variance)) +" ";
          Add1 = 0.0;
          Add2 = 0.0;             
      	}
      temp_Output_Write.append(Average_Display);
      temp_Output_Write.append('\n');
      temp_Output_Write.append(SD_Display);
      
      double[][] normalizedData= new double[Row_Count][New_Column_Count+1];
      
      for (int i =0; i < New_Column_Count; i++) {
      	for(int j=0; j < Row_Count;j++){
      		normalizedData[j][i] = (fileData_Array[j][i] - Average[i]) /SD[i];
      	}
		}
      
      for (int i =0; i < Row_Count; i++) {
      	for(int j=0; j < New_Column_Count;j++){
      		Output_Write.append('\t');
      		Output_Write.append( decimal_number.format(normalizedData[i][j]));
      		Output_Write.append("	");
      	}
      	Output_Write.append('\n');
		}  
      
      
      for (int i =0; i < Row_Count; i++) {
        	for(int j=0; j < Column_Count;j++){
        		 fileData_Array[i][j] = normalizedData[i][j];
        	}
      }
	  }
        
        
      BasicKmeans(K_Val);
       
      System.out.print('\n');
      System.out.print('\n');
      System.out.print("----------------- For Clustering ------------------------");
      System.out.print('\n');
      System.out.print('\n');
      for(int i=0;i<Row_Count;i++)
	    {
	      	for(int j=0;j<=New_Column_Count;j++)
	      	{
	      		System.out.print(basic_Cluster[i][j]);
	      		System.out.print('\t');
	      	}
	      System.out.print('\n');
	     }
      
	}

}
