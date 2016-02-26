# Thermo MSF Parser
![logo](http://genesis.ugent.be/uvpublicdata/thermo-msf-parser/thermo-msf-parser_logo.png) 

 * [Project Description](#project-description)
 * [Downloads](#downloads)
 * [Usage](#usage)
 * [Related Projects](#related-projects)
 * [Project Support](#project-support)

----

## Project Description

Thermo ProteomeDiscoverer integrates both the peptide identification and quantification steps in peptide centric proteomics into a single workflow. This makes it one of the preferred programs to analyze proteomics data from Thermo mass spectrometers. The results of the ProteomeDiscoverer workflow are stored in MSF files. Here, we present a Java library to parse and visualize these MSF files.

*Supported MSF Versions:*
Note that we only support MSF files from ProteomeDiscoverer version 1.2, 1.3 and 1.4. We are not planning to invest development time in supporting older versions, but will however, try to ensure forward compatibility as new versions of ProteomeDiscoverer become available.

### Citation
 * [Colaert et al: J Proteome Res. 2011 Aug 5;10(8):3840-3.](http://pubs.acs.org/doi/abs/10.1021/pr2005154)
 * If you use *Thermo MSF Parser* as part of a paper, please include the reference above.

[Go to top of page](#thermo-msf-parser)

----

## Downloads

<a href="http://genesis.ugent.be/maven2/com/compomics/thermo_msf_parser/thermo_msf_parser_GUI/2.0.5/thermo_msf_parser_GUI-2.0.5-archive.zip" onclick="trackOutboundLink('thermo-msf-parser'); return false;"><img src="https://github.com/compomics/thermo-msf-parser/wiki/images/download_button.png" alt="download" /></a>

| Download | Version | Release info |
| :--: |:--:| :--:|
| <a href="http://genesis.ugent.be/maven2/com/compomics/thermo_msf_parser/thermo_msf_parser_GUI/2.0.5/thermo_msf_parser_GUI-2.0.5-archive.zip" onclick="trackOutboundLink('thermo-msf-parser'); return false;"><img src="https://github.com/compomics/thermo-msf-parser/wiki/images/download_button.png" alt="download" /></a> | *2.0.5 - All platforms* |    [Release notes](https://github.com/compomics/thermo-msf-parser/wiki/ReleaseNotes) |

### Example Dataset
[Example Dataset 1](https://github.com/compomics/thermo-msf-parser/blob/master/thermo_msf_parser_API/src/test/resources/test-msf-v-1.2.msf), click on *raw* to download.

[Go to top of page](#thermo-msf-parser)

----

## Usage
See the [wiki](https://github.com/compomics/thermo-msf-parser/wiki) for additional information on how to use *Thermo MSF Parser*.

[Go to top of page](#thermo-msf-parser)

----

## Related Projects

The Thermo MSF viewer makes use of [compomics-utilities](http://code.google.com/p/compomics-utilities) and [JSparklines](http://code.google.com/p/jsparklines) to visualize every bit of information found in the msf files.

Similar parsers for OMSSA and X!Tandem results files are also available, see [OMSSA Parser](http://code.google.com/p/omssa-parser) and [XTandem Parser](http://code.google.com/p/xtandem-parser).

You might also find some of the following projects interesting: [Peptizer](http://code.google.com/p/peptizer),
[dbtoolkit](https://github.com/compomics/dbtoolkit),
[ms-lims](http://code.google.com/p/ms-lims),
[Rover](http://code.google.com/p/compomics-rover) and
[PeptideShaker](http://code.google.com/p/peptide-shaker).

[Go to top of page](#thermo-msf-parser)

----

## Project Support

The *Thermo MSF Parser* project is grateful for the support by:

| Compomics | VIB | Ghent University|
|:--:|:--:|:--:|
| [![compomics](http://genesis.ugent.be/uvpublicdata/image/compomics.png)](http://www.compomics.com) | [![vib](http://genesis.ugent.be/uvpublicdata/image/vib.png)](http://www.vib.be) | [![ugent](http://genesis.ugent.be/uvpublicdata/image/ugent.png)](http://www.ugent.be/en) |

[Go to top of page](#thermo-msf-parser)

----

| IntelliJ | Netbeans | Java | Maven |
|:--:|:--:|:--:|:--:|
| [![intellij](https://www.jetbrains.com/idea/docs/logo_intellij_idea.png)](https://www.jetbrains.com/idea/) | [![netbeans](https://netbeans.org/images_www/visual-guidelines/NB-logo-single.jpg)](https://netbeans.org/) | [![java](http://genesis.ugent.be/uvpublicdata/image/java.png)](http://java.com/en/) | [![maven](http://genesis.ugent.be/uvpublicdata/image/maven.png)](http://maven.apache.org/) |

[Go to top of page](#thermo-msf-parser)
