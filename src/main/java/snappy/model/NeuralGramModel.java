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

package snappy.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 *
 * @author fjenning
 */
public class NeuralGramModel implements Serializable{

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(NeuralGramModel.class.getName());
    
    private HashMap unigramMap = null;
    private HashMap bigramMap = null;
    private HashMap trigramMap = null;
    private HashMap quadgramMap = null;
    private HashMap verbMap = null;
    
    private TrainerModel trainerModel = null;

    /**
     *
     * @return
     */
    public HashMap getUnigramMap() {
        return unigramMap;
    }

    /**
     *
     * @param unigramMap
     */
    public void setUnigramMap(HashMap unigramMap) {
        this.unigramMap = unigramMap;
    }

    /**
     *
     * @return
     */
    public HashMap getBigramMap() {
        return bigramMap;
    }

    /**
     *
     * @param bigramMap
     */
    public void setBigramMap(HashMap bigramMap) {
        this.bigramMap = bigramMap;
    }

    /**
     *
     * @return
     */
    public HashMap getTrigramMap() {
        return trigramMap;
    }

    /**
     *
     * @param trigramMap
     */
    public void setTrigramMap(HashMap trigramMap) {
        this.trigramMap = trigramMap;
    }

    /**
     *
     * @return
     */
    public HashMap getQuadgramMap() {
        return quadgramMap;
    }

    /**
     *
     * @param quadgramMap
     */
    public void setQuadgramMap(HashMap quadgramMap) {
        this.quadgramMap = quadgramMap;
    }

    /**
     *
     * @return
     */
    public HashMap getVerbMap() {
        return verbMap;
    }

    /**
     *
     * @param verbMap
     */
    public void setVerbMap(HashMap verbMap) {
        this.verbMap = verbMap;
    }

    /**
     *
     * @return
     */
    public TrainerModel getTrainerModel() {
        return trainerModel;
    }

    /**
     *
     * @param trainerModel
     */
    public void setTrainerModel(TrainerModel trainerModel) {
        this.trainerModel = trainerModel;
    }
    
    
    
    
    
}
