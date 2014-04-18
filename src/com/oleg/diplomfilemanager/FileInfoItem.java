package com.oleg.diplomfilemanager;

import java.io.File;

import android.os.Parcel;
import android.os.Parcelable;

public class FileInfoItem implements Parcelable {

	private FileInfoItem(String displayName, String lastModified,
			String fullPath, String contentType, String publicUrl,
			boolean isCollection, boolean isReadable, boolean isWritable,
			boolean isVisible, String contentLength, boolean isPreviousFolder) {

		this.displayName = displayName;
		this.lastModified = lastModified;
		this.fullPath = fullPath;
		this.contentType = contentType;
		this.publicUrl = publicUrl;
		this.isCollection = isCollection;
		this.isReadable = isReadable;
		this.isWritable = isWritable;
		this.isVisible = isVisible;
		this.contentLength = contentLength;
		this.isPreviousFolder = isPreviousFolder;

	}

	private FileInfoItem(Parcel parcel) {
		displayName = parcel.readString();
		lastModified = parcel.readString();
		fullPath = parcel.readString();
		contentType = parcel.readString();
		publicUrl = parcel.readString();
		isCollection = parcel.readByte() > 0;
		isReadable = parcel.readByte() > 0;
		isWritable = parcel.readByte() > 0;
		isVisible = parcel.readByte() > 0;
		contentLength = parcel.readString();
		isPreviousFolder = parcel.readByte() > 0;
	}

	private String displayName, lastModified, fullPath, contentType, publicUrl,
			contentLength;
	private boolean isCollection, isVisible, isReadable, isWritable,
			isPreviousFolder;

	public static class Builder {

		private String displayName, lastModified, fullPath, contentType,
				publicUrl, contentLength;
		private boolean isCollection, isVisible, isReadable, isWritable,
				isPreviousFolder;

		public void setFullPath(String fullPath) {
			this.fullPath = fullPath;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		public void setContentLength(String contentLength) {
			this.contentLength = contentLength;
		}

		public void setLastModified(String lastModified) {
			this.lastModified = lastModified;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public void addCollection(boolean b) {
			if (b)
				this.isCollection = true;
			else
				this.isCollection = false;
		}

		public void addReadable(boolean read) {
			this.isReadable = read;
		}

		public void addWritable(boolean write) {
			this.isWritable = write;
		}

		public void setVisible(boolean visible) {
			this.isVisible = visible;
		}

		public void setPublicUrl(String publicUrl) {
			this.publicUrl = publicUrl;
		}

		public void addPreviousFolder(boolean isPreviousFolder) {
			this.isPreviousFolder = isPreviousFolder;
		}

		public FileInfoItem build() {
			return new FileInfoItem(displayName, lastModified, fullPath,
					contentType, publicUrl, isCollection, isReadable,
					isWritable, isVisible, contentLength, isPreviousFolder);
		}

	}

	public String getFullPath() {
		return fullPath;
	}

	// public String getName() {
	// return new File(fullPath).getName();
	// }

	public String getDisplayName() {
		return displayName;
	}

	public String getContentLength() {
		return contentLength;
	}

	public String getLastModified() {
		return lastModified;
	}

	public boolean isCollection() {
		return isCollection;
	}

	public String getContentType() {
		return contentType;
	}

	public boolean isReadable() {
		return isReadable;
	}

	public boolean isWritable() {
		return isWritable;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public String getPublicUrl() {
		return publicUrl;
	}

	public boolean isPreviousFolder() {
		return isPreviousFolder;
	}

	public String getFilePermissions() {
		String permission = " | ";
		if (isCollection())
			permission += "d";
		else
			permission += "-";
		if (isReadable())
			permission += "r";
		else
			permission += "-";
		if (isWritable())
			permission += "w";
		else
			permission += "-";
		return permission;
	}

	@Override
	public String toString() {
		return "displayName " + displayName + " lastModified " + lastModified
				+ " fullPath " + fullPath + " contentType " + contentType
				+ " publicUrl " + publicUrl + " isCollection " + isCollection
				+ " isReadable " + isReadable + " isWritable " + isWritable
				+ " isVisible " + isVisible + " contentLength " + contentLength
				+ " isPreviousFolder " + isPreviousFolder;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(displayName);
		parcel.writeString(lastModified);
		parcel.writeString(fullPath);
		parcel.writeString(contentType);
		parcel.writeString(publicUrl);
		parcel.writeByte((byte) (isCollection ? 1 : 0));
		parcel.writeByte((byte) (isReadable ? 1 : 0));
		parcel.writeByte((byte) (isWritable ? 1 : 0));
		parcel.writeByte((byte) (isVisible ? 1 : 0));
		parcel.writeString(contentLength);
		parcel.writeByte((byte) (isPreviousFolder ? 1 : 0));
	}
	
	 public static final Parcelable.Creator<FileInfoItem> CREATOR = new Creator<FileInfoItem>() {
		
		@Override
		public FileInfoItem[] newArray(int size) {
			return new FileInfoItem[size];
		}
		
		@Override
		public FileInfoItem createFromParcel(Parcel parcel) {
			return new FileInfoItem(parcel);
		}
	};
}