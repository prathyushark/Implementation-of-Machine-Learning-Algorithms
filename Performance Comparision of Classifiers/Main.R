InstallPackages <- function(){
  packagelist <- c("klaR","caret","party","rpart","class","e1071","neuralnet","adabag","ipred","ada","randomForest","gbm")
  new.packages <- packagelist[!(packagelist %in% installed.packages()[,"Package"])]
  if(length(new.packages)) install.packages(new.packages)
  lapply(packagelist, require, character.only = TRUE)
}

LoadDataAndPreprocess <- function(f){
  trainingData = read.table( header = FALSE, sep=",", url(f) )
  a <- trainingData == '?'
  is.na(trainingData) <- a
  trainingData <- na.omit(trainingData)
  indx <- sapply(trainingData, is.factor)
  trainingData[indx] <- lapply(trainingData[indx], function(x) as.numeric(as.character(x)))
  
  maxs=apply(trainingData,MARGIN = 2,max)
  mins=apply(trainingData,MARGIN = 2,min)
  scaled <- as.data.frame(scale(trainingData, center = mins, scale = maxs - mins))
  
  return (scaled)
}
InstallPackages()
scaled <- LoadDataAndPreprocess(f= "http://archive.ics.uci.edu/ml/machine-learning-databases/dermatology/dermatology.data")
columns <- c("V1","V2","V3","V4","V5","V6","V7","V8","V9","V10","V11","V12","V13","V14","V15","V16","V17","V18","V19","V20","V21","V22","V23","V24","V25","V26","V27","V28","V29","V30","V31","V32","V33","V34")

sigmoid = function(x) {
  1 / (1 + exp(-x))
}

dtSum=0
pSum = 0
nnSum = 0
dlSum = 0
svmSum = 0
nbSum = 0
lrSum=0
knnSum = 0
baggSum = 0
rSum = 0
adbSum = 0
gbmSum=0

dtPrec=0
pPrec = 0
nnPrec = 0
dlPrec = 0
svmPrec = 0
nbPrec = 0
lrPrec=0
knnPrec = 0
baggPrec = 0
rPrec = 0
adbPrec = 0
gbmPrec=0
options(warn=-1)



