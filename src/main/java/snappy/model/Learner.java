
package snappy.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import static java.util.logging.Level.INFO;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import snappy.model.serialized.NeuralGramModel;
import snappy.model.serialized.TrainerModel;
import snappy.ngrams.Populater;
import static snappy.ngrams.Scorer.scoreAllGrams;
import snappy.pos.POSScrapper;
import static snappy.util.grams.GramStats.printGramStats;
import static snappy.util.grams.GramUtils.loadGramsFromDataFile;
import snappy.util.io.IOUtils;
import static snappy.util.io.IOUtils.readModelFromFile;
import static snappy.util.io.IOUtils.writeModelToFile;
import static snappy.util.io.IOUtils.writeSummary;

/**
 *
 * @author fjenning
 */
public class Learner extends AbstractLearner {

    private static final Logger LOG = getLogger(Learner.class.getName());

    private HashMap unigramMap = new HashMap();
    private HashMap bigramMap = new HashMap();
    private HashMap trigramMap = new HashMap();
    private HashMap quadgramMap = new HashMap();
    private HashMap verbMap = new HashMap();
    private HashMap nounMap = new HashMap();

    private NeuralGramModel neuralGramModel = null;

    private ArrayList incidentList = new ArrayList();

    private TrainerModel trainerModel = null;

    private POSScrapper posScrapper = null;

    private int processOnly = 100;

    private String dataFile = null;

    private boolean processLemma = true;

    /**
     *
     * @param neuralGramModel
     * @param dataFile
     * @param trainerModel
     * @param processOnly
     * @param processLemma
     */
    public Learner(NeuralGramModel neuralGramModel, String dataFile, TrainerModel trainerModel, int processOnly, boolean processLemma) {
        this.neuralGramModel = neuralGramModel;
        //Init class labels for learning
        this.dataFile = dataFile;
        this.trainerModel = trainerModel;
        posScrapper = new POSScrapper(new NLPModel());
        this.processOnly = processOnly;
        this.processLemma = processLemma;
    }

    @Override
    void learnFromGrams() {
        incidentList = loadGramsFromDataFile(dataFile, trainerModel, processOnly);

        //First, load all grams from the incident list
        loadGramsFromIncidentList();

        //Second, score all grams
        incidentList = scoreAllGrams(incidentList, unigramMap, bigramMap, trigramMap, quadgramMap, verbMap, nounMap, processLemma);

        //Third, Re-populate grams
        loadGramsFromIncidentList();

    }

    @Override
    void learnFromPOS() {
        //POS Learner
        loadVerbsFromIncidentList(posScrapper);
        loadNounsFromIncidentList(posScrapper);
    }

    /**
     *
     * @param summaryFile
     */
    public void writeIncidents(String summaryFile) {
        writeSummary(dataFile, summaryFile, incidentList, processOnly);
    }

    /**
     *
     */
    public void startLearning() {
        learnFromGrams();
        learnFromPOS();
    }

    /**
     *
     * @param modelFile
     */
    public void loadModels(String modelFile) {

        neuralGramModel = readModelFromFile(modelFile);
        unigramMap = neuralGramModel.getUnigramMap();
        bigramMap = neuralGramModel.getBigramMap();
        trigramMap = neuralGramModel.getTrigramMap();
        quadgramMap = neuralGramModel.getQuadgramMap();
        verbMap = neuralGramModel.getVerbMap();
        nounMap = neuralGramModel.getNounMap();
        trainerModel = neuralGramModel.getTrainerModel();
    }

    /**
     *
     * @return
     */
    public NeuralGramModel getModel() {
        return neuralGramModel;
    }

    /**
     *
     */
    public void printLearnStats() {
        printGramStats(unigramMap, bigramMap, trigramMap, quadgramMap, verbMap, nounMap);
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
                ArrayList verbList = posScrapper.getVerbTokens(incident, false);
                for (int j = 0; j < verbList.size(); j++) {
                    String verb = (String) verbList.get(j);
                    if (verbMap.containsKey(verb)) {
                        int count = (int) verbMap.get(verb);
                        count += 1;
                        verbMap.remove(verb);
                        verbMap.put(verb, count);
                    } else {
                        verbMap.put(verb, 1);
                    }
                }
            }

        }

    }

    private void loadNounsFromIncidentList(POSScrapper posScrapper) {
        nounMap.clear();
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
                ArrayList nounList = posScrapper.getNounTokens(incident, false);
                for (int j = 0; j < nounList.size(); j++) {
                    String noun = (String) nounList.get(j);
                    if (nounMap.containsKey(noun)) {
                        int count = (int) nounMap.get(noun);
                        count += 1;
                        nounMap.remove(noun);
                        nounMap.put(noun, count);
                    } else {
                        nounMap.put(noun, 1);
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

    /**
     *
     * @param overwrite
     * @param outFile
     */
    @Override
    public void updateModel(boolean overwrite, String outFile) {

        //Create a NeuralGramModel that can be persisted
        neuralGramModel.setUnigramMap(unigramMap);
        neuralGramModel.setBigramMap(bigramMap);
        neuralGramModel.setTrigramMap(trigramMap);
        neuralGramModel.setQuadgramMap(quadgramMap);
        neuralGramModel.setVerbMap(verbMap);
        neuralGramModel.setNounMap(nounMap);
        neuralGramModel.setTrainerModel(trainerModel);

        //Write model to file
        writeModelToFile(outFile, neuralGramModel);

    }

    /**
     *
     */
    public void printAllGrams() {
        LOG.log(INFO, "Unigram Map\r\n{0}\r\n", unigramMap.toString());
        LOG.log(INFO, "Bigram Map\r\n{0}\r\n", bigramMap.toString());
        LOG.log(INFO, "Trigram Map\r\n{0}\r\n", trigramMap.toString());
        LOG.log(INFO, "Quadgram Map\r\n{0}\r\n", quadgramMap.toString());
        LOG.log(INFO, "Verb Map\r\n{0}\r\n", verbMap.toString());
        LOG.log(INFO, "Noun Map\r\n{0}\r\n", nounMap.toString());
    }

}
