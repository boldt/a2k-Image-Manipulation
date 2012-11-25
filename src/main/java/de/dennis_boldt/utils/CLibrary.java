package de.dennis_boldt.utils;
import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * @see: https://github.com/twall/jna
 *
 * @author Dennis
 *
 */
public interface CLibrary extends Library {

	/**
	 * TODO: Windows
	 * 		http://stackoverflow.com/questions/783075/creating-a-hard-link-in-java/3023349#3023349
	 */

    CLibrary INSTANCE = (CLibrary) Native.loadLibrary(("c"), CLibrary.class);

    // Hardlink
    int link(String fromFile, String toFile);


}
