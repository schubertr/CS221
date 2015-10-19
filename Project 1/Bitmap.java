import java.io.*;
import java.util.Scanner;



public class Bitmap
{
	char[] type = new char[2];		// always contains 'B' and 'M'
	int	size;			// total size of file
	int	reserved;		// always 0
	int	offset;			// start of data from front of file, should be 54
	int	header;			// size of header, always 40
	int	width;			// width of image in pixels
	int	height;			// height of image in pixels
	short planes;			// planes in image, always 1
	short bits;			// color bit depths, always 24
	int	compression;		// always 0		
	int	dataSize;		// size of color data in bytes
	int	horizontalResolution;	// unreliable, use 72 when writing
	int	verticalResolution;	// unreliable, use 72 when writing
	int	colors;			// colors in palette, use 0 when writing
	int	importantColors; // important colors, use 0 when writing
	byte[][] colorData;

	public void read(String filename) throws IOException //read method
	{
		FileInputStream file;
		try 
		{
			file = new FileInputStream(filename); //opens a new file input stream

			int i;

			i = file.read();
			type[0] = (char) i; //gets the 'B'

			i = file.read();
			type[1] = (char) i; //gets the 'M'

			size = readInt(file.read(), file.read(), file.read(), file.read()); //calls the readInt method to get the size

			reserved = 0; //skips over reserved
			file.skip(4);

			offset = readInt(file.read(), file.read(), file.read(), file.read()); //calls the readInt method to get the offset

			header = 40; //skips over header
			file.skip(4);

			width = readInt(file.read(), file.read(), file.read(), file.read()); //calls the readInt method to get the width

			height = readInt(file.read(), file.read(), file.read(), file.read()); //calls the readInt method to get the height

			planes = readShort(file.read(), file.read()); //calls the readShort method to get the planes

			bits = readShort(file.read(), file.read()); //calls the readShort method to get the bits

			compression = 0; //skips over compression
			file.skip(4);

			dataSize = readInt(file.read(), file.read(), file.read(), file.read()); //calls the readInt method to get the dataSize

			horizontalResolution = readInt(file.read(), file.read(), file.read(), file.read()); //calls the readInt method to get the horizontalResolution

			verticalResolution = readInt(file.read(), file.read(), file.read(), file.read()); //calls the readInt method to get the verticalResolution

			colors = readInt(file.read(), file.read(), file.read(), file.read()); //calls the readInt method to get the colors

			importantColors = readInt(file.read(), file.read(), file.read(), file.read()); //calls the readInt method to get the importantColors

			colorData = new byte[height][width * 3];  //creates a new 2D byte array using height and width*3

			for(int j = 0 ; j < height ; j++) //for loop that reads in the color data into the array
				file.read(colorData[j]);

		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("File " + "\"" + filename + "\"" + " was not found"); //returns "File not Found" and quits ~gracefully~
			Manipulator.quit = true;
			Manipulator.save = false;
		}

	}

	public static int readInt(int a, int b, int c, int d)
	{
		int value = a;
		value += b << 8;
		value += c << 16;		//reads in the 4 bytes and gets the int out of them
		value += d << 24;
		return value;
	}

	public static short readShort(int a, int b)
	{
		short value = (short) a;
		value += b << 8; 	//reads in the two bytes and gets the short out of them
		return value;
	}

	public static void writeInt(int a, FileOutputStream file) throws IOException
	{
		int value = a;
		file.write(value);
		file.write(value >>> 8);  //writes the int back out in 4 bytes
		file.write(value >>> 16);
		file.write(value >>> 24);
	}

	public static void writeShort(int a, FileOutputStream file) throws IOException
	{
		short value = (short) a;
		file.write(value);		//writes the short back out in 2 bytes
		file.write(value >>> 8);
	}


	public void write(String newFile) throws IOException
	{
		FileOutputStream file = new FileOutputStream(newFile); //opens a new file output stream

		int rmndr = width * 3 % 4; //checks to see if the width needs padding
		int padding = 0;

		switch(rmndr)
		{

		case 0:
			padding = 0;
			break;
		case 1:
			padding = 3;  //adds the appropriate amount of padding depending on the width size
			break;
		case 2:
			padding = 2;
			break;
		case 3:
			padding = 1;
			break;
		}

		dataSize = height * ((width * 3) + padding); //updates the data size
		size = 54 + dataSize + 2; //updates the size

		file.write(type[0]);
		file.write(type[1]);
		writeInt(size, file);
		writeInt(reserved, file);
		writeInt(offset, file);
		writeInt(header, file);
		writeInt(width, file);
		writeInt(height, file);				//writes out the new header
		writeShort(planes, file);
		writeShort(bits, file);
		writeInt(compression, file);
		writeInt(dataSize, file);
		writeInt(horizontalResolution, file);
		writeInt(verticalResolution, file);
		writeInt(colors, file);
		writeInt(importantColors, file);

		for(int j = 0 ; j < height ; j++)
		{
			file.write(colorData[j]);
			for(int k = 0 ; k < padding ; k++)		//writes out the header data
				file.write(0);
		}

		file.write(0);
		file.write(0); //adds the two extra bytes back on the end
	}

