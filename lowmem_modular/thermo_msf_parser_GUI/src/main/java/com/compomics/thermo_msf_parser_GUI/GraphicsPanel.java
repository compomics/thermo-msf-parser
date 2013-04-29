/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compomics.thermo_msf_parser_GUI;

import com.compomics.util.Util;
import com.compomics.util.gui.events.RescalingEvent;
import com.compomics.util.gui.interfaces.SpectrumAnnotation;
import com.compomics.util.gui.interfaces.SpectrumPanelListener;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
/**
 *
 * @author Davy
 */
public abstract class GraphicsPanel extends JPanel {

    /**
     * If true the x-axis will be drawn using the scientific annotation.
     * The pattern i set in the "scientificPattern" field.
     */
    private boolean scientificXAxis = false;
    /**
     * If true the y-axis will be drawn using the scientific annotation.
     * The pattern i set in the "scientificPattern" field.
     */
    private boolean scientificYAxis = false;
    /**
     * The number format pattern for scientific annotation.
     */
    private String scientificPattern = "##0.#####E0";
    /**
     * A hashmap of the current x-axis reference areas. Key is the name of
     * the reference area.
     */
    private Map<String, ReferenceArea> referenceAreasXAxis = new HashMap<String, ReferenceArea>();
    /**
     * A hashmap of the current y-axis reference areas. Key is the name of
     * the reference area.
     */
    private Map<String, ReferenceArea> referenceAreasYAxis = new HashMap<String, ReferenceArea>();
    /**
     * If set to true, the y-axis is removed, the y- and x-axis tags are removed,
     * and any annotations are hidden. All to make the graphics panel look better
     * in a smaller version, e.g., when put into a table cell. When turning
     * miniature mode one it is also recommended to reduce the max padding.
     *
     * Note that miniature and reduced max padding is set automatically by the
     * GraphicsPanelTableCellRenderer.
     */
    protected boolean miniature = false;
    /**
     * If set to true, all y data is assumed to be positive. This adds a white
     * polygon under the y-axis hiding any polygon data lines that crosses
     * (slightly) below the y-axis.
     */
    protected boolean yDataIsPositive = true;
    /**
     * The opacity of the spectra. 0 means completely see-through, 1 means
     * opaque.
     */
    protected float alphaLevel = 0.3f;
    /**
     * The number of datasets currently displayed in the panel.
     */
    protected static int dataSetCounter = 0;
    /**
     * This status indicates that no annotation will be displayed,
     * but the user will have a fully functional interface (point clicking, selecting,
     * sequencing etc.)
     */
    public static final int INTERACTIVE_STATUS = 0;
    /**
     * This status indicates that annotation (if present) will be displayed,
     * while limiting the user to zooming in/out.
     */
    public static final int ANNOTATED_STATUS = 1;
    /**
     * This HashMap instance holds all the known mass deltas (if any).
     * The keys are the Doubles with the massdelta, the values are the
     * descriptions.
     */
    protected static HashMap<Double, String> iKnownMassDeltas = null;

    // Static init block takes care of reading the 'SpectrumPanel.properties' file if
    // it hasn't already been done.
    static {
        try {
            if (iKnownMassDeltas == null) {
                iKnownMassDeltas = new HashMap();
                Properties temp = new Properties();
                InputStream is = SpectrumPanel.class.getClassLoader().getResourceAsStream("SpectrumPanel.properties");
                if (is != null) {
                    temp.load(is);
                    is.close();
                    Iterator iter = temp.keySet().iterator();
                    while (iter.hasNext()) {
                        String key = (String) iter.next();
                        iKnownMassDeltas.put(new Double(key), temp.getProperty(key));
                    }
                }
            }
        } catch (Exception e) {
            // Do nothing. So now masses will be known.
        }
    }
    /**
     * The size of the window to use when searching for matches in the known masses
     * list when the user hovers over a second data point after clicking a previous
     * data point.
     */
    protected double deltaMassWindow = 0.2;
    /**
     * The label (and unit between brackets, if available) for the x-axis.
     * Defaults to "m/z".
     */
    protected String iXAxisLabel = "m/z";
    /**
     * The label (and unit between brackets, if available) for the y-axis.
     * Defaults to "Int".
     */
    protected String iYAxisLabel = "Int";
    /**
     * This is the color the filename should be presented in.
     */
    protected Color iFilenameColor = null;
    /**
     * Colors in which the data points and peaks are rendered. Indexed by dataset.
     */
    protected List<Color> iDataPointAndLineColor = new ArrayList<Color>();
    /**
     * Colors in which the chromatogram polyline is rendered. Indexed by dataset.
     */
    protected List<Color> iAreaUnderCurveColor = new ArrayList<Color>();
    /**
     * Size for the point on a polygon.
     */
    protected Integer iPointSize = 0;
    /**
     * The spectrum or chromatogram filename.
     */
    protected String iFilename = null;
    /**
     * The list of SpectrumPanelListeners.
     */
    protected List iSpecPanelListeners = new ArrayList();
    /**
     * The deviation (both left and right) allowed for point highlighting detection.
     */
    protected int iPointDetectionTolerance = 5;
    /**
     * When the mouse is dragged, this represents the
     * X-coordinate of the starting location.
     */
    protected int iStartXLoc = 0;
    /**
     * When the mouse is dragged, this represents the
     * Y-coordinate of the starting location.
     */
    protected int iStartYLoc = 0;
    /**
     * When the mouse is dragged, this represents the
     * X-coordinate of the ending location.
     */
    protected int iEndXLoc = 0;
    /**
     * The lower range for the current zoom range.
     */
    protected double xAxisZoomRangeLowerValue = 0;
    /**
     * The upper range for the current zoom range.
     */
    protected double xAxisZoomRangeUpperValue = 0;
    /**
     * The current dragging location.
     */
    protected int iDragXLoc = 0;
    /**
     * Scale unit for the X axis
     */
    protected double iXScaleUnit = 0.0;
    /**
     * Scale unit for the Y axis
     */
    protected double iYScaleUnit = 0.0;
    /**
     * Graphical unit for the X axis
     */
    protected int iXUnit = 0;
    /**
     * Graphical unit for the Y axis
     */
    protected int iYUnit = 0;
    /**
     * Effective distance from the x-axis to the panel border.
     */
    protected int iXPadding = 0;
    /**
     * Effective distance from the panel top border
     * to 5 pixels above the top of the highest point (or y-tick mark).
     */
    protected int iTopPadding = 0;
    /**
     * This boolean is set to 'true' if the x-axis should start at zero.
     */
    protected boolean iXAxisStartAtZero = true;
    /**
     * This boolean is set to 'true' when dragging is performed.
     */
    protected boolean iDragged = false;
    /**
     * The number of X-axis tags.
     */
    protected int xTagCount = 10;
    /**
     * The number of Y-axis tags.
     */
    protected int yTagCount = 10;
    /**
     * The padding (distance between the axes and the border of the panel).
     */
    protected int padding = 20;
    /**
     * The current padding (distance between the axes and the border of the panel).
     */
    protected int currentPadding = 20;
    /**
     * The maximum padding (distance between the axes and the border of the panel).
     * Increase if font size on the y-axis becomes too small.
     */
    protected int maxPadding = 50;
    /**
     * The boolean is set to 'true' if the file name is to be shown in the panel.
     */
    protected boolean showFileName = true;
    /**
     * The boolean is set to 'true' if the precursor details is to be shown in the panel.
     */
    protected boolean showPrecursorDetails = true;
    /**
     * The boolean is set to 'true' if the resolution is to be shown in the panel.
     */
    protected boolean showResolution = true;
    /**
     * All the x-axis data points. Indexed by dataset (one double[] per dataset).
     * First dataset is the first double[], second dataset is the second double[]
     * etc.Should at all times be sorted from high to low.
     */
    protected List<double[]> iXAxisData = new ArrayList<double[]>();
    /**
     * The minimum x-axis value to display.
     */
    protected double iXAxisMin = 0.0;
    /**
     * The maximum x-axis value to display.
     */
    protected double iXAxisMax = 0.0;
    /**
     * The minimum y-axis value to display.
     */
    protected double iYAxisMin = 0.0;
    /**
     * The maximum  y-axis value to display.
     */
    protected double iYAxisMax = 0.0;
    /**
     * The procentual non-inclusive, minimal y-axis value (compared to the highest
     * point in the spectrum) a point should have before being eligible for annotation.
     * Default is '0.0'.
     */
    protected double iAnnotationYAxisThreshold = 0.0;
    /**
     * All the y-axis values. Indexed by dataset (one double[] per dataset). First
     * dataset is the first double[], second dataset is the second double[] etc.
     * Y-axis values are related to the x-axis values by the table index. So the first
     * y-axis value of the first dataset is the value for the first x-axis value in
     * the first dataset etc.
     */
    protected List<double[]> iYAxisData = new ArrayList<double[]>();
    /**
     * This variable holds the precursor M/Z.
     */
    protected double iPrecursorMZ = 0.0;
    /**
     * This String holds the charge for the precursor.
     */
    protected String iPrecursorCharge = null;
    /**
     * This array will hold the x-coordinates in pixels for
     * all the x-axis values. Link is through index. Again
     * indexed by dataset (one double[] per dataset).
     */
    protected List<int[]> iXAxisDataInPixels = new ArrayList<int[]>();
    /**
     * This array will hold the y-coordinates in pixels for
     * all the y-axis values. Link is through index. Again
     * indexed by dataset (one double[] per dataset).
     */
    protected List<int[]> iYAxisDataInPixels = new ArrayList<int[]>();
    /**
     * Boolean that will be 'true' when a point needs highlighting.
     */
    protected boolean iHighLight = false;
    /**
     * Index of the point that needs to be highlighted.
     */
    protected int iHighLightIndex = 0;
    /**
     * Index of the dataset containing the point that needs to be highlighted.
     */
    protected int iHighLightDatasetIndex = 0;
    /**
     * Boolean that indicates whether a point has been marked by clicking.
     */
    protected boolean iClicked = false;
    /**
     * Int that indicates which point was clicked.
     */
    protected int iClickedIndex = 0;
    /**
     * Int that indicates which dataset contains the clicked point.
     */
    protected int iClickedDataSetIndex = 0;
    /**
     * The List that holds all points clicked up to now.
     */
    protected List iClickedList = new ArrayList();
    /**
     * The List that holds the dataset indices of all points clicked up to now.
     */
    protected List iClickedListDatasetIndices = new ArrayList();
    /**
     * The List that holds a set of stored points from a previously established list.
     */
    protected List iStoredSequence = new ArrayList();
    /**
     * The List that holds the dataset indices of stored points from a
     * previously established list.
     */
    protected List iStoredSequenceDatasetIndices = new ArrayList();
    /**
     * The List that holds a set of Annotation instances.
     */
    protected List iAnnotations = new ArrayList();
    /**
     * Minimal dragging distance in pixels.
     */
    protected int iMinDrag = 15;
    /**
     * This variable holds the drawing style.
     */
    protected int iDrawStyle = -1;
    /**
     * This variable holds the dot radius;
     * only used when drawing style is DOTS style.
     */
    protected int iDotRadius = 2;
    /**
     * Drawstyle which draws lines connecting the X-axis with the measurement.
     */
    protected static final int LINES = 0;
    /**
     * Drawstyle which draws a dot at the measurement height.
     */
    protected static final int DOTS = 1;
    /**
     * The ms level of the current spectrum. O is assumed to mean no ms level
     * given.
     */
    protected int iMSLevel = 0;

