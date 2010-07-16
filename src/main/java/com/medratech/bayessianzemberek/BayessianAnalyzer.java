/*************************************************************************
 * 
 * Copyright (C) 2010 Huseyin Kerem Cevahir <kerem@medra.com.tr>
 * 
 * ***********************************************************************
 * 
 * This file is part of MyDLP.
 * 
 * MyDLP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * MyDLP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MyDLP.  If not, see <http://www.gnu.org/licenses/>.
 * 
 ************************************************************************/

package com.medratech.bayessianzemberek;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 *
 * @author atilla & sinan
 */
public class BayessianAnalyzer {
    
    private BayessianDB bayessianDB = null;

    public BayessianAnalyzer() {
        bayessianDB = new BayessianDB();
    }



    /**
     * @return bayesianDB
     */
    public BayessianDB getBayessianDB() {
        return bayessianDB;
    }

    /**
     * set the bayessianDB element 
     * @param bayessianDB
     */
    public void setBayessianDB(BayessianDB bayessianDB) {
        this.bayessianDB = bayessianDB;
    }

    /**
     * score Overloaded function
     * Just takes the path of the file which will be identified
     * with the help of the other overloaded function of score
     * return a double score value of the file
     * @param String-filename
     * @return double
     * @throws FileNotFoundException
     */
    public  double score(String filename) throws FileNotFoundException {
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        StringBuffer sb = new StringBuffer();
        while (scanner.hasNext())
            sb.append(scanner.nextLine() );
	scanner.close();
        double sonuc =  score(sb);
        return sonuc;
    }

    /**
     * Calculate the wanted file types probabilties 
     * @param stringBuffer
     * @return double
     */
    public double score(StringBuffer stringBuffer) {
        double Bayesianprob=0;
        Map<Integer,String> incelenen_kelimeler = ZemberekUtil.generateDocumentWordRootCountMap(stringBuffer);
        Iterator<Integer> iter = incelenen_kelimeler.keySet().iterator();
        Double wordnumber = new Double(0);
        Double wordProbInClassified, classifiedFileProb , wordProbInNonclassified , nonClassifiedFileProb;
        while(iter.hasNext()){
            Integer index = iter.next();
            String word = incelenen_kelimeler.get(index);
            if((!bayessianDB.getClassifiedWordMap().containsKey(word)) &&
                    (!bayessianDB.getNonclassifiedWordMap().containsKey(word)))
                continue;
            else {
                if(bayessianDB.getClassifiedWordMap().containsKey(word)){
                     wordProbInClassified = bayessianDB.getClassifiedWordMap().get(word);                                    // database deki kelimenin gizli dosyadaki oranı
                     classifiedFileProb = bayessianDB.getClassifiedFileNumber() /
                             (bayessianDB.getClassifiedFileNumber() + bayessianDB.getNonclassifiedFileNumber());          // database de ki gizli dosya oranı
                }
                else {
                   wordProbInClassified = 0.0;
                   classifiedFileProb = 0.0;
                }
                if(bayessianDB.getNonclassifiedWordMap().containsKey(word)){
                    wordProbInNonclassified = bayessianDB.getNonclassifiedWordMap().get(word);
                    nonClassifiedFileProb = bayessianDB.getNonclassifiedFileNumber()/
                            (bayessianDB.getClassifiedFileNumber() + bayessianDB.getNonclassifiedFileNumber());
                }
                else {
                   wordProbInNonclassified = 0.0;
                   nonClassifiedFileProb = 0.0;
                }
                Bayesianprob += (wordProbInClassified * classifiedFileProb)/((wordProbInClassified * classifiedFileProb) + (wordProbInNonclassified * nonClassifiedFileProb));
                wordnumber ++ ;
            }

        }
        if(wordnumber == 0) return 0;
        return Bayesianprob/wordnumber;

    }
    /**
     * Update the database treeMaps and add new elements to the treeMaps
     * @param dbWordScoreMap
     * @param documentWordCountMap
     * @param dbFileCount
     */
    public  void updateDbMap(Map<String,Double> dbWordScoreMap, Map<String,Double> documentWordCountMap, Double dbFileCount) {
        Iterator<String> dwordCMap_iter = documentWordCountMap.keySet().iterator();
        Iterator<String> dbWordSMap_iter = dbWordScoreMap.keySet().iterator();
        Double updateFactor;
        if (dbFileCount != 0) {
            updateFactor = (double) dbFileCount / (dbFileCount + 1);
        } else {
            updateFactor = 1.0;
        }

        while (dbWordSMap_iter.hasNext()) {
            String word = (String) dbWordSMap_iter.next();
            double value = dbWordScoreMap.get(word).doubleValue() * updateFactor;
            dbWordScoreMap.put(word, value);
        }

        if (dbFileCount == 0) {
            while (dwordCMap_iter.hasNext()) {
                String word = (String) dwordCMap_iter.next();
                if (dbWordScoreMap.containsKey(word)) {
                    double value = dbWordScoreMap.get(word).doubleValue() + documentWordCountMap.get(word).doubleValue();
                    dbWordScoreMap.put(word, value);
                } else {
                    double value = ((Double) documentWordCountMap.get(word)).doubleValue();
                    dbWordScoreMap.put(word, value);
                }
            }
        } else {
            while (dwordCMap_iter.hasNext()) {
                updateFactor = 1 / (dbFileCount + 1);
                String word = dwordCMap_iter.next();
                if (dbWordScoreMap.containsKey(word)) {
                    double value = dbWordScoreMap.get(word).doubleValue() + documentWordCountMap.get(word).doubleValue() * updateFactor;
                    dbWordScoreMap.put(word, value);
                } else {
                    Double sayi = ((Double) documentWordCountMap.get(word)).doubleValue() * updateFactor;
                    dbWordScoreMap.put(word, sayi);
                }
            }
        }
    }

