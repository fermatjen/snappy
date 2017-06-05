

package snappy.model;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import java.util.Properties;
import java.util.logging.Logger;

/**
 *
 * @author fjenning
 */
public class NLPModel {

    private LexicalizedParser lp = null;
    private TreebankLanguagePack tlp = null;
    private GrammaticalStructureFactory gsf = null;

    private StanfordCoreNLP pipeline = null;
    private Properties props = null;

    /**
     *
     */
    public NLPModel() {
        lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
        tlp = new PennTreebankLanguagePack();
        gsf = tlp.grammaticalStructureFactory();
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        pipeline = new StanfordCoreNLP(props, false);
    }

    /**
     *
     * @return
     */
    public LexicalizedParser getLp() {
        return lp;
    }

    /**
     *
     * @param lp
     */
    public void setLp(LexicalizedParser lp) {
        this.lp = lp;
    }

    /**
     *
     * @return
     */
    public TreebankLanguagePack getTlp() {
        return tlp;
    }

    /**
     *
     * @param tlp
     */
    public void setTlp(TreebankLanguagePack tlp) {
        this.tlp = tlp;
    }

    /**
     *
     * @return
     */
    public GrammaticalStructureFactory getGsf() {
        return gsf;
    }

    /**
     *
     * @param gsf
     */
    public void setGsf(GrammaticalStructureFactory gsf) {
        this.gsf = gsf;
    }

    /**
     *
     * @return
     */
    public StanfordCoreNLP getPipeline() {
        return pipeline;
    }

    /**
     *
     * @param pipeline
     */
    public void setPipeline(StanfordCoreNLP pipeline) {
        this.pipeline = pipeline;
    }

    /**
     *
     * @return
     */
    public Properties getProps() {
        return props;
    }

    /**
     *
     * @param props
     */
    public void setProps(Properties props) {
        this.props = props;
    }
    private static final Logger LOG = Logger.getLogger(NLPModel.class.getName());
    
    

}
