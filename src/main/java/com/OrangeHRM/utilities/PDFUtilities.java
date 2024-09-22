package com.OrangeHRM.utilities;

import java.io.File;
import java.io.IOException;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.testng.Assert;

public class PDFUtilities {
	
	PDDocument pdDocument;
	PDFTextStripper stripper;

	public PDFUtilities(File filePath) {
		try {
			pdDocument = PDDocument.load(filePath);
			stripper = new PDFTextStripper();
		}
		catch(Exception e) {
			Assert.fail(e.toString());
		}
	}
	
	public String readPdfAsString() {
		String pdfContents = null;
		
		try {
			pdfContents = stripper.getText(pdDocument);
			pdDocument.close();
		}
		catch(IOException e) {
			Assert.fail(e.toString());
		}
		return pdfContents;
	}
	
	
	/*
	 * public static void main(String[] args) { File file = new
	 * File("C:/Users/ADMIN/Downloads/resume.pdf"); PDFUtilities pdfUtilities = new
	 * PDFUtilities(file); String pdfdata = pdfUtilities.readPdfAsString();
	 * System.out.println(pdfdata);
	 * Assert.assertTrue(pdfdata.contains("Project Manager Resume"),
	 * "Downloaded PDF does not contains value"); }
	 */
}
