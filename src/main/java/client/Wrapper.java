package client;

import java.io.IOException;

public class Wrapper {

	public Wrapper() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		ReasoningClient
				.main(new String[] {
						"src/main/resources/ReverbFilter.csv",
						"src/main/resources/Dates",
						"15", ";" });

		ReasoningClient
				.main(new String[] {
						"src/main/resources/ReverbFilter.csv",
						"src/main/resources/Dates",
						"5", ";" });

		ReasoningClient
				.main(new String[] {
						"src/main/resources/ReverbFilter.csv",
						"src/main/resources/Dates",
						"10", ";" });
	}

}