    /**
     * An enumerator of the possible GraphicsPanel types
     */
    protected enum GraphicsPanelType {

        profileSpectrum, centroidSpectrum, chromatogram,
        isotopicDistributionCentroid, isotopicDistributionProfile
    }
   
    /**
     * Sets the current GraphicsPanel type, default to centroid spectrum
     */
    protected GraphicsPanelType currentGraphicsPanelType = GraphicsPanelType.centroidSpectrum;

    /**
     * Returns true of all the y-data is to be assumed as positive.
     *
     * @return true of all the y-data is to be assumed as positive
     */
    public boolean yDataIsPositive() {
        return yDataIsPositive;
    }

    /**
     * Set to true of all y data values can be assumed to be positive.
     *
     * @param yDataIsPositive true of all y data values can be assumed to be positive
     */
    public void setYDataIsPositive(boolean yDataIsPositive) {
        this.yDataIsPositive = yDataIsPositive;
    }

    /**
     * Returns true if the graphics panel is to be drawn in a minature form.
     *
     * @return true if the graphics panel is to be drawn in a minature form
     */
    public boolean isMiniature() {
        return miniature;
    }

    /**
     * Set if the graphics panel is to be drawn in a minature form.
     *
     * @param miniature if the spectrum is to be drawn in a minature form
     */
    public void setMiniature(boolean miniature) {
        this.miniature = miniature;
    }

    /**
     * Returns the lower range for the current zoom range.
     *
     * @return the lower range for the current zoom range
     */
    public double getXAxisZoomRangeLowerValue() {
        return xAxisZoomRangeLowerValue;
    }

    /**
     * Returns the upper range for the current zoom range.
     *
     * @return the upper range for the current zoom range
     */
    public double getXAxisZoomRangeUpperValue() {
        return xAxisZoomRangeUpperValue;
    }

    /**
     * Get the size of the window to use when searching for matches in the known masses
     * list when the user hovers over a second data point after clicking a previous
     * data point.
     *
     * @return the size of the delta mass window
     */
    public double getDeltaMassWindow() {
        return deltaMassWindow;
    }

    /**
     * Set the size of the window to use when searching for matches in the known masses
     * list when the user hovers over a second data point after clicking a previous
     * data point.
     *
     * @param deltaMassWindow the new size of the delta mass window
     */
    public void setDeltaMassWindow(double deltaMassWindow) {
        this.deltaMassWindow = deltaMassWindow;
    }

    /**
     * Get all the known mass deltas (if any). The keys are the Doubles with the
     * massdelta, the values are the descriptions.
     *
     * @return HashMap of the known mass deltas
     */
    public static Map<Double, String> getKnownMassDeltas() {
        return iKnownMassDeltas;
    }

    /**
     * Set all the known mass deltas (if any). The keys are the Doubles with the
     * massdelta, the values are the descriptions.
     *
     * @param aiKnownMassDeltas the HasMap of the known mass deltas
     */
    public static void setKnownMassDeltas(HashMap<Double, String> aiKnownMassDeltas) {
        iKnownMassDeltas = aiKnownMassDeltas;
    }

    /**
     * Returns the x-axis data.
     *
     * @return the x-axis data
     */
    public List<double[]> getXAxisData() {
        return iXAxisData;
    }

    /**
     * Returns the y-axis data.
     *
     * @return the y-axis data
     */
    public List<double[]> getYAxisData() {
        return iYAxisData;
    }

    /**
     * Returns the alpha level.
     *
     * @return the alphaLevel
     */
    public float getAlphaLevel() {
        return alphaLevel;
    }

    /**
     * Sets the alpha level
     *
     * @param alphaLevel the alphaLevel to set
     */
    public void setAlphaLevel(float alphaLevel) {
        this.alphaLevel = alphaLevel;
    }

    /**
     * This method sets the start value of the x-axis to zero.
     *
     * @param aXAxisStartAtZero if true the x axis starts at zero
     */
    public void setXAxisStartAtZero(boolean aXAxisStartAtZero) {
        iXAxisStartAtZero = aXAxisStartAtZero;
    }

    /**
     * Set the max padding (distance between the axes and the border of the panel).
     *
     * @param aMaxPadding the new max padding
     */
    public void setMaxPadding(int aMaxPadding) {
        maxPadding = aMaxPadding;
    }

    /**
     * Returns the max padding (distance between the axes and the border of the panel).
     *
     * @return the max padding
     */
    public int getMaxPadding() {
        return maxPadding;
    }

    /**
     * This method sets all the annotations on this instance. Passing a 'null' value for
     * the List will result in simply removing all annotations. Do note that this method
     * will attempt to remove duplicate annotations on a point by deleting any annotation
     * for which the combination of annotation label and annotation x-axis value has been
     * seen before!
     *
     * @param aAnnotations  List with SpectrumAnnotation instances.
     */
    public void setAnnotations(List aAnnotations) {
        if (!aAnnotations.isEmpty()) {
            // Attempt to remove duplicates.
            Set removeDupes = new HashSet(aAnnotations.size());
            for (Iterator lIterator = aAnnotations.iterator(); lIterator.hasNext();) {
                SpectrumAnnotation annotation = (SpectrumAnnotation) lIterator.next();
                String key = annotation.getLabel() + annotation.getMZ();
                if (removeDupes.contains(key)) {
                    // Duplicate, ignore!
                } else {
                    removeDupes.add(key);
                    this.iAnnotations.add(annotation);
                }
            }
        }
    }

    /**
     * This method allows the caller to set the procentual minimal, non-inclusive y-axis value
     * threshold (compared to the highest point in the spectrum or chromatogram) a point must
     * pass before being eligible for annotation.
     *
     * @param aThreshold    double with the procentual y-axis value (as compared to the highest point
     *                      in the spectrum or chromatogram) cutoff threshold for annotation.
     */
    public void setAnnotationYAxisThreshold(double aThreshold) {
        this.iAnnotationYAxisThreshold = (aThreshold / 100) * iYAxisMax;
    }

    /**
     * This method sets the display color for the filename on the panel.
     * Can be 'null' for default coloring.
     *
     * @param aFilenameColor    Color to render the filename in on the panel.
     *                                  Can be 'null' for default coloring.
     */
    public void setFilenameColor(Color aFilenameColor) {
        iFilenameColor = aFilenameColor;
    }

