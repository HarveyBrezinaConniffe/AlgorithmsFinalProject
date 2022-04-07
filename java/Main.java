import java.util.Scanner;

public class Main {
	static Scanner inputScanner;

	public static void main(String[] args) {
		inputScanner = new Scanner(System.in);
		mainMenu();
	}

	public static void mainMenu() {
		System.out.println("Welcome to my algorithms final project!");
		System.out.println("What would you like to do?");
		System.out.println("	1. Find the shortest path between two bus stops.");
		System.out.println("	2. Search for bus stops by name.");
		System.out.println("	3. Search for trips by arrival time.");
		System.out.print(">> ");
		
		int choice = -1;
		while(choice == -1) {
			if(inputScanner.hasNextInt()) {
				int nextInt = inputScanner.nextInt();
				if(nextInt >= 1 && nextInt <= 3) {
					choice = nextInt;
				}
				else {
					System.out.println("Sorry! I don't understand that input, Please enter a number between 1 and 3!");
					System.out.print(">> ");
				}
			}	
			else if(inputScanner.hasNext()) {
				inputScanner.next();
				System.out.println("Sorry! I don't understand that input, Please enter a number between 1 and 3!");
				System.out.print(">> ");
			}
		}

		System.out.println(choice);
	}
}
