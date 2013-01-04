a2k Image  Manipulation
===

### Background/About this project

This small project is a simple java based command line tool for linux which renames/moves/links images by prepending the creation date (from the EXIF data) and adding an optional infix to the file name.

### Get it:

```
git clone git@github.com:boldt/a2k-Image-Manipulation.git
```

### Build it:

```	
cd a2k-Image-Manipulation
mvn clean package
```

The file `target/a2kImageManipulation-1.1-boldt-SNAPSHOT.jar` will be created.

### Run it:

```
java -jar target/a2kImageManipulation-1.1-boldt-SNAPSHOT.jar --in /path/to/images --out /path/to/output --infix "summer" --link 
```	

It takes all files from `/path/to/images` and creates files as follows:

```
/path/to/output/YYYY-MM-DD_HH-MM-SS_summer_FILENAME.jpg
```

### Arguments

* `--in` The image or folder to be read
* `--out` (optional) The output folder. If not given, the source folder is used
* `--infix` (optional) An additional infix
* `--move` The original file will be renamed OR
* `--copy` The original file will be copied OR
* `--link` The original file will be hard linked
* `--debug` (optional) Prints possible Exceptions

### Thanks to

You rock:

* [args4j](https://github.com/kohsuke/args4j) (Command line arguments parser)
* [exiftool-lib](http://www.thebuzzmedia.com/software/exiftool-enhanced-java-integration-for-exiftool/) (EXIF library)
* [jmimemagic](https://github.com/arimus/jmimemagic) (Mime type library)

