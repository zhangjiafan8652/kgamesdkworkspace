package com.example.moposnsplatdemo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.text.TextUtils;

public class FileUtil {
	public void putData2Disk(final String filePath, final byte[] value) {

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);
			if (fos != null) {
				fos.write(value, 0, value.length);
				fos.flush();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		} 
		finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public File getRecordFile(final String workPath) {
		if (!TextUtils.isEmpty(workPath)) {
			return new File(workPath + "/" + "MOPOSDK.LOG");
		}
		return null;
	}
}
