import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * TODO Put here a description of what this class does.
 * 
 * @author tim. Created Sep 6, 2012.
 */
public class testrunner {
	public static void main(String[] args) {
		int games = 0;
		String opponent = "";
		try {
			games = Integer.parseInt(args[0]);
		} catch (Exception e) {
			System.out.println("invalid use of the tester!");
			System.out.println("testrunner <number of games> <opponent>");
		}

		int port = 5032;

		System.out.print("|");
		for (int i = 0; i < games; i++)
			System.out.print(" ");
		System.out.println("|");
		System.out.print(" ");

		PrintStream originalOut = System.out; // To get it back later
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream newOut = new PrintStream(baos);
		System.setOut(newOut);

		Client c;
		String tmp;
		int wins = 0;

		for (int i = 1; i <= games; i++) {
			c = new Client("dd2380.csc.kth.se",port, ""+i);
			System.setOut(originalOut);
			tmp = baos.toString();
			if (tmp.contains("CORRECT")) {
				System.out.print("x");
				wins++;
			} else
				System.out.print("-");

			baos.reset();
			System.setOut(newOut);
		}

		System.out.flush();

		System.setOut(originalOut); // So you can print again
		System.out.println("\nPlaying against: " + opponent);
		System.out.println("Number of wins: " + wins + " of " + games + " games");

	}

}
