package hungarian;

import java.util.ArrayList;

import util.MyString;
import util.TextFile;


 
public class Hungarian {

	
	
	/**
	 * An implimentation of the O(n^3) Hungarian method for the minimum cost assignment problem
	 * (maximum value matching can be computed by subtracting each value from the minimum value).
	 *
	 * It is assumed that the matrix is SQUARE. Code to ensure this could be easily added to the constructor.
	 *
	 * new Hungarian(costMatrix).execute() returns a 2d array,
	 * with result[i][0] being the row index assigned to the result[i][1] column index (for assignment i).
	 *
	 * This method uses O(n^3) time (or at least, it should) and O(n^2) memory; it is
	 * probably possible to reduce both computation and memory usage by constant factors using a few more tricks.
	 *
	 * I, Shawn O'Neil, hereby release this code into the public domain. Copy it, change it, take credit for it
	 * if you want.
	 */
   private int numRows;
   private int numCols;

   private boolean[][] primes;
   private boolean[][] stars;
   private boolean[] rowsCovered;
   private boolean[] colsCovered;
   private float[][] costsMatrix;
   private float[][] originalCostsMatrix;
  
   public float[][] getOriginalCostsMatrix() {
	return originalCostsMatrix;
}

private int[][] assignment;
   
   public int[][] getAssignment() {
	return assignment;
}




public float getOptimalValue() {
	return optimalValue;
}

private float optimalValue;
   


   public Hungarian(float theCosts[][], boolean minimization) {
      costsMatrix = theCosts;
      
 
      
      this.originalCostsMatrix = new float[costsMatrix.length][costsMatrix.length];
      for(int i = 0; i < costsMatrix.length; i++){
    	  
    	  for(int j = 0; j < costsMatrix.length; j++){
        	  
        	 this.originalCostsMatrix[i][j] = this.costsMatrix[i][j];
        	 
        	// System.out.println(this.costsMatrix[i][j]);
        	  
          }
    	  
      }
      
      
      
      /**
       * Si on veut faire une minimisation
       */
      if(minimization){}
      else{ /**
           * Si on veut faire une minimisation
           */
    	  this.modifyMatrixForMaximization();
    
    	  
      }
      
      //System.out.println("----->>>"+costsMatrix.length);
      
      numRows = costsMatrix.length;
      
      
      
      numCols = costsMatrix[0].length;

      primes = new boolean[numRows][numCols];
      stars = new boolean[numRows][numCols];

      // Initialize arrays telling us which rows/cols are covered,
      // and matrices giving us the primes and stars.
      rowsCovered = new boolean[numRows];
      colsCovered = new boolean[numCols];
      for(int i = 0; i < numRows; i++) {
         rowsCovered[i] = false;
      }
      for(int j = 0; j < numCols; j++) {
         colsCovered[j] = false;
      }
      for(int i = 0; i < numRows; i++) {
         for(int j = 0; j < numCols; j++) {
            primes[i][j] = false;
            stars[i][j] = false;
         }
      }
   }




   public void execute() {
      subtractRowColMins();

      this.findStars(); // O(n^2)
      this.resetCovered(); // O(n);
      this.coverStarredZeroCols(); // O(n^2)

      while(!allColsCovered()) { 
         int[] primedLocation = this.primeUncoveredZero(); // O(n^2)

         // It's possible that we couldn't find a zero to prime, so we have to induce some zeros so we can find one to prime
         if(primedLocation[0] == -1) {  
            this.minUncoveredRowsCols(); // O(n^2)
            primedLocation = this.primeUncoveredZero(); // O(n^2)
         }

         // is there a starred 0 in the primed zeros row?
         int primedRow = primedLocation[0];
         int starCol = this.findStarColInRow(primedRow);
         if(starCol != -1) {
            // cover ther row of the primedLocation and uncover the star column
            rowsCovered[primedRow] = true;
            colsCovered[starCol] = false;
         }
         else { // otherwise we need to find an augmenting path and start over.
            this.augmentPathStartingAtPrime(primedLocation);
            this.resetCovered();
            this.resetPrimes();
            this.coverStarredZeroCols();
         }
      }

      this.assignment = this.starsToAssignments(); // O(n^2)

   }