    /**
     * Invoked by Swing to draw components.
     * Applications should not invoke <code>paint</code> directly,
     * but should instead use the <code>repaint</code> method to
     * schedule the component for redrawing.
     * <p/>
     * This method actually delegates the work of painting to three
     * protected methods: <code>paintComponent</code>,
     * <code>paintBorder</code>,
     * and <code>paintChildren</code>.  They're called in the order
     * listed to ensure that children appear on top of component itself.
     * Generally speaking, the component and its children should not
     * paint in the insets area allocated to the border. Subclasses can
     * just override this method, as always.  A subclass that just
     * wants to specialize the UI (look and feel) delegate's
     * <code>paint</code> method should just override
     * <code>paintComponent</code>.
     *
     * @param g the <code>Graphics</code> context in which to paint
     * @see #paintComponent
     * @see #paintBorder
     * @see #paintChildren
     * @see #getComponentGraphics
     * @see #repaint
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (iXAxisData != null) {
            if (iDragged && iDragXLoc > 0) {
                g.drawLine(iStartXLoc, iStartYLoc, iDragXLoc, iStartYLoc);
                g.drawLine(iStartXLoc, iStartYLoc - 2, iStartXLoc, iStartYLoc + 2);
                g.drawLine(iDragXLoc, iStartYLoc - 2, iDragXLoc, iStartYLoc + 2);
            }

            // round the range of the x- and y-axis to integer values
            iXAxisMin = (int) Math.floor(iXAxisMin);
            iXAxisMax = (int) Math.ceil(iXAxisMax);
            iYAxisMin = (int) Math.floor(iYAxisMin);
            iYAxisMax = (int) Math.ceil(iYAxisMax);

            // @TODO: scale?
            drawAxes(g, iXAxisMin, iXAxisMax, 2, iYAxisMin, iYAxisMax);

            // add reference areas that are to be drawn in the back, if any
            drawYAxisReferenceAreas(g, false);
            drawXAxisReferenceAreas(g, false);

            if (currentGraphicsPanelType.equals(GraphicsPanelType.chromatogram)
                    || currentGraphicsPanelType.equals(GraphicsPanelType.profileSpectrum)
                    || currentGraphicsPanelType.equals(GraphicsPanelType.isotopicDistributionProfile)) {
                drawFilledPolygon(g);
            } else {
                drawPeaks(g);
            }
            if (iClicked && iHighLight && iClickedIndex != iHighLightIndex) {
                // Now we should calculate the distance based on the real values and
                // draw a line to show this.
                this.drawMeasurementLine(iClickedIndex, iClickedDataSetIndex,
                        iHighLightIndex, iHighLightDatasetIndex, g, Color.blue, 0);
            }
            if (iHighLight) {
                this.highLightPeak(iHighLightIndex, iHighLightDatasetIndex, g);
                iHighLight = false;
            }
            if (iClicked) {
                this.highlightClicked(iClickedIndex, iHighLightDatasetIndex, g);
            }

            // See if there is a daisychain to display.
            int liClickedSize = iClickedList.size();

            if (liClickedSize > 0) {
                for (int i = 0; i < liClickedSize; i++) {
                    // The last one should be connected to iClicked.
                    int first = ((Integer) iClickedList.get(i)).intValue();
                    int firstDataSetIndex = ((Integer) iClickedListDatasetIndices.get(i)).intValue();

                    int second;
                    int secondDataSetIndex;
                    if ((i + 1) == liClickedSize) {
                        second = iClickedIndex;
                        secondDataSetIndex = iClickedDataSetIndex;
                    } else {
                        second = ((Integer) iClickedList.get(i + 1)).intValue();
                        secondDataSetIndex = ((Integer) iClickedListDatasetIndices.get(i + 1)).intValue();
                    }
                    this.drawMeasurementLine(first, firstDataSetIndex,
                            second, secondDataSetIndex, g, Color.black, 0);
                }
            }

            // See if there is a secondary daisychain to display.
            if (iStoredSequence.size() > 0) {
                for (int i = 1; i < iStoredSequence.size(); i++) {
                    int first = ((Integer) iStoredSequence.get(i - 1)).intValue();
                    int second = ((Integer) iStoredSequence.get(i)).intValue();
                    int firstDataSetIndex = ((Integer) iStoredSequenceDatasetIndices.get(i - 1)).intValue();
                    int secondDataSetIndex = ((Integer) iStoredSequenceDatasetIndices.get(i)).intValue();
                    this.drawMeasurementLine(first, firstDataSetIndex,
                            second, secondDataSetIndex, g, Color.red, g.getFontMetrics().getAscent() + 15);
                }
            }

            // See if we should annotate and if any are present.
            if (iAnnotations != null && iAnnotations.size() > 0 && !miniature) {
                // This HashMap will contain the indices of the points that already carry an annotation
                // as keys (datasetIndex_peakIndex), and the number of annotations as values.
                HashMap<String, Integer> annotatedPeaks = new HashMap<String, Integer>();
                for (int i = 0; i < iAnnotations.size(); i++) {
                    Object o = iAnnotations.get(i);
                    if (o instanceof SpectrumAnnotation) {
                        SpectrumAnnotation sa = (SpectrumAnnotation) o;
                        this.annotate(sa, g, annotatedPeaks);
                    }
                }
            }

            // add reference areas that are to be drawn on top of the data, if any{
            drawYAxisReferenceAreas(g, true);
            drawXAxisReferenceAreas(g, true);

            // @TODO scale.
            // (re-)draw the axes to have them appear in front of the data points
            drawAxes(g, iXAxisMin, iXAxisMax, 2, iYAxisMin, iYAxisMax);
        }
    }

    /**
     * Draws the x-axis reference areas if any.
     *
     * @param g             Graphics object to draw on.
     * @param drawOnTop     if true the areas to be drawn on top of the data are drawn,
     *                      if false the areas that are to be drawn behind the data are drawn
     */
    protected void drawXAxisReferenceAreas(Graphics g, boolean drawOnTop) {

        // used to find the location of the label
        FontMetrics fm = g.getFontMetrics();

        // switch to 2D graphics
        Graphics2D g2d = (Graphics2D) g;

        // store the original color
        Color originalColor = g2d.getColor();
        Composite originalComposite = g2d.getComposite();

        Iterator<String> allReferenceAreas = referenceAreasXAxis.keySet().iterator();

        while (allReferenceAreas.hasNext()) {

            ReferenceArea currentReferenceArea = referenceAreasXAxis.get(allReferenceAreas.next());

            if (drawOnTop == currentReferenceArea.drawOnTop()) {

                // set the color and opacity level
                g.setColor(currentReferenceArea.getAreaColor());
                g2d.setComposite(makeComposite(currentReferenceArea.getAlpha()));

                // set up the data tables for the polygon
                int[] xTemp = new int[4];
                int[] yTemp = new int[4];

                // x range start
                double tempDouble = (currentReferenceArea.getStart() - iXAxisMin) / iXScaleUnit;
                int start = (int) tempDouble;
                if ((tempDouble - start) >= 0.5) {
                    start++;
                }

                // x range end
                tempDouble = (currentReferenceArea.getEnd() - iXAxisMin) / iXScaleUnit;
                int end = (int) tempDouble;
                if ((tempDouble - end) >= 0.5) {
                    end++;
                }

                xTemp[0] = start + iXPadding;
                yTemp[0] = 0;

                xTemp[1] = end + iXPadding;
                yTemp[1] = 0;

                xTemp[2] = end + iXPadding;
                yTemp[2] = this.getHeight() - currentPadding;

                xTemp[3] = start + iXPadding;
                yTemp[3] = this.getHeight() - currentPadding;

                if (start >= iXAxisMin && end <= iYAxisMax) {
                    g2d.fillPolygon(xTemp, yTemp, xTemp.length);
                }

                // draw the label
                if (currentReferenceArea.drawLabel()) {

                    // set the color and opacity level for the label
                    g2d.setColor(Color.BLACK);
                    g2d.setComposite(originalComposite);

                    // insert the label
                    String label = currentReferenceArea.getLabel();
                    g2d.drawString(label, start + iXPadding + 5, (int) fm.getStringBounds(label, g).getHeight());
                }
            }
        }

        // Change the color and alpha level back to its original setting.
        g2d.setColor(originalColor);
        g2d.setComposite(originalComposite);
    }

    /**
     * Draws the y-axis reference areas if any.
     *
     * @param g             Graphics object to draw on.
     * @param drawOnTop     if true the areas to be drawn on top of the data are drawn,
     *                      if false the areas that are to be drawn behind the data are drawn
     */
    protected void drawYAxisReferenceAreas(Graphics g, boolean drawOnTop) {

        // used to find the location of the label
        FontMetrics fm = g.getFontMetrics();

        // switch to 2D graphics
        Graphics2D g2d = (Graphics2D) g;

        // store the original color
        Color originalColor = g2d.getColor();
        Composite originalComposite = g2d.getComposite();

        Iterator<String> allReferenceAreas = referenceAreasYAxis.keySet().iterator();

        while (allReferenceAreas.hasNext()) {

            ReferenceArea currentReferenceArea = referenceAreasYAxis.get(allReferenceAreas.next());

            if (drawOnTop == currentReferenceArea.drawOnTop()) {

                // set the color and opacity level
                g.setColor(currentReferenceArea.getAreaColor());
                g2d.setComposite(makeComposite(currentReferenceArea.getAlpha()));

                // set up the data tables for the polygon
                int[] xTemp = new int[4];
                int[] yTemp = new int[4];

                // y range start
                double tempDouble = (currentReferenceArea.getStart() - iYAxisMin) / iYScaleUnit;
                int start = (int) tempDouble;
                if ((tempDouble - start) >= 0.5) {
                    start++;
                }

                // y range end
                tempDouble = (currentReferenceArea.getEnd() - iYAxisMin) / iYScaleUnit;
                int end = (int) tempDouble;
                if ((tempDouble - end) >= 0.5) {
                    end++;
                }

                xTemp[0] = currentPadding;
                yTemp[0] = this.getHeight() - start - currentPadding;

                xTemp[1] = currentPadding;
                yTemp[1] = this.getHeight() - end - currentPadding;

                xTemp[2] = this.getWidth() - currentPadding;
                yTemp[2] = this.getHeight() - end - currentPadding;

                xTemp[3] = this.getWidth() - currentPadding;
                yTemp[3] = this.getHeight() - start - currentPadding;

                if (start >= iYAxisMin && end <= iYAxisMax) {
                    g2d.fillPolygon(xTemp, yTemp, xTemp.length);
                }

                // draw the label
                if (currentReferenceArea.drawLabel()) {

                    // set the color and opacity level for the label
                    g2d.setColor(Color.BLACK);
                    g2d.setComposite(originalComposite);

                    // insert the label
                    String label = currentReferenceArea.getLabel();
                    g2d.drawString(label, currentPadding + 5, this.getHeight() - end - currentPadding + (int) fm.getStringBounds(label, g).getHeight());
                }
            }
        }

        // Change the color and alpha level back to its original setting.
        g2d.setColor(originalColor);
        g2d.setComposite(originalComposite);
    }

    /**
     * This method reports on the largest x-axis value in the point collection
     * accross all datasets.
     *
     * @return double with the largest x-axis value in the point collection.
     */
    public double getMaxXAxisValue() {

        double maxValue = Double.MIN_VALUE;

        for (int i = 0; i < iXAxisData.size(); i++) {
            if (iXAxisData.get(i)[iXAxisData.get(i).length - 1] > maxValue) {
                maxValue = iXAxisData.get(i)[iXAxisData.get(i).length - 1];
            }
        }

        return maxValue;
    }

    /**
     * This method reports on the smallest x-axis value in the point collection
     * across all datasets.
     *
     * @return double with the smallest x-axis value in the point collection.
     */
    public double getMinXAxisValue() {

        double minValue = Double.MAX_VALUE;

        for (int i = 0; i < iXAxisData.size(); i++) {
            if (iXAxisData.get(i)[0] < minValue) {
                minValue = iXAxisData.get(i)[0];
            }
        }

        return minValue;
    }

    /**
     * This method registers the specified SpectrumPanelListener with this instance
     * and notifies it of all future events. The Listeners will be notified in
     * order of addition (first addition is notified first).
     *
     * @param aListener SpectrumPanelListener to register on this instance.
     */
    public void addSpectrumPanelListener(SpectrumPanelListener aListener) {
        this.iSpecPanelListeners.add(aListener);
    }

