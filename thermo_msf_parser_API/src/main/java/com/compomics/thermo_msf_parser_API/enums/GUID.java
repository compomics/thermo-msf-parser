/**
 * An enum containing the GUIDs from the MSF file. Used for CustomData fields
 * and Node Types
 */
package com.compomics.thermo_msf_parser_API.enums;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
/**
 * <p>GUID class.</p>
 *
 * @author toorn101
 * @version $Id: $Id
 */
public enum GUID {

    /**
     * phosphoRS sequence probability
     */
    PRS_SEQUENCE_PROBABILITY("648ca109-d0d0-4cee-a4bc-6b65ce8b29b5", "PhosphoRS sequence probability"),
    /**
     * phosphoRS score
     */
    PRS_SCORE("67a9ceea-d1d8-4730-be55-8b2e079d3bcd", "phosphoRS score"),
    /**
     * phosphoRS site probabilities
     */
    PRS_SITE_PROBABILITIES("4baa6fcd-f366-46a4-ad29-674b23c5641c", "phosphoRS site probabilities"),
    /**
     * PTM scorer node, produces the PhosphoRS scores
     */
    NODE_PTM_SCORER("0787547a-095f-4721-bbb2-83a59ea52e13", "phosphoRS"),
    /**
     * Second version of phosphoRS
     */
    NODE_PTM_SCORER2("62846c02-f562-4cde-932d-27c1d22ad4cb", "phosphoRS2"),
    /**
     * Third version of phosphoRS
     */
    NODE_PTM_SCORER3("22d029c2-6deb-4ad8-a860-4eac6e825caa", "phosphoRS3"),
    /**
     * Top N Peaks Filter
     */
    NODE_TOP_N_PEAKS_FILTER("38cd4dcd-2bb4-4493-bc11-0bbd7f71701e", "Top N Peaks Filter"),
    /**
     * Scan event filter
     */
    NODE_SCAN_EVENT_FILTER("86e4d28f-9493-4cc4-921f-c352a99ebfd5", "Scan event filter"),
    /**
     * Mascot search node
     */
    NODE_MASCOT("7643d5ae-af3b-40c7-9be3-da9413336c93", "Mascot search"),
    /**
     * Sequest search node
     */
    NODE_SEQUEST("033d39f9-b398-4cac-9ad7-a308e68c5df9", "Sequest search"),
    /**
     * X-Core search node TODO: obtain the X-CORE GUID
     */
    NODE_XCORE("43091f25-f2d1-4a79-9d07-5f1b9a30bf69", "X-Core search"),
    /**
     * Peptide validator node
     */
    NODE_PEPTIDE_VALIDATOR("4aba1d19-1fd5-4a42-8671-175a1800878a", "Peptide validator"),
    /**
     * Non fragment filter node
     */
    NODE_NON_FRAGMENT_FILTER("aeb65a53-cd12-4957-843e-c3f41e10f7f0", "Non fragment filter"),
    /**
     * Percolator FDR calculation
     */
    NODE_PERCOLATOR("edabd112-fc32-4d12-bb89-7bd90e3bb935", "Percolator"),
    /**
     * Spectrum files node
     */
    NODE_SPECTRUM_FILES("848160ba-2f76-46ca-a281-f7bf66990a5b", "Spectrum files"),
    /**
     * Spectrum normalizer for Sequest
     */
    NODE_SPECTRUM_NORMALIZATION("3359e3e6-8057-4d35-90ad-f0bfaa1adbdb", "Spectrum normalizer"),
    /**
     * Spectrum selector node
     */
    NODE_SPECTRUM_SELECTOR("238d2f70-3dd9-4e2b-a77d-f24933797cf6", "Spectrum selector"),
    /**
     * Spectrum exporter node
     */
    NODE_SPECTRUM_EXPORTER("2ad453d3-093f-4acc-a4d2-ee5452a6d186", "Spectrum exporter"),
    /**
     * Event detector node
     */
    NODE_EVENT_DETECTOR("a8127a07-6a42-4b56-93e7-c6e41af6a0a5", "Event detector"),
    /**
     * Precursor ions quantifier node
     */
    NODE_PRECURSOR_IONS_QUANTIFIER("fd1e56f4-941c-4573-9cfe-f8fa3ad2261c", "Precursor ions quantifier"),
    /**
     * Catch-all for unknown GUIDs. This should be caught
     */
    UNKNOWN_GUID("","");
    /**
     * Map to go from GUID string to a GUID enum member
     */
    private static Map<String, GUID> guidStringToGUID = null;

    /**
     * Initialize the GUIDString to GUID map
     */
    private static void populateGuidStringToGuidMap() {
        guidStringToGUID = new HashMap<String, GUID>();
        for (GUID guid : GUID.values()) {
            guidStringToGUID.put(guid.toString(), guid);
        }
    }

    /**
     * Convert a string representing a GUID to a GUID member
     *
     * @param iNodeGUID a {@link java.lang.String} object.
     * @return a {@link com.compomics.thermo_msf_parser_API.enums.GUID} object.
     */
    public static GUID fromGUIDString(String iNodeGUID) {
        if (guidStringToGUID == null) {
            populateGuidStringToGuidMap();
        }
        GUID result = null;
        if (guidStringToGUID.containsKey(iNodeGUID)) {
            result = guidStringToGUID.get(iNodeGUID);
        }
        if (result == null) {
            result = GUID.UNKNOWN_GUID;
            Logger.getRootLogger().warn("Unknown GUID found in MSF file: " + iNodeGUID);
        }
        return result;
    }
    private String guid;
    private String description;

    GUID(String guid, String description) {
        this.guid = guid;
        this.description = description;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return guid;
    }

    /**
     * <p>Getter for the field <code>description</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDescription() {
        return description;
    }
}
