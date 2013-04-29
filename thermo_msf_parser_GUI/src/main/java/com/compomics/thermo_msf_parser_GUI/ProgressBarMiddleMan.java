package com.compomics.thermo_msf_parser_GUI;

import com.compomics.thermo_msf_parser_API.lowmeminstance.controllers.PeptideLowMemController;
import com.compomics.thermo_msf_parser_API.lowmeminstance.controllers.ProteinLowMemController;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 8/1/12
 * Time: 3:42 PM
 * To change this template use File | Settings | File Templates.
 */
class ProgressBarMiddleMan {

    private boolean peptidesOrProteins = true;
    private final PeptideLowMemController peptideLowMemInstance;
    private final ProteinLowMemController proteinLowMemInstance;


    public ProgressBarMiddleMan(PeptideLowMemController peptideLowMemInstance,ProteinLowMemController proteinLowMemInstance){
        this.peptideLowMemInstance = peptideLowMemInstance;
        this.proteinLowMemInstance = proteinLowMemInstance;
    }


    public int progressBarReturn(){
        if (peptidesOrProteins) {
            return peptideLowMemInstance.getNumberOfPeptidesProcessed();
        } else {
            return proteinLowMemInstance.getNumberOfProteinsProcessed();
        }
    }
    
    public void setPeptidesOrProteins(boolean peptidesOrProteins){
        this.peptidesOrProteins = peptidesOrProteins;
    }
}