	public void invert()
	{ //invert image

		double time = System.nanoTime();

		for(int i = 0; i < height; i++) //going through to get the color data from each pixel
			for(int j = 0; j< width; j++){
				colorData[i][j*3] = (byte) (255-colorData[i][j*3]); //taking blue color data from the max color value
				colorData[i][j*3+1] = (byte) (255-colorData[i][j*3+1]); //taking green color data from the max color value
				colorData[i][j*3+2] = (byte) (255-colorData[i][j*3+2]); //taking red color data from the max color value
			}
		System.out.println("Command took " + (System.nanoTime() - time)*1.0e-9 + " seconds to execute");
	}

	public void grayscale()
	{ //grayscale image

		for(int i = 0; i < height; i++) //going through to get color data from each pixel
			for(int j = 0; j< width; j++){
				byte grayscale = (byte)Math.round ((colorData [i][j*3] & 0xff ) * 0.11 + (colorData [i][j*3+1] & 0xff) * 0.59 + (colorData [i][j*3+2] & 0xff) * 0.3); //adding blue,green,red grayscale values and putting them into one value
				colorData[i][j*3] = grayscale; //taking the result of the variable grayscale and putting it into the blue color values of each pixel
				colorData[i][j*3+1] = grayscale; //taking the result of the variable grayscale and putting it into the green color values of each pixel
				colorData[i][j*3+2] = grayscale; //taking the result of the variable grayscale and putting it into the red color values of each pixel

			}

	}
	public void horizontal() //mirrors the image across the x axis
	{
		for(int i = 0 ; i < height; i++) //going through to get color data from each pixel
			for(int j = 0 ; j < width/2 ; j++)
			{

				byte b = 0;
				byte g = 0; //temp values for the original
				byte r = 0;
				byte flipB = 0;
				byte flipG = 0;  //temp values for the flipped image
				byte flipR = 0;

				b = colorData[i][j*3];
				g = colorData[i][j*3+1];  //stores the originals in the temps
				r = colorData[i][j*3+2];

				flipB = colorData [i][(width - (j) - 1)*3];
				flipG = colorData [i][(width - (j) - 1)*3+1]; //stores the flipped values in the temp
				flipR = colorData [i][(width - (j) - 1)*3+2];

				colorData[i][j*3] = flipB;
				colorData[i][j*3+1] = flipG; //puts the flipped ones in the original
				colorData[i][j*3+2] = flipR;

				colorData [i][(width - (j) - 1)*3] = b;
				colorData [i][(width - (j) - 1)*3+1] = g;  //puts the original values on the other side
				colorData [i][(width - (j) - 1)*3+2] = r;

			}
	}

	public void shrink() //shrink image
	{


		byte [][] newPicture = new byte[height/2][(width/2)*3]; //making a new array for the new picture
		if(height%2 == 1) //if there is an odd amount of columns 
			height--; //height goes down by one every time the columns are odd and it takes it away
		if(width%2 ==1) //if there is an odd amount of rows
			width--; //width goes down by one every time the rows are odd and it takes it away


		int h = 0; //height index for new picture
		int w = 0; //width index for new picture

		float b = 0; //color value for blue 
		float g = 0; //color value for green
		float r = 0; //color value for red

		for(int i = 0; i < height-2; i+=2){ //search through original picture 
			for(int j = 0; j < width*3-6; j+=6){
				b = (colorData[i][j]& 0xff)+(colorData[i][j+3]& 0xff)+(colorData[i+1][j]& 0xff)+(colorData[i+1][j+3]& 0xff); //getting blue color values in 2x2 grid
				g = (colorData[i][j+1]& 0xff)+(colorData[i][j+4]& 0xff)+(colorData[i+1][j+1]& 0xff)+(colorData[i+1][j+4]& 0xff); //getting green color values in 2x2 grid
				r = (colorData[i][j+2]& 0xff)+(colorData[i][j+5]& 0xff)+(colorData[i+1][j+2]& 0xff)+(colorData[i+1][j+5]& 0xff); //getting red color values in 2x2 grid

				newPicture[h][w] = (byte)(b/4); //averaging blue color values 
				newPicture[h][w+1] = (byte)(g/4); //averaging green color values 
				newPicture[h][w+2] = (byte)(r/4); //averaging red color values 
				w += 3; //increase w to advance in the row and to avoid looking at the pixels that were already looked at
			}
			w = 0; //set back to zero to look through the columns of the next row
			h++; //when the loop is done with a row one is added to h to go to the next row
		}

		colorData = newPicture; //set the new picture to be the now "original" picture
		height /= 2; //new picture height is half of the original 
		width /= 2; //new picture width is half of the original 

	}

