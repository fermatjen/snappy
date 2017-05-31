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

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import snappy.model.NLPModel;
import snappy.pos.POSScrapper;

/**
 *
 * @author fjenning
 */
public class Scorer {

    public static ArrayList scoreAllGrams(ArrayList incidentList, HashMap unigramMap, HashMap bigramMap, HashMap trigramMap, HashMap quadgramMap, HashMap verbMap, boolean processLemma) {

        //Second pass
        ArrayList dupIncidentList = new ArrayList();

        for (int j = 0; j < incidentList.size(); j++) {

            String line = (String) incidentList.get(j);

            //System.out.println("INCIDENT: " + line);
            line = line.trim();

            double gramScore = getGramScore(line, unigramMap, bigramMap, trigramMap, quadgramMap, verbMap, processLemma);
            if (gramScore > 0) {
                dupIncidentList.add(line);
            }

        }

        //Flush incidents
        return dupIncidentList;

    }

    public static double getGramScore(String oline, HashMap unigramMap, HashMap bigramMap, HashMap trigramMap, HashMap quadgramMap, HashMap verbMap, boolean processLemma) {
        double score = 0;
        String line = "";
        NLPModel nlpModel = null;

        if (processLemma) {
            //Lemmatize sentence
            nlpModel = new NLPModel();
            Properties props = nlpModel.getProps();
            props.put("annotators", "tokenize, ssplit, pos, lemma");
            StanfordCoreNLP pipeline = nlpModel.getPipeline();
            Annotation document = pipeline.process(oline);

            for (CoreMap sentence : document.get(SentencesAnnotation.class)) {
                for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                    //String word = token.get(TextAnnotation.class);
                    String lemma = token.get(LemmaAnnotation.class);
                    //System.out.println("Word :" + word);
                    //System.out.println("Lemma :" + lemma);
                    line = line + " " + lemma;
                }
            }
        } else {
            line = oline;
        }

        //Strict match in unigram list
        Iterator i1 = unigramMap.keySet().iterator();
        while (i1.hasNext()) {
            String gram = (String) i1.next();
            String lgram = "";

            if (processLemma) {
                //Get lemma
                StanfordCoreNLP pipeline1 = nlpModel.getPipeline();
                Annotation document1 = pipeline1.process(gram);
                for (CoreMap sentence1 : document1.get(SentencesAnnotation.class)) {
                    for (CoreLabel token1 : sentence1.get(TokensAnnotation.class)) {
                        //String word = token.get(TextAnnotation.class);
                        lgram = token1.get(LemmaAnnotation.class);
                        //System.out.println("Word :" + word);
                        //System.out.println("Lemma :" + gram);

                    }
                }
            } else {
                lgram = gram;
            }

            if (line.contains(lgram)) {

                int c1 = (int) unigramMap.get(gram);
                double s = (c1 * 1) + (gram.length() * 0.05);
                score = score + s;
                //System.out.println("         Match (1): " + gram + " (score: +" + (s)+")");
            }
        }

        if (score <= 1) {
            return 0;
        }

        Iterator i0 = verbMap.keySet().iterator();
        while (i0.hasNext()) {
            String gram = (String) i0.next();
            String lgram = "";

            if (processLemma) {
                //Get lemma
                StanfordCoreNLP pipelinev = nlpModel.getPipeline();
                Annotation documentv = pipelinev.process(gram);
                for (CoreMap sentencev : documentv.get(SentencesAnnotation.class)) {
                    for (CoreLabel tokenv : sentencev.get(TokensAnnotation.class)) {
                        //String word = token.get(TextAnnotation.class);
                        lgram = tokenv.get(LemmaAnnotation.class);
                        //System.out.println("Word :" + word);
                        //System.out.println("Lemma :" + gram);

                    }
                }
            } else {
                lgram = gram;
            }

            if (line.contains(lgram)) {

                int c1 = (int) verbMap.get(gram);
                double s = (c1 * 1) + (gram.length() * 0.05);
                score = score + s;
                //System.out.println("         Match (1): " + gram + " (score: +" + (s)+")");
            }
        }

        Iterator i2 = bigramMap.keySet().iterator();
        while (i2.hasNext()) {
            String gram = (String) i2.next();
            String lgram = "";

            if (processLemma) {
                //Get lemma
                StanfordCoreNLP pipeline2 = nlpModel.getPipeline();
                Annotation document2 = pipeline2.process(gram);
                for (CoreMap sentence2 : document2.get(SentencesAnnotation.class)) {
                    for (CoreLabel token2 : sentence2.get(TokensAnnotation.class)) {
                        //String word = token.get(TextAnnotation.class);
                        lgram = token2.get(LemmaAnnotation.class);
                        //System.out.println("Word :" + word);
                        //System.out.println("Lemma :" + gram);

                    }
                }
            } else {
                lgram = gram;
            }

            if (line.contains(lgram)) {
                int c1 = (int) bigramMap.get(gram);

                double s = (c1 * 2) + (gram.length() * 0.15);
                score = score + s;
                //System.out.println("         Match (2): " + gram + " (score: +" + (s));
            }
        }
        Iterator i3 = trigramMap.keySet().iterator();
        while (i3.hasNext()) {
            String gram = (String) i3.next();
            String lgram = "";

            if (processLemma) {
                //Get lemma
                StanfordCoreNLP pipeline3 = nlpModel.getPipeline();
                Annotation document3 = pipeline3.process(gram);
                for (CoreMap sentence3 : document3.get(SentencesAnnotation.class)) {
                    for (CoreLabel token3 : sentence3.get(TokensAnnotation.class)) {
                        //String word = token.get(TextAnnotation.class);
                        lgram = token3.get(LemmaAnnotation.class);
                        //System.out.println("Word :" + word);
                        //System.out.println("Lemma :" + gram);

                    }
                }
            } else {
                lgram = gram;
            }

            if (line.contains(lgram)) {
                int c1 = (int) trigramMap.get(gram);

                double s = (c1 * 3) + (gram.length() * 0.2);
                score = score + s;
                //System.out.println("         Match (3): " + gram + " (score: +" + (s));
            }
        }
        Iterator i4 = quadgramMap.keySet().iterator();
        while (i4.hasNext()) {
            String gram = (String) i4.next();
            String lgram = "";

            if (processLemma) {
                //Get lemma
                StanfordCoreNLP pipeline4 = nlpModel.getPipeline();
                Annotation document4 = pipeline4.process(gram);
                for (CoreMap sentence4 : document4.get(SentencesAnnotation.class)) {
                    for (CoreLabel token4 : sentence4.get(TokensAnnotation.class)) {
                        //String word = token.get(TextAnnotation.class);
                        lgram = token4.get(LemmaAnnotation.class);
                        //System.out.println("Word :" + word);
                        //System.out.println("Lemma :" + gram);

                    }
                }
            } else {
                lgram = gram;
            }

            if (line.contains(lgram)) {
                int c1 = (int) quadgramMap.get(gram);

                double s = (c1 * 4) + (gram.length() * 0.6);
                score = score + s;
                //System.out.println("         Match (4): " + gram + " (score: +" + (s));
            }
        }

        return score;
    }

}
