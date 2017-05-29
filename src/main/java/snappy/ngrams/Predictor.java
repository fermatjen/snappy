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
import snappy.model.NueralGramModel;
import static snappy.ngrams.Scorer.getGramScore;
import static snappy.util.collections.Comparator.sortByComparator;
import snappy.util.io.CSVUtils;
import static snappy.util.io.IOUtils.getAllLinesFromFile;

/**
 *
 * @author fjenning
 */
public class Predictor {

    public static void writePredictions(String dataFile, ArrayList nueralGramModelList, String outFile, int processOnly) {

        FileWriter outFileWriter = null;

        try {
            // Assuming that the gram and pos maps are filled, start
            // scoring the input dataSet
            outFileWriter = new FileWriter(outFile);
            ArrayList linesList = getAllLinesFromFile(dataFile, processOnly);
            for (int i = 0; i < linesList.size(); i++) {
                String line = (String) linesList.get(i);
                //Now get the best label
                // Unpack Nueral Grams
                HashMap nueralScoreMap = new HashMap();

                for (int j = 0; j < nueralGramModelList.size(); j++) {

                    NueralGramModel nueralGramModel = (NueralGramModel) nueralGramModelList.get(j);

                    HashMap unigramMap = (HashMap) nueralGramModel.getUnigramMap();
                    HashMap bigramMap = (HashMap) nueralGramModel.getBigramMap();
                    HashMap trigramMap = (HashMap) nueralGramModel.getTrigramMap();
                    HashMap quadgramMap = (HashMap) nueralGramModel.getQuadgramMap();
                    HashMap verbMap = (HashMap) nueralGramModel.getVerbMap();

                    //Get class label
                    String label = nueralGramModel.getTrainerModel().getLabel();

                    double gramScore = getGramScore(line, unigramMap, bigramMap, trigramMap, quadgramMap, verbMap);
                    //Score for this nueralgram
                    nueralScoreMap.put(label, (int) gramScore);
                }

                Map<String, Integer> sortedNueralScoreMap = sortByComparator(nueralScoreMap, false);
                Iterator i1 = sortedNueralScoreMap.keySet().iterator();
                String predictedLabel = (String) i1.next();

                //outFileWriter.write(predictedLabel+", \""+line+"\"\r\n");
                CSVUtils.writeLine(outFileWriter, Arrays.asList(predictedLabel, line));
            }
        } catch (IOException ex) {
            Logger.getLogger(Predictor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                outFileWriter.close();
            } catch (IOException ex) {
                Logger.getLogger(Predictor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public String predictLabel(String line, HashMap unigramMap, HashMap bigramMap, HashMap trigramMap, HashMap quadgramMap, HashMap verbMap) {

        double gramScore = getGramScore(line, unigramMap, bigramMap, trigramMap, quadgramMap, verbMap);

        return "NA";
    }

}
