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

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author atilla & sinan
 */
public class BayessianDB {
     /**
      * the Count of the classified files in database
      */
    private double classifiedFileNumber = 0.0;
    /**
     * The Count of the non-classified files in database
     */
    private double nonclassifiedFileNumber = 0.0 ;
    /**
     * It keeps the classified words with frequencies which are in database
     */
    private Map<String,Double> classifiedWordMap = new  TreeMap<String,Double>();
    /**
     * It keeps the non-classified words with frequencies which are in database
     */
    private Map<String,Double> nonclassifiedWordMap = new  TreeMap<String,Double>();

    /**.
     * @return double(classifiedFileNumber)
     */
    public double getClassifiedFileNumber() {
        return classifiedFileNumber;
    }

    /**
     * @param double(classifiedFileNumber)
     */
    public void setClassifiedFileNumber(double classifiedFileNumber) {
        this.classifiedFileNumber = classifiedFileNumber;
    }

    /**
     * @return Map(classifiedWordMap)
     */
    public Map<String,Double> getClassifiedWordMap() {
        return classifiedWordMap;
    }

    /**
     * @param Map(classifiedWordMap)
     */
    public void setClassifiedWordMap(Map<String,Double> classifiedWordMap) {
        this.classifiedWordMap = classifiedWordMap;
    }

    /**
     * @return double(nonclassifiedFileNumber)
     */
    public double getNonclassifiedFileNumber() {
        return nonclassifiedFileNumber;
    }

    /**
     * @param double(nonclassifiedFileNumber)
     */
    public void setNonclassifiedFileNumber(double nonclassifiedFileNumber) {
        this.nonclassifiedFileNumber = nonclassifiedFileNumber;
    }

    /**
     * @return Map(nonclassifiedWordMap)
     */
    public Map<String,Double> getNonclassifiedWordMap() {
        return nonclassifiedWordMap;
    }

    /**
     * @param Map(nonclassifiedWordMap)
     */
    public void setNonclassifiedWordMap(Map<String,Double> nonclassifiedWordMap) {
        this.nonclassifiedWordMap = nonclassifiedWordMap;
    }



}
