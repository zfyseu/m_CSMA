package com.zfy.test;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MonterCarloSimu {

	static int freqNum=256;
	static int N=256;
	static int simuTime=100;
	static int maxUserNum=500;
	static Random random=new Random();
	static int maxDelay=2;
	
	public static void main(String[] args) throws IOException
	{
		System.out.println("simu start:");
		long startTime=System.currentTimeMillis();
		csma_RandomChangeFreq();
		long endTime=System.currentTimeMillis();
		System.out.println("Total time:"+(endTime-startTime)+"ms");
	}
	
	public static void csma_RandomChangeFreq() throws IOException
	{
		ArrayList<Integer> maxRunTime_average=new ArrayList<Integer>();
		for(int i=0;i<maxUserNum;i++)
		{
			maxRunTime_average.add(0);
		}
		for(int simu_time=1;simu_time<=simuTime;simu_time++)
		{
			ArrayList<Integer> maxRunTime=new ArrayList<Integer>();
			for(int userNum=1;userNum<=maxUserNum;userNum++)
			{
				int[] index=new int[userNum+1];
				ArrayList<ArrayList<Integer>> freqs=new ArrayList<ArrayList<Integer>>();
				ArrayList<ArrayList<Integer>> hopseqs=new ArrayList<ArrayList<Integer>>();
				
				
				//System.out.println("OTHER user NUM:"+userNum);
				
				//init freqs
				for(int i=0;i<=userNum;i++)
				{
					freqs.add(new ArrayList<Integer>());
					hopseqs.add(new ArrayList<Integer>());
					for(int j=0;j<N;j++)
					{
						freqs.get(i).add(random.nextInt(freqNum)+1);
					}
					
					//System.out.println(freqs.get(i));
				}
				
				//System.out.println();
				
				//caculate hop seqs
				int min_index=getArrayMin(index);
				while(min_index<N)
				{
					boolean[] isChange=new boolean[userNum+1];
					//set state
					for(int i=0;i<=userNum;i++)
					{
						if(isChange[i]||index[i]>=N)
						{
							continue;
						}
						int stdval=freqs.get(i).get(index[i]);
						for(int j=i+1;j<=userNum;j++)
						{
							if(isChange[j]||index[j]>=N) continue;
							if(stdval==freqs.get(j).get(index[j]))
							{
								if(!isChange[i]) 
								{
									freqs.get(i).set(index[i], (random.nextInt(freqNum)+1));
									isChange[i]=true;
								}
								isChange[j]=true;
								freqs.get(j).set(index[j], (random.nextInt(freqNum)+1));
							}
						}
					}
					for(int i=0;i<=userNum;i++)
					{
						if(!isChange[i]&&index[i]<N)
						{
							hopseqs.get(i).add(freqs.get(i).get(index[i]));
							index[i]+=1;
						}
						else if(index[i]<N)
						{
							hopseqs.get(i).add(0);
						}
						isChange[i]=false;
					}
					min_index=getArrayMin(index);
				}
				

				int max=hopseqs.get(0).size();
				for(int i=0;i<hopseqs.size();i++)
				{
					//System.out.println(hopseqs.get(i));
					if(max<hopseqs.get(i).size())
					{
						max=hopseqs.get(i).size();
					}
				}
				maxRunTime.add(max);
				//System.out.println("**************************************************************");
				//System.out.println();System.out.println();System.out.println();
				
			}
			for(int i=0;i<maxUserNum;i++)
			{
				maxRunTime_average.set(i,(maxRunTime_average.get(i)+maxRunTime.get(i)));
			}
			String name_simu="simu-"+simu_time+".txt";
			ArrayToFile(maxRunTime,name_simu);
		}
		for(int i=0;i<maxUserNum;i++)
		{
			maxRunTime_average.set(i, (maxRunTime_average.get(i))/simuTime);
		}
		ArrayToFile(maxRunTime_average,"averageRunTime.txt");
	}
	
	public static void ArrayToFile(ArrayList<Integer> result,String fileName) throws IOException
	{
		String filePathAndName="F:\\M file\\RSSI\\additional work\\paper\\csma_hop\\"+fileName;
		PrintWriter file=new PrintWriter(new FileWriter(filePathAndName));
		for(int i=0;i<result.size();i++)
		{
			file.println(result.get(i));
		}
		file.close();
	}
	
	public static int[] simu()
	{
		int[] finish_time=new int[maxUserNum+1];
		for(int simu_time=1;simu_time<=simuTime;simu_time++)
		{
			//System.out.println("simu "+simu_time+" times");
			for(int userNum=1;userNum<=maxUserNum;userNum++)
			{
				int[] index=new int[userNum+1];
				int[] randomDelay=new int[userNum+1];
				
				ArrayList<ArrayList<Integer>> freqs=new ArrayList<ArrayList<Integer>>();
				ArrayList<ArrayList<Integer>> hopseqs=new ArrayList<ArrayList<Integer>>();
				
				//init freqs
				for(int i=0;i<=userNum;i++)
				{
					freqs.add(new ArrayList<Integer>());
					hopseqs.add(new ArrayList<Integer>());
					for(int j=0;j<N;j++)
					{
						freqs.get(i).add(random.nextInt(freqNum));
					}
					//System.out.println(freqs.get(i));
				}
				
				//calculate hop seq
				int min_index=getArrayMin(index);
				while(min_index<N)
				{
					
					boolean[] isSame=new boolean[userNum+1];
					for(int i=0;i<=userNum;i++)
					{
						if(randomDelay[i]>0)
						{
							randomDelay[i]-=1;
							hopseqs.get(i).add(-1);
						}
					}
					
					//set state of each user
					for(int i=0;i<=userNum;i++)
					{
						if(index[i]>=N) continue;
						if(randomDelay[i]>0)
						{
							continue;
						}
						if(isSame[i]) 
						{
							isSame[i]=false;
							continue;
						}
						for(int k=i+1;k<=userNum;k++)
						{
							if(index[k]>=N||isSame[k]) continue;
							if(freqs.get(i).get(index[i]).equals(freqs.get(k).get(index[k])))
							{
								if(!isSame[i])
								{
									randomDelay[i]+=(random.nextInt(maxDelay)+1);
									isSame[i]=true;
								}
								isSame[k]=true;
								randomDelay[k]+=(random.nextInt(maxDelay)+1);
							}
						}
					}
					
					for(int i=0;i<=userNum;i++)
					{
						if(randomDelay[i]==0&&index[i]<N)
						{
							hopseqs.get(i).add(freqs.get(i).get(index[i]));
							index[i]+=1;
						}
					}
					min_index=getArrayMin(index);
				}
				
				
				//reset isSame
				System.out.println("*********hopseq**"+simu_time+" times"+"***************");
				System.out.println("user NUM:"+userNum);
				for(ArrayList<Integer> hopseq:hopseqs)
				{
					System.out.println(hopseq);
				}
				System.out.println("**************************************************************");
				System.out.println();System.out.println();System.out.println();
				
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
