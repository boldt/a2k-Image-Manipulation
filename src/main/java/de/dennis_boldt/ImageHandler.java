package de.dennis_boldt;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.thebuzzmedia.exiftool.ExifTool;
import com.thebuzzmedia.exiftool.ExifTool.Tag;

import de.dennis_boldt.utils.FileUtils;

public class ImageHandler {

	private File inFile = null;
	private File outFile = null;
	private String infix = null;
	private boolean isCopy = false;
	private boolean isMove = false;
	private boolean isLink = false;
	private ExifTool tool = new ExifTool();

	public ImageHandler(File image) {
		this.inFile = image;
	}

	public void run() throws Exception {

		StringBuffer buffer = new StringBuffer();
		buffer.append(getNewFileName());
		if (infix != null) {
			buffer.append("_" + infix);
		}
		buffer.append("_" + inFile.getName());

		String folder = (outFile != null && outFile.isDirectory()) ?
				outFile.getAbsolutePath() : this.inFile.getParent();

		String newFilePath = folder + "/" + buffer.toString();
		File newFile = new File(newFilePath);

		if(isLink) {
			FileUtils.hardLink(this.inFile, newFile);
		} else if(isCopy) {
			FileUtils.copy(this.inFile, newFile);
		} else if(isMove) {
			FileUtils.move(this.inFile, newFile);
		}
	}

	private String getNewFileName() throws Exception,
			SecurityException, IOException {
		Map<Tag, String> valueMap = tool.getImageMeta(this.inFile,
				Tag.DATE_TIME_ORIGINAL);
		String date = valueMap.get(Tag.DATE_TIME_ORIGINAL);

		if(date == null) {
			throw new Exception ("EXIF date cannot be read");
		}

		date = date.replaceAll(" ", "_");
		date = date.replaceAll(":", "-");
		return date;
	}

	public void setInfix(String infix) {
		this.infix = infix;
	}

	public void setLink(boolean isLink) {
		this.isLink = isLink;
	}

	public void setMove(boolean isMove) {
		this.isMove = isMove;
	}

	public void setCopy(boolean isCopy) {
		this.isCopy = isCopy;
	}

	public void setOut(File out) {
		this.outFile = out;
	}

}
