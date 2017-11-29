package com.sjec.rcms.activity.workorder;

import android.graphics.Bitmap;

public class EntityPicture {

		public EntityPicture(boolean isImage, String path, Bitmap bitmap_thumb,
				byte[] arr) {
			this.imagePath = path;
			this.isImage = isImage;
			this.bitmap_thumb = bitmap_thumb;
			this.byteArr = arr;

		}

		public String imagePath = "";
		public boolean isImage = false;
		public Bitmap bitmap_thumb;
		public byte[] byteArr;
	}