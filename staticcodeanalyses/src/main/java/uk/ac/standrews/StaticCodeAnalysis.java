package uk.ac.standrews;


import uk.ac.standrews.inheritanceHierarchy.InheritanceAnalyses;

/**
 * Initial Commit!
 *
 */
public class StaticCodeAnalysis 
{
    public static void main( String[] args )
    {
        System.out.println( "Initial Commit" );
        InheritanceAnalyses ia = new InheritanceAnalyses("staticcodeanalyses/src/main/java/uk/ac/standrews/example");
        ia.findMaximumBreadth();
        ia.findAverageBranchingFactor();
    }
}
