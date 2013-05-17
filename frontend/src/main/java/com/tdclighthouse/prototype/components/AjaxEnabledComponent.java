package com.tdclighthouse.prototype.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXB;

import org.apache.commons.collections.ListUtils;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdclighthouse.prototype.utils.Constants.Attributes;
import com.tdclighthouse.prototype.utils.Constants.MimeType;
import com.tdclighthouse.prototype.utils.MIMEParse;

public abstract class AjaxEnabledComponent<M> extends BaseTdcComponent {

	public static final String BLANK_TEMPLATE = "prototype.blanktemplate";

	public abstract M getModel(HstRequest request, HstResponse response) throws HstComponentException;

	@Override
	public final void doBeforeRender(HstRequest request, HstResponse response) throws HstComponentException {
		request.setAttribute(Attributes.MODEL, getModel(request, response));
	}

	@Override
	public final void doBeforeServeResource(HstRequest request, HstResponse response) throws HstComponentException {
		try {
			String acceptHeader = request.getHeader("Accept");
			String bestMatch = MIMEParse.bestMatch(ECPECTED_MIME_TYPES, acceptHeader);
			if (MimeType.APPLICATION_JSON.equals(bestMatch) || MimeType.TEXT_JAVASCRIPT.equals(bestMatch)) {
				returnJsonResponse(request, response);
			} else if (MimeType.APPLICATION_XML.equals(bestMatch) || MimeType.TEXT_XML.equals(bestMatch)) {
				returnXmlResponse(request, response);
			} else {
				doBeforeRender(request, response);
			}
		} catch (JsonGenerationException e) {
			throw new HstComponentException(e.getLocalizedMessage(), e);
		} catch (JsonMappingException e) {
			throw new HstComponentException(e.getLocalizedMessage(), e);
		} catch (IOException e) {
			throw new HstComponentException(e.getLocalizedMessage(), e);
		}

	}

	private void returnXmlResponse(HstRequest request, HstResponse response) throws IOException {
		M model = getModel(request, response);
		response.setServeResourcePath(BLANK_TEMPLATE);
		response.setContentType(MimeType.APPLICATION_XML);
		response.setCharacterEncoding(getCharacterEndcoding());
		JAXB.marshal(model, response.getOutputStream());
	}

	private void returnJsonResponse(HstRequest request, HstResponse response) throws IOException,
			JsonGenerationException, JsonMappingException {
		M model = getModel(request, response);
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), model);
		response.setServeResourcePath(BLANK_TEMPLATE);
		response.setContentType(MimeType.APPLICATION_JSON);
		response.setCharacterEncoding(getCharacterEndcoding());
	}

	protected String getCharacterEndcoding() {
		return "UTF-8";
	}

	@SuppressWarnings("unchecked")
	private final static List<String> ECPECTED_MIME_TYPES = ListUtils.unmodifiableList(new ArrayList<String>() {
		private static final long serialVersionUID = 1L;

		{
			add(MimeType.TEXT_HTML);
			add(MimeType.APPLICATION_XHTML_XML);
			add(MimeType.APPLICATION_JSON);
			add(MimeType.TEXT_JAVASCRIPT);
			add(MimeType.APPLICATION_XML);
			add(MimeType.TEXT_XML);
		}
	});

}
