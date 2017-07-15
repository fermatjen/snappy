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
package snappy.util.label;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import snappy.util.text.StringUtils;

/**
 *
 * @author fjenning
 */
public class Cleaner {

    private static final HashMap labelMap = new HashMap();

    public static void main(String ar[]) throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader("C:\\SnappyGIT\\data\\pslabels.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {

                line = line.trim();
                if (!line.contains("(") && !line.contains(")")) {
                    continue;
                }

                //System.out.println(line);
                int loc = line.indexOf("(");
                int roc = line.indexOf(")", loc);

                String label = (line.substring(0, loc)).trim();
                String phrases = (line.substring(loc + 1, roc)).trim();

                if (labelMap.containsKey(label)) {
                    //Merge
                    ArrayList phraseList = (ArrayList)labelMap.get(label);
                    
                    StringTokenizer stok = new StringTokenizer(phrases, ",");

                    while (stok.hasMoreTokens()) {
                        String phrase = stok.nextToken().trim();
                        if(!phraseList.contains(phrase)){
                            phraseList.add(phrase);
                        }
                    }
                    
                    labelMap.remove(label);
                    labelMap.put(label, phraseList);
                    

                } else {
                    //Add
                    ArrayList phraseList = new ArrayList();
                    StringTokenizer stok = new StringTokenizer(phrases, ",");

                    while (stok.hasMoreTokens()) {
                        String phrase = stok.nextToken().trim();
                        if(!phraseList.contains(phrase)){
                            phraseList.add(phrase);
                        }
                    }
                    
                    labelMap.put(label, phraseList);
                }

                //System.out.println(phrases);
            }
        }
        
        //Label Map populated
        Iterator labelIterator = labelMap.keySet().iterator();
        
        while(labelIterator.hasNext()){
            String label = (String) labelIterator.next();
            
            ArrayList phrasesList = (ArrayList)labelMap.get(label);
            
            String entry = label+" ("+phrasesList.toString()+")";
            entry = StringUtils.replace(entry, "([", "(", 0);
            entry = StringUtils.replace(entry, "])", ")", 0);
            
            System.out.println(entry);
            
        }
    }

}
