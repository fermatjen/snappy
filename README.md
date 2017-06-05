![Build Status](https://travis-ci.org/fermatjen/snappy.svg?branch=master)

# Snappy
A Java ML API for compacting and classifying user comments. Snappy is based on a sophisticated NLP pipeline that can:

* Reduce a fragment of text to its most basic form for a given class label
* Train models to classify text
* Test models on text

Snappy's core engine is based on a Neural Gram Model that can extract and score word grams and POS grams along with their scores. Unlike conventional ML techniques, Snappy is based on a unique combinational approach for classifying text. 

Snappy, broadly, works in the following way:

* Prediction labels and clusters are manually built.
* Every instance of the input data set is an "Incident". Incidents are loaded from an external data set.
* NGrams are created from the Incidents. Unigram (1-word), bigram (2-words), trigram (3-words), and quadgram (4-words),  maps of phrases and their frequencies are calculated for every class label.
* Similiarly, POS maps are created for the most significant nouns and verbs occuring in the gram vicinity.
* Sentences are extracted from the Incidents list.
* Each sentence in an incident is scored against the popularity of the NGrams so that the sentences are scored more if they have most frequently occurring "longer grams" than "shorter grams". Also, the sentences are scored based on POS heat maps.

# Build

```
gradle build
```

# Test

```
gradle run
```

# Prepare dataset
Sample dataset is available in the data directory. Every line in the dataset corresponds to an Incident. The labels file contains the taining set in the following form:

```
Label (phrase1, phrase2, phrasen)
```

# Configure 
Edit build/classes/snappy.properties file:

```
# Snappy Configuration File
# This file contains the basic configuration
# file to externally control Snappy.

# File containing the raw text. each line in this file
# is an "Incident". Lines are delimited by the new line char
dataFile=C:\\SnappyGIT\\data\\data.csv

# File that will be used to write the summary
summaryFile=C:\\SnappyGIT\\data\\datasum.csv

# Learning bias file. See the example file in the
# data directory. NGrams with lesser scores can be biased over
# NGrams with higher scores
biasFile=C:\\SnappyGIT\\data\\bias.txt

# The models file path where individual models are stored
# For the first time, when there are no models, run with
# mode = training to generate these files
modelFile=C:\\SnappyGIT\\data\\DISU_MODELS\\

# Training data set
# Training data containing labels and synomnys/phrases
# For example, LABEL (PHRASE1, PHRASE2)
trainingFile = C:\\SnappyGIT\\data\\labels.txt

# Process only these number of lines in the raw data file
processOnly = 200

# Select the mode for the current run (testing or training)
mode = training

# Fast mode will decrease teh training and testing time
# duration but certain NLP features are disabled. For instance,
# grams are not lemmatized for comparison
fastmode = yes

# Single-label or Mult-label prediction
singlelabel = no

# Multi-variate prediction
multivariate = yes
```

# Training 

```java
        //Get all trainer models
        ArrayList trainerModelList = loadTrainingFile(trainingFile);

        for (int i = 0; i < trainerModelList.size(); i++) {
            TrainerModel trainerModel = (TrainerModel) trainerModelList.get(i);
            String label = trainerModel.getLabel();
            label = replace(label, " ", "_", 0);
            File modelFilePath = new File(modelFile, "s_" + label + ".ser");
            //Start Learning
            Learner learner = new Learner(new NeuralGramModel(), dataFile, trainerModel, processOnly);
            learner.startLearning();
            learner.printLearnStats();
            learner.writeIncidents(summaryFile);
            learner.updateModel(true, modelFilePath.getAbsolutePath());

            learner.printAllGrams();
        }
```

# Testing

```java
        int processOnly = 10;
        boolean processLemma = true;
        boolean singleLabel = true;
        
        //Predicts multiple variables that are not prevoisuly trained
        boolean isMultivariate = true;

        //A line of text lesser than these chars are not processed for prediction
        int threshold = 40;
        
        //Start testing
        //Load learning bias vectors
        HashMap biasMap = snappy.util.io.IOUtils.loadBiasMapFromFile(biasFile);

        //Load from the model dir
        File modelFilePath = new File(modelDir);
        File children[] = modelFilePath.listFiles();
        
        ArrayList neuralGramModelList = new ArrayList();

        for (File mFile : children) {
            if (mFile.isFile()) {
                String fpath = mFile.getAbsolutePath();
                
                if (fpath.endsWith(".ser")) {
                
                    //Create a Learner object
                    Learner learner = new Learner(new NeuralGramModel(), null, null, processOnly, processLemma);
                    learner.loadModels(fpath);
                    
                    learner.printLearnStats();
                    
                    //Get the neural gram model
                    NeuralGramModel neuralGramModel = learner.getModel();
                    neuralGramModelList.add(neuralGramModel);
                }
                
            }
        }

        //Write prediction results to a file
        Predictor.writePredictions(biasMap, dataFile, neuralGramModelList, "result.csv", processOnly, threshold, singleLabel, isMultivariate, processLemma);

```
