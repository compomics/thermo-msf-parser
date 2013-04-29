package com.compomics.thermo_msf_parser_API.highmeminstance;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 18-Feb-2011
 * Time: 11:01:50
 * To change this template use File | Settings | File Templates.
 */
public class Taxonomy {
    /**
     * The taxonomy id
     */
    private int iTaxonomyId;
    /**
     * The parent taxonomy id
     */
    private int iParentTaxonomyId;
    /**
     * The taxonomy rank
     */
    private int iTaxonomyRank;
    /**
     * The left node index
     */
    private int iLeftNodeIndex;
    /**
     * The right node index
     */
    private int iRightNodeIndex;
    /**
     * The name
     */
    private String iName;
    /**
     * The name category
     */
    private int iNameCategory;


    /**
     * The Taxonomy constructor
     * @param iTaxonomyId The taxonomy id
     * @param iParentTaxonomyId The parent taxonomy id
     * @param iTaxonomyRank The taxonomy rank
     * @param iLeftNodeIndex The left node index
     * @param iRightNodeIndex The right node index
     */
    public Taxonomy(int iTaxonomyId, int iParentTaxonomyId, int iTaxonomyRank, int iLeftNodeIndex, int iRightNodeIndex) {
        this.iTaxonomyId = iTaxonomyId;
        this.iParentTaxonomyId = iParentTaxonomyId;
        this.iTaxonomyRank = iTaxonomyRank;
        this.iLeftNodeIndex = iLeftNodeIndex;
        this.iRightNodeIndex = iRightNodeIndex;
    }
    
    
    //getters

    public int getTaxonomyId() {
        return iTaxonomyId;
    }

    public int getParentTaxonomyId() {
        return iParentTaxonomyId;
    }

    public int getTaxonomyRank() {
        return iTaxonomyRank;
    }

    public int getLeftNodeIndex() {
        return iLeftNodeIndex;
    }

    public int getRightNodeIndex() {
        return iRightNodeIndex;
    }

    public String getName() {
        return iName;
    }

    public int getNameCategory() {
        return iNameCategory;
    }


    //setters

    public void setName(String iName) {
        this.iName = iName;
    }

    public void setNameCategory(int iNameCategory) {
        this.iNameCategory = iNameCategory;
    }
}
