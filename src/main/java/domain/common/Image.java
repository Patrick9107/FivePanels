package domain.common;

public class Image extends Media {

    // 0 or greater, max 1920
    private int width;
    // 0 or greater, max 1080
    private int height;
    // not null, not blank, max 255 characters
    private String alternativeText;
}
