package de.dennis_boldt;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.thebuzzmedia.exiftool.ExifTool;
import com.thebuzzmedia.exiftool.ExifTool.Tag;

public class ImageHandler {

	private File in = null;
	private File out = null;
	private String infix = null;
	private boolean isCopy = false;
	private boolean isMove = false;
	private boolean isLink = false;
	// Needs installed exiftools
	private ExifTool tool = new ExifTool();

	public ImageHandler(File image) {
		this.in = image;
	}

	public Boolean run() throws Exception {

		StringBuffer buffer = new StringBuffer();
		buffer.append(getNewFileName());
		if (infix != null) {
			buffer.append("_" + infix);
		}
		buffer.append("_" + in.getName());

		String folder = (out != null && out.isDirectory()) ?
				out.getAbsolutePath() :
					this.in.getParent();


		String newFilePath = folder + "/" + buffer.toString();
		File newFile = new File(newFilePath);

		if(isLink) {
			FileUtils.hardLink(this.in, newFile);
		} else if(isCopy) {
			FileUtils.copy(this.in, newFile);
		} else if(isMove) {
			FileUtils.move(this.in, newFile);
		}
		return true;
	}

	private String getNewFileName() throws Exception,
			SecurityException, IOException {
		Map<Tag, String> valueMap = tool.getImageMeta(this.in,
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
		this.out = out;
	}

}
