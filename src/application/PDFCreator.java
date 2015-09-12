package application;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDPixelMap;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

public class PDFCreator {

	public void createPDF(long totalTime, List<File> images,
			List<File> crackResults) {
		String outputFileName = "Crack_Report.pdf";
		double totalTimeinD = (double)totalTime/1000;
		// Create a document and add a page to it
		PDDocument document = new PDDocument();
		PDPage page1 = new PDPage(PDPage.PAGE_SIZE_A4);
		// PDPage.PAGE_SIZE_LETTER is also possible
		PDRectangle rect = page1.getMediaBox();
		// rect can be used to get the page width and height
		document.addPage(page1);

		// Create a new font object selecting one of the PDF base fonts
		PDFont fontPlain = PDType1Font.HELVETICA;

		// Start a new content stream which will "hold" the to be created
		// content
		PDPageContentStream cos = null;
		try {
			cos = new PDPageContentStream(document, page1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int line = 0;

		// Define a text content stream using the selected font, move the cursor
		// and draw some text
		try {
			cos.beginText();
			cos.setFont(fontPlain, 20);
			cos.moveTextPositionByAmount(100, rect.getHeight() - 50 * (++line));
			cos.drawString("Smart Road Crack Report");
			cos.endText();

			cos.beginText();
			cos.setFont(fontPlain, 15);
			cos.moveTextPositionByAmount(100, rect.getHeight() - 50 * (++line));
			cos.drawString("Total number of Image : " + images.size());
			cos.endText();

			cos.beginText();
			cos.setFont(fontPlain, 15);
			cos.moveTextPositionByAmount(100, rect.getHeight() - 50 * (++line));
			cos.drawString("Time elapsed (sec): " + totalTimeinD);
			cos.endText();

			cos.beginText();
			cos.setFont(fontPlain, 15);
			cos.moveTextPositionByAmount(100, rect.getHeight() - 50 * (++line));
			cos.drawString("Total number of cracks : " + crackResults.size());
			cos.endText();

			cos.beginText();
			cos.setFont(fontPlain, 15);
			cos.moveTextPositionByAmount(100, rect.getHeight() - 50 * (++line));
			cos.drawString("----------------Files-----------------");
			cos.endText();
			
			double yValue = 570; 
			float lastvalue = 0;
			
			for (File file : crackResults) {
				cos.beginText();
				cos.setFont(fontPlain, 11);
				cos.moveTextPositionByAmount(100, (float) (yValue - 11* (++line)));
				cos.drawString(file.getName());
				cos.endText();
				
				lastvalue = (float) (yValue - 11* (++line));
			}


			double timePerImage = totalTimeinD / images.size();
			cos.beginText();
			cos.setFont(fontPlain, 15);
			cos.moveTextPositionByAmount(100, lastvalue- 50);
			cos.drawString("Time per Image (sec): " + timePerImage);
			cos.endText();

			// Make sure that the content stream is closed:
			cos.close();

			// Save the results and ensure that the document is properly closed:
			document.save(outputFileName);
			document.close();
		} catch (IOException | COSVisitorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
