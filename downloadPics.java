import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.*;

public class downloadPics {
	
	
	public static void gui()
	{
		JFrame f;
		JPanel p = new JPanel(new FlowLayout());
		JLabel instructions;
		JButton start;
		
		JTextField pasteUrl = new JTextField();
		pasteUrl.setPreferredSize(new Dimension(550,25));
		
		JTextField pasteDirectory = new JTextField();
		pasteDirectory.setPreferredSize(new Dimension(520,25));
		
		JTextField pasteN = new JTextField("20");
		pasteN.setPreferredSize(new Dimension(25,25));
	
		JLabel instructionsURL = new JLabel("URL");
		JLabel instructionsDirectory = new JLabel("Directory");
		JLabel instructionsN = new JLabel("Paste number of pics if you don't want 20");
		
		JTextArea results = new JTextArea();
		results.setPreferredSize(new Dimension(300,300));
		results.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(results);
		
		f = new JFrame("Download Images");
		f.setSize(675,500);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		
		
		
		instructions = new JLabel("This app downloads a set of pictures using a link and a directory.  Paste the link with the index number as 01 or 1");
		start = new JButton("Start Download");
		start.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  if(pasteUrl.getText() != "" && pasteDirectory.getText() != "" && pasteN.getText() != "")
			  {
				  
				try {
					startDownload(pasteUrl.getText(),pasteDirectory.getText(),pasteN.getText(), results);
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			  }
		  }
		});
		
		JButton urlPasteButton = new JButton("Paste");
		urlPasteButton.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  pasteUrl.setText(getClipboard());
		  }
		});
		
		JButton directoryPasteButton = new JButton("Paste");
		directoryPasteButton.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  pasteDirectory.setText(getClipboard());
		  }
		});
		
		p.add(instructions, BorderLayout.PAGE_START);
		p.add(instructionsURL, BorderLayout.LINE_END);
		p.add(pasteUrl, BorderLayout.LINE_END);
		p.add(urlPasteButton);
		
		p.add(instructionsDirectory,BorderLayout.LINE_END);
		p.add(pasteDirectory,BorderLayout.LINE_END);
		p.add(directoryPasteButton);
		
		p.add(instructionsN,BorderLayout.LINE_END);
		p.add(pasteN,BorderLayout.LINE_END);
		
		p.add(start, BorderLayout.PAGE_END);
		p.add(scrollPane);
		
		f.add(p);
	}

	public static void main(String[] args) {
		gui();
	}
	
	private static void startDownload(String pasteUrl, String pasteDirectory, String pasteN, JTextArea results) throws IOException, URISyntaxException
	{
		
		URL url = new URL(pasteUrl);
		String directory = pasteDirectory;
		int n = 20;
		n = Integer.parseInt(pasteN);
		
		String filename = "nope";
		String iIndex ="";
		String iIndexToAdd ="";
	    File outputfile;
	    String old_name = "";
	    boolean use_zero = true;
		try {
		URI uri = url.toURI();
		URI parent = uri.getPath().endsWith("/") ? uri.resolve("..") : uri.resolve(".");
		String path = parent.toString();
		String tempfilename = url.getFile();
		filename = tempfilename.substring(tempfilename.lastIndexOf('/')+1, tempfilename.length() );
		int specialCase = 0;
		if (!filename.contains("01"))
			use_zero = false;
		if(filename.contains("1001"))
			specialCase = 1;
		if(filename.contains("10001"))
			specialCase = 2;
			
		String indx = getIndex(directory);
		results.setText("Started at index:" + indx);
				for (int i = 1; i<n+1 ; i++)
				{
					    if (i < 10)
					    {
					    	outputfile = new File(directory + "/" + indx + "0" + i + ".jpg");
					    	if(use_zero)
					    	{
						    	iIndex = "0" + i;
						    	iIndexToAdd = "0" + (i+1);
					    	}
					    	else
					    	{
					    		iIndex = Integer.toString(i);
						    	iIndexToAdd = Integer.toString(i+1);
					    	}
					    }
					    else
					    {
				    		outputfile = new File(directory + "/" + indx + i + ".jpg");
					    	iIndex = Integer.toString(i);
					    	iIndexToAdd = Integer.toString(i+1);
					    	if (i==9 && use_zero)
					    	{
					    		outputfile = new File(directory + "/" + indx + "0" + i + ".jpg");
					    		iIndex = "0" + Integer.toString(i);
					    	}
					    }
						URL readIn = new URL(path + filename);
						old_name = filename;
						if(specialCase == 1)
						{
							filename = filename.replaceAll("10" + iIndex, "10" + iIndexToAdd);
						}
						else if(specialCase == 2)
						{
							filename = filename.replaceAll("100" + iIndex, "100" + iIndexToAdd);
						}
						else{
							filename = filename.replaceAll(iIndex, iIndexToAdd);
						}
						Runnable threadStarter = new downloadImageThread(readIn,outputfile,old_name,results);
						new Thread(threadStarter).start();
				}
		System.out.println("Finished at index " +  indx);
		} catch (IOException e) {
			System.out.println(e);
			System.out.println(filename);
		}
	}
	
	private static String getClipboard()
	{
		Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		try {
	        if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
	            String text = (String)t.getTransferData(DataFlavor.stringFlavor);
	            return text;
	        }
	    } catch (UnsupportedFlavorException e) {
	    } catch (IOException e) {
	    }
	    return null;
		
	}
	
	private static String getIndex(String directory)
	{
		int count = 1;
		boolean notFound = true;
		String indx = "01";
		while(notFound)
		{
			if (count >=10)
				indx = Integer.toString(count);
			else
				indx = 0 + Integer.toString(count);
			count++;
			for (int i = 1; i < 10;i++)
			{
				File f = new File(directory + "/" + indx + i + ".jpg");
				File ff = new File(directory + "/" + indx + "0" + i + ".jpg");
				File fff = new File(directory + "/" + indx + ".jpg");
			
				if (f.exists() || ff.exists() || fff.exists())
				{
					break;
				}
				if( i == 9)
					notFound=false;
			}
		}

		return indx;
	}

}


