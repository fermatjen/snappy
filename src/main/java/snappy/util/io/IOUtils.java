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
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import snappy.model.serialized.NeuralGramModel;
import snappy.model.serialized.TrainerModel;
import static snappy.util.text.StringUtils.replace;

/**
 *
 * @author fjenning
 */
public class IOUtils {

    private static final Logger LOG = Logger.getLogger(IOUtils.class.getName());

    /**
     *
     * @param modelFile Reads a NeuralGramModel from a serialized model file.
     * @return a NeuralGramModel object that will contain the NGrams and POS Map
     */
    public static NeuralGramModel readModelFromFile(String modelFile) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(modelFile));
            NeuralGramModel neuralGramModel;
            neuralGramModel = (NeuralGramModel) ois.readObject();

            return neuralGramModel;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(IOUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     *
     * @param configFile Loads the Snappy configuration file containing
     * configurable elements of Snappy. Also, certain performance tuning options
     * are available.
     * @return A ConfigModel object containing the configuration values read
     * from the configFile properties file.
     */
    public static ConfigModel loadConfigFile(File configFile) {
        ConfigModel configModel = new ConfigModel();

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(configFile);
            try (
                    //Construct BufferedReader from InputStreamReader
                    BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {
                String line = null;

                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.startsWith("#")) {
                        continue;
                    }
                    if (line.contains("=")) {
                        StringTokenizer stok = new StringTokenizer(line, "=");
                        while (stok.hasMoreTokens()) {
                            String configKey = stok.nextToken().trim();
                            String configValue = stok.nextToken().trim();

                            if (configKey.equals("dataFile")) {
                                configModel.setDataFile(configValue);
                            }
                            if (configKey.equals("trainingFile")) {
                                configModel.setTrainingFile(configValue);
                            }
                            if (configKey.equals("summaryFile")) {
                                configModel.setSummaryFile(configValue);
                            }
                            if (configKey.equals("modelFile")) {
                                configModel.setModelFile(configValue);
                            }
                            if (configKey.equals("biasFile")) {
                                configModel.setBiasFile(configValue);
                            }
                            if (configKey.equals("processOnly")) {
                                configModel.setProcessOnly(Integer.parseInt(configValue));
                            }
                            if (configKey.equals("mode")) {
                                configModel.setMode(configValue);
                            }
                            if (configKey.equals("silent")) {
                                configModel.setSilent(configValue);
                            }
                            if (configKey.equals("fastmode")) {
                                if (configValue.toLowerCase().trim().equals("yes")) {
                                    configModel.setFastmode(true);
                                } else if (configValue.toLowerCase().trim().equals("no")) {
                                    configModel.setFastmode(false);
                                }
                            }
                            if (configKey.equals("singlelabel")) {
                                if (configValue.toLowerCase().trim().equals("yes")) {
                                    configModel.setSinglelabel(true);
                                } else if (configValue.toLowerCase().trim().equals("no")) {
                                    configModel.setSinglelabel(false);
                                }
                            }
                            if (configKey.equals("multivariate")) {
                                if (configValue.toLowerCase().trim().equals("yes")) {
                                    configModel.setMultivariate(true);
                                } else if (configValue.toLowerCase().trim().equals("no")) {
                                    configModel.setMultivariate(false);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException | NumberFormatException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
            System.exit(0);
        }

        return configModel;
    }

    public static HashMap loadBiasMapFromFile(String biasFile) {

        HashMap biasMap = new HashMap();

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(biasFile);
            //Construct BufferedReader from InputStreamReader
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line = null;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#")) {
                    continue;
                }

                int loc = line.indexOf('(');
                if (loc != -1) {
                    //Load labels and patterns

                    //String biasLabel = line.substring(0, loc).trim();
                    int roc = line.indexOf(')', loc);
                    if (roc != -1) {

                        //Pass 1
                        String biasLabels = line.substring(loc + 1, roc);

                        ArrayList biasList = new ArrayList();

                        if (biasLabels.indexOf(',') != -1) {
                            StringTokenizer stok = new StringTokenizer(biasLabels, ",");
                            while (stok.hasMoreTokens()) {
                                String biasLabel = stok.nextToken().toLowerCase().trim();
                                biasList.add(biasLabel);
                            }

                            for (int j = 0; j < biasList.size(); j++) {
                                String label = (String) biasList.get(j);
                                biasMap.put(label, biasList);
                            }

                        }
                    }
                }
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        }

        return biasMap;

    }

    /**
     *
     * @param trainingFile A training file contains the training label denoting
     * a group of words or phrases. A training file must have the label and the
     * associated phrases in the label (phrase1, phrase2, phrase3) format.
     * @return An ArrayList containing the TrainerModel objects.
     */
    public static ArrayList loadTrainingFile(String trainingFile) {

        ArrayList trainerModelList = new ArrayList();

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(trainingFile);
            //Construct BufferedReader from InputStreamReader
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line = null;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#")) {
                    continue;
                }

                int loc = line.indexOf('(');
                if (loc != -1) {
                    //Load labels and patterns
                    TrainerModel trainerModel = new TrainerModel();
                    ArrayList filterList = new ArrayList();
                    String label = line.substring(0, loc).trim();

                    int roc = line.indexOf(')', loc);
                    if (roc != -1) {
                        String patterns = line.substring(loc + 1, roc);
                        if (patterns.indexOf(',') != -1) {
                            StringTokenizer stok = new StringTokenizer(patterns, ",");
                            while (stok.hasMoreTokens()) {
                                String pattern = stok.nextToken().toLowerCase().trim();
                                filterList.add(pattern);
                            }
                        }
                    }

                    trainerModel.setLabel(label);
                    trainerModel.setClusters(filterList);

                    trainerModelList.add(trainerModel);
                }
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        }

        return trainerModelList;
    }

    /**
     *
     * @param modelFile
     * @param neuralGramModel
     */
    public static void writeModelToFile(String modelFile, NeuralGramModel neuralGramModel) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(modelFile))) {
            oos.writeObject(neuralGramModel);
            oos.flush();
        } catch (IOException ex) {
            Logger.getLogger(IOUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param dataFile
     * @param processOnly
     * @param canAlterInstance
     * @return
     */
    public static ArrayList getAllLinesFromFile(String dataFile, int processOnly, boolean canAlterInstance) {

        ArrayList linesList = new ArrayList();

        try (BufferedReader br = new BufferedReader(new FileReader(new File(dataFile)))) {
            String line;

            int count = 0;
            while ((line = br.readLine()) != null) {
                //System.out.println(count);
                if (count >= processOnly) {
                    break;
                }
                if (canAlterInstance) {
                    line = line.replaceAll("\\s+", " ");
                    line = line.replaceAll("\\p{Punct}+", "");
                }
                linesList.add(line.trim());
                count++;
            }
        } catch (Exception ex) {
        }

        return linesList;

    }

    /**
     *
     * @param dataFile
     * @param summaryFile
     * @param incidentList
     * @param processOnly
     */
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
                    line = line.replaceAll("\\s+", " ");
                    line = line.replaceAll("\\p{Punct}+", "");
                    line = line.replaceAll(" . ", ". ");

                    String sumLine = "";

                    //line = line.replaceAll("[^\\w\\s]", "");
                    //line = replace(line, "  ", " ", 0);
                    //line = line.toLowerCase();
                    //populateNgrams(line);
                    String[] sentences = line.split("(?<=[a-z])\\.\\s+");

                    //HashMap incidentMap = new HashMap();
                    for (String sentence1 : sentences) {
                        String osentence = sentence1.trim();
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
            }

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
