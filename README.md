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

For building Snappy

```
gradle build
```


