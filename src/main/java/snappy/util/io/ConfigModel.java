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

package snappy.util.io;

/**
 *
 * @author fjenning
 */
public class ConfigModel {
    
    private String dataFile = null;
    private String summaryFile = null;
    private String modelFile = null;
    private String trainingFile = null;
    private String biasFile = null;
    private int processOnly = 50;
    private String mode = null;
    private String silent = null;
    private boolean fastmode = false;
    private boolean singlelabel = true;
    private boolean multivariate = false;

    public boolean isMultivariate() {
        return multivariate;
    }

    public void setMultivariate(boolean multivariate) {
        this.multivariate = multivariate;
    }
    
    

    /**
     *
     * @return
     */
    public boolean isSinglelabel() {
        return singlelabel;
    }

    /**
     *
     * @param singlelabel
     */
    public void setSinglelabel(boolean singlelabel) {
        this.singlelabel = singlelabel;
    }

    /**
     *
     * @return
     */
    public boolean isFastmode() {
        return fastmode;
    }

    /**
     *
     * @param fastmode
     */
    public void setFastmode(boolean fastmode) {
        this.fastmode = fastmode;
    }

    public String getBiasFile() {
        return biasFile;
    }

    public void setBiasFile(String biasFile) {
        this.biasFile = biasFile;
    }
    
    

    /**
     *
     * @return
     */
    public String getSilent() {
        return silent;
    }

    /**
     *
     * @param silent
     */
    public void setSilent(String silent) {
        this.silent = silent;
    }

    /**
     *
     * @return
     */
    public String getDataFile() {
        return dataFile;
    }

    /**
     *
     * @param dataFile
     */
    public void setDataFile(String dataFile) {
        this.dataFile = dataFile;
    }

    /**
     *
     * @return
     */
    public String getSummaryFile() {
        return summaryFile;
    }

    /**
     *
     * @param summaryFile
     */
    public void setSummaryFile(String summaryFile) {
        this.summaryFile = summaryFile;
    }

    /**
     *
     * @return
     */
    public String getModelFile() {
        return modelFile;
    }

    /**
     *
     * @param modelFile
     */
    public void setModelFile(String modelFile) {
        this.modelFile = modelFile;
    }

    /**
     *
     * @return
     */
    public String getTrainingFile() {
        return trainingFile;
    }

    /**
     *
     * @param trainingFile
     */
    public void setTrainingFile(String trainingFile) {
        this.trainingFile = trainingFile;
    }

    /**
     *
     * @return
     */
    public int getProcessOnly() {
        return processOnly;
    }

    /**
     *
     * @param processOnly
     */
    public void setProcessOnly(int processOnly) {
        this.processOnly = processOnly;
    }

    /**
     *
     * @return
     */
    public String getMode() {
        return mode;
    }

    /**
     *
     * @param mode
     */
    public void setMode(String mode) {
        this.mode = mode;
    }
    
    
    
}
