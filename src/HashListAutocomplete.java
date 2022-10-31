import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashListAutocomplete implements Autocompletor {
  private static final int MAX_PREFIX = 10;
  private Map<String, List<Term>> myMap;
  private int mySize;

  public HashListAutocomplete(String[] terms, double[] weights) {
    if (terms == null || weights == null) {
			throw new NullPointerException("One or more arguments null");
		}
    if (terms.length != weights.length) {
			throw new IllegalArgumentException("terms and weights are not the same length");
		}
		
		initialize(terms,weights);
  }

  @Override
  public List<Term> topMatches(String prefix, int k) {
    if (prefix.length() > MAX_PREFIX)
      prefix = prefix.substring(0, MAX_PREFIX);

    if(!myMap.containsKey(prefix))
      return new ArrayList<>();
      
    List<Term> all = myMap.get(prefix);
    List<Term> list = all.subList(0, Math.min(k, all.size()));

    return list;
  }

  @Override
  public void initialize(String[] terms, double[] weights) {
    mySize = 0;
    myMap = new HashMap<>();

    for (int i = 0; i < terms.length; i++) {
      String t = terms[i];
      double w = weights[i];
      int upperBound = (MAX_PREFIX) > t.length() ? t.length() : (MAX_PREFIX);
      Term term = new Term(t, w);

      // Update size with term info
      mySize += BYTES_PER_DOUBLE
                + BYTES_PER_CHAR * t.length();

      for (int j = 0; j < upperBound+1; j++) {
        String key = t.substring(0, j);
        if(!myMap.containsKey(key)) // Avoid adding duplicate keys
          mySize += BYTES_PER_CHAR * key.length(); // Update size: add key
        
        myMap.putIfAbsent(key, new ArrayList<>());
        myMap.get(key).add(term);
      }
    }

    // Sort by weight
    for (String k : myMap.keySet())
      Collections.sort(myMap.get(k), Comparator.comparing(Term::getWeight).reversed());
  }

  @Override
  public int sizeInBytes() {
    return mySize;
  }  
}