   /*
    * the starred 0's in each column are the assignments.
    * O(n^2)
    */
   public int[][] starsToAssignments() {
      int[][] toRet = new int[numCols][];
      for(int j = 0; j < numCols; j++) {
         toRet[j] = new int[]{this.findStarRowInCol(j), j}; // O(n)
      }
      return toRet;
   }

   /*
    * resets prime information
    */
   public void resetPrimes() {
      for(int i = 0; i < numRows; i++) {
         for(int j = 0; j < numCols; j++) {
            primes[i][j] = false;
         }
      }
   }


   /*
    * resets covered information, O(n)
    */
   public void resetCovered() {
      for(int i = 0; i < numRows; i++) {
         rowsCovered[i] = false;
      }
      for(int j = 0; j < numCols; j++) {
         colsCovered[j] = false;
      }
   }

   /*
    * get the first zero in each column, star it if there isn't already a star in that row
    * cover the row and column of the star made, and continue to the next column
    * O(n^2)
    */
   public void findStars() {
      boolean[] rowStars = new boolean[numRows];
      boolean[] colStars = new boolean[numCols];

      for(int i = 0; i < numRows; i++) {
         rowStars[i] = false;
      }
      for(int j = 0; j < numCols; j++) {
         colStars[j] = false;
      }

      for(int j = 0; j < numCols; j++) {
         for(int i = 0; i < numRows; i++) {
            if(costsMatrix[i][j] == 0 && !rowStars[i] && !colStars[j]) {
               stars[i][j] = true;
               rowStars[i] = true;
               colStars[j] = true;
               break;
            }
         }
      }
   }



   /*
    * Finds the minimum uncovered value, and adds it to all the covered rows then
    * subtracts it from all the uncovered columns. This results in a cost matrix with
    * at least one more zero.
    */
   private void minUncoveredRowsCols() { 
      // find min uncovered value
      float minUncovered = Float.MAX_VALUE;
      for(int i = 0; i < numRows; i++) {
         if(!rowsCovered[i]) {
            for(int j = 0; j < numCols; j++) {
               if(!colsCovered[j]) {
                  if(costsMatrix[i][j] < minUncovered) {
                     minUncovered = costsMatrix[i][j];
                  }
               }
            }
         }
      }

      // add that value to all the COVERED rows.
      for(int i = 0; i < numRows; i++) {
         if(rowsCovered[i]) {
            for(int j = 0; j < numCols; j++) {
               costsMatrix[i][j] = costsMatrix[i][j] + minUncovered;

            }
         }
      }

      // subtract that value from all the UNcovered columns
      for(int j = 0; j < numCols; j++) {
         if(!colsCovered[j]) {
            for(int i = 0; i < numRows; i++) {
               costsMatrix[i][j] = costsMatrix[i][j] - minUncovered;
            }
         }
      }
   }

   /*
    * Finds an uncovered zero, primes it, and returns an array
    * describing the row and column of the newly primed zero.
    * If no uncovered zero could be found, returns -1 in the indices.
    * O(n^2)
    */
   private int[] primeUncoveredZero() { 
      int[] location = new int[2];

      for(int i = 0; i < numRows; i++) {
         if(!rowsCovered[i]) {
            for(int j = 0; j < numCols; j++) {
               if(!colsCovered[j]) {
                  if(costsMatrix[i][j] == 0) {
                     primes[i][j] = true;
                     location[0] = i;
                     location[1] = j;
                     return location;
                  }
               }
            }
         }
      }

      location[0] = -1;
      location[1] = -1;
      return location;
   }

   /*
    * Starting at a given primed location[0=row,1=col], we find an augmenting path
    * consisting of a primed , starred , primed , ..., primed. (note that it begins and ends with a prime)
    * We do this by starting at the location, going to a starred zero in the same column, then going to a primed zero in
    * the same row, etc, until we get to a prime with no star in the column.
    * O(n^2)
    */
   private void augmentPathStartingAtPrime(int[] location) {
      // Make the arraylists sufficiently large to begin with
      ArrayList<int[]> primeLocations = new ArrayList<int[]>(numRows+numCols);
      ArrayList<int[]> starLocations = new ArrayList<int[]>(numRows+numCols);
      primeLocations.add(location);

      int currentRow = location[0];
      int currentCol = location[1];
      while(true) { // add stars and primes in pairs
         int starRow = findStarRowInCol(currentCol);
         // at some point we won't be able to find a star. if this is the case, break.
         if(starRow == -1) {break;}
         int[] starLocation = new int[]{starRow, currentCol};
         starLocations.add(starLocation);
         currentRow = starRow;

         int primeCol = findPrimeColInRow(currentRow);
         int[] primeLocation = new int[]{currentRow, primeCol};
         primeLocations.add(primeLocation);
         currentCol = primeCol;
      }

      unStarLocations(starLocations);
      starLocations(primeLocations);
   }


