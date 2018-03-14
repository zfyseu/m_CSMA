clear
clc

%% Global Parameter
maxSimuTime=1;
freqNum=256;
maxOtherUserNum=100;
dataLen=256;


%% Simu Process

for simu_time=1:maxSimuTime
    
    collisionNums=zeros(1,maxOtherUserNum+1);
    for userNum=1:maxOtherUserNum+1
        
        %collison nums
        collisionNum=0;
        
        %collection to record user hop seqs
        hopseqs=zeros(userNum,dataLen);
        
        %get initial state for every couple of user
        state=zeros(1,userNum);
        state(1)=1;
        for user=2:userNum
         state(user)=randomInitialState();   
        end
        
        % state hop
       for t=1:dataLen
           isCollision=0;
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
                    state(user)=state(user)+1;
                    if state(user)==257
                       state(user)=-2; 
                    end
                else
                    hopseqs(user,t)=state(user);
                end   
            end  
           end
           
           %to judeg whether collision exsits for user 1
           for i=2:userNum
               if state(i)>0
                   if hopseqs(i,t)==hopseqs(1,t)
                       isCollision=1;
                       break;
                   end
               end
           end
           if isCollision==1
            collisionNum=collisionNum+1;
           end    
       end
       
       collisionNums(user)=collisionNum;
    end
    plot(collisionNums);
    grid on;
end


%% plot


