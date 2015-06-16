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


    /**
     * <p>Constructor for ProgressBarMiddleMan.</p>
     *
     * @param peptideLowMemInstance a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.controllers.PeptideLowMemController} object.
     * @param proteinLowMemInstance a {@link com.compomics.thermo_msf_parser_API.lowmeminstance.controllers.ProteinLowMemController} object.
     */
    public ProgressBarMiddleMan(PeptideLowMemController peptideLowMemInstance,ProteinLowMemController proteinLowMemInstance){
        this.peptideLowMemInstance = peptideLowMemInstance;
        this.proteinLowMemInstance = proteinLowMemInstance;
    }


    /**
     * <p>progressBarReturn.</p>
     *
     * @return a int.
     */
    public int progressBarReturn(){
        if (peptidesOrProteins) {
            return peptideLowMemInstance.getNumberOfPeptidesProcessed();
        } else {
            return proteinLowMemInstance.getNumberOfProteinsProcessed();
        }
    }
    
    /**
     * <p>Setter for the field <code>peptidesOrProteins</code>.</p>
     *
     * @param peptidesOrProteins a boolean.
     */
    public void setPeptidesOrProteins(boolean peptidesOrProteins){
        this.peptidesOrProteins = peptidesOrProteins;
    }
}
