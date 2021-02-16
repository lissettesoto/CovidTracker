package com.company;
import java.util.HashSet;

public class PersonCOVIDRecord implements Comparable<PersonCOVIDRecord>{

    private String personName;
    private Boolean isCOVIDPositive;
    private HashSet<String> visitedLocationsLast24Hours;
    private int computedRiskScore;

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public void setCOVIDPositive(Boolean COVIDPositive) {
        isCOVIDPositive = COVIDPositive;
    }

    public void setVisitedLocationsLast24Hours(HashSet<String> visitedLocationsLast24Hours) {
        this.visitedLocationsLast24Hours = visitedLocationsLast24Hours;
    }

    public void setComputedRiskScore(int computedRiskScore) {
        this.computedRiskScore = computedRiskScore;
    }

    public String getPersonName() {
        return personName;
    }

    public Boolean getCOVIDPositive() {
        return isCOVIDPositive;
    }

    public HashSet<String> getVisitedLocationsLast24Hours() {
        return visitedLocationsLast24Hours;
    }

    public int getComputedRiskScore() {
        return computedRiskScore;
    }

    public PersonCOVIDRecord(String personName, String testResult, HashSet<String> visitedLocationsLast24Hours) {
        this.personName = personName;

        if (testResult.trim().equals("+")){
            this.isCOVIDPositive = true;
        }else{
            this.isCOVIDPositive = false;
        }
        this.visitedLocationsLast24Hours = visitedLocationsLast24Hours;
        this.computedRiskScore = 0;
    }


    @Override
    public int compareTo(PersonCOVIDRecord o) {
        // descending order of risk
        return o.computedRiskScore - this.computedRiskScore ;
    }

    public String toString() {

        String locations = String.join(",",  visitedLocationsLast24Hours);

        String stringRep = "PersonCOVIDRecord [name=" + personName + "; isCOVIDPositive=" + Boolean.toString(isCOVIDPositive) + "; locations=" + locations+ "; computedRiskScore="
                + computedRiskScore + "]";

        return stringRep;
    }


}
