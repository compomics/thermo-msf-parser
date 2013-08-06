package com.compomics.thermo_msf_parser_API.highmeminstance;

/**
 *
 * @author Davy
 */
    public class ProteinScore {
        private double score;
        private int processingNodeNumber;
        private double coverage;

        public ProteinScore(double score, int processingNodeNumber, double coverage) {
            this.score = score;
            this.processingNodeNumber = processingNodeNumber;
            this.coverage = coverage;
        }

        public double getCoverage() {
            return coverage;
        }

        public int getProcessingNodeNumber() {
            return processingNodeNumber;
        }

        public double getScore() {
            return score;
        }
    }