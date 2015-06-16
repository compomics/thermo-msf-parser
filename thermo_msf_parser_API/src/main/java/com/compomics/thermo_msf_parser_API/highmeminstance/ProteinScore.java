package com.compomics.thermo_msf_parser_API.highmeminstance;

    /**
     * <p>ProteinScore class.</p>
     *
     * @author Davy
     * @version $Id: $Id
     */
    public class ProteinScore {
        private double score;
        private int processingNodeNumber;
        private double coverage;

        /**
         * <p>Constructor for ProteinScore.</p>
         *
         * @param score a double.
         * @param processingNodeNumber a int.
         * @param coverage a double.
         */
        public ProteinScore(double score, int processingNodeNumber, double coverage) {
            this.score = score;
            this.processingNodeNumber = processingNodeNumber;
            this.coverage = coverage;
        }

        /**
         * <p>Getter for the field <code>coverage</code>.</p>
         *
         * @return a double.
         */
        public double getCoverage() {
            return coverage;
        }

        /**
         * <p>Getter for the field <code>processingNodeNumber</code>.</p>
         *
         * @return a int.
         */
        public int getProcessingNodeNumber() {
            return processingNodeNumber;
        }

        /**
         * <p>Getter for the field <code>score</code>.</p>
         *
         * @return a double.
         */
        public double getScore() {
            return score;
        }
    }