cat("Waiting while creating models......")
#using 10 fold verifictaion
folds <- cut(seq(1,nrow(scaled)),breaks=10,labels=FALSE)
for(i in 1:10) {
  testIndexes <- which(folds==i,arr.ind=TRUE)
  testd <- scaled[testIndexes, ]
  traind <- scaled[-testIndexes, ]
  
  
  #for boosting and LR model - Class values assumed to be 0 for [0-0.5] or 1 for (0.5-1]
  temptrainData<-traind
  temptrainData$V35[temptrainData$V35 >0.5] = 1
  temptrainData$V35[temptrainData$V35 <=0.5] = 0
  temptestData <- testd
  temptestData$V35[temptestData$V35 >0.5] = 1
  temptestData$V35[temptestData$V35 <=0.5] = 0
  
  #Decision tree
  train_tree = rpart(V35~., data=traind, parms = list(split="information"), control = rpart.control(minsplit = 3,minbucket = 1, cp = 0.001, maxdepth = 30, maxcompete = 10))
  testpred = predict(train_tree, newdata=testd)
  conf <- table(pred = round(testpred, digits=1), true = testd$V35)
  accuracy <- sum(testd$V35==round(testpred, digits = 1))/length(testpred)
  accuracy <- accuracy*100
  dtSum = dtSum+accuracy
  dtPrec = dtPrec+(diag(conf)*100/apply(conf,2,sum))
  #cat("Decision Tree Sample  = ", i,", accuracy= ", accuracy,"precision is : ", dtPrec,"\n")
  
  
  
  #Perceptron Model
  myformula <- as.formula(paste0('V35 ~ ', paste(names(traind[!names(traind) %in% 'V35']),collapse = ' + ')))
  pmodel = neuralnet(myformula, data= traind,hidden = 0,linear.output = F, threshold = 0.1)
  testSample <- subset(testd, select=columns)
  pred <- compute(pmodel, testSample)
  accuracy <- 100*sum(round(pred$net.result,digits = 1)==testd$V35)/length(testd$V35)
  pSum = pSum + accuracy
  conf <- table(pred = round(pred$net.result, digits=1), true = testd$V35)
  pPrec = pPrec+(diag(conf)*100/apply(conf,2,sum))
  #cat("Perceptron model Sample : ", i," accuracy= ", accuracy,"precision is : ", pPrec,"\n")
  
  
  #neural net
  myformula <- as.formula(paste0('V35 ~ ', paste(names(traind[!names(traind) %in% 'V35']),collapse = ' + ')))
  nnmodel = neuralnet(myformula, data= traind,hidden = c(5,5,5,3,5),algorithm = 'rprop+',linear.output = F, threshold = 0.1)
  testSample <- subset(testd, select=columns)
  pred <- compute(nnmodel, testSample)
  accuracy <- 100*sum(round(pred$net.result,digits = 1)==testd$V35)/length(testd$V35)
  nnSum = nnSum + accuracy
  conf <- table(pred = round(pred$net.result, digits=1), true = testd$V35)
  nnPrec = nnPrec+(diag(conf)*100/apply(conf,2,sum))
  #cat("NeuralNet model Sample : ", i," accuracy= ", accuracy,"precision is : ", nnPrec,"\n")
  #plot(nnmodel)
  
  
  #DeepLearning
  myformula <- as.formula(paste0('V35 ~ ', paste(names(traind[!names(traind) %in% 'V35']),collapse = ' + ')))
  dlmodel = neuralnet(myformula, data= traind,hidden = c(10,10,10,10,10,10,10,10),linear.output = F, threshold = 0.1)
  testSample <- subset(testd, select=columns)
  pred <- compute(dlmodel, testSample)
  accuracy <- 100*sum(round(pred$net.result,digits = 1)==testd$V35)/length(testd$V35)
  dlSum = dlSum + accuracy
  conf <- table(pred = round(pred$net.result, digits=1), true = testd$V35)
  dlPrec = dlPrec+(diag(conf)*100/apply(conf,2,sum))
  #cat("DeepLearning model Sample : ", i," accuracy= ", accuracy,"precision is : ", dlPrec,"\n")
  
  
  #SVM Model
  # best parameters after tuning:
  # cost gamma
  #10   0.5
  #for less gamma/cost, accuracy goes down
  svmModel <- svm(V35~., data = traind, kernel="radial", cost = 10, gamma = 0.5)
  SVMprediction <- predict(svmModel, testd)
  conf <- table(pred = round(SVMprediction+0.05, digits=1), true = testd$V35)
  accuracy <-sum(diag(conf))/length(testd$V35)
  accuracy <-accuracy*100
  svmSum = svmSum+accuracy
  svmPrec = svmPrec+(diag(conf)*100/apply(conf,2,sum))
  #cat("SVM Model - Sample", i," accuracy= ", accuracy,"precision is : ", svmPrec,"\n")
  
  
  #Naive Bayesian
  model <- naiveBayes(as.factor(traind$V35) ~ ., data = traind)
  pred <- predict(model, testd)
  tab <- table(pred, testd$V35)
  accuracy <- sum(diag(tab))/sum(tab)
  accuracy <-accuracy*100
  nbSum = nbSum+ accuracy
  nbPrec = nbPrec+(diag(tab)*100/apply(tab,2,sum))
  #cat("Naive Bayesian Sample ", i,", accuracy= ", accuracy,"precision is : ", nbPrec,"\n")
  
  #Logistic Regression
  lrmodel <- glm(V35~.,data=temptrainData,family=binomial(link="logit"))
  pred = predict(lrmodel, type="response", newdata=temptestData)
  accuracy <- sum(temptestData$V35==round(pred, digits = 1))/length(temptestData$V35)
  accuracy <- accuracy*100
  lrSum = lrSum + accuracy
  conf <- table(pred = round(pred, digits=1), true = testd$V35)
  lrPrec = lrPrec+ (diag(conf)/apply(conf,2,sum))
  #cat("Logistic Regression Sample ", i," accuracy= ", accuracy,"precision is : ", lrPrec,"\n")
  
  #KNN Model
  #k=1 - accuracy= 94.44444444
  #k=4 - accuracy = 97.22222222
  #k=5 - accuracy = 100
  pred <- knn(train=traind, test=testd,cl=traind$V35, k=2,prob = FALSE, use.all = TRUE)
  accuracy = 100* sum(pred == testd$V35)/length(pred)
  knnSum = knnSum+ accuracy
  conf <- table(pred = round(as.numeric(pred), digits=1), true = testd$V35)
  knnPrec = knnPrec+(diag(conf)*100/apply(conf,2,sum))
  #cat("kNN Model Sample ",i , " accuracy= ", accuracy,"\n")
  
  
  #Bagging
  bgmodel <- bagging(V35~., data=traind, coob=TRUE)
  bgpred <- predict(bgmodel, testd)
  result <- data.frame(actual = testd$V35, prediction = bgpred)
  accuracy <- sum(round(result$prediction,digits = 1)==testd$V35)/length(testd$V35)
  accuracy <- accuracy*100
  baggSum = baggSum+ accuracy
  conf <- table(pred = round(as.numeric(bgpred), digits=1), true = testd$V35)
  baggPrec = baggPrec+(diag(conf)*100/apply(conf,2,sum))
  #cat("Bagging Sample ", i," accuracy= ", accuracy,"\n")
  
  #Random Forest
  rfModel <- randomForest(as.factor(traind$V35)~., data=traind, importance=TRUE, proximity=TRUE, ntree=500)
  pred <- predict(rfModel,testd,type='response')
  predTable <- table(observed = testd$V35, predicted = pred)
  accuracy <- sum(diag(predTable))/sum(predTable)
  accuracy <-accuracy*100
  rSum=rSum+accuracy
  conf <- table(pred = round(as.numeric(pred), digits=1), true = testd$V35)
  rPrec = rPrec+(diag(conf)*100/apply(conf,2,sum))
  #cat("Random Forest Model Sample ", i," accuracy= ", accuracy,"\n")
  
  
  #Ada boosting
  model <- ada(temptrainData$V35 ~ ., data = temptrainData, iter=20, nu=1, type="discrete")
  pred =predict(model,temptestData)
  accuracy <- sum(temptestData$V35==pred)/length(pred)
  accuracy <- accuracy * 100
  adbSum = adbSum+accuracy
  conf <- table(pred = round(as.numeric(pred), digits=1), true = testd$V35)
  adbPrec = adbPrec+(diag(conf)*100/apply(conf,2,sum))
 # cat("Ada boosting Model Sample ", i," accuracy= ", accuracy,"\n")
  
  #Gradient Boosting
  gb = gbm.fit(temptrainData[,1:(35-1)],temptrainData[,35],n.trees=1,verbose = FALSE,shrinkage=0.5 ,bag.fraction = 0.3 ,interaction.depth = 2,n.minobsinnode = 1, distribution = "bernoulli")
  pred <- predict(gb,temptestData[,1:(35-1)],n.trees=1)
  gbm_table <- table(temptestData[,35], pred)
  accuracy <- (sum(diag(gbm_table)) / sum(gbm_table))*100.0
  gbmSum = gbmSum+accuracy
  conf <- table(pred = round(as.numeric(pred), digits=1), true = testd$V35)
  gbmPrec = gbmPrec+(diag(conf)*100/apply(conf,2,sum))
  #cat("Gradient Boosting Model Sample ", i," accuracy= ", accuracy,"\n")
  
}

