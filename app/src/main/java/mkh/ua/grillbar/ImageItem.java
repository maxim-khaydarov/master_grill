package mkh.ua.grillbar;

import android.graphics.Bitmap;

/**
 * Created by ASUS on 22.01.2017.
 */

public class ImageItem {
    private String image;
    private String title;
    private String chislo;
    private int imageview;

    public ImageItem(String image, String title, String chislo) {
        super();
        this.image = image;
        this.title = title;
        this.chislo = chislo;
    }

    public String getColum() {
        return chislo;
    }

    public void setColum(String chislo) {
        this.chislo = chislo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}