    /**
     * This method adds the event listeners to the panel.
     */
    protected void addListeners() {
        this.addMouseListener(new MouseAdapter() {

            /**
             * Invoked when a mouse button has been released on a component.
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                if (iXAxisData != null) {
                    if (e.getButton() == MouseEvent.BUTTON3 || e.getButton() == MouseEvent.BUTTON2) {
                        if (iXAxisStartAtZero) {
                            rescale(0.0, getMaxXAxisValue());
                        } else {

                            double tempMinXValue = getMinXAxisValue();

                            // if isotopic distribution add a little padding on the left side
                            // to make sure that the first peak is not too close to the y-axis
                            if (currentGraphicsPanelType.equals(GraphicsPanelType.isotopicDistributionProfile)
                                    || currentGraphicsPanelType.equals(GraphicsPanelType.isotopicDistributionCentroid)) {
                                tempMinXValue -= 1;

                                if (tempMinXValue < 0) {
                                    tempMinXValue = 0;
                                }
                            }

                            rescale(tempMinXValue, getMaxXAxisValue());
                        }
                        iDragged = false;
                        repaint();
                    } else if (e.getButton() == MouseEvent.BUTTON1) {
                        iEndXLoc = e.getX();
                        int min = Math.min(iEndXLoc, iStartXLoc);
                        int max = Math.max(iEndXLoc, iStartXLoc);
                        double start = iXAxisMin + ((min - iXPadding) * iXScaleUnit);
                        double end = iXAxisMin + ((max - iXPadding) * iXScaleUnit);
                        if (iDragged) {
                            iDragged = false;
                            // Rescale.
                            if ((max - min) > iMinDrag) {
                                rescale(start, end);
                            }
                            iDragXLoc = 0;
                            repaint();
                        }
                    }
                }
            }

            /**
             * Invoked when the mouse has been clicked on a component.
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                if (iXAxisData != null) {
                    if (e.getButton() == MouseEvent.BUTTON1 && e.getModifiersEx() == (MouseEvent.CTRL_DOWN_MASK | MouseEvent.ALT_DOWN_MASK)) {
                        repaint();
                    } else if (e.getButton() == MouseEvent.BUTTON1 && e.getModifiersEx() == MouseEvent.CTRL_DOWN_MASK) {
                        iClicked = false;
                        iClickedList = new ArrayList();
                        iClickedListDatasetIndices = new ArrayList();
                        repaint();
                    } else if (e.getButton() == MouseEvent.BUTTON1 && e.getModifiersEx() == MouseEvent.SHIFT_DOWN_MASK) {
                        // If the clicked point is the last one in the list of previously clicked points,
                        // remove it from the list!
                        if (iClickedList != null && iClickedList.size() > 0 && iHighLightIndex == iClickedIndex) {
                            // Retrieve the previously clicked index from the list and set the currently clicked
                            // one to that value.
                            iClickedIndex = ((Integer) iClickedList.get(iClickedList.size() - 1)).intValue();
                            iClickedDataSetIndex = ((Integer) iClickedListDatasetIndices.get(iClickedListDatasetIndices.size() - 1)).intValue();
                            // Remove the previously clicked index from the list.
                            iClickedList.remove(iClickedList.size() - 1);
                            iClickedListDatasetIndices.remove(iClickedListDatasetIndices.size() - 1);
                            // Repaint.
                            repaint();
                        }
                    } else if (e.getButton() == MouseEvent.BUTTON1 && e.getModifiersEx() == MouseEvent.ALT_DOWN_MASK) {
                        // See if there is a clicked list and if it contains any values.
                        if (iClickedList != null && iClickedList.size() > 0) {
                            iClicked = false;
                            // Reset the clicked list.
                            iClickedList = new ArrayList();
                            iClickedListDatasetIndices = new ArrayList();
                            repaint();
                        }
                    } else if (e.getButton() == MouseEvent.BUTTON1) {
                        if (iClicked && iClickedIndex != iHighLightIndex) {
                            // We need the current point to be stored in the previously clicked
                            // List and set the current one as clicked.
                            iClickedList.add(new Integer(iClickedIndex));
                            iClickedListDatasetIndices.add(new Integer(iClickedDataSetIndex));
                        }
                        iClicked = true;
                        iClickedIndex = iHighLightIndex;
                        iClickedDataSetIndex = iHighLightDatasetIndex;
                        repaint();
                    }
                }
            }

            /**
             * Invoked when a mouse button has been pressed on a component.
             */
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    iStartXLoc = e.getX();
                    iStartYLoc = e.getY();
                }
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {

            /**
             * Invoked when a mouse button is pressed on a component and then
             * dragged.  Mouse drag events will continue to be delivered to
             * the component where the first originated until the mouse button is
             * released (regardless of whether the mouse position is within the
             * bounds of the component).
             */
            @Override
            public void mouseDragged(MouseEvent e) {
                iDragged = true;
                iDragXLoc = e.getX();
                repaint();
            }

            /**
             * Invoked when the mouse button has been moved on a component
             * (with no buttons no down).
             */
            @Override
            public void mouseMoved(MouseEvent e) {
                if (iXAxisData != null && iXAxisDataInPixels != null) {
                    int x = e.getX();
                    int y = e.getY();

                    // this variable is used make sure that the most intense peak within range is highlighted
                    int highestPeakInRange = 0;

                    for (int j = 0; j < iXAxisDataInPixels.size(); j++) {
                        for (int i = 0; i < iXAxisDataInPixels.get(j).length; i++) {
                            int delta = iXAxisDataInPixels.get(j)[i] - x;
                            if (Math.abs(delta) < iPointDetectionTolerance) {
                                int deltaYPixels = y - iYAxisDataInPixels.get(j)[i];
                                if (deltaYPixels < 0
                                        && Math.abs(deltaYPixels) < (getHeight() - iYAxisDataInPixels.get(j)[i])
                                        && highestPeakInRange < (getHeight() - iYAxisDataInPixels.get(j)[i])) {
                                    iHighLight = true;
                                    iHighLightIndex = i;
                                    iHighLightDatasetIndex = j;
                                    highestPeakInRange = (getHeight() - iYAxisDataInPixels.get(j)[i]);
                                    repaint();
                                }
                            } else if (delta >= iPointDetectionTolerance) {
                                break;
                            }
                        }
                    }
                    repaint();
                }
            }
        });
    }

    /**
     * This method rescales the x-axis while notifying the observers.
     *
     * @param aMinXAxisValue  double with the new minimum x-axis value to display.
     * @param aMaxXAxisValue  double with the new maximum x-axis value to display.
     */
    public void rescale(double aMinXAxisValue, double aMaxXAxisValue) {
        this.rescale(aMinXAxisValue, aMaxXAxisValue, true);
    }

    /**
     * Add a x-axis reference area.
     *
     * @param referenceArea the reference area to add
     */
    public void addReferenceAreaXAxis(ReferenceArea referenceArea) {
        referenceAreasXAxis.put(referenceArea.getLabel(), referenceArea);
    }

    /**
     * Removes the x-axis reference area with the given label. Does nothing if no
     * reference with the given label is found.
     *
     * @param label the reference to remove
     */
    public void removeReferenceAreaXAxis(String label) {
        referenceAreasXAxis.remove(label);
    }

    /**
     * Removes all the x-axis reference areas.
     */
    public void removeAllReferenceAreasXAxis() {
        referenceAreasXAxis = new HashMap<String, ReferenceArea>();
    }

    /**
     * Returns all the x-axis references areas as a hashmap, with the labels
     * as the keys.
     *
     * @return hashmap of all reference areas
     */
    public Map<String, ReferenceArea> getAllReferenceAreasXAxis() {
        return referenceAreasXAxis;
    }

    /**
     * Add a y-axis reference area.
     *
     * @param referenceArea the reference area to add
     */
    public void addReferenceAreaYAxis(ReferenceArea referenceArea) {
        referenceAreasYAxis.put(referenceArea.getLabel(), referenceArea);
    }

    /**
     * Removes the y-axis reference area with the given label. Does nothing if no
     * reference with the given label is found.
     *
     * @param label the reference to remove
     */
    public void removeReferenceAreaYAxis(String label) {
        referenceAreasYAxis.remove(label);
    }

    /**
     * Removes all the y-axis reference areas.
     */
    public void removeAllReferenceAreasYAxis() {
        referenceAreasYAxis = new HashMap<String, ReferenceArea>();
    }

    /**
     * Returns all the y-axis references areas as a hashmap, with the labels
     * as the keys.
     *
     * @return hashmap of all reference areas
     */
    public Map<String, ReferenceArea> getAllReferenceAreasYAxis() {
        return referenceAreasYAxis;
    }

    /**
     * Sets the color of data points and line for the dataset with the
     * given dataset index.
     *
     * @param aColor the color to use
     * @param index the index of the dataset
     */
    public void setDataPointAndLineColor(Color aColor, int index) {
        if (index < iDataPointAndLineColor.size() && index >= 0) {
            iDataPointAndLineColor.set(index, aColor);
        }
    }

    /**
     * Sets the color of the area under the curve for chromatograms and
     * profile spectra for the dataset with the given dataset index.
     *
     * @param aColor the color to use
     * @param index the index of the dataset
     */
    public void setAreaUnderCurveColor(Color aColor, int index) {
        if (index < iAreaUnderCurveColor.size() && index >= 0) {
            iAreaUnderCurveColor.set(index, aColor);
        }
    }

    /**
     * Returns the list of colors used for the datasets. Index on dataset.
     *
     * @return the the list of colors used for the datasets
     */
    public List<Color> getAreaUnderCurveColors() {
        return iAreaUnderCurveColor;
    }

