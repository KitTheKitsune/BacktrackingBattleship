import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Analyzer {
	int TABLE_SIZE;
	String fullProblem;
	int boatsSingle;
	int boatsDouble;
	int boatsTriple;
	int boatsQuad;
	int[] vertLimits;
	int[] horizLimits;
	String[][] field;
	String allSolutions = "";
	
	public Analyzer() {
		this.fullProblem = readInProblem("src/p40Extreme.txt");
		breakDown();
		solve();
	}
	
	public Analyzer(String file, boolean t) {
		this.fullProblem = file;
		breakDown();
		solve();
	}
	
	
	public Analyzer(String filename) {
		this.fullProblem = readInProblem(filename);
		breakDown();
		solve();
	}
	
	//Reads in file as a single string
	private String readInProblem(String filename) {
		try {
			return Files.readString(Path.of(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	//Breaks Down a file string into its usable elements
	private void breakDown() {
		//Split the string into an array
		String[] arr = this.fullProblem.split("\n");
		
		//The first 5 lines tell us about the boats and table
		this.TABLE_SIZE = Integer.parseInt(arr[0]);
		
		this.vertLimits = new int[TABLE_SIZE];
		this.horizLimits = new int[TABLE_SIZE];
		this.field = new String[TABLE_SIZE][TABLE_SIZE];
		
		
		this.boatsQuad = Integer.parseInt(arr[1]);
		this.boatsTriple = Integer.parseInt(arr[2]);
		this.boatsDouble = Integer.parseInt(arr[3]);
		this.boatsSingle = Integer.parseInt(arr[4]);
		
		//The next line is all of our vertical limitations
		String[] currLine = arr[5].split(" ");
		for(int i = 0; i < currLine.length ; i++) {
			this.vertLimits[i] = Integer.parseInt(currLine[i]);
		}
		
		//The following lines follow the pattern of:
		//horizontal limitations followed by the puzzle line
		int lineNum = 0;
		for (int i = 6; i < arr.length; i++) {
			currLine = arr[i].split("");
			
			//grab first item and place in horizontal limitations
			horizLimits[lineNum] = Integer.parseInt(currLine[0]);
			
			//grab each following item in currLine and place in field
			for(int x = 1; x < currLine.length; x++) {
				this.field[lineNum][x-1] = currLine[x];
			}
			lineNum++;
		}
	}
	
	//Uses a backtracking method to fill in the blanks in field
	public boolean solve() {
		if(solve(0,0)) {
			return true;
		}else {
			if(this.allSolutions.equals(" ")) {
				System.out.println("There is no valid solution!");
				return false;
			}else {
				return true;
			}
		}
	}
	
	//Helper
	private boolean solve(int row, int col) {
		String[] options = new String[2];
		options[0] = "B";
		options[1] = "~";
		
		
		if(row >= TABLE_SIZE) {
			return solve(0,col+1);
		}
		if(col >= TABLE_SIZE) {
			if(isValidConfiguration()) {
				this.allSolutions = this.allSolutions + toString() + "\n" + ";" + "\n";
				return false;
			}else {
				//System.out.println("bad configuration");
				return false;
			}
		}
		if(!this.field[row][col].equals(" ")) {
			return solve(row+1,col);
		}else {
			for(String i : options) {
				//if not invalid
				//if(isValidShip(row, col)) {
					this.field[row][col] = i;
				//}
				
				if(solve(row+1,col)) {
					return true;
				}
				
				this.field[row][col] = " ";
			}
		}
		//System.out.println("pruned.");
		return false;
	}
	
	
	//determines if this is a valid final configuration
	private boolean isValidConfiguration() {
		for(int i = 0; i < TABLE_SIZE; i++) {
			if(this.horizLimits[i] != countOf(this.field[i])) {
				return false;
			}
			if(this.vertLimits[i] != countOf(i)) {
				return false;
			}
			//if boat size counts are wrong return false
			//is not implemented
		}
		
		return true;
	}

	//determines if the spot would violate any limitations
	private boolean isValidShip(int row, int col) {
		if(this.horizLimits[row] <= countOf(this.field[row])) {
			return false;
		}
		if(this.vertLimits[col] <= countOf(col)) {
			return false;
		}
		if((row > 0 && col > 0 && this.field[row-1][col-1].equals("B")) ||
				(row > 0 && col < TABLE_SIZE-1 && this.field[row-1][col+1].equals("B")) ||
				(row < TABLE_SIZE-1 && col > 0 && this.field[row+1][col-1].equals("B")) ||
				(row < TABLE_SIZE-1 && col < TABLE_SIZE-1 && this.field[row+1][col+1].equals("B"))) {
			return false;
		}
		
		return true;
	}
	
	//counts the number of ships in an array
	private int countOf(String[] arr) {
		int count = 0;
		for(int i = 0; i < TABLE_SIZE; i++) {
			if (arr[i].equals("B")) {
				count++;
			}
		}
		return count;
	}
	
	//counts the number of ships in a column
	private int countOf(int col) {
		int count = 0;
		for(int i = 0; i < TABLE_SIZE; i++) {
			if(this.field[i][col] == "B") {
				count++;
			}
		}
		return count;
	}
	
	//determines if the grid is full
	private boolean isComplete(String[][] field) {
		for(int i = 0; i < TABLE_SIZE; i++) {
			for(int j = 0; j < TABLE_SIZE; j++) {
				if(field[i][j].equals(" ")) {
					return false;
				}
			}
		}
		return true;
	}
	
	//gets all solutions
	public String getAllSolution() {
		return this.allSolutions;
	}
	
	public String getFirstSolution() {
		String[] arr = this.allSolutions.split(";");
		return arr[0];
	}
	
	
	//returns the game as a string
	public String toString() {
		String out = "  ";
		for(int i = 0; i < TABLE_SIZE; i++) {
			out = out + this.vertLimits[i];
		}
		out = out + "\n  ";
		
		for(int i = 0; i < TABLE_SIZE; i++) {
			out = out + "-";
		}
		
		out = out + "\n";
		
		for(int lineNum = 0; lineNum < TABLE_SIZE; lineNum++) {
			out = out + this.horizLimits[lineNum] + "|";
			
			for(int i = 0; i<TABLE_SIZE; i++) {
				out = out + this.field[lineNum][i];
			}
			
			out = out + "|" + "\n";
			
		}
		
		return out;
	}
}
