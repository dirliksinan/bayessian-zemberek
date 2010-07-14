package com.medratech.bayessianzemberek;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.lang.String;
import java.util.TreeMap;
/**
 * Unit test for simple App.
 */
public class BayessianZemberekTest
        extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public BayessianZemberekTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(BayessianZemberekTest.class);
    }

    BayessianFileUtil bayessianFileUtil = null;
    BayessianAnalyzer bayessianAnalyzer = null;
    
    @Override
    public void setUp() throws FileNotFoundException {
         StringBuffer sb1 = new StringBuffer();
         StringBuffer sb2 = new StringBuffer();
         sb1.append("classified_file_number" + "\n");
         sb1.append("0.0"+ "\n");
         sb2.append("non_classified_file_number" + "\n");
         sb2.append("0.0"+"\n");
         java.io.PrintStream fileClassified = new  java.io.PrintStream("/tmp/.bz_ClassifiedBuffer.txt");
         java.io.PrintStream fileNonclassified = new  java.io.PrintStream("/tmp/.bz_NonclassifiedBuffer.txt");
         fileClassified.print(sb1);
         fileClassified.flush();
         fileNonclassified.print(sb2);
         fileNonclassified.flush();
         fileClassified.close();
         fileNonclassified.close();
         bayessianFileUtil = new BayessianFileUtil("/tmp/.bz_ClassifiedBuffer.txt", "/tmp/.bz_NonclassifiedBuffer.txt");
         bayessianAnalyzer = new BayessianAnalyzer();
    }

    @Override
    protected void tearDown() throws Exception {
        File fileClassified = new File("/tmp/.bz_ClassifiedBuffer.txt");
        fileClassified.delete();
        File fileNonclassified  = new File("/tmp/.bz_NonclassifiedBuffer.txt");
        fileNonclassified.delete();
    }



    public void testRead() throws FileNotFoundException {
        bayessianFileUtil.read();
        assertEquals(bayessianFileUtil.getBayessianDB().getClassifiedWordMap().isEmpty() , false );
        assertEquals(bayessianFileUtil.getBayessianDB().getClassifiedWordMap().containsKey("classified_file_number"), true);
    }

   public void testgenerateDocumentWordRootCountMap() throws FileNotFoundException{
       StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("ayrıştıramadıklarımız");
        Map<Integer,String> zemberek = ZemberekUtil.generateDocumentWordRootCountMap(stringBuffer);
        Iterator<Integer> iter = zemberek.keySet().iterator();
        assertEquals(zemberek.get(iter.next()), "ayrış");
    }

   public void testupdateDbMap () {
       Map<String, Double> updatableMap = new TreeMap<String, Double>();
       Map<String, Double> newMap = new TreeMap<String, Double>();
       updatableMap.put("ilk", 0.4);
       updatableMap.put("ikinci", 0.6);
       newMap.put("a", 0.5);
       newMap.put("ikinci",0.5);
       bayessianAnalyzer.updateDbMap(updatableMap,newMap,1.0);
       assertEquals(updatableMap.get("ikinci"), 0.55);
       assertEquals(updatableMap.get("a"), 0.25);
       assertEquals(updatableMap.get("ilk"), 0.2);
   }

   public void testtrainNonclassified() throws FileNotFoundException{
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("ali");
        stringBuffer.append("\n");
        stringBuffer.append("at");
        bayessianAnalyzer.trainNonclassified(stringBuffer);
        assertEquals(bayessianAnalyzer.getBayessianDB().getNonclassifiedWordMap().containsKey("ali"), true);
        assertEquals(bayessianAnalyzer.getBayessianDB().getNonclassifiedWordMap().get("ali"), 0.5);
   }


   public void testtrainClassified() throws FileNotFoundException{
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("ali"+"\n");
        stringBuffer.append("at");
        bayessianAnalyzer.trainClassified(stringBuffer);
        assertEquals(bayessianAnalyzer.getBayessianDB().getClassifiedWordMap().containsKey("ali"), true);
        assertEquals(bayessianAnalyzer.getBayessianDB().getClassifiedWordMap().get("ali"), 0.5);
   }


   public void testscore() throws FileNotFoundException {
       StringBuffer stringBuffer = new StringBuffer();
       stringBuffer.append("ali"+"\n");
       stringBuffer.append("at");
       bayessianAnalyzer.trainClassified(stringBuffer);
       double sonuc = bayessianAnalyzer.score(stringBuffer);
       assertEquals(sonuc, 1.0);
   }

   public void testfindWordCountInDocument() throws FileNotFoundException{
       String word = "at";
       StringBuffer stringBuffer = new StringBuffer();
       stringBuffer.append("ali"+"\n");
       stringBuffer.append("at"+"\n");
       stringBuffer.append("ati"+"\n");
       stringBuffer.append("at"+"\n");
       stringBuffer.append("ati"+"\n");
       Map<Integer,String> KokMap= ZemberekUtil.generateDocumentWordRootCountMap(stringBuffer);
       Double wordCount = bayessianAnalyzer.findWordCountInDocument(word, KokMap);
       assertEquals(wordCount, 0.4);
   }
}
