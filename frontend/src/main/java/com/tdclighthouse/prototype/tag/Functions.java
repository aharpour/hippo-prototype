package com.tdclighthouse.prototype.tag;

import org.hippoecm.hst.content.beans.standard.HippoBean;

import com.tdclighthouse.prototype.beans.Page;

public class Functions {

    private Functions() {
    }

    public static boolean isSubclassOfWebdocument(HippoBean hippoBean) {
        boolean result = false;
        if (hippoBean instanceof Page) {
            result = true;
        }
        return result;
    }

}
