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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.zemberek.erisim.Zemberek;
import net.zemberek.tr.yapi.TurkiyeTurkcesi;

/**
 *
 * @author atilla & sinan
 */
public class ZemberekUtil {

    private static Zemberek zemberek = new Zemberek(new TurkiyeTurkcesi());

    private static Zemberek getZemberek() {
        return zemberek;
    }

    /**
     * Overloaded version of generateDocumentWordRootCountMap function.
     * It takes the path of a file and put the content of the file in a StringBuffer to be used in
     * the other overloaded function of generateDocumentWordRootCountMap
     * @param String(filename)
     * @return Map
     * @throws FileNotFoundException
     */
    public static Map<Integer,String> generateDocumentWordRootCountMap(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        StringBuffer sb = new StringBuffer();
        while (scanner.hasNext())
            sb.append(scanner.nextLine() + "\n");
	scanner.close();
        return generateDocumentWordRootCountMap(sb);
    }

    /**
     * The function to find the root of the words in Turkish using the Zemberek library.
     * @param stringBuffer
     * @return Map<int index ,String words>
     */
    public static Map<Integer,String> generateDocumentWordRootCountMap(StringBuffer stringBuffer) {

        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(stringBuffer.toString().getBytes())));

        Map<Integer,String> rootKeepMap = new TreeMap<Integer,String>();
        Integer rootKeepMapIndex = 1;

        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                if (line.contentEquals("\n")) {
                    continue;
                }
                line = line.replaceAll("([\\p{L}]+)([’\'\"`´])([\\p{L}]+)", "$1");
                line = line.replaceAll("[^\\w\\p{L}\\s]+", "");
                line = line.replaceAll("(\\d+)([\\p{L}]+)", "$1");
                line = line.replaceAll("\\s+", " ");
                String[] tokens = line.split(" ");
                if (tokens.length == 0) {
                    continue;
                }
                for (int i = 0; i < tokens.length; i++) {
                    if (!tokens[i].contentEquals("\n")) {
                        if (tokens[i].length() == 0) {
                            continue;
                        }
                        char c = tokens[i].charAt(0);
                        if (c <= '9' && c >= '0') {
                            rootKeepMap.put(rootKeepMapIndex, tokens[i]);
                            rootKeepMapIndex++;
                        } else {
                            if (getZemberek().kokBulucu().stringKokBul(tokens[i]).length == 0) {
                                String temp23 = tokens[i].toLowerCase();
                                rootKeepMap.put(rootKeepMapIndex, temp23);
                                rootKeepMapIndex++;
                            } else {
                                rootKeepMap.put(rootKeepMapIndex, getZemberek().kokBulucu().stringKokBul(tokens[i])[0].toLowerCase());
                                rootKeepMapIndex++;
                            }
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ZemberekUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
		br.close();
	}
        return  rootKeepMap ;
    }
}
