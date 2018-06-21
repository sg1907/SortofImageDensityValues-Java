
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.*;

public class jpg {

	public static void main(String[] args) throws IOException {
		try {

			double I = 0; // density
			int r, g, b; // red , green , blue
			int rgb;	// rgb value of image	

			File file = new File("tiger.jpg"); // I have fenerbahce.jpg in my
												// working
												// directory

			FileInputStream fis = new FileInputStream(file);

			BufferedImage image = ImageIO.read(fis); // reading the image file

			int rows = 120; // You should decide the values for rows and cols
							// variables
			int cols = 160;
			int chunks = rows * cols;
			int chunkWidth = image.getWidth() / cols; // determines the chunk
														// width
														// and height
			int chunkHeight = image.getHeight() / rows;
			int count = 0;

			BufferedImage imgs[] = new BufferedImage[chunks]; // Image array to
																// hold
																// image chunks
			
			for (int i = 0; i < rows; i++) {									// Image split in here
				for (int j = 0; j < cols; j++) {
					imgs[count] = new BufferedImage(chunkWidth, chunkHeight, image.getType());
					Graphics2D gr = imgs[count++].createGraphics();
					gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * j, chunkHeight * i,
							chunkWidth * j + chunkWidth, chunkHeight * i + chunkHeight, null);
					gr.dispose();
				}
			}

			System.out.println("Splitting done.");

			double[] Is = new double[chunks];

			for (int i = 0; i < chunks; i++) {
				Is[i] = densityCalculation(imgs[i]);	//density holds array
			}

			quickSort(Is, imgs, 0, chunks - 1); // sort here

			int type;
			type = imgs[0].getType();
			chunkWidth = imgs[0].getWidth();
			chunkHeight = imgs[0].getHeight();

			BufferedImage sortedImg = new BufferedImage(chunkWidth * cols, chunkHeight * rows, type); //create new image

			int num = 0;
			
			for (int i = 0; i < rows; i++) {			// images combined in here.
				for (int j = 0; j < cols; j++) {
					sortedImg.createGraphics().drawImage(imgs[num], chunkWidth * j, chunkHeight * i, null);
					num++;
				}
			}
			
			System.out.println("Image concatenated...");
			ImageIO.write(sortedImg, "PNG", new File("sortedImg.png"));	 // create image 
			System.out.println("Picture was created.");
			
		} catch (Exception e) {
			System.out.println(" Error!!! " + e);
		}
	}

	public static double densityCalculation(BufferedImage image) {

		int w = image.getWidth();		// get the width of image
		int h = image.getHeight();		// get the height of image

		double I = 0;

		for (int x = 0; x < w; x++) {

			for (int y = 0; y < h; y++) {

				int rgb = image.getRGB(x, y);	// get the rgb values the image	

				int b = (rgb >> 0) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int r = (rgb >> 16) & 0xFF;

				I += (0.298 * r) + (0.587 * g) + (0.114 * b);	// density calculator each pixel

			}
		}
		return I / (w * h);
	}

	public static void quickSort(double[] arr, BufferedImage[] imgs, int low, int high) {

		if (arr == null || arr.length == 0)
			return;

		if (low >= high)
			return;

		// pick the pivot
		int middle = low + (high - low) / 2;
		double pivot = arr[middle];

		// make left < pivot and right > pivot
		int i = low, j = high;
		while (i <= j) {
			while (arr[i] < pivot) {
				i++;
			}

			while (arr[j] > pivot) {
				j--;
			}
			if (i <= j) {
				double temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;

				BufferedImage temp2 = imgs[i];
				imgs[i] = imgs[j];
				imgs[j] = temp2;

				i++;
				j--;
			}
		}

		// recursively sort two sub parts
		if (low < j)
			quickSort(arr, imgs, low, j);

		if (high > i)
			quickSort(arr, imgs, i, high);
	}

}
