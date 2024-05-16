package domain.common;

import foundation.Assert;

public class Image extends Media {

    // 0 or greater, max 1920
    private int width;
    // 0 or greater, max 1080
    private int height;
    // not null, not blank, max 255 characters
    private String alternativeText;

    public Image(int width, int height, String alternativeText, String path, String mime, Integer fileSize) {
        super(path, mime, fileSize);
        this.width = width;
        this.height = height;
        this.alternativeText = alternativeText;
    }

    public void setWidth(int width) {
        Assert.isGreaterThanOrEqual(width,"width",0 , "0");
        Assert.isLowerThanOrEqual(width, "width", 1920, "1920");
        this.width = width;
    }

    public void setHeight(int height) {
        Assert.isGreaterThanOrEqual(height,"height",0 , "0");
        Assert.isLowerThanOrEqual(height, "height", 1080, "1920");
        this.height = height;
    }

    public void setAlternativeText(String alternativeText) {
        Assert.isNotNull(alternativeText, "alternativeText");
        Assert.isNotBlank(alternativeText, "alternativeText");
        Assert.hasMaxLength(alternativeText, 256, "alternativeText");
        this.alternativeText = alternativeText;
    }
}
