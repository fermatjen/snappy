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
package snappy.ngrams.iterator;

import java.util.Iterator;
import java.util.logging.Logger;

/**
 *
 * @author fjenning
 */
public class NGramIterator implements Iterator<String> {

    String[] words;
    int pos = 0, n;

    /**
     *
     * @param n
     * @param str
     */
    public NGramIterator(int n, String str) {
        this.n = n;
        words = str.split(" ");
        //words = str.split("[[ ]*|[,]*|[;]*|[:]*|[']*|[’]*|[\\\\.]*|[:]*|[/]*|[!]*|[?]*|[+]*]‌​+");
    }

    @Override
    public boolean hasNext() {
        return pos < words.length - n + 1;
    }

    @Override
    public String next() {
        StringBuilder sb = new StringBuilder();
        for (int i = pos; i < pos + n; i++) {
            sb.append(i > pos ? " " : "").append(words[i]);
        }
        pos++;
        return sb.toString();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
    private static final Logger LOG = Logger.getLogger(NGramIterator.class.getName());
}