#Printing results
cat("::::::::::::::::::: Average Accuracies :::::::::::::::::::: \n")
cat("Decision Tree : ", dtSum/10,"\n")
cat("Precision of Decision Tree ", mean(dtPrec)/10,"\n\n")

cat("Perceptron : ", pSum/10,"\n")
cat("precision of PErceptron  ", mean(pPrec)/10,"\n\n")

cat("NEural NEt : ", nnSum/10,"\n")
cat("Precision of Neural Net ", mean(nnPrec)/10,"\n\n")

cat("Deep Learning : ", dlSum/10,"\n")
cat("Precision of Decision Tree ", mean(dlPrec)/10,"\n\n")

cat("SVM : ", svmSum/10,"\n")
cat("Precision of SVM ", mean(svmPrec)/10,"\n\n")

cat("NAive Bayes : ", nbSum/10,"\n")
cat("Precision of Naive Bayes ", mean(nbPrec)/10,"\n\n")

cat("Logistic Regression :" , lrSum/10, "\n")
cat("Precision of Logistic Regression ", mean(lrPrec)/10,"\n\n")

cat("KNN: ", knnSum/10,"\n")
cat("Precision of KNN ", mean(knnPrec)/10,"\n\n")

cat("Bagging : ", baggSum/10,"\n")
cat("Precision of Bagging ", mean(baggPrec)/10,"\n\n")

cat("Random Forest : ", rSum/10, "\n")
cat("Precision of Random Forest ", mean(rPrec)/10,"\n\n")

cat("Ada Boost Model :" , adbSum/10, "\n")
cat("Precision of AdaBoost Model ", mean(adbPrec)/10,"\n\n")

cat("Gradient Boosting Model : ", gbmSum/10, "\n")
cat("Precision of Gradient Boost ", mean(gbmPrec)/10,"\n\n")
