import java.util.HashSet;

/**
 * This class is responsible for storing the subclasses of a parent class.
 */
public class SubClassCollection {
    private String parentClassName; // the name of the parent class
    private boolean isParentClass; // true if the class is a parent class, false otherwise
    private HashSet<String> subClasses; // the set of subclasses

    /**
     * Constructor for the SubClassCollection class.
     * @param className - the name of the parent class
     * @param isParentClass - boolean - true if the class is a parent class, false otherwise
     */ 
    public SubClassCollection(String className, boolean isParentClass) {
        this.parentClassName = className;
        this.isParentClass = isParentClass;
        this.subClasses = new HashSet<>();
    }

    /**
     * This method sets whether the class is a parent class or not.
     * @param isParentClass - boolean - true if the class is a parent
     */
    public void setIsParentClass(boolean isParentClass) {
        this.isParentClass = isParentClass;
    }

    /**
     * This method adds a subclass to the class to the subClasses set.
     * @param subClass - the name of the subclass
     */
    public void addSubClass(String subClass) {
        this.subClasses.add(subClass);
    }

    /**
     * This method returns whether the class is a parent class or not.
     * @return - boolean - true if the class is a parent class, false otherwise.
     */
    public boolean isParentClass() {
        return this.isParentClass;
    }

    /**
     * This method returns the name of the parent class that the subclasses belongs to.
     * @return - String - the name of the parent class.
     */
    public String getParentClassName() {
        return this.parentClassName;
    }

    /**
     * This method returns the set of subclasses that the parent class has.
     * @return - HashSet<String> - the set of subclasses.
     */
    public HashSet<String> getSubClasses() {
        return this.subClasses;
    }

    /**
     * This method returns the number of subclasses that the parent class has.
     * @return - int - the number of subclasses.
     */
    public int getNumberOfSubClasses() {
        return this.subClasses.size();
    }
}
