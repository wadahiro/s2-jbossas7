package org.seasar.framework.container.servlet;

import javax.servlet.annotation.WebServlet;

import org.seasar.framework.util.ResourcesUtil;
import org.seasar.framework.util.VFSResourcesFactory;

/**
 * S2ContainerServlet for JBossAS 7.
 * 
 * @author wadahiro
 */
@WebServlet(name = "s2container", loadOnStartup = 2, urlPatterns = "/s2container")
public class S2ContainerServletOnJBossAS7 extends S2ContainerServlet {

    private static final long serialVersionUID = -2057191189997182603L;

    @Override
    public void init() {
        ResourcesUtil.addResourcesFactory("vfs", new VFSResourcesFactory());
        super.init();
    }
}
