InstallPackages <- function(){
  packagelist <- c("klaR","caret","party","rpart","class","e1071","neuralnet","adabag","ipred","ada","randomForest","gbm")
  new.packages <- packagelist[!(packagelist %in% installed.packages()[,"Package"])]
  if(length(new.packages)) install.packages(new.packages)
  lapply(packagelist, require, character.only = TRUE)
}

LoadDataAndPreprocess <- function(f){
  trainingData = read.table( header = FALSE, sep=",", url = f)
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