    /**
     * An overloaded version of trainNonclassified function.
     * Just finds the content of the files of the given paths
     * and put the content in a StringBuffer.
     * Finally it calls the other overloaded version.
     * @param String
     * @throws FileNotFoundException
     */
    public void trainNonclassified(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        StringBuffer sb = new StringBuffer();
        while (scanner.hasNext())
            sb.append(scanner.nextLine() + "\n");
	scanner.close();
        trainNonclassified(sb); 
    }
    /**
     * It finds the roots and the frequency of the words in a non-classified file which will be added to database and out the data in a TreeMap
     * @param stringBuffer
     */
    public void trainNonclassified(StringBuffer stringBuffer) {
        Map<Integer,String> documentWordRootMap = ZemberekUtil.generateDocumentWordRootCountMap(stringBuffer);
        Map<String,Double>  documentWordProbMap= new TreeMap<String,Double>();
        Iterator <Integer> temp_iter = documentWordRootMap.keySet().iterator();
        while(temp_iter.hasNext()){
            int index = temp_iter.next();
            String word = documentWordRootMap.get(index);
            if(!documentWordProbMap.containsKey(word)){
                documentWordProbMap.put(word, findWordCountInDocument(word, documentWordRootMap));
            }
        }
        updateDbMap(bayessianDB.getNonclassifiedWordMap(), documentWordProbMap, bayessianDB.getNonclassifiedFileNumber());
        bayessianDB.setNonclassifiedFileNumber(bayessianDB.getNonclassifiedFileNumber() + 1);
    }

    /**
     * An overloaded version of trainClassified function.
     * Just finds the content of the files of the given paths
     * and put the content in a StringBuffer.
     * Finally it calls the other overloaded version.
     * @param String
     * @throws FileNotFoundException
     */
    public void trainClassified(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        StringBuffer sb = new StringBuffer();
        while (scanner.hasNext())
            sb.append(scanner.nextLine() + "\n");
	scanner.close();
        trainClassified(sb);
    }

    /**
     * It finds the roots and the frequency of the words in a classified file which will be added to database and out the data in a TreeMap
     * @param stringBuffer
     */
    public void trainClassified(StringBuffer stringBuffer)  {
        Map<Integer,String> documentWordRootMap = ZemberekUtil.generateDocumentWordRootCountMap(stringBuffer);
        Map<String,Double>  documentWordProbMap = new  TreeMap<String,Double>();
        Iterator <Integer> temp_iter = documentWordRootMap.keySet().iterator();
        while(temp_iter.hasNext()){
            Integer index = temp_iter.next();
            String word = documentWordRootMap.get(index);
            if(!documentWordProbMap.containsKey(word)){
                documentWordProbMap.put(word , findWordCountInDocument(word, documentWordRootMap));
            }

        }
        updateDbMap(bayessianDB.getClassifiedWordMap(), documentWordProbMap, bayessianDB.getClassifiedFileNumber());
        bayessianDB.setClassifiedFileNumber(bayessianDB.getClassifiedFileNumber() + 1);
    }

    /**
     * It finds and returns the frequency of just one word in a file
     * @param word
     * @param documentWordMap
     * @return
     */
    public  Double findWordCountInDocument(String documentWord , Map<Integer,String> documentWordMap) {
        int totalWordsInDocument=documentWordMap.size();
        int wordCountInDocument=0;
        Iterator<Integer> iter = documentWordMap.keySet().iterator();
        while(iter.hasNext()){
            int junkTaken = iter.next();
            String takenWord = documentWordMap.get(junkTaken);
            if(documentWord.equals(takenWord))
                wordCountInDocument++;
        }
        return (double)wordCountInDocument/(double)totalWordsInDocument;
    }

    /**
     *It deletes all the data in database
     */
    public void resetDB() {
        bayessianDB = new BayessianDB();
    }
}
