package de.dennis_boldt;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;

public class Main {

	// EFIF is supported by JPEG ad TIFF
	private final String[] acceptedTypes = { "image/jpeg" , "image/tiff"};

	// @Option(name="-r",usage="recursively run something")
	// private boolean recursive;

	@Option(name = "--in", usage = "An image or a folder to maipulate", metaVar = "FILE")
	private File in = null;

	@Option(name = "--out", usage = "A destination folder to store the manipulated files", metaVar = "FOLDER")
	private File out = null;

	@Option(name = "--infix", usage = "An addition infix between the date and the original file name", metaVar = "INFIX")
	private String infix = null;

	@Option(name = "--move", handler = BooleanOptionHandler.class, usage = "Move the file to the new name")
	private boolean isMove;

	@Option(name = "--copy", handler = BooleanOptionHandler.class, usage = "Copy the file. If --outfolder is given, it is copied to the --outfolder")
	private boolean isCopy;

	@Option(name = "--link", handler = BooleanOptionHandler.class, usage = "Hard link the file. If --outfolder is given, it is linked into the --outfolder")
	private boolean isLink;

	@Option(name = "--debug", handler = BooleanOptionHandler.class, usage = "Print java errors, if available.")
	private boolean isDebug;

	public void doMain(String[] args) {
		CmdLineParser parser = new CmdLineParser(this);
		parser.setUsageWidth(80);

		try {
			parser.parseArgument(args);
			// if( arguments.isEmpty() ) {
			//
			// }
			if (!(isMove ^ isCopy ^ isLink)) {
				throw new CmdLineException(parser, "--move, --copy or --link must be given");
			}

			if(in == null) {
				throw new CmdLineException(parser,"--in must be given");
			}

			if(in.isDirectory()) {
				handleFolder(in);
			} else if (in.isFile() && in.exists()) {
				handleFile(in, 1, 1);
			} else {
				System.err.println("File or folder does not exists!");
				System.exit(0);
			}

		} catch (CmdLineException e) {
			// an error message.
			System.err.println(e.getMessage());
			System.err.println("java " + this.getClass().getName()
					+ " [options...] arguments...");
			parser.printUsage(System.err);
			//System.err.println();
			// print option sample. This is useful some time
			//System.err.println(" Example: java " + this.getClass().getName()
			//		+ parser.printExample(in));

			if(isDebug) {
				e.printStackTrace();
			}

			return;
		} catch (Exception e) {
			if(isDebug) {
				e.printStackTrace();
			} else {
				System.err.println(e.getMessage());
			}
			System.exit(0);
		}
	}

	private void handleFolder(final File folder) throws Exception {
		System.out.println("Handle foler " + folder.getAbsolutePath());
		int i = 1;
		int count = folder.listFiles().length;
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isFile()) {
	        	handleFile(fileEntry, i++, count);
	        }
	    }
	}

	private void handleFile(final File file, int index, int count) throws Exception {
		System.out.print("Handle file " + file.getName() + " (" + index + " of " + count + ")");

		String mimetype = FileUtils.mimeType(file);

		if(Arrays.asList(acceptedTypes).contains(mimetype)) {
			EXIF exif = new EXIF(file);
			if (infix != null) {
				exif.setInfix(infix);
			}
			exif.setCopy(isCopy);
			exif.setLink(isLink);
			exif.setMove(isMove);
			exif.setOut(out);
			exif.run();
			System.out.println(" done");
		} else {
			System.out.println("... skipped, because mime type is " + mimetype);
		}
	}

	public static void main(String[] args) throws IOException {
		new Main().doMain(args);
	}

}