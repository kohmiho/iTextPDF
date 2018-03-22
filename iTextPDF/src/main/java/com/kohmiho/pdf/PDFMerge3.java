package com.kohmiho.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;

public class PDFMerge3 {

	public static final String SHARED_DRIVE = "/run/media/oracle/Seagate Backup Plus Drive";

	public static final String WORK_FOLDER = "TempForDelete";

	public static final String RESULT_FOLDER = "Result";

	public static final String RESULT_FILE = "result.pdf";

	public static void main(String[] args) throws IOException, DocumentException {

		File sourceFolder = new File(SHARED_DRIVE + "/" + WORK_FOLDER);
		File[] files = sourceFolder.listFiles();
		List<String> sources = Arrays.stream(files).filter(file -> file.isFile()).map(file -> file.getAbsolutePath())
				.sorted().collect(Collectors.toList());

		String resultFolderPath = SHARED_DRIVE + "/" + WORK_FOLDER + "/" + RESULT_FOLDER;
		File resultFolder = new File(resultFolderPath);
		resultFolder.mkdir();

		Document document = new Document();
		PdfCopy copy = new PdfSmartCopy(document, new FileOutputStream(resultFolderPath +"/"+ RESULT_FILE));
		document.open();

		for (String sourcePath : sources) {

			System.out.println(sourcePath);

			PdfReader reader = new PdfReader(sourcePath);

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