    /**
     * This method rescales the x-axis, allowing the caller to specify whether the
     * observers need be notified.
     *
     * @param aMinXAxisValue    double with the new minimum x-axis value to display.
     * @param aMaxXAxisValue    double with the new maximum x-axis value to display.
     * @param aNotifyListeners  boolean to indicate whether the observers should be notified.
     */
    public void rescale(double aMinXAxisValue, double aMaxXAxisValue, boolean aNotifyListeners) {

        xAxisZoomRangeLowerValue = aMinXAxisValue;
        xAxisZoomRangeUpperValue = aMaxXAxisValue;

        // Calculate the new max y-axis value.
        double maxInt = 1.0;

        for (int j = 0; j < iXAxisData.size(); j++) {
            for (int i = 0; i < iXAxisData.get(j).length; i++) {
                double lMass = iXAxisData.get(j)[i];
                if (lMass < aMinXAxisValue) {
                } else if (lMass > aMaxXAxisValue) {
                    break;
                } else {
                    if (iYAxisData.get(j)[i] > maxInt) {
                        maxInt = iYAxisData.get(j)[i];
                    }
                }
            }
        }

        // Init the new params.
        double delta = aMaxXAxisValue - aMinXAxisValue;

        // Round to nearest order of 10, based on displayed delta.
        double tempOoM = (Math.log(delta) / Math.log(10)) - 1;
        if (tempOoM < 0) {
            tempOoM--;
        }

        int orderOfMagnitude = (int) tempOoM;
        double power = Math.pow(10, orderOfMagnitude);
        iXAxisMin = aMinXAxisValue - (aMinXAxisValue % power);
        iXAxisMax = aMaxXAxisValue + (power - (aMaxXAxisValue % power));

        //@TODO just some helpful printouts for when this is refined further.
        //logger.info(" - Delta: " + delta + "\tAdj. delta: " + (iMassMax-iMassMin) + "\tMinMass: " + iMassMin + "\tMaxMass: " + iMassMax + "\tScale: " + power);

        iYAxisMax = maxInt + (maxInt / 10);
        int liSize = iSpecPanelListeners.size();
        RescalingEvent re = new RescalingEvent(this, aMinXAxisValue, aMaxXAxisValue);
        if (aNotifyListeners) {
            for (int i = 0; i < liSize; i++) {
                ((SpectrumPanelListener) iSpecPanelListeners.get(i)).rescaled(re);
            }
        }
    }

    /**
     * This method reads the x and y values from the specified arrays and stores
     * these internally for drawing. The x-axis values are sorted in this step.
     *
     * @param aXAxisData            double[] with the x-axis values.
     * @param aYAxisData            double[] with the corresponding y-axis values.
     * @param dataPointAndLineColor the color to use for the data points and line
     * @param areaUnderCurveColor   the color to use for the area under the curve
     */
    protected void processXAndYData(double[] aXAxisData, double[] aYAxisData, Color dataPointAndLineColor, Color areaUnderCurveColor) {

        // if first dataset, create the dataset array lists
        if (dataSetCounter == 0 || iXAxisData == null || iYAxisData == null) {
            iXAxisData = new ArrayList<double[]>();
            iYAxisData = new ArrayList<double[]>();
        }

        // set the data colors
        iDataPointAndLineColor.add(dataPointAndLineColor);
        iAreaUnderCurveColor.add(areaUnderCurveColor);

        HashMap peaks = new HashMap(aXAxisData.length);

        // add the peaks to the dataset
        for (int i = 0; i < aXAxisData.length; i++) {
            peaks.put(Double.valueOf(aXAxisData[i]), Double.valueOf(aYAxisData[i]));
        }

        // add the new dataset
        iXAxisData.add(new double[peaks.size()]);
        iYAxisData.add(new double[peaks.size()]);


        // Maximum y-axis value.
        double maxYAxisValue = 0.0;

        // TreeSets are sorted.
        TreeSet masses = new TreeSet(peaks.keySet());
        Iterator iter = masses.iterator();
        int count = 0;
        while (iter.hasNext()) {
            Double key = (Double) iter.next();
            double xValue = key.doubleValue();
            double yValue = ((Double) peaks.get(key)).doubleValue();
            if (yValue > maxYAxisValue) {
                maxYAxisValue = yValue;
            }
            iXAxisData.get(dataSetCounter)[count] = xValue;
            iYAxisData.get(dataSetCounter)[count] = yValue;
            count++;
        }

        // rescale the added dataset
        if (iXAxisStartAtZero) {
            this.rescale(0.0, getMaxXAxisValue());
        } else {
            this.rescale(getMinXAxisValue(), getMaxXAxisValue());
        }

        dataSetCounter++;
    }

    /**
     * This method draws the axes and their labels on the specified Graphics object,
     * taking into account the padding.
     *
     * @param g         Graphics object to draw on.
     * @param aXMin     double with the minimal x value.
     * @param aXMax     double with the maximum x value.
     * @param aXScale   int with the scale to display for the X-axis labels (as used in BigDecimal's setScale).
     * @param aYMin     double with the minimal y value.
     * @param aYMax     double with the maximum y value.
     * @return          int[] with the length of the X axis and Y axis respectively.
     */
    protected int[] drawAxes(Graphics g, double aXMin, double aXMax, int aXScale, double aYMin, double aYMax) {

        // Recalibrate padding so that it holds the axis labels.
        FontMetrics fm = g.getFontMetrics();
        int xAxisLabelWidth = fm.stringWidth(iXAxisLabel);
        int yAxisLabelWidth = fm.stringWidth(iYAxisLabel);
        int minWidth = fm.stringWidth(Double.toString(aYMin));
        int maxWidth = fm.stringWidth(Double.toString(aYMax));
        int max = Math.max(Math.max(xAxisLabelWidth, yAxisLabelWidth), Math.max(minWidth, maxWidth));
        currentPadding = padding;

        if ((padding - max) < 0) {
            currentPadding += max;
            if (currentPadding > maxPadding) {
                currentPadding = maxPadding;
            }
        } else {
            currentPadding *= 2;
        }

        // X-axis.
        int xAxis = (this.getWidth() - (2 * currentPadding));

        // hide any data going slightly below the y-axis
        if (yDataIsPositive) {
            Color currentColor = g.getColor();
            g.setColor(this.getBackground());

            if (miniature) {
                g.fillRect(currentPadding, this.getHeight() - currentPadding, this.getWidth() - currentPadding - 2, 2);
            } else {
                g.fillRect(currentPadding, this.getHeight() - currentPadding, this.getWidth() - currentPadding - 2, 20);
            }

            g.setColor(currentColor);
        }

        g.drawLine(currentPadding, this.getHeight() - currentPadding, this.getWidth() - currentPadding, this.getHeight() - currentPadding);

        if (!miniature) {

            // Arrowhead on X-axis.
            g.fillPolygon(new int[]{this.getWidth() - currentPadding - 3, this.getWidth() - currentPadding - 3, this.getWidth() - currentPadding + 2},
                    new int[]{this.getHeight() - currentPadding + 5, this.getHeight() - currentPadding - 5, this.getHeight() - currentPadding}, 3);

            // X-axis label
            if (iXAxisLabel.equalsIgnoreCase("m/z")) {
                g.drawString(iXAxisLabel, this.getWidth() - (currentPadding - (padding / 2)), this.getHeight() - currentPadding + 4);
            } else {
                g.drawString(iXAxisLabel, this.getWidth() - (xAxisLabelWidth + 5), this.getHeight() - (currentPadding / 2));
            }


            // Y-axis.
            g.drawLine(currentPadding, this.getHeight() - currentPadding, currentPadding, currentPadding / 2);
        }

        iXPadding = currentPadding;
        int yAxis = this.getHeight() - currentPadding - (currentPadding / 2);

        if (!miniature) {

            // Arrowhead on Y axis.
            g.fillPolygon(new int[]{currentPadding - 5, currentPadding + 5, currentPadding},
                    new int[]{(currentPadding / 2) + 3, (currentPadding / 2) + 3, currentPadding / 2 - 2},
                    3);

            // Y-axis label
            if (iYAxisLabel.equalsIgnoreCase("Int")) {
                g.drawString(iYAxisLabel, currentPadding - yAxisLabelWidth, (currentPadding / 2) - 4);
            } else {
                g.drawString(iYAxisLabel, currentPadding - (yAxisLabelWidth / 5), (currentPadding / 2) - 4);
            }
        }

        // Now the tags along the axes.
        this.drawXTags(g, (int) Math.floor(aXMin), (int) Math.ceil(aXMax), aXScale, xAxis, currentPadding);
        int yTemp = yAxis;

        if (iAnnotations != null && iAnnotations.size() > 0 && !miniature) {
            yTemp -= 20;
        }

        iTopPadding = this.getHeight() - yTemp - 5;
        this.drawYTags(g, (int) Math.floor(aYMin), (int) Math.ceil(aYMax), yTemp, currentPadding);

        return new int[]{xAxis, yAxis};
    }

