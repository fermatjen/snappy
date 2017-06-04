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

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import snappy.model.Learner;
import snappy.model.serialized.NeuralGramModel;
import snappy.model.serialized.TrainerModel;
import snappy.ngrams.Predictor;
import snappy.util.io.ConfigModel;
import static snappy.util.io.IOUtils.loadBiasMapFromFile;
import static snappy.util.io.IOUtils.loadConfigFile;
import static snappy.util.io.IOUtils.loadTrainingFile;
import static snappy.util.text.StringUtils.replace;

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
    private static String biasFile = null;
    private static String trainingFile = null;
    private static int processOnly = 10;
    private static String mode = null;
    private static String silent = null;
    private static boolean processLemma = true;
    private static boolean singlelabel = true;

    private static final int threshold = 40;
    private static final Logger LOG = Logger.getLogger(Snappy.class.getName());

    /**
     *
     */
    public static void doTraining() {

        //Get all trainer models
        ArrayList trainerModelList = loadTrainingFile(trainingFile);

        for (int i = 0; i < trainerModelList.size(); i++) {
            TrainerModel trainerModel = (TrainerModel) trainerModelList.get(i);
            String label = trainerModel.getLabel();
            label = replace(label, " ", "_", 0);
            File modelFilePath = new File(modelFile, "s_" + label + ".ser");
            //Start Learning
            Learner learner = new Learner(new NeuralGramModel(), dataFile, trainerModel, processOnly, processLemma);
            learner.startLearning();
            learner.printLearnStats();
            learner.writeIncidents(summaryFile);
            learner.updateModel(true, modelFilePath.getAbsolutePath());

            learner.printAllGrams();
        }
    }

    /**
     *
     */
    public static void doTesting() {
        //TESTING
        //Start testing
        //Load learning bias vectors
        HashMap biasMap = loadBiasMapFromFile(biasFile);
        if (biasMap.size() > 0) {
            LOG.log(Level.INFO, "Loading biases: {0}", biasMap.toString());
        }

        File modelFilePath = new File(modelFile);
        File children[] = modelFilePath.listFiles();
        ArrayList neuralGramModelList = new ArrayList();

        for (File mFile : children) {
            if (mFile.isFile()) {
                String fpath = mFile.getAbsolutePath();
                if (fpath.endsWith(".ser")) {
                    Learner learner = new Learner(new NeuralGramModel(), null, null, processOnly, processLemma);
                    learner.loadModels(fpath);
                    learner.printLearnStats();
                    //Get the neural gram model
                    NeuralGramModel neuralGramModel = learner.getModel();
                    neuralGramModelList.add(neuralGramModel);
                }
            }
        }

        //Write prediction results
        Predictor.writePredictions(biasMap, dataFile, neuralGramModelList, summaryFile, processOnly, threshold, singlelabel, processLemma);

    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        try {
            //Load configuration file
            File base = new File(Snappy.class
                    .getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
            File configFile = new File(base, "snappy.properties");
            //System.out.println("[Snappy] Loading from " + configFile.getAbsolutePath());
            if (!configFile.exists()) {
                System.exit(0);
            } else {
                //Load the configuration file
                ConfigModel configModel = loadConfigFile(configFile);

                try {
                    dataFile = configModel.getDataFile();
                    summaryFile = configModel.getSummaryFile();
                    modelFile = configModel.getModelFile();
                    trainingFile = configModel.getTrainingFile();
                    biasFile = configModel.getBiasFile();
                    processOnly = configModel.getProcessOnly();
                    mode = (configModel.getMode()).toLowerCase().trim();
                    processLemma = !configModel.isFastmode();
                    singlelabel = configModel.isSinglelabel();
                } catch (Exception ex) {
                    LOG.log(Level.SEVERE, "A few config params are missing from the Snappy configuration file.");
                }
            }

            if (mode.equals("testing")) {
                doTesting();
            } else if (mode.equals("training")) {
                doTraining();
            }

        } catch (URISyntaxException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

    }

}
