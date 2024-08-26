package filters;

import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * A filter that represents the logical not of its child filter
 */
public class OrFilter implements Filter {
    private Filter firstChild;
    private Filter secondChild;

    public OrFilter(Filter firstChild, Filter secondChild) {
        this.firstChild = firstChild;
        this.secondChild = secondChild;
    }


    /**
     * A not filter matches when its child doesn't, and vice versa
     * @param s     the tweet to check
     * @return      whether or not it matches
     */
    @Override
    public boolean matches(Status s) {
        return firstChild.matches(s) || secondChild.matches(s);
    }

    @Override
    public List<String> terms() {
        List<String> listOfTerms = new ArrayList<String>();

        listOfTerms.addAll(firstChild.terms());
        listOfTerms.addAll(secondChild.terms());
        
        return listOfTerms;
    }

    public String toString() {
        return "(" + firstChild + " or " + secondChild + ")";
    }
}