   /*
    * Given an arraylist of  locations, star them
    */
   private void starLocations(ArrayList<int[]> locations) {
      for(int k = 0; k < locations.size(); k++) {
         int[] location = locations.get(k);
         int row = location[0];
         int col = location[1];
         stars[row][col] = true;
      }
   }

   /*
    * Given an arraylist of starred locations, unstar them
    */
   private void unStarLocations(ArrayList<int[]> starLocations) {
      for(int k = 0; k < starLocations.size(); k++) {
         int[] starLocation = starLocations.get(k);
         int row = starLocation[0];
         int col = starLocation[1];
         stars[row][col] = false;
      }
   }


   /*
    * Given a row index, finds a column with a prime. returns -1 if this isn't possible.
    */
   private int findPrimeColInRow(int theRow) {
      for(int j = 0; j < numCols; j++) {
         if(primes[theRow][j]) {
            return j;
         }
      }
      return -1;
   }




   /*
    * Given a column index, finds a row with a star. returns -1 if this isn't possible.
    */
   public int findStarRowInCol(int theCol) {
      for(int i = 0; i < numRows; i++) {
         if(stars[i][theCol]) {
            return i;
         }
      }
      return -1;
   }


   public int findStarColInRow(int theRow) {
      for(int j = 0; j < numCols; j++) {
         if(stars[theRow][j]) {
            return j;
         }
      }
      return -1;
   }

   // looks at the colsCovered array, and returns true if all entries are true, false otherwise
   private boolean allColsCovered() {
      for(int j = 0; j < numCols; j++) {
         if(!colsCovered[j]) {
            return false;
         }
      }
      return true;
   }

   /*
    * sets the columns covered if they contain starred zeros
    * O(n^2)
    */
   private void coverStarredZeroCols() {
      for(int j = 0; j < numCols; j++) {
         colsCovered[j] = false;
         for(int i = 0; i < numRows; i++) {
            if(stars[i][j]) {
               colsCovered[j] = true;
               break; // break inner loop to save a bit of time
            }
         }
      }
   }

   private void subtractRowColMins() { 
      for(int i = 0; i < numRows; i++) {//for each row
         float rowMin = Float.MAX_VALUE;
         for(int j = 0; j < numCols; j++) { // grab the smallest element in that row
            if(costsMatrix[i][j] < rowMin) {
               rowMin = costsMatrix[i][j];
            }
         }
         for(int j = 0; j < numCols; j++) { // subtract that from each element
            costsMatrix[i][j] = costsMatrix[i][j] - rowMin;
         }
      }

      for(int j = 0; j < numCols; j++) { // for each col
         float colMin = Float.MAX_VALUE;
         for(int i = 0; i < numRows; i++) { // grab the smallest element in that column
            if(costsMatrix[i][j] < colMin) {
               colMin = costsMatrix[i][j];
            }
         }
         for(int i = 0; i < numRows; i++) { // subtract that from each element
            costsMatrix[i][j] = costsMatrix[i][j] - colMin;
         }
      }
   }

   public void calculateOptimalValue(){
	  
	   float res = 0;
	   for(int i = 0; i < this.assignment.length; i++){
		   res = res + this.originalCostsMatrix[this.assignment[i][0]][this.assignment[i][1]];
		   //System.out.print("   "+this.originalCostsMatrix[this.assignment[i][0]][this.assignment[i][1]]); 
	   }
	   this.optimalValue = res;
	  
   }
   
   public void disp(float[][] matrix){
		java.text.DecimalFormat df = new java.text.DecimalFormat("0.##");
		  for(int i = 0; i<matrix.length; i++){
			  for(int j = 0; j<matrix.length; j++){
				  

					  //System.out.print(matrix[i][j]+"  ");
					  double nombre = matrix[i][j];
						System.out.print(df.format(nombre)+"  ");
						


				  
			  }
			  System.out.println();
			  
			  
			  
		  }
	  }
   
