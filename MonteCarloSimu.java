package com.zfy.test1;

import java.util.ArrayList;
import java.util.Random;

public class MonteCarloSimu {

	static int freqNum=256;
	static int N=15;
	static int simuTime=10;
	static int maxUserNum=5;
	static Random random=new Random();
	static int maxDelay=5;
	
	public static void main(String[] args)
	{
		System.out.println(simu());
	}
	
	public static int[] simu()
	{
		int[] finish_time=new int[maxUserNum];
		for(int simu_time=1;simu_time<=simuTime;simu_time++)
		{
			System.out.println("simu "+simu_time+" times");
			for(int userNum=1;userNum<=maxUserNum;userNum++)
			{
				int[] index=new int[userNum+1];
				int[] randomDelay=new int[userNum+1];
				
				ArrayList<ArrayList<Integer>> freqs=new ArrayList<ArrayList<Integer>>();
				
				//init freqs
				for(int i=0;i<=userNum;i++)
				{
					freqs.add(new ArrayList<Integer>());
					for(int j=0;j<N;j++)
					{
						freqs.get(i).add(random.nextInt(freqNum));
					}
					
					System.out.println(freqs.get(i));
				}
				
				
				
				
				//calculate
				int min_index=getArrayMin(index);
				while(min_index<N)
				{
					for(int i=0;i<=userNum;i++)
					{
						if(index[i]>=N) continue;
						boolean current_flag=false;
						for(int k=i+1;k<=userNum;k++)
						{
							if(index[k]>=N) continue;
							if(freqs.get(i).get(index[i]).equals(freqs.get(k).get(index[k])))
							{
								current_flag=true;
								randomDelay[k]+=(random.nextInt(maxDelay)+1);
							}
							else
							{
								if(randomDelay[k]==0)
								{
									index[k]+=1;
								}
								else
								{
									randomDelay[k]-=1;
								}
							}
						}
						if(current_flag)
						{
							current_flag=false;
							randomDelay[i]+=(random.nextInt(maxDelay)+1);
						}
					}
					min_index=getArrayMin(index);
					System.out.println(min_index);
					finish_time[userNum]++;
				}
				
			}//end of user num cycle
			System.out.println("finish "+simu_time+" times");
		}//end of one time simu
		return finish_time;
	}
	
	public static int getArrayMin(int[] array)
	{
		int min=array[0];
		for(int i=1;i<array.length;i++)
		{
			if(array[i]<min)
			{
				min=array[i];
			}
		}
		return min;
	}
	
}
