package matcher.words.linguistic;

//https://code.google.com/archive/p/ws4j/
 
	import edu.cmu.lti.lexical_db.ILexicalDatabase;
	import edu.cmu.lti.lexical_db.NictWordNet;
	/*import edu.cmu.lti.ws4j.RelatednessCalculator;
	import edu.cmu.lti.ws4j.impl.HirstStOnge;
	import edu.cmu.lti.ws4j.impl.JiangConrath;
	import edu.cmu.lti.ws4j.impl.LeacockChodorow;
	import edu.cmu.lti.ws4j.impl.Lesk;
	import edu.cmu.lti.ws4j.impl.Lin;
	import edu.cmu.lti.ws4j.impl.Path;
	import edu.cmu.lti.ws4j.impl.Resnik;*/
	import edu.cmu.lti.ws4j.impl.WuPalmer;
	import edu.cmu.lti.ws4j.util.WS4JConfiguration;
	 
	/**
	 * The linguistic matching 
	 * @author Boubakir
	 *
	 */
	public class LinguisticMatching  {
	 
		private static ILexicalDatabase db = new NictWordNet();
		
		/**
		 * returns the similarity matching between two strings
		 * @param word1: first string
		 * @param word2: second string
		 * @return
		 */
		public static double compute(String word1, String word2) {
			WS4JConfiguration.getInstance().setMFS(true);
			double s = -1;
			if(word1.equalsIgnoreCase(word2)){
				s = 1;
			}else{
				s = new WuPalmer(db).calcRelatednessOfWords(word1, word2);
			}
			return s;
		
		
		
		}
	 
		public static void main(String[] args) {
			String[] words = {"name", "first_name", "last_name" };
			for(int i=0; i<words.length-1; i++){
				for(int j=i+1; j<words.length; j++){
					double distance = compute(words[i], words[j]);
					System.out.println(words[i] +" -  " +  words[j] + " = " + distance);
				}
			}
		}
	

}
