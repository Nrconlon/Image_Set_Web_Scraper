import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class downloadImageThread implements Runnable{
	private URL readIn;
	private File outputfile;
	private String old_name;
	static  JTextArea results;
	 
    public downloadImageThread(URL readIn, File outputfile, String old_name, JTextArea results)
    {
    	this.readIn = readIn;
    	this.outputfile = outputfile;
    	this.old_name = old_name;
    	downloadImageThread.results = results;
    }
 
    public void run()
    {
    	//you could make it so it grabs the numbers from the right.
    	try {
			Image image = ImageIO.read(readIn);
			if(image == null){
				results.setText(results.getText()+"\n"+"Could not load: " + old_name);
				System.out.println("Couldnt load " + old_name);
			}
			else{
				BufferedImage bi = (BufferedImage) image;
			    if (!outputfile.exists())
			    	ImageIO.write(bi, "jpg", outputfile);
				results.setText(results.getText()+"\n"+"Loaded: " + old_name);
				System.out.println("loaded " + old_name);
			}
		} catch (IOException e) {
			results.setText(results.getText()+"\n"+"Could not load: " + old_name);
			System.out.println("Couldnt load " + old_name);
		}
    }
}
