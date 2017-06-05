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

package snappy.pos;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 *
 * @author fjenning
 */
public class StopWords {
    
    private final ArrayList swords = new ArrayList();

    /**
     *
     */
    public StopWords() {
        swords.add("a");
        swords.add("able");
        swords.add("about");
        swords.add("above");
        swords.add("abst");
        swords.add("accordance");
        swords.add("according");
        swords.add("accordingly");
        swords.add("across");
        swords.add("act");
        swords.add("actually");
        swords.add("added");
        swords.add("adj");
        swords.add("affected");
        swords.add("affecting");
        swords.add("affects");
        swords.add("after");
        swords.add("afterwards");
        swords.add("again");
        swords.add("against");
        swords.add("ah");
        swords.add("all");
        swords.add("almost");
        swords.add("alone");
        swords.add("along");
        swords.add("already");
        swords.add("also");
        swords.add("although");
        swords.add("always");
        swords.add("am");
        swords.add("among");
        swords.add("amongst");
        swords.add("an");
        swords.add("and");
        swords.add("announce");
        swords.add("another");
        swords.add("any");
        swords.add("anybody");
        swords.add("anyhow");
        swords.add("anymore");
        swords.add("anyone");
        swords.add("anything");
        swords.add("anyway");
        swords.add("anyways");
        swords.add("anywhere");
        swords.add("apparently");
        swords.add("approximately");
        swords.add("are");
        swords.add("aren");
        swords.add("arent");
        swords.add("arise");
        swords.add("around");
        swords.add("as");
        swords.add("aside");
        swords.add("ask");
        swords.add("asking");
        swords.add("at");
        swords.add("auth");
        swords.add("available");
        swords.add("away");
        swords.add("awfully");
        swords.add("b");
        swords.add("back");
        swords.add("be");
        swords.add("became");
        swords.add("because");
        swords.add("become");
        swords.add("becomes");
        swords.add("becoming");
        swords.add("been");
        swords.add("before");
        swords.add("beforehand");
        swords.add("begin");
        swords.add("beginning");
        swords.add("beginnings");
        swords.add("begins");
        swords.add("behind");
        swords.add("being");
        swords.add("believe");
        swords.add("below");
        swords.add("beside");
        swords.add("besides");
        swords.add("between");
        swords.add("beyond");
        swords.add("biol");
        swords.add("both");
        swords.add("brief");
        swords.add("briefly");
        swords.add("but");
        swords.add("by");
        swords.add("c");
        swords.add("ca");
        swords.add("came");
        swords.add("can");
        swords.add("cannot");
        swords.add("can't");
        swords.add("cause");
        swords.add("causes");
        swords.add("certain");
        swords.add("certainly");
        swords.add("co");
        swords.add("com");
        swords.add("come");
        swords.add("comes");
        swords.add("contain");
        swords.add("containing");
        swords.add("contains");
        swords.add("could");
        swords.add("couldnt");
        swords.add("d");
        swords.add("date");
        swords.add("did");
        swords.add("didn't");
        swords.add("different");
        swords.add("do");
        swords.add("does");
        swords.add("doesn't");
        swords.add("doing");
        swords.add("done");
        swords.add("don't");
        swords.add("down");
        swords.add("downwards");
        swords.add("due");
        swords.add("during");
        swords.add("e");
        swords.add("each");
        swords.add("ed");
        swords.add("edu");
        swords.add("effect");
        swords.add("eg");
        swords.add("eight");
        swords.add("eighty");
        swords.add("either");
        swords.add("else");
        swords.add("elsewhere");
        swords.add("end");
        swords.add("ending");
        swords.add("enough");
        swords.add("especially");
        swords.add("et");
        swords.add("et-al");
        swords.add("etc");
        swords.add("even");
        swords.add("ever");
        swords.add("every");
        swords.add("everybody");
        swords.add("everyone");
        swords.add("everything");
        swords.add("everywhere");
        swords.add("ex");
        swords.add("except");
        swords.add("f");
        swords.add("far");
        swords.add("few");
        swords.add("ff");
        swords.add("fifth");
        swords.add("first");
        swords.add("five");
        swords.add("fix");
        swords.add("followed");
        swords.add("following");
        swords.add("follows");
        swords.add("for");
        swords.add("former");
        swords.add("formerly");
        swords.add("forth");
        swords.add("found");
        swords.add("four");
        swords.add("from");
        swords.add("further");
        swords.add("furthermore");
        swords.add("g");
        swords.add("gave");
        swords.add("get");
        swords.add("gets");
        swords.add("getting");
        swords.add("give");
        swords.add("given");
        swords.add("gives");
        swords.add("giving");
        swords.add("go");
        swords.add("goes");
        swords.add("gone");
        swords.add("got");
        swords.add("gotten");
        swords.add("h");
        swords.add("had");
        swords.add("happens");
        swords.add("hardly");
        swords.add("has");
        swords.add("hasn't");
        swords.add("have");
        swords.add("haven't");
        swords.add("having");
        swords.add("he");
        swords.add("hed");
        swords.add("hence");
        swords.add("her");
        swords.add("here");
        swords.add("hereafter");
        swords.add("hereby");
        swords.add("herein");
        swords.add("heres");
        swords.add("hereupon");
        swords.add("hers");
        swords.add("herself");
        swords.add("hes");
        swords.add("hi");
        swords.add("hid");
        swords.add("him");
        swords.add("himself");
        swords.add("his");
        swords.add("hither");
        swords.add("home");
        swords.add("how");
        swords.add("howbeit");
        swords.add("however");
        swords.add("hundred");
        swords.add("i");
        swords.add("id");
        swords.add("ie");
        swords.add("if");
        swords.add("i'll");
        swords.add("im");
        swords.add("immediate");
        swords.add("immediately");
        swords.add("importance");
        swords.add("important");
        swords.add("in");
        swords.add("inc");
        swords.add("indeed");
        swords.add("index");
        swords.add("information");
        swords.add("instead");
        swords.add("into");
        swords.add("invention");
        swords.add("inward");
        swords.add("is");
        swords.add("isn't");
        swords.add("it");
        swords.add("itd");
        swords.add("it'll");
        swords.add("its");
        swords.add("itself");
        swords.add("i've");
        swords.add("j");
        swords.add("just");
        swords.add("k");
        swords.add("keep");
        swords.add("keeps");
        swords.add("kept");
        swords.add("kg");
        swords.add("km");
        swords.add("know");
        swords.add("known");
        swords.add("knows");
        swords.add("l");
        swords.add("largely");
        swords.add("last");
        swords.add("lately");
        swords.add("later");
        swords.add("latter");
        swords.add("latterly");
        swords.add("least");
        swords.add("less");
        swords.add("lest");
        swords.add("let");
        swords.add("lets");
        swords.add("like");
        swords.add("liked");
        swords.add("likely");
        swords.add("line");
        swords.add("little");
        swords.add("'ll");
        swords.add("look");
        swords.add("looking");
        swords.add("looks");
        swords.add("ltd");
        swords.add("m");
        swords.add("made");
        swords.add("mainly");
        swords.add("make");
        swords.add("makes");
        swords.add("many");
        swords.add("may");
        swords.add("maybe");
        swords.add("me");
        swords.add("mean");
        swords.add("means");
        swords.add("meantime");
        swords.add("meanwhile");
        swords.add("merely");
        swords.add("mg");
        swords.add("might");
        swords.add("million");
        swords.add("miss");
        swords.add("ml");
        swords.add("more");
        swords.add("moreover");
        swords.add("most");
        swords.add("mostly");
        swords.add("mr");
        swords.add("mrs");
        swords.add("much");
        swords.add("mug");
        swords.add("must");
        swords.add("my");
        swords.add("myself");
        swords.add("n");
        swords.add("na");
        swords.add("name");
        swords.add("namely");
        swords.add("nay");
        swords.add("nd");
        swords.add("near");
        swords.add("nearly");
        swords.add("necessarily");
        swords.add("necessary");
        swords.add("need");
        swords.add("needs");
        swords.add("neither");
        swords.add("never");
        swords.add("nevertheless");
        swords.add("new");
        swords.add("next");
        swords.add("nine");
        swords.add("ninety");
        swords.add("no");
        swords.add("nobody");
        swords.add("non");
        swords.add("none");
        swords.add("nonetheless");
        swords.add("noone");
        swords.add("nor");
        swords.add("normally");
        swords.add("nos");
        swords.add("not");
        swords.add("noted");
        swords.add("nothing");
        swords.add("now");
        swords.add("nowhere");
        swords.add("o");
        swords.add("obtain");
        swords.add("obtained");
        swords.add("obviously");
        swords.add("of");
        swords.add("off");
        swords.add("often");
        swords.add("oh");
        swords.add("ok");
        swords.add("okay");
        swords.add("old");
        swords.add("omitted");
        swords.add("on");
        swords.add("once");
        swords.add("one");
        swords.add("ones");
        swords.add("only");
        swords.add("onto");
        swords.add("or");
        swords.add("ord");
        swords.add("other");
        swords.add("others");
        swords.add("otherwise");
        swords.add("ought");
        swords.add("our");
        swords.add("ours");
        swords.add("ourselves");
        swords.add("out");
        swords.add("outside");
        swords.add("over");
        swords.add("overall");
        swords.add("owing");
        swords.add("own");
        swords.add("p");
        swords.add("page");
        swords.add("pages");
        swords.add("part");
        swords.add("particular");
        swords.add("particularly");
        swords.add("past");
        swords.add("per");
        swords.add("perhaps");
        swords.add("placed");
        swords.add("please");
        swords.add("plus");
        swords.add("poorly");
        swords.add("possible");
        swords.add("possibly");
        swords.add("potentially");
        swords.add("pp");
        swords.add("predominantly");
        swords.add("present");
        swords.add("previously");
        swords.add("primarily");
        swords.add("probably");
        swords.add("promptly");
        swords.add("proud");
        swords.add("provides");
        swords.add("put");
        swords.add("q");
        swords.add("que");
        swords.add("quickly");
        swords.add("quite");
        swords.add("qv");
        swords.add("r");
        swords.add("ran");
        swords.add("rather");
        swords.add("rd");
        swords.add("re");
        swords.add("readily");
        swords.add("really");
        swords.add("recent");
        swords.add("recently");
        swords.add("ref");
        swords.add("refs");
        swords.add("regarding");
        swords.add("regardless");
        swords.add("regards");
        swords.add("related");
        swords.add("relatively");
        swords.add("research");
        swords.add("respectively");
        swords.add("resulted");
        swords.add("resulting");
        swords.add("results");
        swords.add("right");
        swords.add("run");
        swords.add("s");
        swords.add("said");
        swords.add("same");
        swords.add("saw");
        swords.add("say");
        swords.add("saying");
        swords.add("says");
        swords.add("sec");
        swords.add("section");
        swords.add("see");
        swords.add("seeing");
        swords.add("seem");
        swords.add("seemed");
        swords.add("seeming");
        swords.add("seems");
        swords.add("seen");
        swords.add("self");
        swords.add("selves");
        swords.add("sent");
        swords.add("seven");
        swords.add("several");
        swords.add("shall");
        swords.add("she");
        swords.add("shed");
        swords.add("she'll");
        swords.add("shes");
        swords.add("should");
        swords.add("shouldn't");
        swords.add("show");
        swords.add("showed");
        swords.add("shown");
        swords.add("showns");
        swords.add("shows");
        swords.add("significant");
        swords.add("significantly");
        swords.add("similar");
        swords.add("similarly");
        swords.add("since");
        swords.add("six");
        swords.add("slightly");
        swords.add("so");
        swords.add("some");
        swords.add("somebody");
        swords.add("somehow");
        swords.add("someone");
        swords.add("somethan");
        swords.add("something");
        swords.add("sometime");
        swords.add("sometimes");
        swords.add("somewhat");
        swords.add("somewhere");
        swords.add("soon");
        swords.add("sorry");
        swords.add("specifically");
        swords.add("specified");
        swords.add("specify");
        swords.add("specifying");
        swords.add("still");
        swords.add("stop");
        swords.add("strongly");
        swords.add("sub");
        swords.add("substantially");
        swords.add("successfully");
        swords.add("such");
        swords.add("sufficiently");
        swords.add("suggest");
        swords.add("sup");
        swords.add("sure 	t");
        swords.add("take");
        swords.add("taken");
        swords.add("taking");
        swords.add("tell");
        swords.add("tends");
        swords.add("th");
        swords.add("than");
        swords.add("thank");
        swords.add("thanks");
        swords.add("thanx");
        swords.add("that");
        swords.add("that'll");
        swords.add("thats");
        swords.add("that've");
        swords.add("the");
        swords.add("their");
        swords.add("theirs");
        swords.add("them");
        swords.add("themselves");
        swords.add("then");
        swords.add("thence");
        swords.add("there");
        swords.add("thereafter");
        swords.add("thereby");
        swords.add("thered");
        swords.add("therefore");
        swords.add("therein");
        swords.add("there'll");
        swords.add("thereof");
        swords.add("therere");
        swords.add("theres");
        swords.add("thereto");
        swords.add("thereupon");
        swords.add("there've");
        swords.add("these");
        swords.add("they");
        swords.add("theyd");
        swords.add("they'll");
        swords.add("theyre");
        swords.add("they've");
        swords.add("think");
        swords.add("this");
        swords.add("those");
        swords.add("thou");
        swords.add("though");
        swords.add("thoughh");
        swords.add("thousand");
        swords.add("throug");
        swords.add("through");
        swords.add("throughout");
        swords.add("thru");
        swords.add("thus");
        swords.add("til");
        swords.add("tip");
        swords.add("to");
        swords.add("together");
        swords.add("too");
        swords.add("took");
        swords.add("toward");
        swords.add("towards");
        swords.add("tried");
        swords.add("tries");
        swords.add("truly");
        swords.add("try");
        swords.add("trying");
        swords.add("ts");
        swords.add("twice");
        swords.add("two");
        swords.add("u");
        swords.add("un");
        swords.add("under");
        swords.add("unfortunately");
        swords.add("unless");
        swords.add("unlike");
        swords.add("unlikely");
        swords.add("until");
        swords.add("unto");
        swords.add("up");
        swords.add("upon");
        swords.add("ups");
        swords.add("us");
        swords.add("use");
        swords.add("used");
        swords.add("useful");
        swords.add("usefully");
        swords.add("usefulness");
        swords.add("uses");
        swords.add("using");
        swords.add("usually");
        swords.add("v");
        swords.add("value");
        swords.add("various");
        swords.add("'ve");
        swords.add("very");
        swords.add("via");
        swords.add("viz");
        swords.add("vol");
        swords.add("vols");
        swords.add("vs");
        swords.add("w");
        swords.add("want");
        swords.add("wants");
        swords.add("was");
        swords.add("wasnt");
        swords.add("way");
        swords.add("we");
        swords.add("wed");
        swords.add("welcome");
        swords.add("we'll");
        swords.add("went");
        swords.add("were");
        swords.add("werent");
        swords.add("we've");
        swords.add("what");
        swords.add("whatever");
        swords.add("what'll");
        swords.add("whats");
        swords.add("when");
        swords.add("whence");
        swords.add("whenever");
        swords.add("where");
        swords.add("whereafter");
        swords.add("whereas");
        swords.add("whereby");
        swords.add("wherein");
        swords.add("wheres");
        swords.add("whereupon");
        swords.add("wherever");
        swords.add("whether");
        swords.add("which");
        swords.add("while");
        swords.add("whim");
        swords.add("whither");
        swords.add("who");
        swords.add("whod");
        swords.add("whoever");
        swords.add("whole");
        swords.add("who'll");
        swords.add("whom");
        swords.add("whomever");
        swords.add("whos");
        swords.add("whose");
        swords.add("why");
        swords.add("widely");
        swords.add("willing");
        swords.add("wish");
        swords.add("with");
        swords.add("within");
        swords.add("without");
        swords.add("wont");
        swords.add("words");
        swords.add("world");
        swords.add("would");
        swords.add("wouldnt");
        swords.add("www");
        swords.add("x");
        swords.add("y");
        swords.add("yes");
        swords.add("yet");
        swords.add("you");
        swords.add("youd");
        swords.add("you'll");
        swords.add("your");
        swords.add("youre");
        swords.add("yours");
        swords.add("yourself");
        swords.add("yourselves");
        swords.add("you've");
        swords.add("z");
        swords.add("zero");
        swords.add("has been");
        swords.add("have been");
        
    }
    
    /**
     *
     * @param word
     * @return
     */
    public boolean containsWord(String word){
        word = word.trim();
        return swords.contains(word);
    }
    private static final Logger LOG = Logger.getLogger(StopWords.class.getName());

}
