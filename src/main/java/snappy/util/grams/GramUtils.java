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

package snappy.util.grams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import snappy.model.serialized.TrainerModel;

/**
 *
 * @author fjenning
 */
public class GramUtils {
    private static final Logger LOG = Logger.getLogger(GramUtils.class.getName());
    
    /**
     *
     * @param filterList
     * @param gram
     * @return
     */
    public static boolean isFilteredGram(ArrayList filterList, String gram) {

        for (int i = 0; i < filterList.size(); i++) {
            String filteredWord = (String) filterList.get(i);
            if (gram.contains(filteredWord)) {
                return true;
            }
        }

        return false;
    }
    
    /**
     *
     * @param dataFile
     * @param trainerModel
     * @param processOnly
     * @return
     */
    public static ArrayList loadGramsFromDataFile(String dataFile, TrainerModel trainerModel, int processOnly) {
        ArrayList incidentList = new ArrayList();
        ArrayList clusters = trainerModel.getClusters();
        
        try (BufferedReader br = new BufferedReader(new FileReader(new File(dataFile)))) {
            String line;

            int count = 0;
            while ((line = br.readLine()) != null) {
                //System.out.println(count);
                if (count >= processOnly) {
                    break;
                }
                line = line.replaceAll("\\s+", " ");
                line = line.replaceAll("\\p{Punct}+", "");
                line = line.toLowerCase().trim();

                //System.out.println(filterList.size());
                if (isFilteredGram(clusters, line)) {
                    //System.out.println("Processing: " + line);

                    line = line.replaceAll(" . ", ". ");
                    //line = replace(line, "  ", " ", 0);
                    //line = line.toLowerCase();
                    //populateNgrams(line);
                    String[] sentences = line.split("(?<=[a-z])\\.\\s+");

                    //HashMap incidentMap = new HashMap();
                    for (String sentence1 : sentences) {
                        String sentence = sentence1.trim();
                        if (isFilteredGram(clusters, sentence)) {
                            incidentList.add(sentence);
                        }
                    }
                }

                count++;
            }

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "[Snappy] {0}", ex.getMessage());
        }
        
        return incidentList;
    }
    
}
