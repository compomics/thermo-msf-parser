package com.compomics.thermo_msf_parser.msf;

import java.sql.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 10/1/12
 * Time: 10:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProteinLowMemController extends Observable implements ProteinInterface {


    private int proteinCounter;
    private int internalcounter = 0;

    /**
     *
     * @param iConnection connection to the msf file
     * @return an iterator containing all the proteins from the msf file
     * @throws java.sql.SQLException
     */
    public Iterator getAllProteins(Connection iConnection) throws SQLException {
        Statement stat = iConnection.createStatement();
        int lProteinID;
        String lSequence = "";
        ProteinLowMem protein;
        ArrayList<ProteinLowMem> proteinList = new ArrayList<ProteinLowMem>();
        ResultSet rs = stat.executeQuery("select ProteinID,Sequence from Proteins");
        while(rs.next()){
            lProteinID = rs.getInt(1);
            lSequence = rs.getString(2);
            protein = new ProteinLowMem(lSequence,iConnection,lProteinID);
            proteinList.add(protein);
        }
        rs.close();
        stat.close();
        protein = null;
        return proteinList.iterator();
    }

    /**
     *
     * @param proteinAccession a protein accession
     * @param aConnection  a connection to the msf file
     * @return a proteinlowmem object
     * @throws java.sql.SQLException
     */

    public ProteinLowMem getProteinFromAccession(String proteinAccession, Connection aConnection) throws SQLException {
        Statement stat = aConnection.createStatement();
        int lProteinID;
        ResultSet rs = stat.executeQuery("select protanno.ProteinID,Proteins.Sequence from ,(ProteinID from ProteinAnnotations where ProteinAnnotations.Description like ('%"+proteinAccession+"%')) as protanno where protanno.ProteinID = Proteins.ProteinID");
        rs.next();
        lProteinID = rs.getInt(1);
        //String lSequence = rs.getString(2);
        rs.close();
        stat.close();
        return new ProteinLowMem(proteinAccession,aConnection,lProteinID);
    }

    /**
     *
     * @param proteinID a protein id in the db
     * @param aConnection a connection to the msf file
     * @return a string containing the accession of a protein
     * @throws java.sql.SQLException
     */

    public String getAccessionFromProteinID(int proteinID,Connection aConnection) throws SQLException {
        Statement stat = aConnection.createStatement();
        String accession = "";
        ResultSet rs = stat.executeQuery("select Description,Sequence from ProteinAnnotations,Proteins where ProteinAnnotations.ProteinID = "+proteinID +" and Proteins.ProteinID = "+proteinID);
        com.compomics.util.protein.Protein iUtilProtein;
        while(rs.next()) {
            String lFasta = rs.getString(1)+"\n"+rs.getString(2);
            if ( lFasta.contains(">GENSCAN")){
                iUtilProtein = new com.compomics.util.protein.Protein(lFasta);
                accession=(iUtilProtein.getHeader().toString().substring(lFasta.indexOf("GEN"), lFasta.indexOf("pep") - 1));
            }
            else if (lFasta.contains(">ENS")) {
                try{
                    iUtilProtein = new com.compomics.util.protein.Protein(lFasta);
                    accession=(iUtilProtein.getHeader().toString().substring(lFasta.indexOf("ENS"), lFasta.indexOf("pep") - 1));
                } catch (Exception e){e.printStackTrace();}

            } else {
                iUtilProtein = new com.compomics.util.protein.Protein(lFasta);
                accession=(iUtilProtein.getHeader().getAccession());
            }
        }
        rs.close();
        stat.close();
        return accession;
    }

    /**
     *
     * @param aConnection a connection to an msf file
     * @return a hashmap with key: the accession value: a proteinlowmem object
     */

    public HashMap<String,ProteinLowMem> getAllProteinAccessions(Connection aConnection) {
        HashMap<String,ProteinLowMem> allProteinAccessions = new HashMap<String, ProteinLowMem>();
        int internalCounter = 0;
        try {
            String proteinAccession;
            PreparedStatement stat = aConnection.prepareStatement("select * from Proteins");
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                int lProteinID = rs.getInt("ProteinID");
                String lSequence = rs.getString("Sequence");
                proteinAccession = getAccessionFromProteinID(lProteinID, aConnection);
                allProteinAccessions.put(proteinAccession,new ProteinLowMem(proteinAccession, aConnection, lProteinID));
                internalCounter++;
                proteinCounter++;
                if (internalCounter == 200){
                    setChanged();
                    notifyObservers();
                    internalCounter = 0;
                }
            }
            proteinCounter = 0;
            rs.close();
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allProteinAccessions;
    }

    /**
     *
     * @param confidenceLevel the confidence level of the desired proteins
     * @param aConnection  a connection to the msf file
     * @return  an arraylist containing all the proteins in proteinlowmem objects
     */

    public ArrayList<ProteinLowMem> getProteinsForConfidenceLevel(int confidenceLevel, Connection aConnection){
        ArrayList<ProteinLowMem> changedAccessions = new ArrayList<ProteinLowMem>();
        String proteinAccession;
        proteinCounter = 0;
        try {
            Statement stat = aConnection.createStatement();
            ResultSet rs = stat.executeQuery("select Proteins.ProteinID from PeptidesProteins,Peptides,Proteins where Peptides.PeptideID = PeptidesProteins.PeptideID and Proteins.ProteinID = PeptidesProteins.ProteinID and Peptides.confidencelevel = "+confidenceLevel);
            while(rs.next()){
                int lProteinID = rs.getInt(1);
                proteinAccession = getAccessionFromProteinID(lProteinID,aConnection);
                changedAccessions.add(new ProteinLowMem(proteinAccession,aConnection,lProteinID));
                proteinCounter++;
                internalcounter++;
                if (internalcounter > 30) {
                    internalcounter = 0;
                    setChanged();
                    notifyObservers();
                }
            }
            rs.close();
            stat.close();
        }catch(SQLException sqle){
            sqle.printStackTrace();
        }
        return changedAccessions;
    }

    /**
     *
     * @param proteinID the ID of the protein in the SQLite DB
     * @param aConnection a connection to the msf file
     * @return the sequence for the protein id
     * @throws java.sql.SQLException
     */
    public String getSequenceForProteinID(int proteinID,Connection aConnection) throws SQLException {
        PreparedStatement stat = aConnection.prepareStatement("select Sequence from Proteins where ProteinID = "+proteinID);
        ResultSet rs = stat.executeQuery();
        rs.next();
        String lSequence = rs.getString(1);
        rs.close();
        stat.close();
        return lSequence;
    }

    /**
     *
     * @param peptideID peptideID stored in the SQLite DB
     * @param aConnection a connection to the msf file
     * @return a vector containing all the proteins in protein objects connected to the peptide
     * @throws java.sql.SQLException
     */
    public Vector<ProteinLowMem> getProteinsForPeptide(int peptideID,Connection aConnection) throws SQLException {
        Vector<ProteinLowMem> proteinVector = new Vector<ProteinLowMem>();
        PreparedStatement stat = aConnection.prepareStatement("select Proteins.ProteinID from Proteins,(select ProteinID from PeptidesProteins where PeptideID = "+peptideID+" ) as protid where protid.ProteinID = Proteins.ProteinID");
        ResultSet rs = stat.executeQuery();
        while(rs.next()){
            int lProteinID = rs.getInt(1);
            proteinVector.add(new ProteinLowMem(getAccessionFromProteinID(lProteinID,aConnection),aConnection,lProteinID));
        }
        rs.close();
        stat.close();
        return proteinVector;
    }

    /**
     *
     * @param peptideVector a vector containing all the peptides to find proteins for
     * @param aConnection  a connection to an msf file
     * @return a vector containing all the proteins connected to the peptides
     * @throws java.sql.SQLException
     */
    public Vector<ProteinLowMem> getProteinsForPeptideVector(Vector<Peptide> peptideVector,Connection aConnection) throws SQLException {
        Vector<ProteinLowMem> proteinVector = new Vector<ProteinLowMem>();
        String iAccession;
        StringBuilder stringBuilder = new StringBuilder();
        for (Peptide aPeptideVector : peptideVector) {
            stringBuilder.append(",").append(aPeptideVector.getPeptideId());
        }
        stringBuilder.replace(0,1,"");
        PreparedStatement stat = aConnection.prepareStatement("select Proteins.ProteinID,Sequence from Proteins,(select PeptidesProteins.ProteinID from PeptidesProteins where PeptideID in ("+stringBuilder.toString()+" )) as protid where protid.ProteinID = Proteins.ProteinID");
        ResultSet rs = stat.executeQuery();
        while(rs.next()){
            int lProteinID = rs.getInt(1);
            //String lSequence = rs.getString(2);
            iAccession = getAccessionFromProteinID(lProteinID,aConnection);
            proteinVector.add(new ProteinLowMem(iAccession,aConnection,lProteinID));
        }
        rs.close();
        stat.close();
        return proteinVector;
    }

    public Vector<String> getAllProteinAccessionsForPeptide(int peptideID, Connection aConnection) throws SQLException {
        Vector<String> proteinVector = new Vector<String>();
        PreparedStatement stat = aConnection.prepareStatement("select Proteins.ProteinID from Proteins,(select ProteinID from PeptidesProteins where PeptideID = "+peptideID+" ) as protid where protid.ProteinID = Proteins.ProteinID");
        ResultSet rs = stat.executeQuery();
        while(rs.next()){
            int lProteinID = rs.getInt(1);
            proteinVector.add(getAccessionFromProteinID(lProteinID, aConnection));
        }
        rs.close();
        stat.close();
        return proteinVector;
    }

    public Integer getNumberOfPeptidesForProtein(int proteinID,Connection aConnection) throws SQLException {
        Integer numberOfPeptides = 0;

        Statement stat = aConnection.createStatement();
        ResultSet rs = stat.executeQuery("select count(PeptideID) from PeptidesProteins where ProteinID = "+proteinID);
        rs.next();
        numberOfPeptides = rs.getInt(1);
        return numberOfPeptides;
    }
    public int getNumberOfProteinsProcessed(){
        return proteinCounter;
    }
}
