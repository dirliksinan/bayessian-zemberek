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
import java.util.Scanner;

/**
 *
 * @author atilla & sinan
 *
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        BayessianAnalyzer analyzer = new BayessianAnalyzer();
        BayessianFileUtil bayessianFileUtil = new BayessianFileUtil("classified_files.dat", "non_classified_files.dat");
        bayessianFileUtil.read();
        analyzer.setBayessianDB(bayessianFileUtil.getBayessianDB());

        while (true) {
            int choice;
            String path;
            Scanner scanner = new Scanner(System.in);
            System.out.println("1.Update classified files: ");
            System.out.println("2.Update non-classified files: ");
            System.out.println("3.Identify your file: ");
            System.out.println("4.Delete the database files: ");
            System.out.println("To exit click any other number: ");
            System.out.print("Now, you can enter your choice: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Press 1 to add from file:");
                    System.out.println("Press 2 to add from folder:");
                    choice = scanner.nextInt();
                    System.out.print("Enter an absolute path to update classified file database: ");
                    Scanner scan = new Scanner(System.in);
                    path = scan.nextLine();
                    switch (choice) {
                        case 1:
                            analyzer.trainClassified(path);
                            break;
                        case 2:
                            File actual = new File(path + "/.");
                            for (File f : actual.listFiles()) {
                                String path2 = path + "/" + f.getName();
                                analyzer.trainClassified(path2);
                            }
                            break;
                    }
                    break;
                case 2:
                    System.out.println("Press 1 to add from file:");
                    System.out.println("Press 2 to add from folder:");
                    choice = scanner.nextInt();
                    System.out.print("Enter an absolute path to update non-classified file database: ");
                    Scanner scan2 = new Scanner(System.in);
                    path = scan2.nextLine();
                    switch (choice) {
                        case 1:
                            analyzer.trainNonclassified(path);
                            break;
                        case 2:
                            File actual = new File(path + "/.");
                            for (File f : actual.listFiles()) {
                                String path3 = path + "/" + f.getName();
                                analyzer.trainNonclassified(path3);
                            }
                            break;
                    }
                    break;
                case 3:
                    double bayesprob;
                    System.out.print("Type the path of the file you want to examine:");
                    Scanner scan3 = new Scanner(System.in);
                    path = scan3.nextLine();
                    bayesprob = (Double) analyzer.score(path);
                    System.out.println("your file is a classified file as likely as " + bayesprob);
                    break;
                case 4:
                    analyzer.resetDB();
                    System.out.println("Database files have been deleted...");
                    break;
                default:
                    bayessianFileUtil.setBayessianDB(analyzer.getBayessianDB());
                    bayessianFileUtil.write();
                    System.exit(0);
            }
        }

    }
}