    /**
     * This method draws tags on the X axis.
     *
     * @param aMin          double with the minimum value for the axis.
     * @param aMax          double with the maximum value for the axis.
     * @param aXScale       int with the scale to display for the X-axis labels (as used in BigDecimal's setScale).
     * @param g             Graphics object to draw on.
     * @param aXAxisWidth   int with the axis width in pixels.
     * @param aPadding      int with the amount of padding to take into account.
     */
    protected void drawXTags(Graphics g, int aMin, int aMax, int aXScale, int aXAxisWidth, int aPadding) {

        // Font Metrics. We'll be needing these.
        FontMetrics fm = g.getFontMetrics();

        //this.setFont(new Font(this.getFont().getName(), this.getFont().getStyle(), 18));

        // find the scale unit
        double delta = aMax - aMin;
        iXScaleUnit = delta / aXAxisWidth; // note: do not alter! also used when drawing the peaks

        // The next section will only be drawn for spectra.
        if (currentGraphicsPanelType.equals(GraphicsPanelType.centroidSpectrum)
                || currentGraphicsPanelType.equals(GraphicsPanelType.profileSpectrum)) {

            if (!miniature) {

                // Since we know the scale unit, we also know the resolution.
                // This will be displayed on the bottom line.
                String resolution = "";
                if (showResolution) {
                    resolution = "Resolution: " + new BigDecimal(iXScaleUnit).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                }

                // Also print the MS level and precursor MZ and charge (if known, '?' otherwise).
                String msLevel_and_optional_precursor = "";
                if (showPrecursorDetails) {
                    msLevel_and_optional_precursor = "MS level: " + iMSLevel;

                    if (iMSLevel > 0) {
                        // Also print the precursor MZ and charge (if known, '?' otherwise).
                        msLevel_and_optional_precursor += "   Precursor M/Z: " + this.iPrecursorMZ + " (" + this.iPrecursorCharge + ")";
                    } else {
                        msLevel_and_optional_precursor = "Precursor M/Z: " + this.iPrecursorMZ + " (" + this.iPrecursorCharge + ")";
                    }
                }

                // Finally, we also want the filename.
                String filename = "";
                if (showFileName) {
                    filename = "Filename: " + iFilename;
                }

                int precLength = fm.stringWidth(msLevel_and_optional_precursor);
                int resLength = fm.stringWidth(resolution);
                int xDistance = ((this.getWidth() - (iXPadding * 2)) / 4) - (precLength / 2);
                int fromBottom = fm.getAscent() / 2;
                Font oldFont = this.getFont();

                int smallFontCorrection = 0;
                int yHeight = this.getHeight() - fromBottom;
                int xAdditionForResolution = precLength + 15;
                int xAdditionForFilename = xAdditionForResolution + resLength + 15;

                if (precLength + resLength + 45 + fm.stringWidth(filename) > aXAxisWidth) {
                    g.setFont(new Font(oldFont.getName(), oldFont.getStyle(), oldFont.getSize() - 2));
                    smallFontCorrection = g.getFontMetrics().getAscent();
                    xAdditionForFilename = g.getFontMetrics().stringWidth(msLevel_and_optional_precursor) + 5;
                    xAdditionForResolution = g.getFontMetrics().stringWidth(msLevel_and_optional_precursor) / 2;
                    xDistance = aPadding;
                }

                g.drawString(msLevel_and_optional_precursor, xDistance, yHeight - smallFontCorrection);
                g.drawString(resolution, xDistance + xAdditionForResolution, yHeight);

                Color foreground = null;

                if (iFilenameColor != null) {
                    foreground = g.getColor();
                    g.setColor(iFilenameColor);
                }

                g.drawString(filename, xDistance + xAdditionForFilename, yHeight - smallFontCorrection);

                if (foreground != null) {
                    g.setColor(foreground);
                }

                // Restore original font.
                g.setFont(oldFont);
            }
        }

        if (!miniature) {

            int labelHeight = fm.getAscent() + 5;

            // Find out how many tags we have room for
            int tagWidthEstimate = fm.stringWidth("1545") + 15;
            int numberTimes = (aXAxisWidth / tagWidthEstimate);

            // find the scale unit for the x tags
            double scaleUnitXTags = aXAxisWidth / delta;

            // try to find the optimal distance to use between the tags
            int distanceBetweenTags = findOptimalTagDistance(numberTimes, delta);

            // set up the number formatting
            DecimalFormat numberFormat = new DecimalFormat();
            numberFormat.setGroupingSize(3);
            numberFormat.setGroupingUsed(true);

            if (scientificXAxis) {
                numberFormat = new DecimalFormat(scientificPattern);
            }

            // add the tags and values
            if (delta > 1) {

                long lTagValue = 0;
                // now we can mark the first unit
                for (int i = 0; i < aMax; i++) {

                    if ((aMin + i) % distanceBetweenTags == 0) {

                        int xLoc = (int) (aPadding + (i * scaleUnitXTags));

                        if (xLoc < (aPadding + aXAxisWidth)) {
                            g.drawLine(xLoc, this.getHeight() - aPadding, xLoc, this.getHeight() - aPadding + 3);
                            int labelAsInt = aMin + i;
                            String label = numberFormat.format(labelAsInt);
                            int labelWidth = fm.stringWidth(label);
                            g.drawString(label, xLoc - (labelWidth / 2), this.getHeight() - aPadding + labelHeight);
                            lTagValue = i;
                            break;
                        }
                    }
                }

                // now we can mark the rest of the units
                while (lTagValue < aMax) {
                    int xLoc = (int) (aPadding + (lTagValue * scaleUnitXTags));
                    if (xLoc < (aPadding + aXAxisWidth)) {
                        g.drawLine(xLoc, this.getHeight() - aPadding, xLoc, this.getHeight() - aPadding + 3);
                        long labelAsInt = aMin + lTagValue;
                        String label = numberFormat.format(labelAsInt);
                        int labelWidth = fm.stringWidth(label);
                        g.drawString(label, xLoc - (labelWidth / 2), this.getHeight() - aPadding + labelHeight);
                    }
                    lTagValue = lTagValue + distanceBetweenTags;
                }

            } else {

                // special case for when zoom size is 1Da. we then need to use float values, e.g., 155.1, 155.2 etc.

                // first find how many tags we have room for: 10, 5, 2 or 1
                int numberOfTags = 10;

                // find the scale unit for the x tags
                if (numberTimes >= numberOfTags) {
                    scaleUnitXTags = aXAxisWidth / (double)numberOfTags;
                } else {
                    numberOfTags = 5;

                    if (numberTimes >= numberOfTags) {
                        scaleUnitXTags = aXAxisWidth / numberOfTags;
                    } else {
                        numberOfTags = 2;

                        if (numberTimes >= numberOfTags) {
                            numberOfTags = 1;
                        }
                    }
                }

                // add the tags
                for (int i = 0; i < numberOfTags; i++) {

                    int xLoc = (int) (aPadding + (i * scaleUnitXTags));

                    if (xLoc < (aPadding + aXAxisWidth)) {
                        g.drawLine(xLoc, this.getHeight() - aPadding, xLoc, this.getHeight() - aPadding + 3);
                        double labelAsdouble = aMin + ((1 / (double) numberOfTags) * i);
                        String label = numberFormat.format(labelAsdouble);
                        int labelWidth = fm.stringWidth(label);
                        g.drawString(label, xLoc - (labelWidth / 2), this.getHeight() - aPadding + labelHeight);
                    }
                }
            }
        }
    }

    /**
     * This method draws tags on the Y axis.
     *
     * @param aMin          double with the minimum value for the axis.
     * @param aMax          double with the maximum value for the axis.
     * @param g             Graphics object to draw on.
     * @param aYAxisHeight  int with the axis height in pixels.
     * @param aPadding      int with the amount of padding to take into account.
     */
    protected void drawYTags(Graphics g, int aMin, int aMax, int aYAxisHeight, int aPadding) {

        // Font Metrics. We'll be needing these.
        FontMetrics fm = g.getFontMetrics();
        int labelHeight = fm.getAscent();

        // Find out how many tags we have room for
        int tagHeightEstimate = labelHeight + 10;
        int numberTimes = (aYAxisHeight / tagHeightEstimate);

        // find the scale unit
        double delta = aMax - aMin;
        iYScaleUnit = delta / aYAxisHeight; // note: do not alter! also used when drawing the peaks

        if (!miniature) {

            // find the scale unit for the x tags
            double scaleUnitYTags = aYAxisHeight / delta;

            // ind the optimal distance to use between the tags
            int distanceBetweenTags = findOptimalTagDistance(numberTimes, delta);

            // set up the number formatting
            DecimalFormat numberFormat = new DecimalFormat();
            numberFormat.setGroupingSize(3);
            numberFormat.setGroupingUsed(true);

            if (scientificYAxis) {
                numberFormat = new DecimalFormat(scientificPattern);
            }

            // max y-value to display
            String largestLabel = numberFormat.format((int) aMax);

            // old font storage
            Font oldFont = g.getFont();

            // find the required scaling level for the y-axis tags
            int sizeCounter = 0;
            int margin = aPadding - 10;

            while (g.getFontMetrics().stringWidth(largestLabel) >= margin) {
                sizeCounter++;
                g.setFont(new Font(oldFont.getName(), oldFont.getStyle(), oldFont.getSize() - sizeCounter));
            }

            // set the font to use for the y-axis tags
            if (oldFont.getSize() - sizeCounter > 0) {
                g.setFont(new Font(oldFont.getName(), oldFont.getStyle(), oldFont.getSize() - sizeCounter));
            } else {
                // have to make sure that the font at least has a size of 1
                g.setFont(new Font(oldFont.getName(), oldFont.getStyle(), 1));
            }

            long lTagValue = 0;
            while (lTagValue < aMax) {
                int yLoc = (int) (aPadding + (lTagValue * scaleUnitYTags));
                g.drawLine(aPadding, this.getHeight() - yLoc, aPadding - 3, this.getHeight() - yLoc);
                long labelAsInt = aMin + lTagValue;
                String label = numberFormat.format(labelAsInt);
                int labelWidth = g.getFontMetrics().stringWidth(label) + 5;
                g.drawString(label, aPadding - labelWidth, this.getHeight() - yLoc + (g.getFontMetrics().getAscent() / 2) - 1);
                lTagValue = lTagValue + distanceBetweenTags;
            }

            // restore original font
            g.setFont(oldFont);
        }
    }

    /**
     * Try to find the optimal distance between the tags. The most detailed
     * option is always used, i.e., the option containing the most tags
     * within the current boundaries. Note always returns a round number,
     * e.g., 1, 5, 10, 25, 50, etc.
     *
     * @param maxNumberOfTags   the maxium number of tags there is room for
     * @param delta             the difference between the max and the min value
     * @return                  the optimal distance between the tags
     */
    private int findOptimalTagDistance(int maxNumberOfTags, double delta) {

        // the two smallest tag options
        int[] distanceAlternatives = {1, 5};

        int optimalDistance = 1;
        boolean optimalDistanceFound = false;

        // check if the two minimum tag options can be used
        for (int i = 0; i < distanceAlternatives.length && !optimalDistanceFound; i++) {
            if (delta / distanceAlternatives[i] <= maxNumberOfTags) {
                optimalDistance = distanceAlternatives[i];
                optimalDistanceFound = true;
            }
        }

        int tempOptimalDistance = 10;

        // find the optimal tag distance
        while (!optimalDistanceFound) {

            if (delta / (tempOptimalDistance * 2.5) <= maxNumberOfTags) {
                optimalDistance = Double.valueOf(tempOptimalDistance * 2.5).intValue();
                optimalDistanceFound = true;
            }

            if (delta / (tempOptimalDistance * 5) <= maxNumberOfTags && !optimalDistanceFound) {
                optimalDistance = Double.valueOf(tempOptimalDistance * 5).intValue();
                optimalDistanceFound = true;
            }

            if (delta / (tempOptimalDistance * 10) <= maxNumberOfTags && !optimalDistanceFound) {
                optimalDistance = Double.valueOf(tempOptimalDistance * 10).intValue();
                optimalDistanceFound = true;
            }

            tempOptimalDistance *= 10;
        }

        return optimalDistance;
    }

