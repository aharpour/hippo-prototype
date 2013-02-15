/*
 *  Copyright 2012 Smile B.V.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.tdclighthouse.prototype.utils;

import java.io.File;

import javax.activation.MimetypesFileTypeMap;

/**
 * @author Ebrahim Aharpour
 *
 */
public class ImageUtils {

	public static boolean isHippoFriendlyImage(File file) {
		boolean result = false;
		MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
		String contentType = mimetypesFileTypeMap.getContentType(file);
		if (contentType.startsWith("image/") && !"image/tiff".equals(contentType) && !"image/bmp".equals(contentType)) {
			result = true;
		}
		return result;
	}

}
