/*
 *  Copyright 2012 Finalist B.V.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License")
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
package com.tdclighthouse.prototype.beans.compounds;

import org.hippoecm.hst.content.beans.Node;

/**
 * @author Ebrahim Aharpour
 *
 */
@Node(jcrType = ParagraphImageBean.JCR_TYPE)
public class ParagraphImageBean extends ImageBean {

	public static final String JCR_TYPE = "tdc:ParagraphImage";

	private String horizontalPosition;
	private String verticalPosition;

	public String getHorizontalPosition() {
		if (this.horizontalPosition == null) {
			this.horizontalPosition = getProperty("tdc:horizontalPosition");
		}
		return horizontalPosition;
	}

	public String getVerticalPosition() {
		if (this.verticalPosition == null) {
			this.verticalPosition = getProperty("tdc:verticalPosition");
		}
		return verticalPosition;
	}

}