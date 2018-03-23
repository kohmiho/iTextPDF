package com.kohmiho.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;

public class PDFMerge1 {

	private static final String KEY_RESULT_FILE_NAME = "result.file.name";

	private static final String KEY_RESULT_FOLDER_NAME = "result.folder.name";

	private static final String KEY_SOURCE_FOLDER_NAME = "source.folder.name";

	private static final String SOURCE_FOLDER_NAME_DEFAULT_VALUE = "PDFSource";

	private static final String RESULT_FOLDER_NAME_DEFAULT_VALUE = "PDFResult";

	private static final String RESULT_FILE_NAME_DEFAULT_VALUE = "result.pdf";

	final static Logger logger = Logger.getLogger(PDFMerge1.class);

	public static void main(String[] args) throws IOException, DocumentException {

		logger.info("Start PDFMerge1 main");

		Properties appConfig = new Properties();

		logger.debug("start loadAppConfig(appConfig)");
		loadAppConfig(appConfig);
		logger.debug("end loadAppConfig(appConfig)");

		logger.debug("start open source folder");
		String sourceFolderName = appConfig.getProperty(KEY_SOURCE_FOLDER_NAME, SOURCE_FOLDER_NAME_DEFAULT_VALUE);
		File sourceFolder = new File(sourceFolderName);
		logger.debug("end open source folder");

		logger.debug("start get PDF folder or file");
		File[] files = sourceFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
		logger.debug("end get PDF folder or file: " + files);
		
		if(null == files) {
			logger.info("These is no PDF files in souce folder");
			logger.info("Exit PDFMerge1 main");
			return;
		}

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

		String resultFileName = appConfig.getProperty(KEY_RESULT_FILE_NAME, RESULT_FILE_NAME_DEFAULT_VALUE);
		String resultFolderName = appConfig.getProperty(KEY_RESULT_FOLDER_NAME, RESULT_FOLDER_NAME_DEFAULT_VALUE);

		File resultFolder = new File(resultFolderName);
		if (!resultFolder.exists()) {
			logger.info("Create result folder");
			resultFolder.mkdir();
		}

		Document document = new Document();
		PdfCopy copy = new PdfSmartCopy(document, new FileOutputStream(resultFolderName + "/" + resultFileName));
		document.open();

		for (String source : sources) {

			PdfReader reader;
			try {
				reader = new PdfReader(sourceFolderName + "/" + source);
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

	private static void loadAppConfig(Properties appConfig) {
		logger.info("Start load config from PDFMerger.properties");
		try {
			appConfig.load(new FileInputStream("PDFMerger.properties"));
		} catch (Exception e) {
			logger.info("Failed to load config from PDFMerger.properties");
		}
		logger.info("End load config from PDFMerger.properties");
	}

}
