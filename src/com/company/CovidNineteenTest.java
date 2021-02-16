package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CovidNineteenTest {

    public static void main(String args[]) {

        LinkedList<PersonCOVIDRecord> covidRecords = new LinkedList<PersonCOVIDRecord>();

        Scanner inputScanner = null;

        try {
            inputScanner = new Scanner (new File ("/Users/lissettesoto/Desktop/Covid19Project.txt"));
        }catch (FileNotFoundException e){
            System.out.println("File not found");
            e.printStackTrace();
        }


        while (inputScanner.hasNext()) {
            String [] fields = inputScanner.nextLine().split(",");
            String name = fields[0].trim();
            String testResult = fields[1].trim();
            HashSet<String> locations = new HashSet<String>();
            for (int i= 2; i < fields.length; i++){
                locations.add(fields[i].trim());
            }

            PersonCOVIDRecord covidRecord = new PersonCOVIDRecord(name, testResult, locations);
            covidRecords.add(covidRecord);
        }

        System.out.println("Input Records:\n");
        PrintCovidRecords(covidRecords);


        HashMap<String, Integer> locationsRiskScore = computeLocationsRiskScore(covidRecords);

        // now we determine risk scores for non-covid-positive people
        for (PersonCOVIDRecord r: covidRecords){
            int riskScore = 0;
            // we only check people that are NOT positive
            if (!r.getCOVIDPositive()){
                // iterate over all locations visited by healthy person
                for(String l : r.getVisitedLocationsLast24Hours()){
                    if (locationsRiskScore.containsKey(l)){
                        riskScore += locationsRiskScore.get(l) ;
                    }
                }
            }
            r.setComputedRiskScore(riskScore);
        }


        ArrayList<PersonCOVIDRecord> covidPositive = new ArrayList<PersonCOVIDRecord>();
        ArrayList<PersonCOVIDRecord> atRiskOfCovid = new ArrayList<PersonCOVIDRecord>();
        ArrayList<PersonCOVIDRecord> lowRiskOfCovid = new ArrayList<PersonCOVIDRecord>();


        for (PersonCOVIDRecord covidRecord: covidRecords) {
            // COVID positive people
            if (covidRecord.getCOVIDPositive() ){
                covidPositive.add(covidRecord);
            }
            else{
                // COVID negative: we split into at-risk and low risk
                if (covidRecord.getComputedRiskScore() == 0 ){
                    lowRiskOfCovid.add(covidRecord);
                }else{
                    atRiskOfCovid.add(covidRecord);
                }
            }
        }

        System.out.println("\n\nLocal COVID 24 Hour report\n\n");
        System.out.println("The following tested positive (ignore computedRiskScore):");
        PrintCovidRecords(covidPositive);

        System.out.println("\nThe following are at medium or high risk (sorted highest risk first):");
        Collections.sort(atRiskOfCovid);
        PrintCovidRecords(atRiskOfCovid);

        System.out.println("\nThe following are at low risk:");
        PrintCovidRecords(lowRiskOfCovid);



    }

    public static void PrintCovidRecords(List<PersonCOVIDRecord> covidRecords){
        for (PersonCOVIDRecord covidRecord: covidRecords) {
            System.out.println(covidRecord);
        }
    }
    // computing risk score from records of people with positive test
    public static HashMap<String, Integer> computeLocationsRiskScore(LinkedList<PersonCOVIDRecord> covidRecords){
        HashMap<String, Integer> locationsRiskScore = new HashMap<String, Integer>();
        for (PersonCOVIDRecord r: covidRecords){

            // we only check people that are positive
            if (r.getCOVIDPositive()){
                // iterate over all locations visited by infected person
                for(String l : r.getVisitedLocationsLast24Hours()){
                    if (locationsRiskScore.containsKey(l)){
                        locationsRiskScore.put(l, locationsRiskScore.get(l) + 1);
                    }else{
                        locationsRiskScore.put(l, 1);
                    }
                }
            }
        }

        return locationsRiskScore;
    }
}

