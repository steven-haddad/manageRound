package com.example.haddad.managemyrounds.model;

/**
 * Created by haddad on 09/11/17.
 */

public class Face {

    private String faceCode;
    private String newDesignImage;
    private String oldDesignImage;
    private String newDesignName;
    private String oldDesignName;
    private String roundFurnCode;
    private String adress;


    public Face(String faceCode, String newDesignImage, String oldDesignImage, String roundFurnCode, String newDesignName, String oldDesignName) {
        this.faceCode = faceCode;
        this.newDesignImage = newDesignImage;
        this.oldDesignImage = oldDesignImage;
        this.roundFurnCode= roundFurnCode;
        this.newDesignName=newDesignName;
        this.oldDesignName=oldDesignName;

    }

    public String getNewDesignName() {
        return newDesignName;
    }

    public void setNewDesignName(String newDesignName) {
        this.newDesignName = newDesignName;
    }

    public String getOldDesignName() {
        return oldDesignName;
    }

    public void setOldDesignName(String oldDesignName) {
        this.oldDesignName = oldDesignName;
    }

    public String getRoundFurnCode() {
        return roundFurnCode;
    }

    public void setRoundFurnCode(String roundFurnCode) {
        this.roundFurnCode = roundFurnCode;
    }

    public String getFaceCode() {
        return faceCode;
    }

    public void setIdFace(String faceCode) {
        this.faceCode = faceCode;
    }

    public String getnewDesignImage() {
        return newDesignImage;
    }

    public void setnewDesignImage(String newDesignImage) {
        this.newDesignImage = newDesignImage;
    }

    public String getoldDesignImage() {
        return oldDesignImage;
    }

    public void setoldDesignImage(String oldDesignImage) {
        this.oldDesignImage = oldDesignImage;
    }
}
