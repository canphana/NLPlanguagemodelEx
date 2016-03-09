import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LaplaceBigramLanguageModel implements LanguageModel {
  
	HashMap<String,Integer> unigramCounts = new HashMap<String,Integer>();
	HashMap<String,Integer> bigramCounts = new HashMap<String,Integer>();
  /** Initialize your data structures in the constructor. */
  public LaplaceBigramLanguageModel(HolbrookCorpus corpus) {
    train(corpus); 
  }
  
  /** Takes a corpus and trains your language model. 
    * Compute any counts or other corpus statistics in this function.
    */
  public void train(HolbrookCorpus corpus) { 
    // TODO: your code here
	  for(Sentence sentence :corpus.data){
		  ArrayList<Datum> datum = sentence.data;
		  for(int i=0; i< datum.size();i++)
		  {
			  String token_i = datum.get(i).word;
			  if(unigramCounts.get(token_i)==null){
				  unigramCounts.put(token_i, 1);
			  }
			  else{
				  unigramCounts.put(token_i,unigramCounts.get(token_i)+1);
			  }
			  
			  if(i>0)
			  {
				  String token_i_1 = datum.get(i-1).word;
				  String bi_key = token_i_1 + "," + token_i;
				  if(bigramCounts.get(bi_key)==null){
					  bigramCounts.put(bi_key, 1);
				  }
				  else{
					  bigramCounts.put(bi_key,bigramCounts.get(bi_key)+1);
				  }
			  }
		  }
		  
	  }
  }


  /** Takes a list of strings as argument and returns the log-probability of the 
    * sentence using your language model. Use whatever data you computed in train() here.
    */
  public double score(List<String> sentence) {
    // TODO: your code here
	  double score = 0;
	  int count_bigram = 0;
	  int count_unigram = 0;
	  for(int i=0; i<sentence.size();i++)
	  {
		  if(i>0)
		  {
			  String bi_key = sentence.get(i-1)+","+sentence.get(i);
			  if(bigramCounts.get(bi_key)!=null)
			  {
				  count_bigram = bigramCounts.get(bi_key);
			  }
			  else
			  {
				  count_bigram = 0;
			  }
			  
			  if(unigramCounts.get(sentence.get(i-1))!=null)
			  {
				  count_unigram = unigramCounts.get(sentence.get(i-1));
			  }
			  else
			  {
				  count_unigram = 0;
			  }
			  score += Math.log(count_bigram + 1);
			  score -= Math.log(count_unigram + bigramCounts.size());
		  }
	  }
    return score;
  }
}
