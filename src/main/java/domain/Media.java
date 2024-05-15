package domain;

public class Media extends Content {

    // not null, not blank, max 255 characters
    private String path;
    // not null, not blank, max 128 characters
    private String mime;
    // not null, 0 or greater, max 50 000 000
    private Integer fileSize;
}
