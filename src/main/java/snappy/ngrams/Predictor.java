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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import snappy.model.NeuralGramModel;
import static snappy.ngrams.Scorer.getGramScore;
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
    public static void writePredictions(HashMap biasMap, String dataFile, ArrayList neuralGramModelList, String outFile, int processOnly, int threshold, boolean singleLabel, boolean processLemma) {

        FileWriter outFileWriter = null;
        //POSScrapper posScrapper = new POSScrapper(new NLPModel());

        try {
            // Assuming that the gram and pos maps are filled, start
            // scoring the input dataSet
            outFileWriter = new FileWriter(outFile);
            ArrayList linesList = getAllLinesFromFile(dataFile, processOnly, false);
            for (int i = 0; i < linesList.size(); i++) {

                String line = (String) linesList.get(i);

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

                    //Get class label
                    String label = neuralGramModel.getTrainerModel().getLabel();

                    double gramScore = getGramScore(line, unigramMap, bigramMap, trigramMap, quadgramMap, verbMap, processLemma);
                    //Score for this neuralgram
                    //Eliminate weak biases
                    if (gramScore > 1) {
                        neuralScoreMap.put(label.toLowerCase(), (int) gramScore);
                    }
                }

                if (neuralScoreMap.size() > 0) {
                    Map<String, Integer> sortedNeuralScoreMap = sortByComparator(neuralScoreMap, false);

                    Iterator i1 = sortedNeuralScoreMap.keySet().iterator();
                    String predictedLabel = ((String) i1.next()).trim();
                    String lpredictedLabel = predictedLabel.toLowerCase();
                    //outFileWriter.write(predictedLabel+", \""+line+"\"\r\n");
                    if (singleLabel) {

                        //Check prediction bias
                        LOG.log(Level.INFO, "Predicted: "+predictedLabel);
                        if (biasMap.containsKey(lpredictedLabel)) {
                            LOG.log(Level.INFO, "Bias found for {0}", predictedLabel);

                            //Find the weaker player/label
                            ArrayList playerList = (ArrayList) biasMap.get(lpredictedLabel);
                            
                            for (int p = 0; p < playerList.size(); p++) {
                                String player = (String) playerList.get(p);
                                
                                if(player.toLowerCase().equals(lpredictedLabel)){
                                    continue;
                                }
                                if (neuralScoreMap.containsKey(player)) {
                                    //Winning label
                                    predictedLabel = player;
                                    break;
                                }
                            }
                        }

                        line = replace(line, ",", " ", 0);
                        CSVUtils.writeLine(outFileWriter, Arrays.asList(toTitleCase(predictedLabel), line));
                    } else {
                        //Print all labels and their freqs
                        String neuralScores = sortedNeuralScoreMap.toString();
                        neuralScores = replace(neuralScores, ",", " ", 0);
                        CSVUtils.writeLine(outFileWriter, Arrays.asList(neuralScores, line));
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
