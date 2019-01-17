

public class Main {

	public static void main(String[] args) {
		try {
			String inputfile = args[1];
			String outputfile = args[2];
			int k = Integer.parseInt(args[0]);
			System.out.println("K = " + k);
			KMeansAlgo km = new KMeansAlgo();
			km.getData(inputfile);
			km.generateRandomCenters(k);
			km.applyKMeans();
			float SSEValue = km.calculateSSE();
			km.writeIntoOutputFile(outputfile, SSEValue);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
