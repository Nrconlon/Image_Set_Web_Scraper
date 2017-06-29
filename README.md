![Alt text](https://user-images.githubusercontent.com/7052432/27676813-68af274e-5c7d-11e7-9d0a-df0bdc4e9d0c.PNG?raw=true "User Interface")
# Image_Set_Web_Scraper
Fun side project to save some time when downloading sets of images.


This is a java program used to download sets of images in the bulk in a numbered form.  This works by taking the pasted image link, downloading it, then changing the index number to the index number + 1 to download all the images in the set.  If the link of the first picture is not indexed at 1 or 01, the program will not work correctly.  Image saved names are based off the calculated index in the current folder directory, so each new set will be 1 number greater than the last set.  This way all the photos are in order of when they were downloaded.

Current features: 
Can set the minimum index
Can set max number of images downloaded in a set
Multithreaded so no wait times.

Possible Future Features:
Option to choose index, with a check box to allow the user to replace images or not.

Use of Program:
To open, have java installed and run the Executable Jar File called "ImageSetWebScraper".
1. Grab your directory where you want the photos to go, and paste it into the "Directory" text box.
2. Copy a link of the first image of the set, denoted by (1) or 001 or 1.
3. Select how many pictures you want, or the minimum index (if either of these are applicable)
4. Hit "Start download"
5. Repeat steps 2-5 until done.


Notes on code:
downloadPics.java has the main function of the program.  It runs GUI(), which loads the GUI and its elements.  The button press of the "Start Button" calls the startDownload() function, which uses the information from the GUI to run the actual calculations and downloading.
First it runs getIndex, which goes through the folder starting from 000 and searches upward until it finds a set number that has no images.  Once the desired index is found, a thread is ran (downloadImageThread) which handles the actual downloading of the file. The URL is incremented and the next picture is downloaded as index+1.
