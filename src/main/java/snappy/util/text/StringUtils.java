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


package snappy.util.text;

/**
 *
 * @author fjenning
 */
public class StringUtils {
    
    /**
     *
     * @param source
     * @param pattern
     * @param replace
     * @param startAt
     * @return
     */
    public static String replace(String source, String pattern, String replace, int startAt) {

        if (source != null) {
            final int len = pattern.length();
            StringBuilder sb = new StringBuilder();
            int found = -1;
            int start = startAt;

            while ((found = source.indexOf(pattern, start)) != -1) {

                sb.append(source.substring(start, found));
                sb.append(replace);
                start = found + len;
            }

            sb.append(source.substring(start));
            //debugger.showDebugMessage("PatternMatchingIC", startAt, "Pattern Matching...Done");

            return sb.toString();
        } else {
            return "";
        }
    }
    
}
