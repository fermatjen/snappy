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
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import snappy.model.Learner;
import snappy.model.NueralGramModel;
import snappy.model.TrainerModel;
import snappy.util.io.ConfigModel;
import static snappy.util.io.IOUtils.loadConfigFile;
import static snappy.util.io.IOUtils.loadTrainingFile;

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
    private static String trainingFile = null;

    private static ArrayList filterList = new ArrayList();
    private static int processOnly = 50;

    private static String label = null;

    public static void doTraining() {
        //TRAINING

        //Load the training set
        System.out.println("[Snappy] Loading the training set...");
        TrainerModel trainerModel = loadTrainingFile(trainingFile);
        label = trainerModel.getLabel();
        filterList = trainerModel.getClusters();

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

    public static void main(String[] args) {
        try {
            //Load configuration file
            File base = new File(Snappy.class
                    .getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
            File configFile = new File(base, "snappy.properties");
            System.out.println("[Snappy] - Loading from " + configFile.getAbsolutePath());
            if (!configFile.exists()) {
                System.out.println("[Snappy] - FATAL: Configuration file not found! Shutting down!");
                System.exit(0);
            } else {
                //Load the configuration file
                ConfigModel configModel = loadConfigFile(configFile);
                dataFile = configModel.getDataFile();
                summaryFile = configModel.getSummaryFile();
                modelFile = configModel.getModelFile();
                trainingFile = configModel.getTrainingFile();
                processOnly = configModel.getProcessOnly();

            }

            //Training
            doTraining();

            //Testing
            //doTesting();
        } catch (URISyntaxException ex) {
            Logger.getLogger(Snappy.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

}
