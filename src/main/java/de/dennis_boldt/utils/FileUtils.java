package de.dennis_boldt.utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;

/*
 * http://www.onyxbits.de/content/blog/patrick/how-deal-filesystem-softlinkssymbolic-links-java
 */
public class FileUtils {

	/*
	 * With java 7
	 * http://www.javabeat.net/2012/07/creating-hard-links-and-soft-links-for-a-file-in-java/
	 * http://www.tutego.de/blog/javainsel/2011/06/dateien-kopieren-und-verschieben-mit-files-methode-in-java-7/
	 * http://docs.oracle.com/javase/tutorial/essential/io/links.html
	 *
	 * Files.createLink(hLink, file1);
	 * Files.createSymbolicLink(sLink, file1);
	 * Files.copy(hLink, file1);
	 * Files.move(hLink, file1);
	 */

	/*
	 * @see: http://stackoverflow.com/a/3023349/605890
	 */
	public static int hardLink(File from, File to) {
		return CLibrary.INSTANCE.link(from.getAbsolutePath(), to.getAbsolutePath());
	}

	/*
	 * @see: http://www.javalobby.org/java/forums/t17036.html
	 */
	public static void copy(File sourceFile, File destFile)
			throws IOException {
		if (!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;
		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		} finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}

	/*
	 * @see http://www.java-forum.org/allgemeine-java-themen/84189-effektives-file-move-copy.html#post525503
	 */
	public static boolean move(File from, File to) {
        if(to.exists()) {
        	to.delete();
        }
        return from.renameTo(to);
    }

	/*
	 * http://www.rgagnon.com/javadetails/java-0487.html
	 */
	public static String mimeType(File file) throws Exception {
		MagicMatch match = Magic.getMagicMatch(file, false);
		return match.getMimeType();
	}

}