  /**
   * Modifier la matrice des couts pour pouvpor faire une maximisation
   * Choisir le plus grand nombre et trouver l’écart de tous les
	*éléments par rapport à ce nombre; i.e. on trouve la matrice
	*des pénalités.
   * @return
   */
   
  public void modifyMatrixForMaximization(){

	
	//Calculer le max
	float max = -1;
	
	for(int i = 0; i < this.costsMatrix.length; i++){
		
		for(int j = 0; j < costsMatrix.length; j++){
			
			if(max == -1){
				max = costsMatrix[i][j];
			}else{
				if(max<costsMatrix[i][j]){
					max = costsMatrix[i][j];
				}
			}
			
		}
	}
	
	
	for(int i = 0; i < costsMatrix.length; i++){
		
		for(int j = 0; j < costsMatrix.length; j++){
			costsMatrix[i][j] = max - costsMatrix[i][j];
		}
	  
	}
	
  }
   
   
   public static float[][] getMatrixFromFile(String path){
		
	   int tailleDeLaMatrice;
	   		
	   		
	   		ArrayList<String> lines = TextFile.getLines(path);
	   		tailleDeLaMatrice =  lines.size();
	   	
	   		
	   		float[][] costsMatrix = new float[tailleDeLaMatrice][tailleDeLaMatrice];
	   		
	   		for(int i = 0; i < lines.size(); i++){
	   			String line = lines.get(i);
	   			ArrayList<String> words = MyString.getWords(line,' ');
	   			System.out.println();
	   			for(int j = 0; j < words.size(); j++){
	   				
	   				String word = words.get(j);
	   				//System.out.print(word+"    ");

	   				costsMatrix[i][j] = Float.valueOf(word);
	   				
	   			}
	   		}
return costsMatrix;
	   		
	   	}
   
   
   public static float[][] getRandomMatrix(int n){
	   
	   float[][] matrix = new float[n][n];
	   
	   for(int i = 0; i < matrix.length; i++){
		   for(int j = 0; j < matrix.length; j++){
			   
			   float x = (float)util.MyMath.getRandomNumber(1, 100);
			   matrix[i][j] = x;
			   
		   }
	   }

		
		
		
		return matrix;
	   
   }
   
   
	public static void main(String[] args) {
		
		
		float[][] matrix = {{0.65f, 0, 0.75f, 0}, 
							{0, 0, 0.55f, 0.65f}, 
							{0, 0.95f, 0, 0}, 
							
							{0, 0, 0, 0}};
	
		
		
		
		/*
		String path = "files/matrices/m0.txt";
		float[][] matrix = Hungarian.getMatrixFromFile(path);
		*/
		
		
	/*	System.out.println("Obtenir une matric Aléatoire");
		float[][] matrix =  getRandomMatrix(20);
*/
		
		

		
		
		Hungarian h  = new Hungarian(matrix, false);
		h.execute();
		int[][] result = h.getAssignment();
		
		//h.disp(h.originalCostsMatrix);
		
		//System.out.println(result.length);

		
		for(int i = 0; i < result.length; i++){
			int n = result[i].length;
			for(int j = 0; j < n; j++){
				
				System.out.print(result[i][j]+"      ");
			}
			System.out.println();
		}

		
		System.out.println();
		System.out.println();
		h.calculateOptimalValue();
		System.out.println(h.getOptimalValue());
		
		
		System.out.println(" ------------------ MAXIMIZATIO--------------------------");
		
		//float[][] matrix2 = Hungarian.getMatrixFromFile(path);
		System.out.println("Obtenir une matric Aléatoire");
		float[][] matrix2 =  getRandomMatrix(10);
		
		
		//h.disp(matrix2);
		System.out.println();
		
		//h.disp(matrix2);
		
		
		Hungarian h2  = new Hungarian(matrix2, false);
		//h2.disp(matrix2);
		h2.execute();
		h2.calculateOptimalValue();

		System.out.println(" MAX = : "+h2.getOptimalValue());
		
		
		
		
		
		
		
		
		
	}
   
   
}