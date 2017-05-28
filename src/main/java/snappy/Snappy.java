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

package snappy;

import java.util.ArrayList;
import java.util.HashMap;
import snappy.model.Learner;
import snappy.model.NueralGramModel;
import snappy.model.TrainerModel;

/**
 *
 * @author fjenning
 */
public class Snappy {

    /**
     * @param args the command line arguments
     */
    private static final String dataFile = "C:\\Snappy\\data\\data.csv";
    private static final String summaryFile = "C:\\Snappy\\data\\datasum.csv";
    private static final String modelFile = "C:\\Snappy\\data\\snappy.ser";

    private static ArrayList filterList = new ArrayList();
    private static final int processOnly = 50;
    
    

    private static void populatefilterList() {

        filterList.add("error");
        filterList.add("error");
        filterList.add("warning");
        filterList.add("issue");
        filterList.add("slowdown");
        filterList.add("stuck");
        filterList.add("stall");
        filterList.add("terms");

    }
    
    public static void doTraining(){
        //TRAINING
        //Step 1: Creater Trainer Model
        System.out.println("[Snappy] Initializing Labels and Clusters...");
        populatefilterList();
        TrainerModel trainerModel = new TrainerModel();
        trainerModel.setLabel("Issue");
        trainerModel.setClusters(filterList);
        
        //Start Learning
        Learner learner = new Learner(new NueralGramModel(), dataFile, trainerModel, processOnly);
        learner.startLearning();
        learner.printLearnStats();
        learner.writeIncidents(summaryFile);
        learner.updateModel(true, modelFile);
    }
    
    public static void doTesting(){
        //TESTING
        //Start testing
        Learner learner = new Learner(new NueralGramModel(), null, null, processOnly);
        learner.startTesting(modelFile);
        learner.printLearnStats();
    }

    public static void main(String[] args) {
        //doTraining();
        doTesting();

    }

    

}
