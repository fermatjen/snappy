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

package snappy.util.grams;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import static java.util.logging.Level.INFO;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import static snappy.util.collections.Comparator.sortByComparator;

/**
 *
 * @author fjenning
 */
public class GramStats {

    private static final Logger LOG = getLogger(GramStats.class.getName());

    /**
     *
     * @param unigramMap
     * @param bigramMap
     * @param trigramMap
     * @param quadgramMap
     * @param verbMap
     * @param nounMap
     */
    public static void printGramStats(HashMap unigramMap, HashMap bigramMap, HashMap trigramMap, HashMap quadgramMap, HashMap verbMap, HashMap nounMap) {
        Map<String, Integer> sortedUnigramMap = sortByComparator(unigramMap, false);
        Map<String, Integer> sortedBigramMap = sortByComparator(bigramMap, false);
        Map<String, Integer> sortedTrigramMap = sortByComparator(trigramMap, false);
        Map<String, Integer> sortedQuadgramMap = sortByComparator(quadgramMap, false);
        Map<String, Integer> sortedVerbMap = sortByComparator(verbMap, false);
        Map<String, Integer> sortedNounMap = sortByComparator(nounMap, false);
        LOG.log(INFO, "    Unigrams :{0}", sortedUnigramMap.size());
        LOG.log(INFO, "    Bigrams :{0}", sortedBigramMap.size());
        LOG.log(INFO, "    Trigrams :{0}", sortedTrigramMap.size());
        LOG.log(INFO, "    Quadgrams :{0}", sortedQuadgramMap.size());
        LOG.log(INFO, "    Verbs :{0}", sortedVerbMap.size());
        LOG.log(INFO, "    Nouns :{0}", sortedNounMap.size());
    }

    private GramStats() {
    }

}
