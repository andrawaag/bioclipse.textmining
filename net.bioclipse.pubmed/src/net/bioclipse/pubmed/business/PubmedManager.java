/*******************************************************************************
 * Copyright (c) 2012  Andra Waagmeester <andra.waagmeester@gmail.com>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.pubmed.business;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
 

public class PubmedManager implements IBioclipseManager {

    private static final Logger logger = Logger.getLogger(PubmedManager.class);

    /**
     * Gives a short one word name of the manager used as variable name when
     * scripting.
     */
    public String getManagerName() {
        return "pubmed";
    }
    
    public String loadPubMedEntry(int pmid, IProgressMonitor monitor) throws IOException, BioclipseException, CoreException{
    	if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		monitor.beginTask(
				"Downloading PMID: " + pmid + " from PubMed (Eutils).", 2
		);
		URL url = new URL("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id=" + Integer.toString(pmid) + "&retmode=xml");
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(
						url.openConnection().getInputStream()
				)
		);
		String line = reader.readLine();
		String pubmedString = ""; 
		while (line != null) {
			pubmedString = pubmedString + line+'\n';
			line = reader.readLine();
			
		}
    	
    	return pubmedString;
    	
    }
    
    public int loadPubMedEntryInBrowser(int pmid, IProgressMonitor monitor) throws IOException, BioclipseException, CoreException{
        
        IWebBrowser browser = PlatformUI.getWorkbench().getBrowserSupport().createBrowser(IWorkbenchBrowserSupport.LOCATION_BAR, null,"Expert Viewer", "Validation");
        browser.openURL(new URL("http://www.ncbi.nlm.nih.gov/pubmed/"+pmid));


    	return 0;
    	
    }
    
    
    public String findPubmedIdentifiers(String pubmedQuery, IProgressMonitor monitor) throws IOException, BioclipseException, CoreException{
    	if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		monitor.beginTask(
				"Querying PubMed: \"" + pubmedQuery + "\" from PubMed (Eutils).", 2
		);
		URL url = new URL("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&term=" + pubmedQuery + "&retmode=xml");
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(
						url.openConnection().getInputStream()
				)
		);
		String line = reader.readLine();
		String pubmedString = ""; 
		while (line != null) {
			pubmedString = pubmedString + line+'\n';
			line = reader.readLine();
			
		}
    	
    	return pubmedString;
    	
    }
    
}
