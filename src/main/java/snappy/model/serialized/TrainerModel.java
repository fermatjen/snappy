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

package snappy.model.serialized;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author fjenning
 */
public class TrainerModel implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private String label = null;
    private ArrayList clusters = null;

    /**
     *
     * @return
     */
    public String getLabel() {
        return label;
    }

    /**
     *
     * @param label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     *
     * @return
     */
    public ArrayList getClusters() {
        return clusters;
    }

    /**
     *
     * @param clusters
     */
    public void setClusters(ArrayList clusters) {
        this.clusters = clusters;
    }
    
    
    
}
