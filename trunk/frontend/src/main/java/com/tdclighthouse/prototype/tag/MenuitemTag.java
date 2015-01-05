package com.tdclighthouse.prototype.tag;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.core.sitemenu.EditableMenuItem;
import org.hippoecm.hst.util.HstRequestUtils;

import com.tdclighthouse.prototype.provider.RepoBasedMenuProvider;
import com.tdclighthouse.prototype.utils.Constants.HstParametersConstants;

public class MenuitemTag extends TagSupport {

    private static final long serialVersionUID = 1L;

    private EditableMenuItem siteMenuItem;
    private Integer depth;
    private Boolean recurseOnlyExpanded;
    private String selectedClass;
    private String expandedClass;
    private String unexpandedClass;
    private String leafClass;
    private HstRequestContext requestContext;
    @SuppressWarnings("rawtypes")
    private Map labels;

    public MenuitemTag() {
        init();
    }

    @Override
    public void release() {
        super.release();
        init();
    }

    private void init() {
        siteMenuItem = null;
        depth = 5;
        selectedClass = "selected";
        expandedClass = "expanded";
        unexpandedClass = "unexpanded";
        leafClass = "leaf";
        recurseOnlyExpanded = false;
        requestContext = null;
        labels=null;
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            JspWriter out = pageContext.getOut();
            printItemsRecursively(siteMenuItem, out, depth);

            return EVAL_PAGE;
        } catch (IOException e) {
            throw new JspException("IOException while trying to write script tag", e);
        } finally {
            init();
        }
    }

    private void printItemsRecursively(EditableMenuItem item, JspWriter out, int recursionDepth) throws IOException {
        List<EditableMenuItem> children = item.getChildMenuItems();
        int depth= recursionDepth;
        if (!RepoBasedMenuProvider.getBooleanProperty(item, HstParametersConstants.INVISIBLE)) {
            if (item.isSelected()) {
                printItem(item, out, selectedClass);
            } else if (item.isExpanded()) {
                printItem(item, out, expandedClass);
            } else if (!children.isEmpty() && depth > 0) {
                printItem(item, out, unexpandedClass);
            } else {
                printItem(item, out, leafClass);
            }
            
            depth = RepoBasedMenuProvider.getBooleanProperty(item, HstParametersConstants.DISABLED) ? depth + 1 : depth;
             
            if (!children.isEmpty() && depth > 0 && (!recurseOnlyExpanded || item.isExpanded())) {
                out.print("<ul>");
                for (EditableMenuItem child : children) {
                    printItemsRecursively(child, out, depth - 1);
                }
                out.print("</ul>");
            }
            out.print("</li>");
        }
    }

    private void printItem(EditableMenuItem item, JspWriter out, String cssClass) throws IOException {
        boolean disabled = RepoBasedMenuProvider.getBooleanProperty(item, HstParametersConstants.DISABLED);
        out.print("<li ");
        if (StringUtils.isNotBlank(cssClass)) {
            out.print("class=\"" + cssClass + "\"");
        }
        out.print(">");
        if (!disabled) {
            out.print("<a href=\"");
            out.print(getLink(item, getRequestContext(), false));
            out.print("\">");
        }
        
        if(labels!=null){            
            out.print(getLabelValue(StringEscapeUtils.escapeXml(item.getName())));
        }else{
            out.print(StringEscapeUtils.escapeXml(item.getName()));
        }
        
        if (!disabled) {
            out.print("</a>");
        }
    }
    
    private String getLabelValue(String itemName){
        String result = itemName;
        if(labels.get(itemName)!=null){
            result =  (String) labels.get(itemName);
        }
        return result;
    }

    private HstRequestContext getRequestContext() {
        if (requestContext == null) {
            HttpServletRequest servletRequest = (HttpServletRequest) pageContext.getRequest();
            requestContext = HstRequestUtils.getHstRequestContext(servletRequest);
        }
        return requestContext;
    }

    private String getLink(EditableMenuItem item, HstRequestContext requestContext, boolean fullyQualified) {
        String result = "";
        String externalLink = item.getExternalLink();
        if (StringUtils.isNotBlank(externalLink)) {
            result = externalLink;
        } else {
            HstLink hstLink = item.getHstLink();
            if (hstLink != null) {
                result = hstLink.toUrlForm(requestContext, fullyQualified);
            }

        }
        return result;
    }

    public void setSiteMenuItem(EditableMenuItem siteMenuItem) {
        this.siteMenuItem = siteMenuItem;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public void setSelectedClass(String selectedClass) {
        this.selectedClass = selectedClass;
    }

    public void setExpandedClass(String expandedClass) {
        this.expandedClass = expandedClass;
    }

    public void setUnexpandedClass(String unexpandedClass) {
        this.unexpandedClass = unexpandedClass;
    }

    public void setLeafClass(String leafClass) {
        this.leafClass = leafClass;
    }

    public void setRecurseOnlyExpanded(Boolean recurseOnlyExpanded) {
        this.recurseOnlyExpanded = recurseOnlyExpanded;
    }

    @SuppressWarnings("rawtypes")
    public void setLabels(Map labels) {
        this.labels = labels;
    }

}
