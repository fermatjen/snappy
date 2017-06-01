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
import java.util.logging.Logger;
import static snappy.util.collections.Comparator.sortByComparator;

/**
 *
 * @author fjenning
 */
public class GramStats {

    private static final Logger LOG = Logger.getLogger(GramStats.class.getName());

    /**
     *
     * @param unigramMap
     * @param bigramMap
     * @param trigramMap
     * @param quadgramMap
     * @param verbMap
     */
    public static void printGramStats(HashMap unigramMap, HashMap bigramMap, HashMap trigramMap, HashMap quadgramMap, HashMap verbMap) {
        Map<String, Integer> sortedUnigramMap = sortByComparator(unigramMap, false);
        Map<String, Integer> sortedBigramMap = sortByComparator(bigramMap, false);
        Map<String, Integer> sortedTrigramMap = sortByComparator(trigramMap, false);
        Map<String, Integer> sortedQuadgramMap = sortByComparator(quadgramMap, false);
        Map<String, Integer> sortedVerbMap = sortByComparator(verbMap, false);
        LOG.log(Level.INFO, "    Unigrams :{0}", sortedUnigramMap.size());
        LOG.log(Level.INFO, "    Bigrams :{0}", sortedBigramMap.size());
        LOG.log(Level.INFO, "    Trigrams :{0}", sortedTrigramMap.size());
        LOG.log(Level.INFO, "    Quadgrams :{0}", sortedQuadgramMap.size());
    }

}
