package mx.unam.cfata.labsampleanalyser;

/**
 * Created by José Luis Rodríguez Fragoso.
 */

public class card_item {
    private String sample_name;
    private int thumbnail;

    public card_item(int thumbnail, String sample_name) {
        this.sample_name = sample_name;
        this.thumbnail = thumbnail;
    }

    public String getSample_name() {
        return sample_name;
    }

    public int getThumbnail() {
        return thumbnail;
    }
}
