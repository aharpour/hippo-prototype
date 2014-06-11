package com.tdclighthouse.prototype.tag;

import static com.tdclighthouse.prototype.tag.TagUtils.hippoHtmlToString;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;

import com.tdclighthouse.prototype.beans.TdcImageSetBean;
import com.tdclighthouse.prototype.beans.compounds.ParagraphBean;
import com.tdclighthouse.prototype.utils.FreemarkerUtils;

import freemarker.template.Template;
import freemarker.template.TemplateException;

public class ParagraphBlockTagV2 extends PrototypeTagSupport {

	private static final String TEMPLATE_PATH = "/com/tdclighthouse/prototype/tag/paragraphblock.ftl";
	private static final String LEFT = "left";
	private static final String RIGHT = "right";
	private static final Template template = FreemarkerUtils.getTemplate(TEMPLATE_PATH,  ParagraphBlockTag.class);

	private static final long serialVersionUID = 1L;

	private ParagraphBean content;

	public void setContent(ParagraphBean content) {
		this.content = content;
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			renderParagraph(content, out, getHstRequest(), getHstResponse());
			return EVAL_PAGE;
		} catch (IOException e) {
			throw new JspException(e.getMessage(), e);
		} catch (TemplateException e) {
			throw new JspException(e.getMessage(), e);
		} finally {
			reset();
		}

	}

	private static void renderParagraph(ParagraphBean content, JspWriter out, HstRequest hstRequest,
			HstResponse hstResponse) throws IOException, JspException, TemplateException {
		HashMap<String, Object> model = new HashMap<String, Object>();
		model.put("content", content);
		model.put("image", getImage(content, hstRequest, hstResponse));
		model.put("html", getHtmlContent(content, hstRequest));
		template.process(model, out);
	}

	private static Image getImage(ParagraphBean content, HstRequest hstRequest, HstResponse hstResponse) {
		Image result = null;
		if (content.getImage() != null && content.getImage().getLinkBean() != null) {
			result = new Image(content, hstRequest, hstResponse);
		}
		return result;
	}

	public static class Image {
		private final String link;
		private final String title;
		private final String alt;
		private final String caption;
		private final String horizontalPosition;

		public Image(ParagraphBean content, HstRequest hstRequest, HstResponse hstResponse) {
			HippoBean imageBean = content.getImage().getLinkBean();
			if (imageBean instanceof TdcImageSetBean) {
				imageBean = ((TdcImageSetBean) imageBean).getParagraphImage();
			}
			link = TagUtils.getLink(imageBean, hstRequest.getRequestContext(), false);
			title = content.getImage().getTitle();
			alt = content.getImage().getAlt();
			caption = content.getImage().getCaption();
			horizontalPosition = gethorizontalPosition(content);
		}

		public String getLink() {
			return link;
		}

		public String getTitle() {
			return title;
		}

		public String getAlt() {
			return alt;
		}

		public String getCaption() {
			return caption;
		}

		public String getHorizontalPosition() {
			return horizontalPosition;
		}

	}

	private static String gethorizontalPosition(ParagraphBean content) {
		String result = LEFT;
		if (content.getImage() != null && RIGHT.equals(content.getImage().getHorizontalPosition())) {
			result = RIGHT;
		}
		return result;
	}

	private static String getHtmlContent(ParagraphBean content, HstRequest hstRequest)
			throws JspException {
		String result = null;
		if (content.getContent() != null && StringUtils.isNotBlank(content.getContent().getContent())) {
			result = hippoHtmlToString(content.getContent(), false, hstRequest);
		}
		return result;
	}

}
