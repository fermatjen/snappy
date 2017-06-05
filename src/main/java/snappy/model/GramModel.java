

package snappy.model;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 *
 * @author fjenning
 */
public class GramModel {
    
    private HashMap ngramMap = null;

    /**
     *
     */
    public GramModel() {
        //An empty constructor looks intriguing
    }
    
    /**
     *
     * @return
     */
    public HashMap getNgramMap() {
        return ngramMap;
    }

    /**
     *
     * @param ngramMap
     */
    public void setNgramMap(HashMap ngramMap) {
        this.ngramMap = ngramMap;
    }
    private static final Logger LOG = Logger.getLogger(GramModel.class.getName());
    
    
    
    
}
