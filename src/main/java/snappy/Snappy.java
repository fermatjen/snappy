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
package snappy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import snappy.model.Learner;
import snappy.model.NueralGramModel;
import snappy.model.TrainerModel;

/**
 *
 * @author fjenning
 */
public class Snappy {

    /**
     * @param args the command line arguments
     */
    private static String dataFile = null;
    private static String summaryFile = null;
    private static String modelFile = null;

    private static ArrayList filterList = new ArrayList();
    private static int processOnly = 50;

    private static void populatefilterList() {

        filterList.add("error");
        filterList.add("error");
        filterList.add("warning");
        filterList.add("issue");
        filterList.add("slowdown");
        filterList.add("stuck");
        filterList.add("stall");
        filterList.add("terms");

    }

    public static void doTraining() {
        //TRAINING
        //Step 1: Creater Trainer Model
        System.out.println("[Snappy] Initializing Labels and Clusters...");
        populatefilterList();
        TrainerModel trainerModel = new TrainerModel();
        trainerModel.setLabel("Issue");
        trainerModel.setClusters(filterList);

        //Start Learning
        Learner learner = new Learner(new NueralGramModel(), dataFile, trainerModel, processOnly);
        learner.startLearning();
        learner.printLearnStats();
        learner.writeIncidents(summaryFile);
        learner.updateModel(true, modelFile);
    }

    public static void doTesting() {
        //TESTING
        //Start testing
        Learner learner = new Learner(new NueralGramModel(), null, null, processOnly);
        learner.startTesting(modelFile);
        learner.printLearnStats();
    }
    
    public static void loadConfigFile(File configFile){
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(configFile);
            //Construct BufferedReader from InputStreamReader
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line = null;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#")) {
                    continue;
                }
                if (line.indexOf("=") != -1) {
                    StringTokenizer stok = new StringTokenizer(line, "=");
                    while (stok.hasMoreTokens()) {
                        String configKey = stok.nextToken().trim();
                        String configValue = stok.nextToken().trim();

                        if (configKey.equals("dataFile")) {
                            dataFile = configValue;
                        }
                        if (configKey.equals("summaryFile")) {
                            summaryFile = configValue;
                        }
                        if (configKey.equals("modelFile")) {
                            modelFile = configValue;
                        }
                        if (configKey.equals("processOnly")) {
                            processOnly = Integer.parseInt(configValue);
                        }
                    }
                }
            }

            br.close();
        } catch (IOException | NumberFormatException ex) {
            System.out.println("FATAL - Snappy configuration file error: " + ex.getMessage());
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        try {
            //Load configuration file
            File base = new File(Snappy.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
            File configFile = new File(base, "snappy.properties");
            System.out.println("[Snappy] - Loading from "+configFile.getAbsolutePath());
            if (!configFile.exists()) {
                System.out.println("[Snappy] - FATAL: Configuration file not found! Shutting down!");
                System.exit(0);
            } else {
                //Load the configuration file
                loadConfigFile(configFile);

            }

            //Training
            //doTraining();
            
            //Testing
            doTesting();
        } catch (URISyntaxException ex) {
            Logger.getLogger(Snappy.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
