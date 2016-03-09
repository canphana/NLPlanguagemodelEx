
import java.util.HashMap;
import java.util.List;
 //Good Turing
public class CustomLanguageModel implements LanguageModel {
	 		HashMap<String,Integer>	counts = new HashMap<String, Integer>(); 
	 		HashMap<Integer,Integer>freq = new HashMap<Integer, Integer>();
			int total = 0;
			double a= 0.0;
			double b= 0.0;
			double N0 = 0.0; 
			int max_count =5;
  /** Initialize your data structures in the constructor. */
  public CustomLanguageModel(HolbrookCorpus corpus) {
    train(corpus);
  }

  /** Takes a corpus and trains your language model. 
    * Compute any counts or other corpus statistics in this function.
    */
  public void train(HolbrookCorpus corpus) { 
    // TODO: your code here
	  for (Sentence sentence : corpus.data)
	  {
		  for (Datum datum : sentence.data)
		  {
			  String token = datum.word;
			  if(counts.get(token)!= null)
			  {
				  counts.put(token,counts.get(token)+1);
			  }	
			  else
			  {
				  counts.put(token,1);
			  }
			  total +=1;
		  }
	  }
	 
	  for (String bigram : counts.keySet())
	  { int count = 0;
		  
		  if(counts.get(bigram)!=null)
		  {
			  count = counts.get(bigram);
		  }
		  else 
		  { 
			  count =0;
		}
		  if(freq.get(count)!= null)
		  {
			  freq.put(count,freq.get(count)+1);
		  }	
		  else
		  {
			  freq.put(count,1);
		  }
		  this.regression();
		  N0 = Math.pow(counts.size(),2)-total;	  
	  }
  }

  public void regression()
  {
	double  s1=0.0;
	for (int c : freq.keySet())
	{
		s1 += Math.log(c)+Math.log(freq.get(c));
	}
	double ax = 0.0;
	double  ay= 0.0;
	for (int c : freq.keySet())
	{
		ax += Math.log(c);
		ay+= Math.log(freq.get(c));
	}
	double s2 = ax *ay / (freq.size());
	double numerator = s1 - s2;
	
	s1 = 0;
	for (int c : freq.keySet())
	{
		  s1 += Math.pow(Math.log(c),2);
		  s2 +=Math.log(c);
	}
	 s2 = Math.pow(s2, 2) / freq.size();
	 double denominator = s1 - s2;
	 b = numerator / denominator;
	 a= (ay - b*ax)/freq.size();
  }
  
  public double get_smoothed_N(double c)
  {
	 double N = Math.exp(this.a + this.b*Math.log(c));
	 return N;
  }
  /** Takes a list of strings as argument and returns the log-probability of the 
    * sentence using your language model. Use whatever data you computed in train() here.
    */
  public double score(List<String> sentence) {
    // TODO: your code here
	  double score = 0.0;
	  double N2 = 0.0;
	  double N1 = 0.0;
	  int count = 0;
	  int i=1;
	  for(String token : sentence)
	  {
		  if(counts.get(token)!= null)
		  {
			  count = counts.get(token);
		  }
		  else
		  {
			  count =0;
		  }
		if(count ==0)
		  {
				score += Math.log(1.0 * freq.get(1));
				score -= Math.log(total);
			    score -= Math.log(N0);
			    i += 1;
			    continue;
		  }
		if(count < max_count)
		{
			N2 = 1.0 * freq.get(count+1);
			N1 = 1.0 * freq.get(count);
		}
		else
		{
			N2 = get_smoothed_N(count+1);
			N1 = get_smoothed_N(count);
		}
				double c_star = (count+1) * N2 / N1;
			      score += Math.log(c_star);
			      score -= Math.log(total);
	  }
    return score;
  }
  
}
