a2k Image  Manipulation
===

### Background/About this project

This small project is a simple command line tool which renames/moves/links images by prepending the creation date (from the EXIF data) and adding an optional infix to the file name.

### Get it:

	git clone git://...

### Build it:
	
	cd 
		mvn clean package

### Run it:

An example:

java -jar ImageManipulation-0.0.1-SNAPSHOT-jar-with-dependencies.jar 
	--in /path/to/images 
	--out /path/to/output
	--infix "summer" 
	--link 
	
It takes all files from /path/to/images and creates files as follows:

/path/to/output/YYYY-MM-DD_HH-MM-SS_summer_FILENAME.jpg

### Thanks to

* 
* 
* 

You rock!
