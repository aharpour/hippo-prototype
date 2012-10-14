package com.tdclighthouse.prototype.beans.compounds;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

@Node(jcrType = VideoBean.JCR_TYPE)
public class VideoBean extends HippoDocument {

	public static final String JCR_TYPE = "tdc:Video";

	private String title;
	private String alt;
	private HippoHtml transcript;
	private VideoImageBean image;
	private VideoItemBean wmv;
	private VideoItemBean srt;
	private VideoItemBean mp4;
	private VideoItemBean flv;
	private VideoItemBean threeGp;
	private VideoItemBean audio;

	public String getTitle() {
		if (this.title == null) {
			this.title = getProperty("tdc:title");
		}
		return title;
	}

	public String getAlt() {
		if (this.alt == null) {
			this.alt = getProperty("tdc:alt");
		}
		return alt;
	}

	public HippoHtml getTranscript() {
		if (this.transcript == null) {
			this.transcript = getHippoHtml("tdc:transcript");
		}
		return transcript;
	}

	public VideoImageBean getImage() {
		if (this.image == null) {
			this.image = getBean("tdc:image");
		}
		return image;
	}

	public VideoItemBean getWmv() {
		if (this.wmv == null) {
			this.wmv = getBean("tdc:wmv");
		}
		return wmv;
	}

	public VideoItemBean getSrt() {
		if (this.srt == null) {
			this.srt = getBean("tdc:srt");
		}
		return srt;
	}

	public VideoItemBean getMp4() {
		if (this.mp4 == null) {
			this.mp4 = getBean("tdc:mp4");
		}
		return mp4;
	}

	public VideoItemBean getFlv() {
		if (this.flv == null) {
			this.flv = getBean("tdc:flv");
		}
		return flv;
	}

	public VideoItemBean getThreeGp() {
		if (this.threeGp == null) {
			this.threeGp = getBean("tdc:3gp");
		}
		return threeGp;
	}

	public VideoItemBean getAudio() {
		if (this.audio == null) {
			this.audio = getBean("tdc:audio");
		}
		return audio;
	}

}