# Snappy
A Java ML API for compacting and classifying user comments. Snappy is based on a sophisticated NLP pipeline that can:

* Reduce a fragment of text to its most basic form for a given class label
* Train models to classify text
* Test models on text

Snappy's core engine is based on a Nueral Gram Model that can extract and score word grams and POS grams along with their scores. Unlike conventional ML techniques, Snappy is based on a unique combinational approach for classifying text. 

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

# File containing the raw text
dataFile=C:\\SnappyGIT\\data\\data.csv

# File that will be used to write the summary
summaryFile=C:\\SnappyGIT\\data\\datasum.csv

# The models file path where individual models are stored
modelFile=C:\\SnappyGIT\\data\\

# Training data set
trainingFile = C:\\SnappyGIT\\data\\labels.txt

# Process only these number of lines in the raw data file
processOnly = 100
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
            Learner learner = new Learner(new NueralGramModel(), dataFile, trainerModel, processOnly);
            learner.startLearning();
            learner.printLearnStats();
            learner.writeIncidents(summaryFile);
            learner.updateModel(true, modelFilePath.getAbsolutePath());

            learner.printAllGrams();
        }
```

# Testing

```java
        //Start testing
        File modelFilePath = new File(modelFile);
        File children[] = modelFilePath.listFiles();
        ArrayList nueralGramModelList = new ArrayList();

        for (int i = 0; i < children.length; i++) {
            File mFile = children[i];
            if (mFile.isFile()) {
                String fpath = mFile.getAbsolutePath();
                if (fpath.endsWith(".ser")) {
                    Learner learner = new Learner(new NueralGramModel(), null, null, processOnly);
                    learner.loadModels(fpath);
                    learner.printLearnStats();
                    //Get the nueral gram model
                    NueralGramModel nueralGramModel = learner.getModel();
                    nueralGramModelList.add(nueralGramModel);
                }
            }
        }

        //Write prediction results
        boolean singleLabel = false;
        Predictor.writePredictions(dataFile, nueralGramModelList, summaryFile, processOnly, threshold, singleLabel);
```
