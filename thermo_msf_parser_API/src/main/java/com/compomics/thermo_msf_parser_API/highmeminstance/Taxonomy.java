package com.compomics.thermo_msf_parser_API.highmeminstance;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas
 * Date: 18-Feb-2011
 * Time: 11:01:50
 * To change this template use File | Settings | File Templates.
 *
 * @author Davy Maddelein
 * @version $Id: $Id
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
     *
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

    /**
     * <p>getTaxonomyId.</p>
     *
     * @return a int.
     */
    public int getTaxonomyId() {
        return iTaxonomyId;
    }

    /**
     * <p>getParentTaxonomyId.</p>
     *
     * @return a int.
     */
    public int getParentTaxonomyId() {
        return iParentTaxonomyId;
    }

    /**
     * <p>getTaxonomyRank.</p>
     *
     * @return a int.
     */
    public int getTaxonomyRank() {
        return iTaxonomyRank;
    }

    /**
     * <p>getLeftNodeIndex.</p>
     *
     * @return a int.
     */
    public int getLeftNodeIndex() {
        return iLeftNodeIndex;
    }

    /**
     * <p>getRightNodeIndex.</p>
     *
     * @return a int.
     */
    public int getRightNodeIndex() {
        return iRightNodeIndex;
    }

    /**
     * <p>getName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName() {
        return iName;
    }

    /**
     * <p>getNameCategory.</p>
     *
     * @return a int.
     */
    public int getNameCategory() {
        return iNameCategory;
    }


    //setters

    /**
     * <p>setName.</p>
     *
     * @param iName a {@link java.lang.String} object.
     */
    public void setName(String iName) {
        this.iName = iName;
    }

    /**
     * <p>setNameCategory.</p>
     *
     * @param iNameCategory a int.
     */
    public void setNameCategory(int iNameCategory) {
        this.iNameCategory = iNameCategory;
    }
}
