package org.seasar.framework.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.jar.JarFile;

import org.jboss.vfs.VFS;
import org.jboss.vfs.VFSUtils;
import org.jboss.vfs.VirtualFile;
import org.seasar.framework.container.servlet.S2ContainerServletOnJBossAS7;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ResourcesUtil.FileSystemResources;
import org.seasar.framework.util.ResourcesUtil.JarFileResources;
import org.seasar.framework.util.ResourcesUtil.Resources;
import org.seasar.framework.util.ResourcesUtil.ResourcesFactory;

/**
 * ResourcesFactory for JBossAS 7 vfs.
 * 
 * @author wadahiro
 */
public class VFSResourcesFactory implements ResourcesFactory {

    private static final Logger logger = Logger
            .getLogger(S2ContainerServletOnJBossAS7.class);

    public Resources create(final URL url, final String rootPackage,
            final String rootDir) {

        debugArgs(url, rootPackage, rootDir);

        try {
            // convert vfs to file protocol
            VirtualFile vf = VFS.getChild(url.toURI());
            URI physicalURI = VFSUtils.getPhysicalURI(vf);

            debugPhysicalURI(physicalURI);

            if (url.getPath().toLowerCase().endsWith(".jar/")) {
                return handleJar(physicalURI, rootPackage, rootDir);
            } else {
                return handleDir(physicalURI, rootPackage, rootDir);
            }
        } catch (Exception e) {
            throw new RuntimeException("create resource error. url:" + url
                    + ", rootPackage:" + rootPackage + ", rootDir:" + rootDir,
                    e);
        }
    }

    private Resources handleDir(URI physicalURI, final String rootPackage,
            final String rootDir) throws MalformedURLException {
        return new FileSystemResources(ResourcesUtil.getBaseDir(
                physicalURI.toURL(), rootDir), rootPackage, rootDir);
    }

    private Resources handleJar(URI physicalURI, final String rootPackage,
            final String rootDir) throws IOException {
        File f = new File(physicalURI);
        File[] jar = f.getParentFile().listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.getName().toLowerCase().endsWith(".jar");
            }
        });

        debugJarPath(jar);

        JarFile jarFile = new JarFile(jar[0]);

        return new JarFileResources(jarFile, rootPackage, rootDir);
    }

    private void debugPhysicalURI(URI physicalURI) {
        if (logger.isDebugEnabled()) {
            logger.debug("Converted physicalURI:" + physicalURI);
        }
    }

    private void debugJarPath(File[] jarFile) {
        if (logger.isDebugEnabled()) {
            for (File file : jarFile) {
                logger.debug("handle jar. path:" + file.getAbsolutePath());
            }
        }
    }

    private void debugArgs(URL url, String rootPackage, String rootDir) {
        if (logger.isDebugEnabled()) {
            logger.debug("create resource: dump args...");
            logger.debug("--> url:" + url);
            logger.debug("--> rootPackage:" + rootPackage);
            logger.debug("--> rootDir:" + rootPackage);
        }
    }

}