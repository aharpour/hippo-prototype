package com.tdclighthouse.prototype.tag;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.jsp.tagext.TagSupport;

import com.tdclighthouse.commons.utils.general.Html2Text;

public class StringChopperTag extends TagSupport {

    private static final long serialVersionUID = 1L;

    private static final String PUNCTUATION_REGEX = "([\\.\\,\\;\\:\\(\\)])[^\\.\\,\\;\\:\\(\\)]*";
    private static final int PUNCTUATION_REGEX_TARGET_GROUP = 1;

    private String suffix = "...";
    private boolean addSuffix = true;
    private String targetString;
    private int maxLength;
    private final Pattern punctuationPattern;
    private int punctuationSearchIntervalPercentage = 20;

    /*
     * <%@ attribute name="bean" required="true"
     * type="org.hippoecm.hst.content.beans.standard.HippoBean"
     * rtexprvalue="true"%> <%@ attribute name="stringPath" required="true"
     * type="java.lang.String" rtexprvalue="true"%> <%@ attribute
     * name="maxLength" required="true" type="java.lang.Integer"
     * rtexprvalue="true"%> <%@ attribute name="showReadMore" required="false"
     * type="java.lang.Boolean" rtexprvalue="true"%> <%@ attribute
     * name="showDots" required="false" type="java.lang.Boolean"
     * rtexprvalue="true"%> <%@ attribute name="allowedLengthTolerance"
     * required="false" type="java.lang.Integer" rtexprvalue="true"%>
     */
    public StringChopperTag() {
        punctuationPattern = Pattern.compile(PUNCTUATION_REGEX);
    }

    /**
     * @return the targerString
     */
    public String getTargetString() {
        return targetString;
    }

    /**
     * @param targerString
     *            the targerString to set
     */
    public void setTargetString(String targerString) {
        this.targetString = Html2Text.getText(targerString);
    }

    /**
     * @return the maxLength
     */
    public int getMaxLength() {
        return maxLength;
    }

    /**
     * @param maxLength
     *            the maxLength to set
     */
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * @return the result
     */
    public String getResult() {
        String result;
        if ((this.targetString != null) && (this.maxLength != 0)) {
            if (this.targetString.length() > this.maxLength) {
                Matcher pMatcher = this.punctuationPattern.matcher(this.targetString);
                Matcher regPMatcher = pMatcher.region(
                        Math.round(((float) this.maxLength * (100 - this.punctuationSearchIntervalPercentage)) / 100),
                        this.maxLength);
                int index = -1;
                String itemMatched = null;
                while (regPMatcher.find()) {
                    itemMatched = regPMatcher.group(PUNCTUATION_REGEX_TARGET_GROUP);
                    index = regPMatcher.start(PUNCTUATION_REGEX_TARGET_GROUP);
                }

                if (index != -1) {
                    if (")".equals(itemMatched)) {
                        ++index;
                    }
                } else {
                    index = this.targetString.lastIndexOf(' ', this.maxLength);
                    index = index == -1 ? this.maxLength : index;
                }
                if (this.addSuffix) {
                    result = this.targetString.substring(0, index) + this.suffix;
                } else {
                    result = this.targetString.substring(0, index);
                }
            } else {
                result = this.targetString;
            }
        } else {
            throw new IllegalStateException("the targetString and maxLength "
                    + "property most be set before invocation of getResult method");
        }

        return result;
    }

    /**
     * @return the punctuationSearchIntervalPercentage
     */
    public int getPunctuationSearchIntervalPercentage() {
        return punctuationSearchIntervalPercentage;
    }

    /**
     * @param punctuationSearchIntervalPercentage
     *            the punctuationSearchIntervalPercentage to set
     */
    public void setPunctuationSearchIntervalPercentage(int punctuationSearchIntervalPercentage) {
        if ((punctuationSearchIntervalPercentage <= 100) && (punctuationSearchIntervalPercentage >= 0)) {
            this.punctuationSearchIntervalPercentage = punctuationSearchIntervalPercentage;
        } else {
            throw new IllegalArgumentException("Percentage must be between 0 to 100");
        }
    }

    /**
     * @return the suffix
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * @param suffix
     *            the suffix to set
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /**
     * @return the addSuffix
     */
    public boolean isAddSuffix() {
        return addSuffix;
    }

    /**
     * @param addSuffix
     *            the addSuffix to set
     */
    public void setAddSuffix(boolean addSuffix) {
        this.addSuffix = addSuffix;
    }

}
