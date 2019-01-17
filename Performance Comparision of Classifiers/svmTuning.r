#SVM Tuning process

#initial method
svmModel <- svm(V35~., data = traind)
x = subset(traind, select = -V35)
y = traind$V35
svm(x,y)
svm_tune <- tune(svm, train.x=x, train.y=y,kernel="radial", ranges=list(cost=10^(-1:2), gamma=c(.5,1,2)))
print(svm_tune)
 # Parameter tuning of 'svm':
  
  #- sampling method: 10-fold cross validation 

#- best parameters:
 # cost gamma
  #10   0.5

#- best performance: 0.08896220202 