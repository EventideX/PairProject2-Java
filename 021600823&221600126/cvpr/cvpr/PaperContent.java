package cvpr;

/**
 * A simple paper content entity which contains title and abstract
 *
 * @author Liu Zhongyu
 */
public class PaperContent {
    private String paperTitle;
    private String paperAbstract;

    public PaperContent() {
        this.paperTitle = "";
        this.paperAbstract = "";
    }

    public PaperContent(String paperTitle, String paperAbstract) {
        this.paperTitle = paperTitle;
        this.paperAbstract = paperAbstract;
    }

    public String getPaperTitle() {
        return paperTitle;
    }

    public void setPaperTitle(String paperTitle) {
        this.paperTitle = paperTitle;
    }

    public String getPaperAbstract() {
        return paperAbstract;
    }

    public void setPaperAbstract(String paperAbstract) {
        this.paperAbstract = paperAbstract;
    }
}
