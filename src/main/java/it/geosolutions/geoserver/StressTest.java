package it.geosolutions.geoserver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class StressTest {

	/**
	 * USAGE:<br>
	 * java StressTest [getCapabilities.xml] [getCapabilities.csv]
	 * [params.properties] [] [jmeeter.ftl TEMPLATE_DIR] EXAMPLE: java
	 * StressTest src/main/resources/getCapabilities.xml
	 * src/main/resources/getCapabilities.csv
	 * src/main/resources/params.properties jmeeter.ftl src/main/resources/
	 * 
	 * @param args
	 * @throws IOException
	 * @throws TemplateException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public static void main(String[] args) throws IOException,
			TemplateException {

		File sourceDom;
		if (args.length > 0 && args[0] != null)
			sourceDom = new File(args[0]);
		else
			sourceDom = new File("src/main/resources/getCapabilities.xml");

		File destCSV;
		if (args.length > 1 && args[1] != null)
			destCSV = new File(args[1]);
		else
			destCSV = new File("src/main/resources/getCapabilities.csv");

		File properties;
		if (args.length > 2 && args[2] != null)
			properties = new File(args[2]);
		else
			properties = new File("src/main/resources/params.properties");
		Properties prop = new Properties();
		prop.load(new FileReader(properties));

		String templateFileName;
		if (args.length > 3 && args[3] != null)
			templateFileName = args[3];
		else
			templateFileName = "jmeeter.ftl";

		File baseDir;
		if (args.length > 4 && args[4] != null)
			baseDir = new File(args[4]);
		else
			baseDir = new File("src/main/resources/");

		/* ------------------------------------------------------------------- */
		/* You should do this ONLY ONCE in the whole application life-cycle: */
		final Template temp = setup(baseDir, templateFileName);

		/* Create a data-model */
		final Map root = new HashMap();

		/* Merge data-model with template */
		Writer writer = null;
		Writer bwriter = null;
		Tools stressTest = new Tools();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			// Use the factory to create a builder
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(sourceDom);

			root.put("doc", doc);
			root.put("properties", prop);
			root.put("StressTest", stressTest);

			writer = new FileWriter(destCSV);
			bwriter = new BufferedWriter(writer);
			temp.process(root, bwriter);

			System.out.println("WCS Count: " + stressTest.getWCSCount());
			System.out.println("WFS Count: " + stressTest.getWFSCount());
			System.out.println("LayerGroup Count: "
					+ stressTest.getLayerGroupCount());
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(bwriter);
			IOUtils.closeQuietly(writer);
		}


	}

	/**
	 * tools used by the template to randomize BB and w,h
	 * 
	 * @author cancellieri
	 * 
	 */
	public static class Tools {

		private Random rand = new Random();

		private long WFSCount = 0;
		private long WCSCount = 0;
		private long LayerGroupCount = 0;

		public double rand(double min, double max) {
			return (rand.nextDouble()) * (max - min) + min;
		}

		public long getWFSCount() {
			return WFSCount;
		}

		public void setWFSCount(long wFSCount) {
			WFSCount = wFSCount;
		}

		public long getWCSCount() {
			return WCSCount;
		}

		public void setWCSCount(long wCSCount) {
			WCSCount = wCSCount;
		}

		public long getLayerGroupCount() {
			return LayerGroupCount;
		}

		public void setLayerGroupCount(long layerGroupCount) {
			LayerGroupCount = layerGroupCount;
		}
	}

	private static Template setup(final File baseDir,
			final String templateFileName) throws IOException {

		/* Create and adjust the configuration */
		Configuration cfg = new Configuration();
		cfg.setDirectoryForTemplateLoading(baseDir);
		cfg.setObjectWrapper(new DefaultObjectWrapper());

		/* ------------------------------------------------------------------- */
		/* You usually do these for many times in the application life-cycle: */

		/* Get or create a template */
		Template temp = cfg.getTemplate(templateFileName);
		return temp;
	}
}