    /**
     * This method will draw a highlighting triangle + x-value on top of the marked point.
     *
     * @param aIndex int with the index of the point to highlight.
     * @param dataSetIndex the index of the dataset
     * @param g Graphics object to draw the highlighting on.
     */
    protected void highLightPeak(int aIndex, int dataSetIndex, Graphics g) {
        this.highLight(aIndex, dataSetIndex, g, Color.blue, null, 0, true);
    }

    /**
     * This method will draw a highlighting triangle + x-value on top of the clicked marked point.
     *
     * @param aIndex int with the index of the clicked point to highlight.
     * @param dataSetIndex the index of the dataset
     * @param g Graphics object to draw the highlighting on.
     */
    protected void highlightClicked(int aIndex, int dataSetIndex, Graphics g) {
        this.highLight(aIndex, dataSetIndex, g, Color.BLACK, null, 0, true);
    }

    /**
     * This method will highlight the specified point in the specified color by
     * drawing a floating triangle + x-value above it.
     *
     * @param aIndex    int with the index.
     * @param dataSetIndex the index of the dataset
     * @param g Graphics object to draw on
     * @param aColor    Color to draw the highlighting in.
     * @param aComment  String with an optional comment. Can be 'null' in which case
     *                  it will be omitted.
     * @param aPixelsSpacer int that gives the vertical spacer in pixels for the highlighting.
     * @param aShowArrow boolean that indicates whether a downward-pointing arrow and dotted line
     *                           should be drawn over the point.
     */
    protected void highLight(int aIndex, int dataSetIndex, Graphics g, Color aColor, String aComment, int aPixelsSpacer, boolean aShowArrow) {

        int x = iXAxisDataInPixels.get(dataSetIndex)[aIndex];
        int y;

        if (aPixelsSpacer < 0) {
            y = iTopPadding;
        } else {
            y = iYAxisDataInPixels.get(dataSetIndex)[aIndex] - aPixelsSpacer;
            // Correct for absurd heights.
            if (y < iTopPadding / 3) {
                y = iTopPadding / 3;
            }
        }

        // Temporarily change the color
        Color originalColor = g.getColor();
        g.setColor(aColor);

        // Draw the triangle first, if appropriate.
        int arrowSpacer = 6;
        if (aShowArrow) {
            g.fillPolygon(new int[]{x - 3, x + 3, x},
                    new int[]{y - 6, y - 6, y - 3},
                    3);
            arrowSpacer = 9;
        }

        // Now the x-value.
        // If there is any, print the comment instead of the x-value.
        if (aComment != null && !aComment.trim().equals("")) {
            aComment = aComment.trim();
            g.drawString(aComment, x - g.getFontMetrics().stringWidth(aComment) / 2, y - arrowSpacer);
        } else {
            // No comment, so print the x- and y-value. Note: both are rounded to four decimals
            String xValue = Double.toString(Util.roundDouble(iXAxisData.get(dataSetIndex)[aIndex], 4));
            String yValue = Double.toString(Util.roundDouble(iYAxisData.get(dataSetIndex)[aIndex], 4));

            String label = "(" + xValue + ", " + yValue + ")";

            int halfWayMass = g.getFontMetrics().stringWidth(label) / 2;
            g.drawString(label, x - halfWayMass, y - arrowSpacer);
        }

        // If we drew above the point, drop a dotted line.
        if (aPixelsSpacer != 0 && aShowArrow) {
            dropDottedLine(aIndex, dataSetIndex, y + 2, g);
        }

        // Restore original color.
        g.setColor(originalColor);
    }

    /**
     * This method draws a line, measuring the distance between two points in real mass units.
     *
     * @param aFirstIndex int with the first point index to draw from.
     * @param aFirstDatasetIndex the dataset index of the first data point
     * @param aSecondIndex int with the second point index to draw to.
     * @param aSecondDatasetIndex the dataset index of the second data point
     * @param g Graphics object on which to draw.
     * @param aColor Color object with the color for all the drawing.
     * @param aExtraPadding int with an optional amount of extra padding (lower on the graph
     *                      if positive, higher on the graph if negative)
     */
    protected void drawMeasurementLine(int aFirstIndex, int aFirstDatasetIndex, int aSecondIndex, int aSecondDatasetIndex, Graphics g, Color aColor, int aExtraPadding) {

        // First get the x coordinates of the two points.
        int x1 = iXAxisDataInPixels.get(aFirstDatasetIndex)[aFirstIndex];
        int x2 = iXAxisDataInPixels.get(aSecondDatasetIndex)[aSecondIndex];

        if (x1 == 0 && x2 == 0) {
            return;
        } else if (x1 == 0) {
            if (iXAxisData.get(aFirstDatasetIndex)[aFirstIndex] < iXAxisMin) {
                x1 = iXPadding + 1;
            } else {
                x1 = this.getWidth() - iXPadding - 1;
            }
        } else if (x2 == 0) {
            if (iXAxisData.get(aSecondDatasetIndex)[aSecondIndex] < iXAxisMin) {
                x2 = iXPadding + 1;
            } else {
                x2 = this.getWidth() - iXPadding - 1;
            }
        }

        // Now the real x-value difference as a String.
        double delta = Math.abs(iXAxisData.get(aFirstDatasetIndex)[aFirstIndex] - iXAxisData.get(aSecondDatasetIndex)[aSecondIndex]);
        String deltaMass = new BigDecimal(delta).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        String matches = this.findDeltaMassMatches(delta, deltaMassWindow);
        int width = g.getFontMetrics().stringWidth(deltaMass);

        // Vertical position of the bar will the position of the highest point + a margin.
        int y = (int) (iYScaleUnit / iYAxisMax + (iXPadding / 2)) + aExtraPadding;

        // Draw the line, color is black.
        Color originalColor = g.getColor();
        g.setColor(aColor);
        g.drawLine(x1, y, x2, y);
        g.drawLine(x1, y - 3, x1, y + 3);
        g.drawLine(x2, y - 3, x2, y + 3);

        // Drop a dotted line down to the peaks.
        dropDottedLine(aFirstIndex, aFirstDatasetIndex, y - 3, g);
        dropDottedLine(aSecondIndex, aSecondDatasetIndex, y - 3, g);
        int xPosText = Math.min(x1, x2) + (Math.abs(x1 - x2) / 2) - (width / 2);
        g.drawString(deltaMass, xPosText, y - 5);
        if (!matches.trim().equals("")) {
            g.drawString(" (" + matches + ")", xPosText + width, y - 5);
        }

        // Return original color.
        g.setColor(originalColor);
    }

    /**
     * This method drops a dotted line from the specified total height to the
     * top of the indicated point.
     *
     * @param aPeakIndex    int with the index of the point to draw the dotted line for.
     * @param aDatasetIndex the index of the dataset
     * @param aTotalHeight  int with the height (in pixels) to drop the dotted line from.
     * @param g Graphics    object to draw the dotted line on.
     */
    protected void dropDottedLine(int aPeakIndex, int aDatasetIndex, int aTotalHeight, Graphics g) {

        int x = iXAxisDataInPixels.get(aDatasetIndex)[aPeakIndex];
        int y = iYAxisDataInPixels.get(aDatasetIndex)[aPeakIndex];

        // Draw the dotted line.
        if ((y - aTotalHeight) > 10) {
            int start = aTotalHeight;
            while (start < y) {
                g.drawLine(x, start, x, start + 2);
                start += 7;
            }
        }
    }

    /**
     * This method attempts to find a list of known mass deltas,
     * corresponding with the specified x value in the given window.
     *
     * @param aDelta
     * @param aWindow
     * @return String with the description of the matching mass delta
     *                or empty String if none was found.
     */
    protected String findDeltaMassMatches(double aDelta, double aWindow) {
        StringBuilder result = new StringBuilder("");
        boolean appended = false;
        if (iKnownMassDeltas != null) {
            for (Double mass : iKnownMassDeltas.keySet()) {
                if (Math.abs(mass.doubleValue() - aDelta) < aWindow) {
                    if (appended) {
                        result.append("/");
                    } else {
                        appended = true;
                    }
                    result.append(iKnownMassDeltas.get(mass));
                }
            }
        }

        return result.toString();
    }

