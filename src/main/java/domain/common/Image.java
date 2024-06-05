package domain.common;

import static foundation.Assert.*;

public class Image extends Media {

    // 0 or greater, max 1920
    private int width;
    // 0 or greater, max 1080
    private int height;
    // not null, not blank, max 255 characters
    private String alternativeText;

    public Image(int width, int height, String alternativeText, String path, String mime, Integer fileSize) {
        super(path, mime, fileSize);
        setWidth(width);
        setHeight(height);
        setAlternativeText(alternativeText);
    }

    public void setWidth(int width) {
        isGreaterThanOrEqual(width, "width", 0, "0");
        this.width = isLowerThanOrEqual(width, "width", 1920, "1920");
    }

    public void setHeight(int height) {
        isGreaterThanOrEqual(height, "height", 0, "0");
        this.height = isLowerThanOrEqual(height, "height", 1080, "1080");
    }

    public void setAlternativeText(String alternativeText) {
        this.alternativeText = hasMaxLength(alternativeText, 256, "alternativeText");
    }
}
