/*
 * Copyright (C) 2017 Frank Jennings
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package snappy.dl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;
import static java.util.logging.Level.INFO;
import java.util.logging.Logger;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.NGramTokenizerFactory;
import snappy.pos.POSScrapper;
import snappy.pos.StopWords;
import snappy.util.io.IOUtils;
import static snappy.util.text.StringUtils.replace;

/**
 *
 * @author fjenning
 */
public class DeepLearner {

    private int minWordFrequency;
    private int epochs;
    private boolean useAdaGrad;
    private int iterations;
    private int layerSize;
    private int seed;
    private int windowSize;
    private int maxGrams;

    private POSScrapper posScrapper;
    private ArrayList swordsList;

    private static final Logger LOG = Logger.getLogger(DeepLearner.class.getName());

    public DeepLearner(POSScrapper posScrapper, int minWordFrequency, int epochs, boolean useAdaGrad, int iterations, int layerSize, int seed, int windowSize, int maxGrams) {
        this.posScrapper = posScrapper;
        this.minWordFrequency = minWordFrequency;
        this.epochs = epochs;
        this.useAdaGrad = useAdaGrad;
        this.iterations = iterations;
        this.layerSize = layerSize;
        this.seed = seed;
        this.windowSize = windowSize;
        this.maxGrams = maxGrams;

        StopWords stopWords = new StopWords();
        swordsList = stopWords.getSwords();
    }

    public DeepLearner(POSScrapper posScrapper) {
        this.posScrapper = posScrapper;
        this.minWordFrequency = 1;
        this.epochs = 1;
        this.useAdaGrad = false;
        this.iterations = 20;
        this.layerSize = 100;
        this.seed = 47;
        this.windowSize = 5;
        this.maxGrams = 2;

    }

    public Word2Vec createModelFromFile(File filePath) {

        SentenceIterator iter = new LineSentenceIterator(filePath);
        iter.setPreProcessor((String sentence) -> {

            sentence = sentence.toLowerCase();

            ArrayList phrases = posScrapper.getPhrases(sentence, "NP");
            ArrayList vphrases = posScrapper.getPhrases(sentence, "VP");

            phrases.addAll(vphrases);

            String msentence = sentence;

            for (int i = 0; i < phrases.size(); i++) {
                String phrase = ((String) phrases.get(i)).trim();
                if (phrase.contains(" ")) {
                    String mphrase = replace(phrase, " ", "_", 0);
                    msentence = replace(msentence, phrase, phrase + " " + mphrase, 0);
                }
            }

            msentence = " " + msentence + " ";
            for (int i = 0; i < swordsList.size(); i++) {
                String swordA = " " + (String) swordsList.get(i) + " ";
                msentence = replace(msentence, swordA, " ", 0);
            }

            return msentence;

        });

        NGramTokenizerFactory t = new NGramTokenizerFactory(new DefaultTokenizerFactory(), 1, maxGrams);

        LOG.log(INFO, "Building Word2vec model...");

        Word2Vec vecModel = new Word2Vec.Builder()
                //Minimum number of times a word must appear in the corpus
                .minWordFrequency(minWordFrequency)
                .epochs(epochs)
                .useAdaGrad(useAdaGrad)
                .iterations(iterations)
                .layerSize(layerSize)
                .seed(seed)
                .windowSize(windowSize)
                .iterate(iter)
                .tokenizerFactory(t)
                .build();

        LOG.log(INFO, "Fitting Word2vec model...");
        vecModel.fit();

        return vecModel;
    }

    public Collection<String> getNearestWords(Word2Vec vecModel, String query, int limit) {
        return vecModel.wordsNearest(query, limit);
    }

    public void saveModelToFile(Word2Vec vecModel, File filePath) {
        // Write word vectors to file
        WordVectorSerializer.writeWord2VecModel(vecModel, filePath);
    }

    public Word2Vec loadModelFromFile(File filePath) {
        //Load learning model
        return WordVectorSerializer.readWord2VecModel("C:\\S2V\\data\\test.model");
    }

    public void writePhraseOntology(Word2Vec vecModel, File phraseOntologyPath, int maxConenctions, ArrayList seeds) {

        ArrayList ontologyList = new ArrayList();

        String vocabs = "" + vecModel.getVocab().words();
        vocabs = replace(vocabs, "[", "", 0);
        vocabs = replace(vocabs, "]", "", 0);

        StringTokenizer stok = new StringTokenizer(vocabs, ",");

        while (stok.hasMoreTokens()) {

            String vocab = stok.nextToken().trim();

            int numTokens = (new StringTokenizer(vocab, "_")).countTokens();

            if (numTokens < 3) {
                continue;
            }
            if (vocab.contains(" ")) {
                continue;
            }
            if (!vocab.contains("_")) {
                continue;
            }

            String phrase = replace(vocab, "_", " ", 0);
            phrase = replace(phrase, ".", "", 0);

            Collection<String> nearest = vecModel.wordsNearest(vocab, maxConenctions);

            String connections = "" + nearest;
            connections = replace(connections, "[", "", 0);
            connections = replace(connections, "]", "", 0);

            String realConnections = "";

            ArrayList realConnectionList = new ArrayList();

            StringTokenizer stok2 = new StringTokenizer(connections, ",");

            while (stok2.hasMoreTokens()) {

                String connection = stok2.nextToken().trim();

                StringTokenizer stok3 = new StringTokenizer(connection, " ");

                while (stok3.hasMoreTokens()) {

                    String realConnection = stok3.nextToken().trim();

                    for (int i = 0; i < swordsList.size(); i++) {
                        String swordA = " " + (String) swordsList.get(i) + "";
                        realConnection = replace(realConnection, swordA, " ", 0);
                        String swordB = " " + (String) swordsList.get(i) + "";
                        realConnection = replace(realConnection, swordB, " ", 0);
                    }

                    realConnection = realConnection.trim();

                    if (realConnection.length() > 2) {
                        if (!realConnectionList.contains(realConnection)) {
                            realConnections = realConnections + ", " + realConnection;
                            realConnectionList.add(realConnection);
                        }
                    }
                }
            }

            if (realConnections.trim().startsWith(",")) {
                realConnections = (realConnections.substring(1, realConnections.length())).trim();
            }

            realConnections = replace(realConnections, "_", " ", 0);
            realConnections = replace(realConnections, "(", "", 0);
            realConnections = replace(realConnections, ")", "", 0);
            realConnections = replace(realConnections, ".", "", 0);

            phrase = replace(phrase, "_", " ", 0);
            phrase = replace(phrase, "(", "", 0);
            phrase = replace(phrase, ")", "", 0);
            phrase = replace(phrase, ".", "", 0);

            if (realConnections.trim().length() > 2) {
                ontologyList.add(phrase + " (" + realConnections + ")");
            }

            IOUtils.writeListToFile(ontologyList, phraseOntologyPath.getAbsolutePath());
        }
    }
}
