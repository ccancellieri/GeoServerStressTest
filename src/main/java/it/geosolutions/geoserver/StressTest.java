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
import org.apache.jmeter.services.FileServer;
import org.apache.log.Logger;
import org.apache.log.Priority;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class StressTest extends AbstractJavaSamplerClient implements JavaSamplerClient {

    /**
     * USAGE:<br>
     * java StressTest [getCapabilities.xml] [getCapabilities.csv] [params.properties] [] [jmeter.ftl TEMPLATE_DIR] EXAMPLE: java StressTest
     * src/main/resources/getCapabilities.xml src/main/resources/getCapabilities.csv src/main/resources/params.properties jmeter.ftl
     * src/main/resources/
     * 
     * @param args
     * @throws IOException
     * @throws TemplateException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static void main(String[] args) throws IOException, TemplateException,
            ParserConfigurationException, SAXException {
        StressTest.run(null,FileServer.getDefaultBase(),args);
    }

    private static void run(Logger logger, String baseDir, String... args) throws TemplateException, IOException,
            ParserConfigurationException, SAXException {

        File sourceDom;
        if (args != null && args.length > 0 && args[0] != null) {
            sourceDom = new File(args[0]);
        } else
            sourceDom = new File(baseDir, "src/test/jmeter/getCapabilities.xml");
        if (!sourceDom.isAbsolute()){
            sourceDom=new File(baseDir,sourceDom.getPath());
        }
        
        if (logger!=null && logger.isDebugEnabled())
            logger.debug("Source dom: " + sourceDom);

        File destCSV;
        if (args != null && args.length > 1 && args[1] != null)
            destCSV = new File(args[1]);
        else
            destCSV = new File(baseDir, "src/test/jmeter/getCapabilities.csv");
        if (!destCSV.isAbsolute()){
            destCSV=new File(baseDir,destCSV.getPath());
        }
        
        if (logger!=null && logger.isDebugEnabled())
            logger.debug("destCSV: " + destCSV);

        File properties;
        if (args != null && args.length > 2 && args[2] != null)
            properties = new File(args[2]);
        else
            properties = new File(baseDir, "src/test/jmeter/params.properties");
        if (!properties.isAbsolute()){
            properties=new File(baseDir,properties.getPath());
        }
        
        if (logger!=null && logger.isDebugEnabled())
            logger.debug("properties: " + properties);
        
        Properties prop = new Properties();
        FileReader fr = null;
        try {
            fr = new FileReader(properties);
            prop.load(fr);
        } finally {
            IOUtils.closeQuietly(fr);
        }

        String templateFileName;
        if (args != null && args.length > 3 && args[3] != null)
            templateFileName = args[3];
        else
            templateFileName = "jmeter.ftl";
        
        if (logger!=null && logger.isDebugEnabled())
            logger.debug("FreeMarker template file name: " + templateFileName);

        File fmBaseDir;
        if (args != null && args.length > 4 && args[4] != null)
            fmBaseDir = new File(args[4]);
        else
            fmBaseDir = new File(baseDir, "src/test/jmeter/");
        if (!fmBaseDir.isAbsolute()){
            fmBaseDir=new File(baseDir,fmBaseDir.getPath());
        }
        
        if (logger!=null && logger.isDebugEnabled())
            logger.debug("FreeMarker template base dir: " + fmBaseDir);

        /* ------------------------------------------------------------------- */
        /* You should do this ONLY ONCE in the whole application life-cycle: */
        Template temp = setup(fmBaseDir, templateFileName);

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

            if (logger!=null && logger.isInfoEnabled()) {
                logger.info("WCS Count: " + stressTest.getWCSCount());
                logger.info("WFS Count: " + stressTest.getWFSCount());
                logger.info("LayerGroup Count: " + stressTest.getLayerGroupCount());
            }

        } finally {
            IOUtils.closeQuietly(bwriter);
            IOUtils.closeQuietly(writer);
        }

    }

    public static void excapeXMLFile(File path, File out) throws IOException {
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

            StressTest.run(getLogger(),baseDir,list.toArray(new String[] {}));
            sampleResult.setSuccessful(true);
            sampleResult.setResponseMessage("ok");
            sampleResult.setResponseCodeOK();
            sampleResult.setResponseMessageOK();

        } catch (Exception e) {
            sampleResult.setSuccessful(false);
            sampleResult.setResponseMessage(e.getLocalizedMessage());
            if (getLogger().isErrorEnabled())
                getLogger().error(e.getLocalizedMessage(), e);
        }
        return sampleResult;
    }

    @Override
    public Arguments getDefaultParameters() {
        Arguments args = new Arguments();
        args.addArgument("sourceDom", "getCapabilities.xml");
        //
        args.addArgument("destCSV", "getCapabilities.csv");
        //
        args.addArgument("properties", "params.properties");
        //
        args.addArgument("templateFileName", "jmeter.ftl");
        //
        args.addArgument("freemarkerBaseDir", "./");

        return args;
    }

    private List<String> list;

    private String baseDir;

    private FileServer fileServer;

    @Override
    public void setupTest(JavaSamplerContext arg0) {
        fileServer = FileServer.getFileServer();
        baseDir = fileServer.getBaseDir();
//        JMeterContextService.getContext()
        if (getLogger().isInfoEnabled())
            getLogger().info("Context folder: " + baseDir);

        Iterator<String> it = arg0.getParameterNamesIterator();
        list = new ArrayList<String>();
        int i = 0;
        while (it.hasNext()) {
            String arg = arg0.getParameter(it.next());
            list.add(arg);
            if (getLogger().isInfoEnabled())
                getLogger().info("Adding argument_" + i++ + ": " + arg);
        }
    }

    @Override
    public void teardownTest(JavaSamplerContext arg0) {

    }
}
