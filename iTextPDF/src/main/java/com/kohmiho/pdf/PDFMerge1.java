package com.kohmiho.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;

public class PDFMerge1 {

	final static Logger logger = Logger.getLogger(PDFMerge1.class);

	public static final String SOURCE_FOLDER = "PDFSource";

	public static final String RESULT_FOLDER = "PDFResult";

	public static final String RESULT_FILE = "result.pdf";

	public static void main(String[] args) throws IOException, DocumentException {

		logger.info("Start PDFMerge1 main");

		logger.debug("open source folder");
		File sourceFolder = new File(SOURCE_FOLDER);

		logger.debug("start get PDF folder or file");
		File[] files = sourceFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
		logger.debug("end get PDF folder or file");

		logger.debug("start filter PDF file only and sort by file name");
		List<String> sources = Arrays.stream(files).filter(file -> file.isFile()).map(file -> file.getName()).sorted()
				.collect(Collectors.toList());
		logger.debug("end filter PDF file only and sort by file name");

		logger.info(String.format("Find %s PDF files", sources.size()));

		if (sources.size() == 0) {
			logger.info("These is no PDF files in souce folder");
			logger.info("Exit PDFMerge1 main");
			return;
		}

		File resultFolder = new File(RESULT_FOLDER);
		if (!resultFolder.exists()) {
			logger.info("Create result folder");
			resultFolder.mkdir();
		}

		Document document = new Document();
		PdfCopy copy = new PdfSmartCopy(document, new FileOutputStream(RESULT_FOLDER + "/" + RESULT_FILE));
		document.open();

		for (String source : sources) {

			PdfReader reader;
			try {
				reader = new PdfReader(SOURCE_FOLDER + "/" + source);
			} catch (Exception e) {
				logger.error("Can not open PDF file " + source);
				continue;
			}

			if (null != reader) {

				int n = reader.getNumberOfPages();

				logger.info(String.format("%s has %s pages", source, n));

				for (int i = 0; i < n;) {

					logger.debug(String.format("Copy #%s page", i + 1));

					copy.addPage(copy.getImportedPage(reader, ++i));
				}

				reader.close();
			}

		}

		document.close();

		logger.info("Exit PDFMerge1 main");

		System.exit(0);
	}

}
