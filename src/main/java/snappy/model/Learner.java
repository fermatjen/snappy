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
package snappy.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import snappy.ngrams.Populater;
import static snappy.ngrams.Scorer.scoreAllGrams;
import snappy.pos.POSScrapper;
import static snappy.util.grams.GramStats.printGramStats;
import static snappy.util.grams.GramUtils.loadGramsFromDataFile;
import snappy.util.io.IOUtils;
import static snappy.util.io.IOUtils.getAllLinesFromFile;
import static snappy.util.io.IOUtils.writeSummary;

/**
 *
 * @author fjenning
 */
public class Learner extends AbstractLearner {

    private HashMap unigramMap = new HashMap();
    private HashMap bigramMap = new HashMap();
    private HashMap trigramMap = new HashMap();
    private HashMap quadgramMap = new HashMap();
    private HashMap verbMap = new HashMap();

    private NeuralGramModel nueralGramModel = null;

    private ArrayList incidentList = new ArrayList();

    private TrainerModel trainerModel = null;

    private POSScrapper posScrapper = null;

    private int processOnly = 100;

    private String dataFile = null;

    public Learner(NeuralGramModel nueralGramModel, String dataFile, TrainerModel trainerModel, int processOnly) {
        this.nueralGramModel = nueralGramModel;
        //Init class labels for learning
        this.dataFile = dataFile;
        this.trainerModel = trainerModel;
        posScrapper = new POSScrapper(new NLPModel());
        this.processOnly = processOnly;
    }

    @Override
    void learnFromGrams() {
        incidentList = loadGramsFromDataFile(dataFile, trainerModel, processOnly);

        //First, load all grams from the incident list
        loadGramsFromIncidentList();

        //Second, score all grams
        incidentList = scoreAllGrams(incidentList, unigramMap, bigramMap, trigramMap, quadgramMap, verbMap);

        //Third, Re-populate grams
        loadGramsFromIncidentList();

    }

    @Override
    void learnFromPOS() {
        //POS Learner
        loadVerbsFromIncidentList(posScrapper);

    }

    public void writeIncidents(String summaryFile) {
        writeSummary(dataFile, summaryFile, incidentList, processOnly);
    }

    public void startLearning() {
        learnFromGrams();
        learnFromPOS();
    }

    public void loadModels(String modelFile) {
        
        nueralGramModel = IOUtils.readModelFromFile(modelFile);
        unigramMap = nueralGramModel.getUnigramMap();
        bigramMap = nueralGramModel.getBigramMap();
        trigramMap = nueralGramModel.getTrigramMap();
        quadgramMap = nueralGramModel.getQuadgramMap();
        verbMap = nueralGramModel.getVerbMap();
        trainerModel = nueralGramModel.getTrainerModel();
    }
    
    public NeuralGramModel getModel(){
        return nueralGramModel;
    }

    public void printLearnStats() {
        printGramStats(unigramMap, bigramMap, trigramMap, quadgramMap, verbMap);
    }

    private void loadVerbsFromIncidentList(POSScrapper posScrapper) {
        verbMap.clear();
        ArrayList clusters = trainerModel.getClusters();

        //Load POS Tokens
        for (int i = 0; i < incidentList.size(); i++) {
            String incident = (String) incidentList.get(i);
            //Filter for class label

            boolean canParse = false;

            for (int k = 0; k < clusters.size(); k++) {
                String classLabel = (String) clusters.get(k);
                if (incident.contains(classLabel)) {
                    canParse = true;
                    break;
                }
            }
            if (canParse) {
                //System.out.println(incident);
                //Get verb tokens
                ArrayList verbList = posScrapper.getVerbTokens(incident);
                for (int j = 0; j < verbList.size(); j++) {
                    String verb = (String) verbList.get(j);
                    if (verbMap.containsKey(verb)) {
                        int count = (int) verbMap.get(verb);
                        count = count + 1;
                        verbMap.remove(verb);
                        verbMap.put(verb, count);
                    } else {
                        verbMap.put(verb, 1);
                    }
                }
            }

        }

    }

    private void loadGramsFromIncidentList() {
        //Flush all gramLists
        unigramMap.clear();
        bigramMap.clear();
        trigramMap.clear();
        quadgramMap.clear();

        for (int i = 0; i < incidentList.size(); i++) {
            String incident = (String) incidentList.get(i);
            populateNgrams(incident);
        }
    }

    private void populateNgrams(String line) {

        ArrayList clusters = trainerModel.getClusters();

        //Building unigram map
        GramModel gramModel = new GramModel();
        gramModel.setNgramMap(unigramMap);
        Populater p1 = new Populater(gramModel, clusters);
        p1.populateNgrams(line, 1);
        //Unpack
        unigramMap = gramModel.getNgramMap();

        //Building bigram map
        GramModel gramModel2 = new GramModel();
        gramModel2.setNgramMap(bigramMap);
        Populater p2 = new Populater(gramModel2, clusters);
        p2.populateNgrams(line, 2);
        //Unpack
        bigramMap = gramModel2.getNgramMap();

        //Building trigram map
        GramModel gramModel3 = new GramModel();
        gramModel3.setNgramMap(trigramMap);
        Populater p3 = new Populater(gramModel3, clusters);
        p3.populateNgrams(line, 3);
        //Unpack
        trigramMap = gramModel3.getNgramMap();

        //Building quadgram map
        GramModel gramModel4 = new GramModel();
        gramModel4.setNgramMap(quadgramMap);
        Populater p4 = new Populater(gramModel4, clusters);
        p4.populateNgrams(line, 4);
        //Unpack
        quadgramMap = gramModel4.getNgramMap();

    }

    @Override
    public void updateModel(boolean overwrite, String outFile) {

        //Create a NeuralGramModel that can be persisted
        nueralGramModel.setUnigramMap(unigramMap);
        nueralGramModel.setBigramMap(bigramMap);
        nueralGramModel.setTrigramMap(trigramMap);
        nueralGramModel.setQuadgramMap(quadgramMap);
        nueralGramModel.setVerbMap(verbMap);
        nueralGramModel.setTrainerModel(trainerModel);

        //Write model to file
        IOUtils.writeModelToFile(outFile, nueralGramModel);

    }

    public void printAllGrams() {

        System.out.println("Unigram Map\r\n" + unigramMap.toString() + "\r\n");
        System.out.println("Bigram Map\r\n" + bigramMap.toString() + "\r\n");
        System.out.println("Trigram Map\r\n" + trigramMap.toString() + "\r\n");
        System.out.println("Quadgram Map\r\n" + quadgramMap.toString() + "\r\n");
        System.out.println("POS Map\r\n" + verbMap.toString() + "\r\n");
    }

}
