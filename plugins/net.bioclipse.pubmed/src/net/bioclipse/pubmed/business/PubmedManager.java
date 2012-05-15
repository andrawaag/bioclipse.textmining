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
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;




public class PubmedManager implements IBioclipseManager {

	private static final Logger logger = Logger.getLogger(PubmedManager.class);

	/**
	 * Gives a short one word name of the manager used as variable name when
	 * scripting.
	 */
	public String getManagerName() {
		return "pubmed";
	}

	public Document getPubmedEntryasDocument(int pmid) throws ParserConfigurationException, SAXException, IOException{
		URL url = new URL("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id=" + Integer.toString(pmid) + "&retmode=xml");
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(url.openStream());
		return doc;
	}

	public String getPubMedEntry(int pmid, IProgressMonitor monitor) throws IOException, BioclipseException, CoreException{
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


	public String getAbstract(int pmid, IProgressMonitor monitor) throws IOException, BioclipseException, CoreException, ParserConfigurationException, SAXException{
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		monitor.beginTask(
				"Downloading PMID: " + pmid + " from PubMed (Eutils).", 2
		);
		Document doc = this.getPubmedEntryasDocument(pmid);
		NodeList pubmedAbstractList = doc.getElementsByTagName("AbstractText");
		String pubmedAbstract = "";
		if (pubmedAbstractList.getLength() > 0){

			for (int i=0; i<pubmedAbstractList.getLength(); i++){
				pubmedAbstract = pubmedAbstract + pubmedAbstractList.item(i).getTextContent();
			}
		}

		return pubmedAbstract;

	}

	public String getTitle(int pmid, IProgressMonitor monitor) throws IOException, BioclipseException, CoreException, ParserConfigurationException, SAXException{
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		monitor.beginTask(
				"Downloading PMID: " + pmid + " from PubMed (Eutils).", 2
		);
		Document doc = this.getPubmedEntryasDocument(pmid);
		NodeList pubmedTitleList = doc.getElementsByTagName("ArticleTitle");
		String pubmedTitle = "";
		if (pubmedTitleList.getLength() > 0){

			for (int i=0; i<pubmedTitleList.getLength(); i++){
				pubmedTitle = pubmedTitle + pubmedTitleList.item(i).getTextContent();
			}
		}

		return pubmedTitle;

	}

	public String getJournalTitle(int pmid, IProgressMonitor monitor) throws IOException, BioclipseException, CoreException, ParserConfigurationException, SAXException{
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		monitor.beginTask(
				"Downloading PMID: " + pmid + " from PubMed (Eutils).", 2
		);
		Document doc = this.getPubmedEntryasDocument(pmid);
		NodeList pubmedJournalList = doc.getElementsByTagName("Title");
		String pubmedTitle = "";
		if (pubmedJournalList.getLength() > 0){

			for (int i=0; i<pubmedJournalList.getLength(); i++){
				pubmedTitle = pubmedTitle + pubmedJournalList.item(i).getTextContent();
			}
		}

		return pubmedTitle;

	}


	public String getFullTextLink(int pmid, IProgressMonitor monitor) throws IOException, BioclipseException, CoreException, ParserConfigurationException, SAXException{
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		monitor.beginTask(
				"Searching full text link " + pmid + " from PubMed (Eutils).", 2
		);
		URL url = new URL("http://www.ncbi.nlm.nih.gov/entrez/eutils/elink.fcgi?dbfrom=pubmed&id=" + Integer.toString(pmid) + "&retmode=xml&cmd=prlinks");


		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(url.openStream());
		NodeList urlList = doc.getElementsByTagName("Url");
		String returnUrl = urlList.item(0).getTextContent();
		return returnUrl;

	}


	public int loadPubMedEntryInBrowser(int pmid, IProgressMonitor monitor) throws IOException, BioclipseException, CoreException{

		IWebBrowser browser = PlatformUI.getWorkbench().getBrowserSupport().createBrowser(IWorkbenchBrowserSupport.AS_EDITOR, null,"Expert Viewer", "Validation");
		browser.openURL(new URL("http://www.ncbi.nlm.nih.gov/pubmed/"+pmid));


		return 0;

	}


	public String findPubmedIdentifiers(String pubmedQuery, int retMax, int retStart, IProgressMonitor monitor) throws IOException, BioclipseException, CoreException{


		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		monitor.beginTask(
				"Querying PubMed: \"" + pubmedQuery + "\" from PubMed (Eutils).", 2
		);
		URL url = new URL("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&term=" + pubmedQuery + "&retmode=xml&retmax="+Integer.toString(retMax)+"&retstart="+Integer.toString(retStart));
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

	public String findPubmedIdentifiers(String pubmedQuery, int retMax, IProgressMonitor monitor) throws IOException, BioclipseException, CoreException{


		String pubmedString = this.findPubmedIdentifiers(pubmedQuery, retMax, 0, monitor);

		return pubmedString;

	}

	public String findPubmedIdentifiers(String pubmedQuery, IProgressMonitor monitor) throws IOException, BioclipseException, CoreException{


		String pubmedString = this.findPubmedIdentifiers(pubmedQuery, 20, monitor);

		return pubmedString;

	}



	public String getMeshHeadings(int pmid, IProgressMonitor monitor) throws IOException, BioclipseException, CoreException, ParserConfigurationException, SAXException{
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		monitor.beginTask(
				"Downloading PMID: " + pmid + " from PubMed (Eutils).", 2
		);
		Document doc = this.getPubmedEntryasDocument(pmid);
		NodeList pubmedMeshList = doc.getElementsByTagName("DescriptorName");
		String pubmedMesh = "";
		if (pubmedMeshList.getLength() > 0){

			for (int i=0; i<pubmedMeshList.getLength(); i++){
				pubmedMesh = pubmedMesh + pubmedMeshList.item(i).getTextContent()+", ";
			}
		}

		return pubmedMesh;

	}

	public String getJournalAbbrevation(int pmid, IProgressMonitor monitor) throws IOException, BioclipseException, CoreException, ParserConfigurationException, SAXException{
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		monitor.beginTask(
				"Downloading PMID: " + pmid + " from PubMed (Eutils).", 2
		);
		Document doc = this.getPubmedEntryasDocument(pmid);
		NodeList isoAbbreviationList = doc.getElementsByTagName("ISOAbbreviation");
		String isoAbbreviation = "";
		if (isoAbbreviationList.getLength() > 0){

			for (int i=0; i<isoAbbreviationList.getLength(); i++){
				isoAbbreviation = isoAbbreviation + isoAbbreviationList.item(i).getTextContent();
			}
		}

		return isoAbbreviation;

	}

	public String getChemicals(int pmid, IProgressMonitor monitor) throws IOException, BioclipseException, CoreException, ParserConfigurationException, SAXException{
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		monitor.beginTask(
				"Downloading PMID: " + pmid + " from PubMed (Eutils).", 2
		);
		Document doc = this.getPubmedEntryasDocument(pmid);
		NodeList chemicalList = doc.getElementsByTagName("Chemical");
		String chemicals = "";
		if (chemicalList.getLength() > 0){

			for (int i=0; i<chemicalList.getLength(); i++){
				Element chemicalElement = (Element) chemicalList.item(i);

				chemicals = chemicals + chemicalElement.getElementsByTagName("NameOfSubstance").item(0).getTextContent()+": "+chemicalElement.getElementsByTagName("RegistryNumber").item(0).getTextContent()+"\n";
			}
		}

		return chemicals;

	}

	public String getAffiliation(int pmid, IProgressMonitor monitor) throws IOException, BioclipseException, CoreException, ParserConfigurationException, SAXException{
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		monitor.beginTask(
				"Downloading PMID: " + pmid + " from PubMed (Eutils).", 2
		);
		Document doc = this.getPubmedEntryasDocument(pmid);
		NodeList affiliationList = doc.getElementsByTagName("Affiliation");
		String affiliation = "NA";
		if (affiliationList.getLength() > 0){
			affiliation = "";
			for (int i=0; i<affiliationList.getLength(); i++){
				affiliation = affiliation + affiliationList.item(i).getTextContent();
			}
		}

		return affiliation;
	}

	public String getArticleIdentifiers(int pmid, IProgressMonitor monitor) throws IOException, BioclipseException, CoreException, ParserConfigurationException, SAXException{
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		monitor.beginTask(
				"Downloading PMID: " + pmid + " from PubMed (Eutils).", 2
		);
		Document doc = this.getPubmedEntryasDocument(pmid);
		NodeList docIdList = doc.getElementsByTagName("ArticleId");
		String docIds = "";
		if (docIdList.getLength() > 0){

			for (int i=0; i<docIdList.getLength(); i++){			
				docIds = docIds + docIdList.item(i).getAttributes().getNamedItem("IdType").getTextContent()+": "+docIdList.item(i).getTextContent()+"\n";
			}
		}

		return docIds;

	}

	public String getDOI(int pmid, IProgressMonitor monitor) throws IOException, BioclipseException, CoreException, ParserConfigurationException, SAXException{
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		monitor.beginTask(
				"Downloading PMID: " + pmid + " from PubMed (Eutils).", 2
		);
		Document doc = this.getPubmedEntryasDocument(pmid);
		NodeList docIdList = doc.getElementsByTagName("ArticleId");
		String docIds = "";
		if (docIdList.getLength() > 0){

			for (int i=0; i<docIdList.getLength(); i++){	
				if (docIdList.item(i).getAttributes().getNamedItem("IdType").getTextContent().equals("doi")) {
					docIds = docIds + docIdList.item(i).getTextContent()+"\n";
				}
			}
		}

		return docIds;

	}


}
