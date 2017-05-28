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
import snappy.model.GramModel;
import snappy.ngrams.iterator.NGramIterator;
import snappy.util.grams.GramUtils;

/**
 *
 * @author fjenning
 */
public class Populater {

    private GramModel gramModel = null;
    private ArrayList filterModelList = null;

    public Populater(GramModel gramModel, ArrayList filterModelList) {
        this.gramModel = gramModel;
        this.filterModelList = filterModelList;
    }

    public void populateNgrams(String line, int n) {

        //Unpack gram model
        HashMap ngramMap = gramModel.getNgramMap();

        //Building unigram map
        NGramIterator iter = new NGramIterator(n, line);
        while (iter.hasNext()) {
            String ngram = ((String) iter.next()).trim();
            if (ngram.length() < 3) {
                continue;
            }

            if (GramUtils.isFilteredGram(filterModelList, ngram)) {

                if (ngramMap.containsKey(ngram)) {
                    int count = (int) ngramMap.get(ngram);
                    count = count + 1;
                    ngramMap.remove(ngram);
                    ngramMap.put(ngram, count);
                } else {
                    ngramMap.put(ngram, 1);
                }
            }

        }

        gramModel.setNgramMap(ngramMap);

    }

}
