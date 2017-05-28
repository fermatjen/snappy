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

package snappy.pos;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import snappy.model.NLPModel;

/**
 *
 * @author fjenning
 */
public class POSScrapper {

    private LexicalizedParser lp = null;
    private TreebankLanguagePack tlp = null;
    private StopWords swords = null;

    public POSScrapper(NLPModel nlpModel) {
        lp = nlpModel.getLp();
        tlp = nlpModel.getTlp();
        swords = new StopWords();
    }

    public String getPennString(String text) {
        //System.out.println("FOR:" + text);

        String cpennString = "";
        //Properties props = new Properties();
        //props.setProperty("annotators", "tokenize, ssplit, parse");
        // StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        //Annotation annotation = pipeline.process(text);

        StringTokenizer stok = new StringTokenizer(text, ".");
        while (stok.hasMoreTokens()) {
            String sentence = stok.nextToken().trim();

            Tokenizer<? extends HasWord> toke = tlp.getTokenizerFactory().getTokenizer(new StringReader(sentence));
            List<? extends HasWord> sentence2 = toke.tokenize();
            Tree parse = lp.apply(sentence2);
            String pennString = parse.toString();
            //System.out.println(pennString);
            //System.exit(0);
            pennString = pennString.replaceAll("[\\r\\n]", "");
            pennString = pennString.replaceAll("\\s+", " ");
            cpennString = cpennString + " " + pennString;

        }

        return cpennString;
    }

    public ArrayList getNounTokens(String raw) {

        raw = getPennString(raw);

        //raw = raw.replaceAll("\\W", "");

        ArrayList phrases = new ArrayList();

        ArrayList idenList = new ArrayList();
        idenList.add("NN");
        idenList.add("NNS");
        idenList.add("NNP");
        idenList.add("NNPS");

        for (int j = 0; j < idenList.size(); j++) {
            String phraseIdentifier = (String) idenList.get(j);
            int pointer = 0;

            while (true) {
                //Extract NP
                int loc = raw.indexOf("(" + phraseIdentifier, pointer);
                if (loc == -1) {
                    break;
                }

                boolean isOpen = false;
                int extractCharAt = 0;
                for (int i = loc + 3; i < raw.length(); i++) {
                    char nextChar = raw.charAt(i);
                    if (nextChar == '(') {
                        isOpen = true;
                        continue;
                    }
                    if (nextChar == ')') {
                        if (isOpen) {
                            isOpen = false;
                        } else {
                            //End
                            extractCharAt = i;
                            break;
                        }
                    }
                }
                String hit = raw.substring(loc, extractCharAt);
                String phrase = "";
                //System.out.println("HIT:" + hit);
                StringTokenizer stok = new StringTokenizer(hit, " ");
                stok.nextToken();
                phrase = stok.nextToken().toLowerCase().trim();
                //check stop word
                if (!swords.containsWord(phrase)) {
                    //clean starting numerics
                    phrases.add(cleanStartingPeriod(phrase));
                }

                loc = extractCharAt;
                pointer = loc + 1;
                if (pointer > raw.length()) {
                    break;
                }

            }
        }

        //System.out.println("   Noun: " + phrases.toString());
        return phrases;
    }

    public ArrayList getVerbTokens(String raw) {

        raw = getPennString(raw);
        
        //raw = raw.replaceAll("\\W", "");

        ArrayList phrases = new ArrayList();

        ArrayList idenList = new ArrayList();
        idenList.add("VB");
        idenList.add("VBD");
        idenList.add("VBG");
        idenList.add("VBN");
        idenList.add("VBP");
        idenList.add("VBZ");

        for (int j = 0; j < idenList.size(); j++) {
            String phraseIdentifier = (String) idenList.get(j);
            int pointer = 0;

            while (true) {
                //Extract NP
                int loc = raw.indexOf("(" + phraseIdentifier, pointer);
                if (loc == -1) {
                    break;
                }

                boolean isOpen = false;
                int extractCharAt = 0;
                for (int i = loc + 3; i < raw.length(); i++) {
                    char nextChar = raw.charAt(i);
                    if (nextChar == '(') {
                        isOpen = true;
                        continue;
                    }
                    if (nextChar == ')') {
                        if (isOpen) {
                            isOpen = false;
                        } else {
                            //End
                            extractCharAt = i;
                            break;
                        }
                    }
                }
                String hit = raw.substring(loc, extractCharAt);
                String phrase = "";
                //System.out.println("HIT:" + hit);
                StringTokenizer stok = new StringTokenizer(hit, " ");
                stok.nextToken();
                phrase = stok.nextToken().toLowerCase().trim();
                //check stop word
                if (!swords.containsWord(phrase)) {
                    //clean starting numerics
                    phrases.add(cleanStartingPeriod(phrase));
                }

                loc = extractCharAt;
                pointer = loc + 1;
                if (pointer > raw.length()) {
                    break;
                }

            }
        }
        //System.out.println("   Verb: " + phrases.toString());
        return phrases;
    }

