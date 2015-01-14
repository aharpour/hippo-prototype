package org.hippoecm.hst.site;

import org.hippoecm.hst.core.container.ComponentManager;
import org.hippoecm.hst.mock.core.container.MockComponentManager;

public class HstServices {

    public static ComponentManager getComponentManager() {
        return new MockComponentManager();
    }
}
