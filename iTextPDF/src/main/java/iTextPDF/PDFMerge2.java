package iTextPDF;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;

public class PDFMerge2 {

	public static final String FOLDER = "/run/media/oracle/Seagate Backup Plus Drive/TempForDelete/";

	public static final String RESULT = "USO-1065-2017-result.pdf";

	public static final String[] SOURCES = { "USO-1065-dd2017 (1).pdf", "USO-1065-2017 (2).pdf",
			"USO-1065-2017 (3).pdf", "USO-1065-2017 (4).pdf", "USO-1065-2017 (5).pdf", "USO-1065-2017 (6).pdf",
			"USO-1065-2017 (7).pdf" };

	public static void main(String[] args) throws IOException, DocumentException {

		Document document = new Document();
		PdfCopy copy = new PdfCopy(document, new FileOutputStream(FOLDER + RESULT));
		document.open();

		for (String flieName : SOURCES) {
			PdfReader reader = new PdfReader(FOLDER + flieName);
			// reader.selectPages("4-8");

			int n = reader.getNumberOfPages();

			for (int i = 0; i < n;) {
				copy.addPage(copy.getImportedPage(reader, ++i));
			}

			reader.close();
		}

		document.close();

		System.exit(0);
	}

}
