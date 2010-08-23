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
public class BayessianDBUtil {

    private BayessianDB bayessianDB = new BayessianDB();

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
    public  void read(StringBuffer classified, StringBuffer non_classified) {
        Scanner inFile = new Scanner(classified.toString());
        while (inFile.hasNext()) {
            String word = inFile.nextLine();
            Double prob = inFile.nextDouble();
            inFile.nextLine();
            bayessianDB.getClassifiedWordMap().put(word, prob);
        }
        inFile.close();
        
        if (bayessianDB.getClassifiedWordMap().isEmpty()) {
            bayessianDB.setClassifiedFileNumber(0.0);
        } else {
            double classified_file_number = ((Double) bayessianDB.getClassifiedWordMap().get("classified_file_number")).doubleValue();
            bayessianDB.setClassifiedFileNumber(classified_file_number);
        }

        Scanner inFile2 = new Scanner(non_classified.toString());
        while (inFile2.hasNext()) {
            String word2 = inFile2.nextLine();
            Double prob2 = inFile2.nextDouble();
            inFile2.nextLine();
            bayessianDB.getNonclassifiedWordMap().put(word2, prob2);
        }
        inFile2.close();
        
        if (bayessianDB.getNonclassifiedWordMap().isEmpty()) {
            bayessianDB.setNonclassifiedFileNumber(0.0);
        } else {
            double non_classified_file_number = ((Double) bayessianDB.getNonclassifiedWordMap().get("non_classified_file_number")).doubleValue();
            bayessianDB.setNonclassifiedFileNumber(non_classified_file_number);
        }
    }
    /**
     * It puts the data in Maps which all updated while program running and put them in database files to be used again
     */
    public StringBuffer getClassifiedDB(){
        StringBuffer sb = new StringBuffer();
        bayessianDB.getClassifiedWordMap().put("classified_file_number", bayessianDB.getClassifiedFileNumber());
        Iterator <String> iter = bayessianDB.getClassifiedWordMap().keySet().iterator();
        while(iter.hasNext()){
            String word = iter.next();
            Double prob = (Double) bayessianDB.getClassifiedWordMap().get(word);
            sb.append(word).append("\n");
            sb.append(prob).append("\n");
        }
        return sb;
    }

    public StringBuffer getNonClassifiedDB(){
        StringBuffer sb = new StringBuffer();
        bayessianDB.getNonclassifiedWordMap().put("non_classified_file_number",bayessianDB.getNonclassifiedFileNumber());
        Iterator <String> n_iter = bayessianDB.getNonclassifiedWordMap().keySet().iterator();
        while(n_iter.hasNext()){
            String word = n_iter.next();
            Double prob = (Double) bayessianDB.getNonclassifiedWordMap().get(word);
            sb.append(word).append("\n");
            sb.append(prob).append("\n");
        }
        return sb;
    }
}
