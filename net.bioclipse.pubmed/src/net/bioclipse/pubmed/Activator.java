/*******************************************************************************
 * Copyright (c) 2012  Your Name <you@example.com>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.pubmed;

import net.bioclipse.pubmed.business.IPubmedManager;
import net.bioclipse.pubmed.business.IJavaPubmedManager;
import net.bioclipse.pubmed.business.IJavaScriptPubmedManager;

import org.apache.log4j.Logger;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The Activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    private static final Logger logger = Logger.getLogger(Activator.class);

    // The shared instance
    private static Activator plugin;

    // Trackers for getting the managers
    private ServiceTracker javaFinderTracker;
    private ServiceTracker jsFinderTracker;

    public Activator() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        javaFinderTracker
            = new ServiceTracker( context,
                                  IJavaPubmedManager.class.getName(),
                                  null );

        javaFinderTracker.open();
        jsFinderTracker
            = new ServiceTracker( context,
                                  IJavaScriptPubmedManager.class.getName(),
                                  null );

        jsFinderTracker.open();
    }

    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static Activator getDefault() {
        return plugin;
    }

    public IPubmedManager getJavaPubmedManager() {
        IPubmedManager manager = null;
        try {
            manager = (IPubmedManager)
                      javaFinderTracker.waitForService(1000*10);
        }
        catch (InterruptedException e) {
            throw new IllegalStateException(
                          "Could not get the Java PubmedManager",
                          e );
        }
        if (manager == null) {
            throw new IllegalStateException(
                          "Could not get the Java PubmedManager");
        }
        return manager;
    }

    public IJavaScriptPubmedManager getJavaScriptPubmedManager() {
        IJavaScriptPubmedManager manager = null;
        try {
            manager = (IJavaScriptPubmedManager)
                      jsFinderTracker.waitForService(1000*10);
        }
        catch (InterruptedException e) {
            throw new IllegalStateException(
                          "Could not get the JavaScript PubmedManager",
                          e );
        }
        if (manager == null) {
            throw new IllegalStateException(
                          "Could not get the JavaScript PubmedManager");
        }
        return manager;
    }
}
