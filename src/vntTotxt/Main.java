package vntTotxt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {

	final static Logger log = Logger.getLogger(""); // Logger to help with
													// debugging and providing
													// information to the user.
	static Handler ch = new ConsoleHandler();

	public static void main(String[] args) {
		LogManager.getLogManager().reset();
		ch.setLevel(Level.ALL);
		log.addHandler(ch);
		log.setLevel(Level.ALL); // Initialise logger and handler

		Scanner scanner = new Scanner(System.in);

		log.fine("Scanner on");

		System.out.println("Path pls");

		String path = scanner.nextLine();

		log.finer("Path input - path is " + path);

		scanner.close();

		File folder = new File(path);

		File[] folderFiles = folder.listFiles(); // Get all files

		log.fine("Got list of files.");

		File[] vntFiles; // Array for vnt files.
		int array = 0;
		vntFiles = new File[100];
		String extension = "";

		for (File file : folderFiles) {
			if (!file.isDirectory())
			{
				log.finest("Currently processing file: " + file.getName());
				int i = file.getName().lastIndexOf('.');
				if (i != -1) {
					extension = file.getName().substring(i + 1);
					log.finer("File has extension: " + extension);
				}
				if (extension.equals("vnt")) {
					vntFiles[array] = file;
					array++;
					log.finer("File " + file.getName() + " has correct extension.");
				}				
			}
		}

		if (vntFiles[0] == null) {
			log.severe("No suitable files. Quitting.");
			System.exit(0);
		}

		new File(folder + "/converted/").mkdir();

		log.fine("Created new directory (if necessary)");

		for (File file : vntFiles) {
			if (file == null)
			{
				break;
			}
			try (BufferedReader in = new BufferedReader(new FileReader(file))) {
				in.readLine();
				in.readLine();
				String data = in.readLine();

				log.finest("Got vnt data.");

				data = data.substring(45);

				String[] paras = data.split("=0A");

				log.finest("Split new lines and removed crap.");

				File newFile = new File(folder
						+ "/converted/"
						+ file.getName()
								.substring(0, (int) (file.getName().length() - 4))
						+ ".txt");

				try (BufferedWriter out = new BufferedWriter(new FileWriter(
						newFile))) {
					for (String string : paras) {
						out.write(string);
						out.newLine();
						log.finest("Written line to file.");
					}
					out.close();
					log.finest("Closed writer.");
				}

				in.close();
			} catch (IOException x) {
				x.printStackTrace();
			}
			log.info("Conversion complete.");
		}

	}
}
