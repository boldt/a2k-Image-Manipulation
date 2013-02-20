package de.dennis_boldt;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;

import de.dennis_boldt.utils.MimeTypeUtil;

/**
 *
 * The Main class
 *
 * @author Dennis Boldt
 *
 */
public class Main {

	// EXIF is only supported by JPEG and TIFF
	private final String[] acceptedMimeTypes = { "image/jpeg" , "image/tiff"};

	@Option(name = "--in", usage = "An image or a folder to maipulate", metaVar = "FILE")
	private File input = null;

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

	public Main(String[] args) {

		// The args4j command line parser
		CmdLineParser parser = new CmdLineParser(this);
		parser.setUsageWidth(80);

		// Parse the arguments
		try {
			parser.parseArgument(args);

			// Check, if move, copy or link is set by the user
			if (!(isMove ^ isCopy ^ isLink)) {
				throw new CmdLineException(parser, "--move, --copy or --link must be given");
			}

			// An input file/folder must be given
			if(input == null) {
				throw new CmdLineException(parser,"--in must be given");
			}

			if(input.isDirectory()) {
				handleFolder(input);
			} else if (input.isFile() && input.exists()) {
				handleFile(input, 1, 1);
			} else {
				throw new Exception("File or folder does not exists!");
			}
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			System.out.println();
			System.err.println("java " + this.getClass().getName()
					+ " [options...] arguments...");
			parser.printUsage(System.out);
			// FIXME: not all param are allowed!
			// System.err.println(" Example: java " + this.getClass().getName()
			//		+ parser.printExample(in));

			if(isDebug) {
				e.printStackTrace();
			}
			System.exit(1);
		} catch (Exception e) {
			System.out.println();
			if(isDebug) {
				e.printStackTrace();
			} else {
				System.err.println(e.getMessage());
			}
			System.exit(1);
		}
		System.out.println("Successful.");
		System.exit(0);
	}

	/**
	 *
	 * @param folder The folder to be read
	 * @throws Exception
	 */
	private void handleFolder(final File folder) throws Exception {
		System.out.println("Handle folder " + folder.getAbsolutePath());
		int i = 1;
		int count = folder.listFiles().length;
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isFile()) {
	        	handleFile(fileEntry, i++, count);
	        }
	    }
	}

	/**
	 *
	 * @param file The file to be parsed
	 * @param index the index
	 * @param count th number of all files
	 * @throws Exception
	 */
	private void handleFile(final File file, int index, int count) throws Exception {
		System.out.print("Handle file " + file.getName() + " (" + index + " of " + count + ")");

		// Get the mimetype
		String mimetype = MimeTypeUtil.getMimeType(file);

		if(Arrays.asList(acceptedMimeTypes).contains(mimetype)) {

			ImageHandler handler = new ImageHandler(file);

			// Just add the infix, if it is given
			if (infix != null) {
				handler.setInfix(infix);
			}

			// Set all other configurations
			handler.setCopy(isCopy);
			handler.setLink(isLink);
			handler.setMove(isMove);
			handler.setOut(out);
			handler.run();
			System.out.println(" done");
		} else {
			System.out.println("... skipped, because mime type is " + mimetype);
		}
	}

	public static void main(String[] args) throws IOException {
		new Main(args);
	}

}