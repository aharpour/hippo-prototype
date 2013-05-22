/*
 *  Copyright 2012 Finalist B.V.
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
package com.tdclighthouse.prototype.beans.compounds;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import com.tdclighthouse.prototype.beans.TdcDocument;

/**
 * @author Ebrahim Aharpour
 *
 */
@Node(jcrType = AudioBean.JCR_TYPE)
public class AudioBean extends TdcDocument {

	public static final String JCR_TYPE = "tdc:Audio";

	private String alt;
	private String caption;
	private String title;
	private VideoItemBean audio;
	private HippoHtml transcript;

	public String getAlt() {
		if (this.alt == null) {
			this.alt = getProperty("tdc:alt");
		}
		return alt;
	}

	public String getCaption() {
		if (this.caption == null) {
			this.caption = getProperty("tdc:caption");
		}
		return caption;
	}

	public String getTitle() {
		if (this.title == null) {
			this.title = getProperty("tdc:title");
		}
		return title;
	}

	public VideoItemBean getAudio() {
		if (this.audio == null) {
			this.audio = getBean("tdc:audio");
		}
		return audio;
	}

	public HippoHtml getTranscript() {
		if (this.transcript == null) {
			this.transcript = getHippoHtml("tdc:transcript");
		}
		return transcript;
	}

}