	public void doubleSize() //double the size of image
	{

		byte [][] newPicture = new byte[height*2][(width*2)*3]; //making new array for the bigger picture


		int h = 0; //height index for original picture
		int w = 0; //width index for original picture

		for(int i = 0; i < height*2; i+=2){ //loop to search new picture
			for(int j = 0; j < width*6; j+=6){ 

				//storing value of the pixel from the original picture to the four pixels in the new picture 
				//blue color value 
				newPicture[i][j] =  colorData[h][w]; 
				newPicture[i][j+3]= colorData[h][w];  
				newPicture[i+1][j] = colorData[h][w];  
				newPicture[i+1][j+3] = colorData[h][w];

				//storing value of the pixel from the original picture to the four pixels in the new picture 
				//green color value
				newPicture[i][j+1] =  colorData[h][w+1];
				newPicture[i][j+4]= colorData[h][w+1];  
				newPicture[i+1][j+1] = colorData[h][w+1];  
				newPicture[i+1][j+4] = colorData[h][w+1];

				//storing value of the pixel from the original picture to the four pixels in the new picture 
				//red color value
				newPicture[i][j+2] =  colorData[h][w+2];
				newPicture[i][j+5]= colorData[h][w+2];  
				newPicture[i+1][j+2] = colorData[h][w+2];  
				newPicture[i+1][j+5] = colorData[h][w+2];

				w += 3; //up by three to skip over the color values that were already taken
			}
			w = 0; //set back to zero to look through the columns of the next row
			h++; //when the loop is done with a row one is added to h to go to the next row
		}

		colorData = newPicture; //set the new picture to be the now "original" picture
		height *= 2; //new picture height is double of the original
		width *= 2; //new picture width is double of the original

	}

	public void rotate()
	{
		byte [][] newPicture = new byte[width][height*3];
		for(int i = 0 ; i < width ; i++)					//loops to go through the image
			for(int j = 0 ; j < height ; j++)
			{
				newPicture[i][(height - j - 1)*3] = colorData[j][i*3];
				newPicture[i][(height - j - 1)*3+1] = colorData[j][i*3+1];		//rotates the image
				newPicture[i][(height - j - 1)*3+2] = colorData[j][i*3+2];
			}
		int temp = width;
		width = height;			//updates the width and height
		height = temp;
		colorData = newPicture; //sets colorData to newPicture

	}

	public void blur()
	{
		byte[][] blurred = new byte [height][width*3]; //making new array for the blur picture
		for (int i = 0; i < height; i++){ //counts colors in each pixel
			for (int j = 0; j < width; j++){
				double count = 0; //counts the number that each color occurs
				int b = 0; //color value for blue
				int g = 0; //color value for green
				int r = 0; //color value for red
				for(int x = i-2; x <= i+2 ; x++) { //loop for 5x5 grid
					for(int y = j-2; y <= j+2; y++) {
						if(x >= 0 && x < height && y >= 0 && y < width){ //check so no out of bounds
							b += (colorData[x][3*y]& 0xff); //getting and setting the blue color data for each pixel in 5x5 grid
							g += (colorData[x][3*y+1]& 0xff); //getting and setting the green color data for each pixel in 5x5 grid
							r += (colorData[x][3*y+2]& 0xff); //getting and setting the red color data for each pixel in 5x5 grid
							count++; //up the count for each color value found
						}
					}
					//store image color data for blurred image
					blurred[i][j*3] = (byte)Math.round(b/count);
					blurred[i][j*3+1] = (byte)Math.round(g/count);
					blurred[i][j*3+2] = (byte)Math.round(r/count);
				}
				colorData = blurred; //store the new pixel values into the picture
			}
		}
	}
}

