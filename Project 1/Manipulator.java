import java.io.IOException;
import java.util.Scanner;

public class Manipulator {

	static boolean quit = false;
	static boolean save = true; 

	public static void main(String[] args) throws IOException {
		@SuppressWarnings("resource")
		Scanner kb = new Scanner(System.in); //scanner to read in what the user types
		System.out.print("What image file would you like to edit: "); //ask what file to edit

		String filename = kb.next(); //what is entered after the print line above should be a recognized filename

		Bitmap picture = new Bitmap(); //create a picture object of the class Bitmap to be able to manipulate picture

		picture.read(filename); //read method reads the file that was typed to see if it is valid file
		
		if(save)
		{
			System.out.println("How many threads would you like to use: "); //asks the user how many threads the want to use
			int sorryDrWittman = kb.nextInt();			//stores their response
		}

		while(!quit) { //if quit does not equal true that the print line below keeps asking for a command
			System.out.print("What command would you like to perform (i, g, b, h, s, d, r, or q) :"); //asks for a command to edit image
			String action = kb.next(); //reads what command wants to be done

			switch(action){ 
			case "i": //if i is chosen it performs the invert manipulation
				double time1 = System.nanoTime(); //variable that stores the start time

				picture.invert();

				System.out.println("Command took " + String.format("%.3f", ((System.nanoTime() - time1)*1.0e-9)) + " seconds to execute"); //calculates and prints out the total run time of that command
				break;
			case "g": //if g is chosen it performs the grayscale manipulation
				double time2 = System.nanoTime();

				picture.grayscale();

				System.out.println("Command took " + String.format("%.3f", ((System.nanoTime() - time2)*1.0e-9)) + " seconds to execute");
				break;
			case "b": //if b is chosen it performs the blur manipulation
				double time3 = System.nanoTime();

				//picture.blur();  

				System.out.println("Command took " + String.format("%.3f", ((System.nanoTime() - time3)*1.0e-9)) + " seconds to execute");
				break; 
			case "h": //if h is chosen it performs the horizontal manipulation
				double time4 = System.nanoTime();

				picture.horizontal();

				System.out.println("Command took " + String.format("%.3f", ((System.nanoTime() - time4)*1.0e-9)) + " seconds to execute");
				break;
			case "s": //if s is chosen it performs the shrink manipulation
				double time5 = System.nanoTime();

				picture.shrink();

				System.out.println("Command took " + String.format("%.3f", ((System.nanoTime() - time5)*1.0e-9)) + " seconds to execute");
				break;
			case "d": //if d is chosen it performs the doubleSize manipulation
				double time6 = System.nanoTime();

				picture.doubleSize();

				System.out.println("Command took " + String.format("%.3f", ((System.nanoTime() - time6)*1.0e-9)) + " seconds to execute");
				break;
			case "r": //if r is chosen it performs the rotate manipulation
				double time7 = System.nanoTime();

				picture.rotate();
				picture.rotate();
				picture.rotate();

				System.out.println("Command took " + String.format("%.3f", ((System.nanoTime() - time7)*1.0e-9)) + " seconds to execute");
				break;
			case "q": //if q is chosen the program stops asking for a command and gets out of the switch 
				quit = true;
				break;
			default: //if the user enters anything besides the prompted commands 
				System.out.println("Enter a valid command!");

			}

		}

		if(save)
		{
			System.out.print("What do you want to name your new image file: "); //when q is selected it will ask for a new file name to save the new picture
			filename = kb.next(); //the filename typed is read in through the scanner
			picture.write(filename); //the write method writes the new filename to the file
		}


	}




}