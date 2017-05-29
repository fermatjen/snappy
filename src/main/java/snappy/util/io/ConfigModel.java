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

import java.util.ArrayList;

/**
 *
 * @author fjenning
 */
public class ConfigModel {
    
    private String dataFile = null;
    private String summaryFile = null;
    private String modelFile = null;
    private String trainingFile = null;
    private int processOnly = 50;

    public String getDataFile() {
        return dataFile;
    }

    public void setDataFile(String dataFile) {
        this.dataFile = dataFile;
    }

    public String getSummaryFile() {
        return summaryFile;
    }

    public void setSummaryFile(String summaryFile) {
        this.summaryFile = summaryFile;
    }

    public String getModelFile() {
        return modelFile;
    }

    public void setModelFile(String modelFile) {
        this.modelFile = modelFile;
    }

    public String getTrainingFile() {
        return trainingFile;
    }

    public void setTrainingFile(String trainingFile) {
        this.trainingFile = trainingFile;
    }

    public int getProcessOnly() {
        return processOnly;
    }

    public void setProcessOnly(int processOnly) {
        this.processOnly = processOnly;
    }
    
    
    
}
