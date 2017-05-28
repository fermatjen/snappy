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

package snappy.util.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import snappy.Snappy;
import snappy.model.NueralGramModel;
import static snappy.util.text.StringUtils.replace;

/**
 *
 * @author fjenning
 */
public class IOUtils {
    
    public static NueralGramModel readModelFromFile(String modelFile){
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(modelFile));
            NueralGramModel nueralGramModel = (NueralGramModel)ois.readObject();
            return nueralGramModel;
        } catch (IOException ex) {
            Logger.getLogger(IOUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(IOUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public static void writeModelToFile(String modelFile, NueralGramModel nueralGramModel){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(modelFile));
            oos.writeObject(nueralGramModel);
            oos.flush();
            oos.close();
        } catch (IOException ex) {
            Logger.getLogger(IOUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void writeSummary(String dataFile, String summaryFile, ArrayList incidentList, int processOnly) {

        FileWriter writer = null;

        int lnum = 1;

        try {
            writer = new FileWriter(summaryFile);

            try (BufferedReader br = new BufferedReader(new FileReader(new File(dataFile)))) {
                String line;

                int count = 0;
                while ((line = br.readLine()) != null) {
                    //System.out.println(count);
                    if (count >= processOnly) {
                        break;
                    }
                    line = line.trim();

                    line = line.replaceAll(" . ", ". ");

                    String sumLine = "";

                    //line = line.replaceAll("[^\\w\\s]", "");
                    //line = replace(line, "  ", " ", 0);
                    //line = line.toLowerCase();
                    //populateNgrams(line);
                    String[] sentences = line.split("(?<=[a-z])\\.\\s+");

                    //HashMap incidentMap = new HashMap();
                    for (int i = 0; i < sentences.length; i++) {
                        String osentence = sentences[i].trim();
                        String sentence = osentence.toLowerCase();
                        if (incidentList.contains(sentence)) {
                            if (sentence.length() < 5) {
                                continue;
                            }

                            sumLine = sumLine + ", " + osentence;

                        }
                    }

                    sumLine = sumLine.trim();
                    if (sumLine.startsWith(",")) {
                        sumLine = sumLine.substring(1, sumLine.length());
                    }
                    if (sumLine.endsWith(",")) {
                        sumLine = sumLine.substring(0, sumLine.length() - 1);
                    }

                    if (sumLine.length() > 5) {

                        sumLine = replace(sumLine, "\"", "", 0).trim();
                        sumLine = replace(sumLine, ",", ";", 0).trim();

                        writer.write("" + lnum + ", \"" + sumLine + "\"\r\n");
                    }

                    lnum++;
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(IOUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(IOUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
}
