package snappy.ngrams;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import java.util.logging.Logger;
import snappy.model.NLPModel;
import snappy.model.serialized.NeuralGramModel;
import static snappy.ngrams.Scorer.getGramScore;
import snappy.pos.POSScrapper;
import static snappy.util.collections.Comparator.sortByComparator;
import snappy.util.io.CSVUtils;
import static snappy.util.io.CSVUtils.writeLine;
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
     * @param biasMap
     * @param dataFile
     * @param neuralGramModelList
     * @param outFile
     * @param processOnly
     * @param threshold
     * @param singleLabel
     * @param processLemma
     * @param isMultivariate
     */
    public static void writePredictions(HashMap biasMap, String dataFile, ArrayList neuralGramModelList, String outFile, int processOnly, int threshold, boolean singleLabel, boolean isMultivariate, boolean processLemma) {

        FileWriter outFileWriter = null;
        POSScrapper posScrapper = new POSScrapper(new NLPModel());

        try {
            // Assuming that the gram and pos maps are filled, start
            // scoring the input dataSet
            outFileWriter = new FileWriter(outFile);
            ArrayList linesList = getAllLinesFromFile(dataFile, processOnly, false);
            for (int i = 0; i < linesList.size(); i++) {
                LOG.log(INFO, "Processing {0} of {1}", new Object[]{i + 1, linesList.size()});
                String line = (String) linesList.get(i);
                String oline = line;
                String pline = line;
                pline = pline.trim();
                pline = pline.replaceAll("\\s+", " ");
                pline = pline.replaceAll("\\p{Punct}+", "");
                pline = pline.replaceAll(" . ", ". ");

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
                    HashMap nounMap = neuralGramModel.getNounMap();

                    //Get class label
                    String label = neuralGramModel.getTrainerModel().getLabel();

                    double gramScore = getGramScore(line, unigramMap, bigramMap, trigramMap, quadgramMap, verbMap, nounMap, processLemma);
                    //Score for this neuralgram
                    //Eliminate weak biases
                    if (gramScore > 1) {
                        neuralScoreMap.put(label.toLowerCase(), (int) gramScore);
                    }
                }

                if (neuralScoreMap.size() > 0) {

                    line = replace(line, ",", " ", 0);

                    Map<String, Integer> sortedNeuralScoreMap = sortByComparator(neuralScoreMap, false);

                    Iterator i1 = sortedNeuralScoreMap.keySet().iterator();
                    String predictedLabel = ((String) i1.next()).trim();
                    String lpredictedLabel = predictedLabel.toLowerCase();
                    //outFileWriter.write(predictedLabel+", \""+line+"\"\r\n");
                    if (singleLabel) {

                        //Check prediction bias
                        LOG.log(INFO, "Predicted: {0}", predictedLabel);
                        if (biasMap.containsKey(lpredictedLabel)) {
                            LOG.log(INFO, "Bias found for {0}", predictedLabel);

                            //Find the weaker player/label
                            ArrayList playerList = (ArrayList) biasMap.get(lpredictedLabel);

                            for (int p = 0; p < playerList.size(); p++) {
                                String player = (String) playerList.get(p);

                                if (player.toLowerCase().equals(lpredictedLabel)) {
                                    continue;
                                }
                                if (neuralScoreMap.containsKey(player)) {
                                    //Winning label
                                    predictedLabel = player;
                                    break;
                                }
                            }
                        }
                    }

                    if (isMultivariate) {
                        //Mult-variate prediction
                        //Get noun and verb tokens
                        ArrayList lineNounList = posScrapper.getNounTokens(oline, true);
                        ArrayList lineVerbList = posScrapper.getVerbTokens(oline, true);

                        for (int j = 0; j < neuralGramModelList.size(); j++) {

                            NeuralGramModel neuralGramModel = (NeuralGramModel) neuralGramModelList.get(j);

                            //Get class label
                            String label = neuralGramModel.getTrainerModel().getLabel();

                            if (label.toLowerCase().equals(predictedLabel)) {
                                //Get best action and best object AS PER THE TRAINING not TESTING)
                                HashMap verbMap = neuralGramModel.getVerbMap();
                                HashMap nounMap = neuralGramModel.getNounMap();
                                HashMap bigramMap = neuralGramModel.getBigramMap();
                                //HashMap trigramMap = neuralGramModel.getTrigramMap();
                                //HashMap quadgramMap = neuralGramModel.getQuadgramMap();

                                //Sort
                                Map<String, Integer> sortedVerbMap = sortByComparator(verbMap, false);
                                Map<String, Integer> sortedNounMap = sortByComparator(nounMap, false);
                                Map<String, Integer> sortedBigramMap = sortByComparator(bigramMap, false);
                                //Map<String, Integer> sortedTrigramMap = sortByComparator(trigramMap, false);
                                //Map<String, Integer> sortedQuadgramMap = sortByComparator(quadgramMap, false);

                                String bestVerb = "NA";
                                String bestNoun = "NA";
                                String bestBigram = "NA";
                                //String bestTrigram = "NA";
                                //String bestQuadgram = "NA";

                                ArrayList bestGramsList = new ArrayList();
                                ArrayList relatedGramsList = new ArrayList();

                                //Find best verb
                                if (verbMap.size() >= 1) {
                                    Iterator bestVerbIter = sortedVerbMap.keySet().iterator();
                                    bestVerb = (String) bestVerbIter.next();
                                }

                                //Find best noun
                                if (nounMap.size() >= 1) {
                                    Iterator bestNounIter = sortedNounMap.keySet().iterator();
                                    bestNoun = (String) bestNounIter.next();
                                }

                                if (verbMap.size() >= 1) {
                                    sortedVerbMap.keySet().stream().map((verb) -> posScrapper.getLemma(verb)).map((verb) -> {
                                        //verb = toTitleCase(verb);
                                        if (!relatedGramsList.contains(verb)) {
                                            relatedGramsList.add(verb);
                                        }
                                        return verb;
                                    }).filter((verb) -> (lineVerbList.contains(verb))).filter((verb) -> (!bestGramsList.contains(verb))).forEach((verb) -> {
                                        bestGramsList.add(verb);
                                    });
                                }

                                if (nounMap.size() >= 1) {
                                    sortedNounMap.keySet().stream().map((noun) -> posScrapper.getLemma(noun)).map((noun) -> {
                                        //noun = toTitleCase(noun);
                                        if (!relatedGramsList.contains(noun)) {
                                            relatedGramsList.add(noun);
                                        }
                                        return noun;
                                    }).filter((noun) -> (lineNounList.contains(noun))).filter((noun) -> (!bestGramsList.contains(noun))).forEach((noun) -> {
                                        bestGramsList.add(noun);
                                    });
                                }

                                if (bigramMap.size() >= 1) {
                                    for (String bigram : sortedBigramMap.keySet()) {
                                        if (pline.contains(bigram)) {
                                            //Best bigra
                                            bestBigram = bigram;
                                            break;
                                        }
                                    }
                                }
                                /*
                                if (trigramMap.size() >= 1) {
                                    for (String trigram : sortedTrigramMap.keySet()) {
                                        if (pline.contains(trigram)) {
                                            //Best bigra
                                            bestTrigram = trigram;
                                            break;
                                        }
                                    }
                                }
                                if (quadgramMap.size() >= 1) {
                                    for (String quadgram : sortedQuadgramMap.keySet()) {
                                        if (pline.contains(quadgram)) {
                                            //Best bigra
                                            bestQuadgram = quadgram;
                                            break;
                                        }
                                    }
                                }
                                 */
                                //Get lemma of best noun and verb
                                bestNoun = posScrapper.getLemma(bestNoun);
                                bestVerb = posScrapper.getLemma(bestVerb);
                                String bestGrams = bestGramsList.toString();
                                bestGrams = replace(bestGrams, ", ", " | ", 0);

                                //Get Phrases
                                ArrayList nounPhraseList = posScrapper.getPhrases(oline, "NP");
                                ArrayList allPhraseList = posScrapper.getPhrases(oline, "VP");
                                allPhraseList.addAll(nounPhraseList);

                                String bestPhrase = "NA";
                                String longestPhrase = "";
                                int longestPhraseWords = 0;

                                for (int q = 0; q < allPhraseList.size(); q++) {
                                    String bestPhraseCandidate = (String) allPhraseList.get(q);
                                    bestPhraseCandidate = bestPhraseCandidate.replaceAll("\\s+", " ");
                                    bestPhraseCandidate = bestPhraseCandidate.replaceAll("\\p{Punct}+", "");

                                    int spaces = bestPhraseCandidate == null ? 0 : bestPhraseCandidate.length() - bestPhraseCandidate.replace(" ", "").length();
                                    if (spaces > longestPhraseWords) {
                                        longestPhrase = bestPhraseCandidate;
                                        longestPhraseWords = spaces;
                                    }

                                    if (bestPhraseCandidate.contains(bestNoun) && bestPhraseCandidate.contains(bestVerb)) {
                                        bestPhrase = bestPhraseCandidate;
                                    } else if (bestPhraseCandidate.contains(bestNoun) || bestPhraseCandidate.contains(bestVerb)) {
                                        bestPhrase = bestPhraseCandidate;
                                    }

                                }

                                if (bestPhrase.equals("NA")) {
                                    bestPhrase = bestBigram;
                                }
                                if (bestPhrase.contains("lrb") || bestPhrase.contains("rrb")) {
                                    bestPhrase = bestBigram;
                                }
                                if (longestPhrase.contains("lrb") || longestPhrase.contains("rrb")) {
                                    longestPhrase = bestBigram;
                                }

                                bestPhrase = toTitleCase(bestPhrase.trim());
                                longestPhrase = toTitleCase(longestPhrase.trim());

                                //If first word is a singel character, remove
                                int firstSpaceIndex = bestPhrase.indexOf(" ");
                                if (firstSpaceIndex != -1) {
                                    StringTokenizer stok = new StringTokenizer(bestPhrase, " ");
                                    String token = stok.nextToken().trim();
                                    if(token.length() < 2){
                                        bestPhrase = (bestPhrase.substring(firstSpaceIndex, bestPhrase.length())).trim();
                                    }
                                }
                                firstSpaceIndex = longestPhrase.indexOf(" ");
                                if (firstSpaceIndex != -1) {
                                    StringTokenizer stok = new StringTokenizer(longestPhrase, " ");
                                    String token = stok.nextToken().trim();
                                    if(token.length() < 2){
                                        longestPhrase = (longestPhrase.substring(firstSpaceIndex, longestPhrase.length())).trim();
                                    }
                                }

                                if (!bestPhrase.equals(longestPhrase)) {
                                    bestPhrase = longestPhrase + " | " + bestPhrase + "";
                                }

                                if (!bestPhrase.contains(" ")) {
                                    bestPhrase = bestBigram;
                                }

                                if (bestPhrase.startsWith("'")) {
                                    //truncate the word
                                    int loc = bestPhrase.indexOf(' ', 0);
                                    bestPhrase = (bestPhrase.substring(loc, bestPhrase.length())).trim();
                                }
                                
                                //eliminate duplicates in relatedGrams
                                ArrayList cleanRelatedGramsSubList = new ArrayList();
                                
                                for(int k=0;k<relatedGramsList.size();k++){
                                    String entry = (String) relatedGramsList.get(k);
                                    if(!bestGramsList.contains(entry)){
                                        cleanRelatedGramsSubList.add(entry);
                                    }
                                }

                                List relatedGramsSubList = cleanRelatedGramsSubList.subList(0, 10);
                                String relatedGramsListString = replace(relatedGramsSubList.toString(), ", ", " | ", 0);

                                writeLine(outFileWriter, asList(toTitleCase(predictedLabel), toTitleCase(bestVerb), toTitleCase(bestNoun), bestPhrase, bestGrams, relatedGramsListString, line));

                            }
                        }

                    } else //Not-multivariate
                     if (singleLabel) {
                            writeLine(outFileWriter, asList(toTitleCase(predictedLabel), line));
                        } else {
                            //Print all labels and their freqs
                            String neuralScores = sortedNeuralScoreMap.toString();
                            neuralScores = replace(neuralScores, ",", " ", 0);
                            writeLine(outFileWriter, asList(neuralScores, line));
                        }
                }
            }
        } catch (IOException ex) {
            LOG.log(SEVERE, null, ex);
        } finally {
            try {
                outFileWriter.close();
            } catch (IOException ex) {
                LOG.log(SEVERE, null, ex);
            }
        }
    }

    private Predictor() {
    }
}
