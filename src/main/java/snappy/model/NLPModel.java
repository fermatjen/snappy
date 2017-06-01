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
    private static final Logger LOG = Logger.getLogger(NLPModel.class.getName());

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
    
    

}