    private ArrayList getPhrases(String raw, String phraseIdentifier) {

        //System.out.println(raw);
        boolean isNounExtraction = true;

        if (phraseIdentifier.contains("NP")) {
            isNounExtraction = true;
        } else {
            isNounExtraction = false;
        }
        int pointer = 0;
        ArrayList phrases = new ArrayList();
        while (true) {
            //Extract NP
            int loc = raw.indexOf("(" + phraseIdentifier, pointer);
            if (loc == -1) {
                break;
            }

            boolean isOpen = false;
            int extractCharAt = 0;
            for (int i = loc + 3; i < raw.length(); i++) {
                char nextChar = raw.charAt(i);
                if (nextChar == '(') {
                    isOpen = true;
                    continue;
                }
                if (nextChar == ')') {
                    if (isOpen) {
                        isOpen = false;
                    } else {
                        //End
                        extractCharAt = i;
                        break;
                    }
                }
            }
            String hit = raw.substring(loc, extractCharAt);
            String phrase = "";
            //System.out.println("HIT:"+hit);
            StringTokenizer stok = new StringTokenizer(hit, " ");
            while (stok.hasMoreTokens()) {
                String token = stok.nextToken().trim();
                if (token.endsWith(")")) {
                    phrase = phrase + " " + token.substring(0, token.length() - 1);
                }

            }
            phrase = phrase.toLowerCase().trim();
            //check every work for stop word
            StringTokenizer stok2 = new StringTokenizer(phrase, " ");
            int numWords = stok2.countTokens();
            int bad = 0;
            while (stok2.hasMoreTokens()) {
                String word = stok2.nextToken().trim();
                if (swords.containsWord(word)) {
                    bad++;
                }
            }

            boolean allBad = false;
            if (bad == numWords) {
                //ignore
                allBad = true;
            }

            if (phrase.length() >= 3) {

                //check stop word
                if (!swords.containsWord(phrase)) {
                    //clean starting numerics
                    if (!allBad) {
                        phrases.add(cleanStartingPeriod(phrase));
                    }
                }
            }
            //The following might prevent proper nouns
            //if (phrase.contains(" ")) {
            //phrases.add(phrase);
            //}
            loc = extractCharAt;
            pointer = loc + 1;
            if (pointer > raw.length()) {
                break;
            }

        }

        //ArrayList masterTokens = new ArrayList();
        //masterTokens.addAll(phrases);
        //ENABLE THIS ON SOME GREEDY SWITCH
        /*
        if (isNounExtraction) {
            masterTokens.addAll(getNounTokens(raw));
        } else {
            masterTokens.addAll(getVerbTokens(raw));
        }
         */
        //remove duplciates
// add elements to al, including duplicates
        Set<String> hs = new HashSet<>();
        hs.addAll(phrases);
        phrases.clear();
        phrases.addAll(hs);
        Collections.sort(phrases);

        return phrases;
    }

    public String getPOSSignature(String text) {
        if (text == null) {
            return null;
        }
        //Computes the POS signature of a text string
        //System.out.println(text);
        Pattern p = Pattern.compile("\\((.*?)\\)");
        String posSignature = "";
        Matcher m = p.matcher(text);
        while (m.find()) {
            String pattern = m.group(1);
            //System.out.println("PAT:" + pattern);
            if (pattern.contains("(")) {
                int loc = pattern.lastIndexOf("(");
                String spattern = pattern.substring(loc + 1, pattern.length());
                //System.out.println("SPAT:" + spattern);
                StringTokenizer stok = new StringTokenizer(spattern, " ");
                String posToken = stok.nextToken().trim();
                if (posToken.length() != 1) {
                    posSignature = posSignature + ":" + posToken;
                }
            } else {
                //Clean pattern
                StringTokenizer stok = new StringTokenizer(pattern, " ");
                String posToken = stok.nextToken().trim();
                if (posToken.length() != 1) {
                    posSignature = posSignature + ":" + posToken;
                }
            }
        }
        //System.out.println(posSignature);
        return posSignature;
    }

    private static String cleanStartingPeriod(String str) {
        String clean = "";

        boolean charHit = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (str.charAt(i) == '.') {
                if (charHit) {
                    clean = clean + c;
                }
            } else {
                clean = clean + c;
                charHit = true;
            }
        }

        return clean.trim();
    }

}
