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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author fjenning
 */
public class Scorer {

    public static ArrayList scoreAllGrams(ArrayList incidentList, HashMap unigramMap, HashMap bigramMap, HashMap trigramMap, HashMap quadgramMap, HashMap verbMap) {

        //Second pass
        ArrayList dupIncidentList = new ArrayList();

        int count = 1;
        for (int j = 0; j < incidentList.size(); j++) {

            String line = (String) incidentList.get(j);

            //System.out.println("INCIDENT: " + line);
            line = line.trim();
            double gramScore = getGramScore(line, unigramMap, bigramMap, trigramMap, quadgramMap, verbMap);
            if (gramScore > 0) {
                //System.out.println(count + ". " + line + " SCORE: " + getGramScore(line));
                //incidentMap.put(sentence, (int) gramScore);
                dupIncidentList.add(line);
            }

            //System.out.println("------------");
            /*
                //Sort incident map
                Map<String, Integer> sortedIncidentMap = sortByComparator(incidentMap, false);
                Iterator i1 = sortedIncidentMap.keySet().iterator();
                String hib = (String)i1.next();
                System.out.println("    "+count + ". " + hib + " SCORE: " + incidentMap.get(hib));
                System.out.println("");
             */
            count++;

        }

        //Flush incidents
        return dupIncidentList;

    }

    public static double getGramScore(String sentence, HashMap unigramMap, HashMap bigramMap, HashMap trigramMap, HashMap quadgramMap, HashMap verbMap) {
        double score = 0;

        //Strict match in unigram list
        Iterator i1 = unigramMap.keySet().iterator();
        while (i1.hasNext()) {
            String gram = (String) i1.next();
            if (sentence.contains(gram)) {

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
            if (sentence.contains(gram)) {

                int c1 = (int) verbMap.get(gram);
                double s = (c1 * 1) + (gram.length() * 0.05);
                score = score + s;
                //System.out.println("         Match (1): " + gram + " (score: +" + (s)+")");
            }
        }

        Iterator i2 = bigramMap.keySet().iterator();
        while (i2.hasNext()) {
            String gram = (String) i2.next();
            if (sentence.contains(gram)) {
                int c1 = (int) bigramMap.get(gram);

                double s = (c1 * 2) + (gram.length() * 0.15);
                score = score + s;
                //System.out.println("         Match (2): " + gram + " (score: +" + (s));
            }
        }
        Iterator i3 = trigramMap.keySet().iterator();
        while (i3.hasNext()) {
            String gram = (String) i3.next();
            if (sentence.contains(gram)) {
                int c1 = (int) trigramMap.get(gram);

                double s = (c1 * 3) + (gram.length() * 0.2);
                score = score + s;
                //System.out.println("         Match (3): " + gram + " (score: +" + (s));
            }
        }
        Iterator i4 = quadgramMap.keySet().iterator();
        while (i4.hasNext()) {
            String gram = (String) i4.next();
            if (sentence.contains(gram)) {
                int c1 = (int) quadgramMap.get(gram);

                double s = (c1 * 4) + (gram.length() * 0.6);
                score = score + s;
                //System.out.println("         Match (4): " + gram + " (score: +" + (s));
            }
        }

        return score;
    }

}
