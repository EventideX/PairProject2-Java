package cvpr;

/**
 * Some String constants used by PaperSniffer.
 * You can modify it to make PaperSniffer work on other website. (maybe?)
 *
 * @author Liu Zhongyu
 */
public class SnifferConfig {
    // The website need to be sniffed.
    static final String URL_BASE = "http://openaccess.thecvf.com/";

    // The entrance for sniffer
    static final String START_URL = URL_BASE + "CVPR2018.py";

    // The CSS query to select link to paper
    static final String PAPER_URL_QUERY = "dt.ptitle > a";

    // The CSS query to select the title of each paper
    static final String PAPER_TITLE_QUERY = "#papertitle";

    // The CSS query to select the abstract of each paper
    static final String PAPER_ABSTRACT_QUERY = "#abstract";
}
