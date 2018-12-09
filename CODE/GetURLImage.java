import java.awt.image.*;
import javax.imageio.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.io.*;
import java.net.URL;
import java.net.MalformedURLException;
/**
 * Get Images off the web
 * @author Steven Bedoya
 *
 */
public class GetURLImage {

	private URL url = null;
	private File outputFile = null;
	private static BufferedImage image = null;
	private static ImageIcon PIC;

	private static void fetchImageFromURL (URL url) {
		try {
		// Read from a URL
		// URL url = new URL("http://hostname.com/image.gif");
		image = ImageIO.read(url);
		} 
		catch (IOException e) {
			System.out.println("Either Link is bad or could not connect");
		} // catch

	} // fetchImageFromURL

    // Create a URL from the specified address, open a connection to it,
    // and then display information about the URL.
    public GetURLImage(String URL) throws MalformedURLException, IOException{
    	int slashIndex = URL.lastIndexOf('/');
    	String fileName= URL.substring(slashIndex + 1);		//Gets the last part of the URL name 
    	
    	//Connect to the website and get the picture
		URL url = new URL(URL);
		File outputFile = new File("Image\\"+ fileName );
		fetchImageFromURL(url);								//Fetches the image from the website
		ImageIO.write(image, "jpg", outputFile);			//Saves the file into the computer
	    ImageIcon PIC = new ImageIcon(image, "Image");														//Returns the Picture
    } // main
    
    public ImageIcon GetImage(){
    	return PIC;
    }

} // GetURLImage
