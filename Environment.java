// This class provides a stubbed-out environment.
// You are expected to implement the methods.
// Accessing an undefined variable should throw an exception.

// Hint!
// Use the Java API to implement your Environment.
// Browse:
//   https://docs.oracle.com/javase/tutorial/tutorialLearningPaths.html
// Read about Collections.
// Focus on the Map interface and HashMap implementation.
// Also:
//   https://www.tutorialspoint.com/java/java_map_interface.htm
//   http://www.javatpoint.com/java-map
// and elsewhere.
import java.util.*;
public class Environment {
    
 
    Map<String, Double> vas = new HashMap<String, Double>();
    public double put(String var, double val) // changed int val to double val
    {
        vas.put(var,val);
        return val;
    }
    public double get(int pos, String var) throws EvalException 
    {
        
        return vas.get(var);
        
    }

}
