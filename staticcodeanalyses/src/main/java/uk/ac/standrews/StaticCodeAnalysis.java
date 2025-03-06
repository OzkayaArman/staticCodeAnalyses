package uk.ac.standrews;

import uk.ac.standrews.inheritenceHierarchy.MaximumBreadth;

/**
 * Initial Commit!
 *
 */
public class StaticCodeAnalysis 
{
    public static void main( String[] args )
    {
        System.out.println( "Initial Commit" );
        MaximumBreadth mb = new MaximumBreadth("staticcodeanalyses/src/main/java/uk/ac/standrews/example");
        System.out.println("Maximum Breath of directory: "+ mb.getMaximumBreadth());
    }
}
