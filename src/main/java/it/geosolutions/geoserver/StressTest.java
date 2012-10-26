package it.geosolutions.geoserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class StressTest implements JavaSamplerClient {

    /**
     * USAGE:<br>
     * java StressTest [getCapabilities.xml] [getCapabilities.csv] [params.properties] [] [jmeeter.ftl TEMPLATE_DIR] EXAMPLE: java StressTest
     * src/main/resources/getCapabilities.xml src/main/resources/getCapabilities.csv src/main/resources/params.properties jmeeter.ftl
     * src/main/resources/
     * 
     * @param args
     * @throws IOException
     * @throws TemplateException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static void main(String[] args) throws IOException, TemplateException {

        File sourceDom;
        if (args != null && args.length > 0 && args[0] != null)
            sourceDom = new File(args[0]);
        else
            sourceDom = new File("src/main/jmeter/getCapabilities.xml");

        File destCSV;
        if (args != null && args.length > 1 && args[1] != null)
            destCSV = new File(args[1]);
        else
            destCSV = new File("src/main/jmeter/getCapabilities.csv");

        File properties;
        if (args != null && args.length > 2 && args[2] != null)
            properties = new File(args[2]);
        else
            properties = new File("src/main/resources/params.properties");
        Properties prop = new Properties();
        prop.load(new FileReader(properties));

        String templateFileName;
        if (args != null && args.length > 3 && args[3] != null)
            templateFileName = args[3];
        else
            templateFileName = "jmeeter.ftl";

        File baseDir;
        if (args != null && args.length > 4 && args[4] != null)
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
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // Use the factory to create a builder
            DocumentBuilder builder = factory.newDocumentBuilder();

            File destDom = File.createTempFile("escaped", ".xml");
            // destDom.deleteOnExit();
            excapeXMLFile(sourceDom, destDom);

            Document doc = builder.parse(destDom);

            root.put("doc", doc);
            root.put("properties", prop);
            root.put("StressTest", stressTest);

            writer = new FileWriter(destCSV);
            bwriter = new BufferedWriter(writer);
            temp.process(root, bwriter);

            System.out.println("WCS Count: " + stressTest.getWCSCount());
            System.out.println("WFS Count: " + stressTest.getWFSCount());
            System.out.println("LayerGroup Count: " + stressTest.getLayerGroupCount());

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

    public static void excapeXMLFile(File path, File out) {
        if (path == null)
            throw new IllegalArgumentException("Input path is not valid");
        if (out == null)
            throw new IllegalArgumentException("Ouptut path is not valid");

        DataInputStream in = null;
        InputStreamReader is = null;
        BufferedReader br = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {

            // Open the file that is the first
            // command line parameter
            FileInputStream fstream = new FileInputStream(path);
            // Get the object of DataInputStream
            in = new DataInputStream(fstream);
            is = new InputStreamReader(in);

            br = new BufferedReader(is);

            fw = new FileWriter(out);
            bw = new BufferedWriter(fw);

            String strLine;
            // Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                // Print the content on the console
                // bw.write(StringEscapeUtils.escapeXml(strLine));
                bw.write(strLine.replaceAll("&", "&amp;"));
            }
            bw.flush();
        } catch (Exception e) {// Catch exception if any
            System.err.println("Error: " + e.getMessage());
        } finally {

            // Close the input stream
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(br);
            IOUtils.closeQuietly(fw);
            IOUtils.closeQuietly(bw);
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

    private static Template setup(final File baseDir, final String templateFileName)
            throws IOException {

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

    @Override
    public SampleResult runTest(JavaSamplerContext arg0) {
        SampleResult sampleResult = new SampleResult();
        try {
            
            Iterator<String> it=arg0.getParameterNamesIterator();
            List<String> list=new ArrayList<String>();
            while (it.hasNext()){
                list.add(it.next());
            }
            
//           
            
            StressTest.main(list.toArray(new String[]{}));
            sampleResult.setSuccessful(true);
            sampleResult.setResponseMessage("ok");
            sampleResult.setResponseCodeOK();
            sampleResult.setResponseMessageOK();
        } catch (IOException e) {
            // LOGGER.log(Level.FINER, e.getMessage(), e);
            sampleResult.setSuccessful(false);
            sampleResult.setResponseMessage(e.getLocalizedMessage());
        } catch (TemplateException e) {
            // LOGGER.log(Level.FINER, e.getMessage(), e);
            sampleResult.setSuccessful(false);
            sampleResult.setResponseMessage(e.getLocalizedMessage());
        }
        return sampleResult;
    }

    @Override
    public Arguments getDefaultParameters() {
        Arguments args=new Arguments();
            args.addArgument("sourceDom","src/main/jmeter/getCapabilities.xml");
//
            args.addArgument("destCSV","src/main/jmeter/getCapabilities.csv");
//
            args.addArgument("properties","src/main/resources/params.properties");
//      
            args.addArgument("templateFileName","jmeeter.ftl");
//
            args.addArgument("baseDir","src/main/resources/");
        return args;
    }

    @Override
    public void setupTest(JavaSamplerContext arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void teardownTest(JavaSamplerContext arg0) {
        // TODO Auto-generated method stub
        
    }
}
