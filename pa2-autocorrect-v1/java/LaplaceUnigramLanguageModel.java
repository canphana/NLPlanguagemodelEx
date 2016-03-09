import java.util.HashMap;
import java.util.List;

public class LaplaceUnigramLanguageModel implements LanguageModel {

	int total =0;
	HashMap<String,Integer> unigramCounts = new HashMap<String,Integer>();
  /** Initialize your data structures in the constructor. */
  public LaplaceUnigramLanguageModel(HolbrookCorpus corpus) {
    train(corpus);
  }

  /** Takes a corpus and trains your language model. 
    * Compute any counts or other corpus statistics in this function.
    */
  public void train(HolbrookCorpus corpus) {
    // TODO: your code here
	  
	  for(Sentence sentence :corpus.data)
	  {
		 
		  for(Datum datum : sentence.data){
			  String token = datum.word;
			  if(unigramCounts.get(token)==null){
			  unigramCounts.put(token,1);
			  }
			  else{
				  unigramCounts.put(token,unigramCounts.get(token)+1);
			  }
			  total +=1;
		  }
	  }
  }

  /** Takes a list of strings as argument and returns the log-probability of the 
    * sentence using your language model. Use whatever data you computed in train() here.
    */
  public double score(List<String> sentence) {
    // TODO: your code here
	  double score = 0;
	  for( String token: sentence){	  
	  int count = 0 ;
	  if(unigramCounts.get(token) == null)
	  {
		  count =0;
	  }
	  else{
		  count = unigramCounts.get(token);
	  }
	  score += Math.log(count + 1);
	  score -= Math.log(total + unigramCounts.size());
	  }
    return score;
  }
}
