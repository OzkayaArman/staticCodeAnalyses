
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests for the InheritanceAnalyses class
 */
public class InheritenceTests {
    
    /**
     * Test tthat the maximum breadth is 0 when there is no inheritance in a project
     */
    @Test
    void testNoInheritenceMaximumBreadth(){
        InheritanceAnalyses ia = new InheritanceAnalyses("Examples/Inheritance/NoInheritance");
        assertEquals(3, ia.getNumberOfClasses());
        assertEquals(0, ia.getMaximumBreadth());
    } 

    /**
     * Test that the maximum breadth is 1 when there is one subclass in the inheritance hierarchy
     */
    @Test 
    void testOneSubClassMaximumBradth(){
        InheritanceAnalyses ia = new InheritanceAnalyses("Examples/Inheritance/OneSubClass");
        assertEquals(4, ia.getNumberOfClasses());
        assertEquals(1, ia.getMaximumBreadth());
    }

    /**
     * Test that the maximum breadth is 3 in a tree of depth 1. 
     * In this example, there are 2 parent classes and 5 subclasses. 
     * 3 are children of the first parent class and 2 are children of the second parent class.
     */
    @Test 
    void testFiveSubClassesWhereMaximumBreadthThree(){
        InheritanceAnalyses ia = new InheritanceAnalyses("examples/Inheritance/FourSubClass");
        assertEquals(8, ia.getNumberOfClasses());
        assertEquals(3, ia.getMaximumBreadth());
    }

    /**
     * Test that the average branching factor is 0 when there is no inheritance.
     */
    @Test
    void testNoInheritenceAverageBranchingFactor(){
        InheritanceAnalyses ia = new InheritanceAnalyses("Examples/Inheritance/NoInheritance");
        double numParentClasses = 3;
        double numSubClasses = 0;
        assertEquals(numParentClasses, ia.getNumberOfParentClasses());
        assertEquals(numSubClasses, ia.getNumberOfSubClasses());
        assertEquals(numSubClasses+numParentClasses, ia.getNumberOfClasses());
        assertEquals(0, ia.getAverageBranchingFactor());
    }

    /**
     * Test that the average branching factor where there is only one subclass of depth 1.
     * In this example, there are 3 parent classes and 1 subclass.
     */
    @Test 
    void testOneSubClassAverageBranchingFactor(){
        InheritanceAnalyses ia = new InheritanceAnalyses("Examples/Inheritance/OneSubClass");
        double numParentClasses = 3;
        double numSubClasses = 1;
        assertEquals(numParentClasses, ia.getNumberOfParentClasses());
        assertEquals(numSubClasses, ia.getNumberOfSubClasses());
        assertEquals(numSubClasses+numParentClasses, ia.getNumberOfClasses());
        assertEquals(numSubClasses/numParentClasses, ia.getAverageBranchingFactor());
    }


    /**
     * Test that the average branching factor for tree of depth 1.
     * In this example, there are 2 parent classes and 5 subclass.
     */
    @Test 
    void testFiveSubClassesAverageBranchingFactor(){
        InheritanceAnalyses ia = new InheritanceAnalyses("examples/Inheritance/FourSubClass");
        double numParentClasses = 2;
        double numSubClasses = 5;
        assertEquals(numParentClasses, ia.getNumberOfParentClasses());
        assertEquals(numSubClasses, ia.getNumberOfSubClasses());
        assertEquals(numSubClasses+numParentClasses, ia.getNumberOfClasses());
        assertEquals(numSubClasses/numParentClasses, ia.getAverageBranchingFactor());

    }

    /**
     * Test that the average branching factor and Maximum Breadth for a deeper tree (> depth of 1).
     * In this example, there are 4 parent classes and 9 subclasses.
     */
    @Test
    void testMaximumBreadthAverageBranchingFactorDepthThree(){
        InheritanceAnalyses ia = new InheritanceAnalyses("Examples/Inheritance/Deep");
        double numParentClasses = 4;
        double numSubClasses = 9;
        assertEquals(numParentClasses, ia.getNumberOfParentClasses());
        assertEquals(numSubClasses, ia.getNumberOfSubClasses());
        assertEquals(numSubClasses+numParentClasses, ia.getNumberOfClasses());

        

    }

}
