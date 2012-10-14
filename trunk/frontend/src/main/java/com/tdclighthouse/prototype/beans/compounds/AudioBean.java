package com.tdclighthouse.prototype.beans.compounds;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

@Node(jcrType = AudioBean.JCR_TYPE)
public class AudioBean extends HippoDocument {

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
