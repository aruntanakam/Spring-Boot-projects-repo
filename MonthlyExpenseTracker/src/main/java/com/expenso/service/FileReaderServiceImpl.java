package com.expenso.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.expenso.Iservice.IFileReaderService;
import com.expenso.exception.InputFileReadException;

@Service
public class FileReaderServiceImpl implements IFileReaderService {

	private static Logger log = LoggerFactory.getLogger(FileReaderServiceImpl.class);

	@Override
	public List<String> readFile(String inputpath) {

		log.info("Entered readFile method to read the input file");

		try {
			List<String> list = Files.readAllLines(Paths.get(inputpath));
			log.info("reading input file is completed");
			return list;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("Exception occured while reading input file:" + e.getMessage());
			throw new InputFileReadException();
		}

	}

}
