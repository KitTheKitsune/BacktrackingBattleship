
public class Battleship {

	public static void main(String[] args) {
		String simple = "3\n0\n0\n0\n3\n0 0 2\n0~~~\n1   \n1   ";
		String difficult = "5\n0\n0\n0\n0\n0 1 0 0 4\n1     \n1     \n1     \n1     \n1     ";
		//Analyzer analyzer = new Analyzer(simple, true);
		//Analyzer analyzer = new Analyzer(difficult, true);
		Analyzer analyzer = new Analyzer();
		System.out.println(analyzer.getAllSolution());
	}

}
