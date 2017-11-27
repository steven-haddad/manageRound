package com.example.haddad.managemyrounds.model;


import java.util.List;

public class Round {

    private String postingDay;
    private String numberOfFaces;
    private String region;
    private String furnCode;



    public Round(String postingDay,String numberOfFaces,String region,String furnCode ) {
        this.postingDay = postingDay;;
        this.numberOfFaces=numberOfFaces;
        this.region=region;
        this.furnCode=furnCode;

    }

    public String getFurnCode() {
        return furnCode;
    }

    public void setFurnCode(String furnCode) {
        this.furnCode = furnCode;
    }

    public String getpostingDay() {
        return postingDay;
    }

    public void setpostingDay(String postingDay) {
        this.postingDay = postingDay;
    }

    public String getnumberOfFaces() {

        return numberOfFaces;
    }

    public void setnumberOfFaces(String numberOfFaces) {
        this.numberOfFaces = numberOfFaces;
    }

    public String getRegion()
    {
        return region;
    }

    public void setRegion(String region)
    {
        this.region = region;
    }

}
