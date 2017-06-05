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
package snappy.ngrams;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import snappy.model.NLPModel;
import snappy.model.serialized.NeuralGramModel;
import static snappy.ngrams.Scorer.getGramScore;
import snappy.pos.POSScrapper;
import static snappy.util.collections.Comparator.sortByComparator;
import snappy.util.io.CSVUtils;
import static snappy.util.io.IOUtils.getAllLinesFromFile;
import static snappy.util.text.StringUtils.replace;
import static snappy.util.text.StringUtils.toTitleCase;

/**
 *
 * @author fjenning
 */
public class Predictor {

    private static final Logger LOG = Logger.getLogger(Predictor.class.getName());

    /**
     *
     * @param dataFile
     * @param neuralGramModelList
     * @param outFile
     * @param processOnly
     * @param threshold
     * @param singleLabel
     * @param processLemma
     */
    public static void writePredictions(HashMap biasMap, String dataFile, ArrayList neuralGramModelList, String outFile, int processOnly, int threshold, boolean singleLabel, boolean isMultivariate, boolean processLemma) {

        FileWriter outFileWriter = null;
        POSScrapper posScrapper = new POSScrapper(new NLPModel());

        try {
            // Assuming that the gram and pos maps are filled, start
            // scoring the input dataSet
            outFileWriter = new FileWriter(outFile);
            ArrayList linesList = getAllLinesFromFile(dataFile, processOnly, false);
            for (int i = 0; i < linesList.size(); i++) {

                String line = (String) linesList.get(i);
                String oline = line;
                String pline = line;
                pline = pline.trim();
                pline = pline.replaceAll("\\s+", " ");
                pline = pline.replaceAll("\\p{Punct}+", "");
                pline = pline.replaceAll(" . ", ". ");

                if (line.length() < threshold) {
                    continue;
                }

                //Now get the best label
                // Unpack Neural Grams
                HashMap neuralScoreMap = new HashMap();

                for (int j = 0; j < neuralGramModelList.size(); j++) {

                    NeuralGramModel neuralGramModel = (NeuralGramModel) neuralGramModelList.get(j);

                    HashMap unigramMap = neuralGramModel.getUnigramMap();
                    HashMap bigramMap = neuralGramModel.getBigramMap();
                    HashMap trigramMap = neuralGramModel.getTrigramMap();
                    HashMap quadgramMap = neuralGramModel.getQuadgramMap();
                    HashMap verbMap = neuralGramModel.getVerbMap();
                    HashMap nounMap = neuralGramModel.getNounMap();

                    //Get class label
                    String label = neuralGramModel.getTrainerModel().getLabel();

                    double gramScore = getGramScore(line, unigramMap, bigramMap, trigramMap, quadgramMap, verbMap, nounMap, processLemma);
                    //Score for this neuralgram
                    //Eliminate weak biases
                    if (gramScore > 1) {
                        neuralScoreMap.put(label.toLowerCase(), (int) gramScore);
                    }
                }

                if (neuralScoreMap.size() > 0) {

                    line = replace(line, ",", " ", 0);

                    Map<String, Integer> sortedNeuralScoreMap = sortByComparator(neuralScoreMap, false);

                    Iterator i1 = sortedNeuralScoreMap.keySet().iterator();
                    String predictedLabel = ((String) i1.next()).trim();
                    String lpredictedLabel = predictedLabel.toLowerCase();
                    //outFileWriter.write(predictedLabel+", \""+line+"\"\r\n");
                    if (singleLabel) {

                        //Check prediction bias
                        LOG.log(Level.INFO, "Predicted: " + predictedLabel);
                        if (biasMap.containsKey(lpredictedLabel)) {
                            LOG.log(Level.INFO, "Bias found for {0}", predictedLabel);

                            //Find the weaker player/label
                            ArrayList playerList = (ArrayList) biasMap.get(lpredictedLabel);

                            for (int p = 0; p < playerList.size(); p++) {
                                String player = (String) playerList.get(p);

                                if (player.toLowerCase().equals(lpredictedLabel)) {
                                    continue;
                                }
                                if (neuralScoreMap.containsKey(player)) {
                                    //Winning label
                                    predictedLabel = player;
                                    break;
                                }
                            }
                        }
                    }

                    if (isMultivariate) {
                        //Mult-variate prediction
                        //Get noun and verb tokens
                        ArrayList lineNounList = posScrapper.getNounTokens(oline, true);
                        ArrayList lineVerbList = posScrapper.getVerbTokens(oline, true);

                        for (int j = 0; j < neuralGramModelList.size(); j++) {

                            NeuralGramModel neuralGramModel = (NeuralGramModel) neuralGramModelList.get(j);

                            //Get class label
                            String label = neuralGramModel.getTrainerModel().getLabel();

                            if (label.toLowerCase().equals(predictedLabel)) {
                                //Get best action and best object AS PER THE TRAINING not TESTING)
                                HashMap verbMap = neuralGramModel.getVerbMap();
                                HashMap nounMap = neuralGramModel.getNounMap();
                                HashMap bigramMap = neuralGramModel.getBigramMap();

                                //Sort
                                Map<String, Integer> sortedVerbMap = sortByComparator(verbMap, false);
                                Map<String, Integer> sortedNounMap = sortByComparator(nounMap, false);
                                Map<String, Integer> sortedBigramMap = sortByComparator(bigramMap, false);

                                String bestVerb = "NA";
                                String bestNoun = "NA";
                                String bestBigram = "NA";
                                ArrayList bestGramsList = new ArrayList();
                                ArrayList relatedGramsList = new ArrayList();

                                //Find best verb
                                if (verbMap.size() >= 1) {
                                    Iterator bestVerbIter = sortedVerbMap.keySet().iterator();
                                    bestVerb = (String) bestVerbIter.next();
                                }

                                //Find best noun
                                if (nounMap.size() >= 1) {
                                    Iterator bestNounIter = sortedNounMap.keySet().iterator();
                                    bestNoun = (String) bestNounIter.next();
                                }

                                if (verbMap.size() >= 1) {
                                    sortedVerbMap.keySet().stream().map((verb) -> posScrapper.getLemma(verb)).map((verb) -> {
                                        if (!relatedGramsList.contains(verb)) {
                                            relatedGramsList.add(verb);
                                        }
                                        return verb;
                                    }).filter((verb) -> (lineVerbList.contains(verb))).filter((verb) -> (!bestGramsList.contains(verb))).forEach((verb) -> {
                                        bestGramsList.add(verb);
                                    });
                                }

                                if (nounMap.size() >= 1) {
                                    sortedNounMap.keySet().stream().map((noun) -> posScrapper.getLemma(noun)).map((noun) -> {
                                        if (!relatedGramsList.contains(noun)) {
                                            relatedGramsList.add(noun);
                                        }
                                        return noun;
                                    }).filter((noun) -> (lineNounList.contains(noun))).filter((noun) -> (!bestGramsList.contains(noun))).forEach((noun) -> {
                                        bestGramsList.add(noun);
                                    });
                                }

                                if (bigramMap.size() >= 1) {
                                    for (String bigram : sortedBigramMap.keySet()) {
                                        if (pline.contains(bigram)) {
                                            //Best bigra
                                            bestBigram = bigram;
                                            break;
                                        }
                                    }
                                }

                                //Get lemma of best noun and verb
                                bestNoun = posScrapper.getLemma(bestNoun);
                                bestVerb = posScrapper.getLemma(bestVerb);
                                String bestGrams = bestGramsList.toString();
                                bestGrams = replace(bestGrams, ", ", " | ", 0);

                                List relatedGramsSubList = (List) relatedGramsList.subList(0, 5);
                                String relatedGramsListString = replace(relatedGramsSubList.toString(), ", ", " | ", 0);

                                CSVUtils.writeLine(outFileWriter, Arrays.asList(toTitleCase(predictedLabel), toTitleCase(bestVerb), toTitleCase(bestNoun), toTitleCase(bestBigram), toTitleCase(bestGrams), toTitleCase(relatedGramsListString), line));

                            }
                        }

                    } else //Not-multivariate
                    {
                        if (singleLabel) {
                            CSVUtils.writeLine(outFileWriter, Arrays.asList(toTitleCase(predictedLabel), line));
                        } else {
                            //Print all labels and their freqs
                            String neuralScores = sortedNeuralScoreMap.toString();
                            neuralScores = replace(neuralScores, ",", " ", 0);
                            CSVUtils.writeLine(outFileWriter, Arrays.asList(neuralScores, line));
                        }
                    }
                }
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } finally {
            try {
                outFileWriter.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }
}
