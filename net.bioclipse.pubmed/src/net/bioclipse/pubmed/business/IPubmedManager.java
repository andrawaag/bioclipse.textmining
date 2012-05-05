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

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import net.bioclipse.core.PublishedClass;
import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.Recorded;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;

@PublishedClass(
    value="Manager for calling Pubmed through Eutils: "
)
public interface IPubmedManager extends IBioclipseManager {

	  @Recorded
	  @PublishedMethod(
	        params = "int pmid", 
	        methodSummary = "Loads the PubMed entry from a given pmid as XML"
	        	
	    )
	public String loadPubMedEntry(int pmid)
    throws IOException, BioclipseException, CoreException;
	  
	  @Recorded
	  @PublishedMethod(
	        params = "int pmid", 
	        methodSummary = "Get full text link if available"
	        	
	    )
	public String getFullTextLink(int pmid)
    throws IOException, BioclipseException, CoreException;
	  
	  @Recorded
	  @PublishedMethod(
	        params = "int pmid", 
	        methodSummary = "Loads the PubMed entry from a given pmid as XML"
	        	
	    )
	public int loadPubMedEntryInBrowser(int pmid)
    throws IOException, BioclipseException, CoreException;
	  
	  @Recorded
	  @PublishedMethod(
	        params = "String pubmedQuery", 
	        methodSummary = "Returns the pubmed Identifiers returned on a pubmed query"
	        	
	    )
    public String findPubmedIdentifiers(String pubmedQuery)
	throws IOException, BioclipseException, CoreException;

}