    /**
     * This method attempts to find the specified SpectrumAnnotation in
     * the current peak list and if so, annotates it correspondingly on the screen.
     *
     * @param aSA               SpectrumAnnotation with the annotation to find.
     * @param g                 Graphics instance to annotate on.
     * @param aAlReadyAnnotated HashMap with the index of a point as key, and the number
     *                          of times it has been annotated as value (or 'null' if not
     *                          yet annotated).
     */
    protected void annotate(SpectrumAnnotation aSA, Graphics g, HashMap<String, Integer> aAlReadyAnnotated) {

        double xValue = aSA.getMZ();
        double error = Math.abs(aSA.getErrorMargin());

        // Only do those that fall within the current visual range.
        if (!(xValue < iXAxisMin || xValue > iXAxisMax)) {

            // See if any match is to be found.
            boolean foundMatch = false;
            int peakIndex = -1;
            int dataSetIndex = -1;

            for (int j = 0; j < iXAxisData.size(); j++) {
                for (int i = 0; i < iXAxisData.get(j).length; i++) {
                    double delta = iXAxisData.get(j)[i] - xValue;
                    if (Math.abs(delta) <= error) {
                        if (!foundMatch) {
                            foundMatch = true;
                            peakIndex = i;
                            dataSetIndex = j;
                        } else {
                            // Oops, we already had one...
                            // Take the one with the largest intensity.
                            if (iYAxisData.get(j)[i] > iYAxisData.get(dataSetIndex)[peakIndex]) {
                                peakIndex = i;
                                dataSetIndex = j;
                            }
                        }
                    } else if (delta > error) {
                        break;
                    }
                }
            }

            // If a match was found and it qualifies against the minimal intensity,
            // we now have a peak index so we can annotate.
            if (foundMatch && iYAxisData.get(dataSetIndex)[peakIndex] > iAnnotationYAxisThreshold) {
                //String label = aSA.getLabel() + " (" + new BigDecimal(mz-iMasses[peakIndex]).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + ")";
                String label = aSA.getLabel();
                int spacer = (int) ((iYAxisData.get(dataSetIndex)[peakIndex] - iYAxisMin) / iYScaleUnit) / 2;
                boolean showArrow = true;
                String key = dataSetIndex + "_" + peakIndex;
                if (aAlReadyAnnotated.containsKey(key)) {
                    int count = ((Integer) aAlReadyAnnotated.get(key)).intValue();
                    spacer += count * (g.getFontMetrics().getAscent() + 2);
                    aAlReadyAnnotated.put(key,(count++));
                    showArrow = false;
                } else {
                    aAlReadyAnnotated.put(key, new Integer(1));
                }
                this.highLight(peakIndex, dataSetIndex, g, aSA.getColor(), label, spacer, showArrow);
            }
        }
    }

    /**
     * This method draws all of the peaks for all datasets in the current
     * x-axis range on the panel.
     *
     * @param g Graphics object to draw on.
     */
    protected void drawPeaks(Graphics g) {

        Color originalColor = g.getColor();

        // Init an array that holds pixel coordinates for each peak.
        iXAxisDataInPixels = new ArrayList<int[]>();
        iYAxisDataInPixels = new ArrayList<int[]>();

        // cycle the datasets
        for (int j = 0; j < iXAxisData.size(); j++) {

            // set the color
            g.setColor(iDataPointAndLineColor.get(j));

            iXAxisDataInPixels.add(new int[iXAxisData.get(j).length]);
            iYAxisDataInPixels.add(new int[iYAxisData.get(j).length]);

            // cycle the peaks for the dataset
            for (int i = 0; i < iXAxisData.get(j).length; i++) {

                double lXAxisValue = iXAxisData.get(j)[i];

                // Only draw those x values within the ('low x value', 'high x value') window.
                if (lXAxisValue < iXAxisMin) {
                } else if (lXAxisValue > iXAxisMax) {
                    break;
                } else {
                    double lYAxisValue = iYAxisData.get(j)[i];

                    // Calculate pixel coordinates for x and y values.
                    // X value first.
                    double tempDouble = (lXAxisValue - iXAxisMin) / iXScaleUnit;
                    int temp = (int) tempDouble;
                    if ((tempDouble - temp) >= 0.5) {
                        temp++;
                    }
                    int xAxisPxl = temp + iXPadding;
                    iXAxisDataInPixels.get(j)[i] = xAxisPxl;

                    // Now intensity.
                    tempDouble = (lYAxisValue - iYAxisMin) / iYScaleUnit;
                    temp = (int) tempDouble;
                    if ((tempDouble - temp) >= 0.5) {
                        temp++;
                    }
                    int yValuePxl = this.getHeight() - (temp + iXPadding);
                    iYAxisDataInPixels.get(j)[i] = yValuePxl;
                    if (iDrawStyle == LINES) {
                        // Draw the line.
                        g.drawLine(xAxisPxl, this.getHeight() - iXPadding, xAxisPxl, yValuePxl);
                    } else if (iDrawStyle == DOTS) {
                        // Draw the dot.
                        g.fillOval(xAxisPxl - iDotRadius, yValuePxl - iDotRadius, iDotRadius * 2, iDotRadius * 2);
                    }
                }
            }
        }

        // Change the color back to its original setting.
        g.setColor(originalColor);
    }

    /**
     * This method draws filled polygons for all of the peaks for all datasets
     * in the current x-axis range on the panel.
     *
     * @param g Graphics object to draw on.
     */
    protected void drawFilledPolygon(Graphics g) {

        // switch to 2D graphics
        Graphics2D g2d = (Graphics2D) g;

        // store the original color
        Color originalColor = g2d.getColor();
        Composite originalComposite = g2d.getComposite();

        // init an array that holds pixel coordinates for each point.
        iXAxisDataInPixels = new ArrayList<int[]>();
        iYAxisDataInPixels = new ArrayList<int[]>();

        // cycle the datasets
        for (int j = 0; j < iXAxisData.size(); j++) {

            iXAxisDataInPixels.add(new int[iXAxisData.get(j).length]);
            iYAxisDataInPixels.add(new int[iYAxisData.get(j).length]);

            // These arrays only contain the visible points.
            ArrayList<Integer> xAxisPointsShown = new ArrayList<Integer>();
            ArrayList<Integer> yAxisPointsShown = new ArrayList<Integer>();

            // cycle the datapoints
            for (int i = 0; i < iXAxisData.get(j).length; i++) {

                double xMeasurement = iXAxisData.get(j)[i];

                // Only draw those x-axis measurements within the ('low x', 'high x') window.
                if (xMeasurement < iXAxisMin) {
                } else if (xMeasurement > iXAxisMax) {
                    break;
                } else {
                    // See if we need to initialize the start index.
                    double yMeasurement = iYAxisData.get(j)[i];

                    // Calculate pixel coordinates for X and Y.
                    // X first.
                    double tempDouble = (xMeasurement - iXAxisMin) / iXScaleUnit;
                    int temp = (int) tempDouble;
                    if ((tempDouble - temp) >= 0.5) {
                        temp++;
                    }
                    int xAxisPxl = temp + iXPadding;
                    iXAxisDataInPixels.get(j)[i] = xAxisPxl;

                    // Now intensity.
                    tempDouble = (yMeasurement - iYAxisMin) / iYScaleUnit;
                    temp = (int) tempDouble;
                    if ((tempDouble - temp) >= 0.5) {
                        temp++;
                    }
                    int yAxisPxl = this.getHeight() - (temp + iXPadding);
                    iYAxisDataInPixels.get(j)[i] = yAxisPxl;

                    // Add to the list of points shwon.
                    xAxisPointsShown.add(xAxisPxl);
                    yAxisPointsShown.add(yAxisPxl);
                }
            }

            // check if there are any data points to draw
            if (!xAxisPointsShown.isEmpty()) {

                // set the color and opacity level
                g.setColor(iAreaUnderCurveColor.get(j));

                if (j != 0) {
                    g2d.setComposite(makeComposite(alphaLevel));
                }

                // First draw the filled polygon.
                int[] xTemp = new int[xAxisPointsShown.size() + 2];
                int[] yTemp = new int[yAxisPointsShown.size() + 2];
                xTemp[0] = xAxisPointsShown.get(0).intValue();
                yTemp[0] = this.getHeight() - iXPadding;
                for (int i = 0; i < xAxisPointsShown.size(); i++) {
                    xTemp[i + 1] = xAxisPointsShown.get(i).intValue();
                    yTemp[i + 1] = yAxisPointsShown.get(i).intValue();
                }
                xTemp[xTemp.length - 1] = xAxisPointsShown.get(xAxisPointsShown.size() - 1).intValue();
                yTemp[xTemp.length - 1] = this.getHeight() - iXPadding;

                // Fill out the chromatogram.
                g2d.fillPolygon(xTemp, yTemp, xTemp.length);

                // set the color
                g.setColor(iDataPointAndLineColor.get(j));

                // Now draw the points, and a line connecting them.
                g2d.drawPolyline(xTemp, yTemp, xTemp.length);

                // Skip the point for the first and last element;
                // these are just there to nicely fill the polygon.
                for (int i = 1; i < xTemp.length - 1; i++) {
                    int x = xTemp[i] - (iPointSize / 2);
                    int y = yTemp[i] - (iPointSize / 2);
                    g2d.fillOval(x, y, iPointSize, iPointSize);
                }

                g2d.setComposite(originalComposite);
            }
        }

        // Change the color back to its original setting.
        g2d.setColor(originalColor);
    }

    /**
     * Helper method for setting the opacity.
     *
     * @param alpha the opacity value, 0 means completely see-through, 1 means opaque.
     * @return an AlphaComposite object
     */
    private AlphaComposite makeComposite(float alpha) {
        int type = AlphaComposite.SRC_OVER;
        return (AlphaComposite.getInstance(type, alpha));
    }

    /**
     * Set if the x-axis tags are to be drawn using scientific annotation. The
     * default is false. The default pattern is "##0.#####E0".
     *
     * @param scientificXAxis if the x-axis tags is to be drawn using scientific annotation
     */
    public void setScientificXAxis(boolean scientificXAxis) {
        this.scientificXAxis = scientificXAxis;
    }

    /**
     * Set if the x-axis tags are to be drawn using scientific annotation. The
     * default is false. For pattern details see java.text.DecimalFormat. The
     * default pattern is "##0.#####E0".
     *
     * @param pattern            the number format pattern to use
     */
    public void setScientificXAxis(String pattern) {
        this.scientificXAxis = true;
        this.scientificPattern = pattern;
    }

    /**
     * Set if the y-axis tags are to be drawn using scientific annotation. The
     * default is false. The default pattern is "##0.#####E0".
     *
     * @param scientificYAxis if the y-axis tags is to be drawn using scientific annotation
     */
    public void setScientificYAxis(boolean scientificYAxis) {
        this.scientificYAxis = scientificYAxis;
    }

    /**
     * Set if the y-axis tags are to be drawn using scientific annotation. The
     * default is false. For pattern details see java.text.DecimalFormat. The
     * default pattern is "##0.#####E0".
     *
     * @param pattern            the number format pattern to use
     */
    public void setScientificYAxis(String pattern) {
        this.scientificYAxis = true;
        this.scientificPattern = pattern;
    }
}
