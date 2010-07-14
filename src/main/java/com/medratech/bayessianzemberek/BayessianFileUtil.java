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
import java.util.Scanner;

/**
 *
 * @author atilla & sinan
 */
public class BayessianFileUtil {

    private String classified_filename;
    private String non_classified_filename;

    private BayessianDB bayessianDB = new BayessianDB();

    /**
     * BayesinaFileUtil constructor
     * @param String(classified_filename)
     * @param String(non_classified_filename)
     */
    public BayessianFileUtil(String classified_filename, String non_classified_filename) {
        this.classified_filename = classified_filename;
        this.non_classified_filename = non_classified_filename;
    }

    /**
     * @param BayessianDb(bayessianDB)
     */
    public void setBayessianDB(BayessianDB bayessianDB) {
        this.bayessianDB = bayessianDB;
    }

    /**
     * @return BayessianDB(bayessianDB)
     */
    public BayessianDB getBayessianDB() {
        return bayessianDB;
    }
    /**
     * It gets the data in database files and put them in two Map to be used in program.
     * @throws FileNotFoundException
     */
    public  void read() throws FileNotFoundException {
        File file = new File(classified_filename);
        if (file.exists()) {
            Scanner inFile = new Scanner(file);
            while (inFile.hasNext()) {
                String word = inFile.nextLine();
                Double prob = inFile.nextDouble();
                bayessianDB.getClassifiedWordMap().put(word, prob);
            }
            inFile.close();
        }
        if (bayessianDB.getClassifiedWordMap().isEmpty()) {
            bayessianDB.setClassifiedFileNumber(0.0);
        } else {
            double classified_file_number = ((Double) bayessianDB.getClassifiedWordMap().get("classified_file_number")).doubleValue();
            bayessianDB.setClassifiedFileNumber(classified_file_number);
        }

        File file2 = new File(non_classified_filename);
        if (file2.exists()) {
            Scanner inFile2 = new Scanner(file2);

            while (inFile2.hasNext()) {
                String word2 = inFile2.nextLine();
                Double prob2 = inFile2.nextDouble();
                bayessianDB.getNonclassifiedWordMap().put(word2, prob2);
            }
            inFile2.close();
        }
        if (bayessianDB.getNonclassifiedWordMap().isEmpty()) {
            bayessianDB.setNonclassifiedFileNumber(0.0);
        } else {
            double non_classified_file_number = ((Double) bayessianDB.getNonclassifiedWordMap().get("non_classified_file_number")).doubleValue();
            bayessianDB.setNonclassifiedFileNumber(non_classified_file_number);
        }

    }
    /**
     * It puts the data in Maps which all updated while program running and put them in database files to be used again
     * @throws FileNotFoundException
     */
    public void write() throws FileNotFoundException {
        java.io.PrintStream ps = new java.io.PrintStream("classified_files.dat");
        bayessianDB.getClassifiedWordMap().put("classified_file_number", bayessianDB.getClassifiedFileNumber());
        Iterator <String> iter = bayessianDB.getClassifiedWordMap().keySet().iterator();
        while(iter.hasNext()){
            String word = iter.next();
            Double prob = (Double) bayessianDB.getClassifiedWordMap().get(word);
            ps.println(word);
            ps.println(prob);
            ps.flush();
        }
        ps.close();
        java.io.PrintStream ns = new java.io.PrintStream("non_classified_files.dat");
        bayessianDB.getNonclassifiedWordMap().put("non_classified_file_number",bayessianDB.getNonclassifiedFileNumber());
        Iterator <String> n_iter = bayessianDB.getNonclassifiedWordMap().keySet().iterator();
        while(n_iter.hasNext()){
            String word = n_iter.next();
            Double prob = (Double) bayessianDB.getNonclassifiedWordMap().get(word);
            ns.println(word);
            ns.println(prob);
            ns.flush();
        }
        ns.close();
    }
}
