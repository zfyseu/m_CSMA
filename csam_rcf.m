clear
clc

%% Global Parameter
maxSimuTime=1;
freqNum=256;
maxOtherUserNum=100;
dataLen=256;


%% Simu Process

for simu_time=1:maxSimuTime
    
    delay=zeros(1,maxOtherUserNum+1);
    for userNum=1:maxOtherUserNum+1
        
        
        
        %collection to record user hop seqs
        hopseqs=zeros(userNum,dataLen);
        
        %get initial state for every couple of user
        state=zeros(1,userNum);
        state(1)=1;
        for user=2:userNum
         state(user)=randomInitialState();   
        end
        
        % state hop
        t=1;
       while state(1)<=dataLen
           
           %isChangeFreq
           isChangeFreq=zeros(1,userNum);
           
           for user=1:userNum
            if state(user)==-1
                hopseqs(user,t)=state(user);
                tmp=rand();
                if tmp<0.1
                   state(user)=1; 
                end
            else if state(user)~=-2
                    cur_f=randperm(256,1);
                    hopseqs(user,t)=cur_f;
%                     state(user)=state(user)+1;
%                     if state(user)==257
%                        state(user)=-2; 
%                     end
                else
                    hopseqs(user,t)=state(user);
                end   
            end  
           end
           
           %random change freqency
           for i=1:userNum
               if isChangeFreq(i)==1||state(i)<0
                  continue; 
               end
               stdval=hopseqs(i,t);
              for j=i+1:userNum
                  if isChangeFreq(i)==1||state(i)<0
                     continue; 
                  end
                  if stdval==hopseqs(j,t)
                     if isChangeFreq(i)==0
                        isChangeFreq(i)=1;
                     end
                     isChangeFreq(j)=1;
                  end
              end
           end
           
           for i=1:userNum
              if isChangeFreq(i)==0&&state(i)>0
                  state=state+1;
              else if state(i)>0
                 hopseqs(i,t)=0; 
                  end
              end
              isChangeFreq(i)=0; 
           end
          
           % time slot
           t=t+1;
       end
        delay(user)=t;
       
    end
    plot(delay);
  
end


%% plot


