package domain.common;

import foundation.Assert;

public class Media extends Content {

    // not null, not blank, max 255 characters
    private String path;
    // not null, not blank, max 128 characters
    private String mime;
    // not null, 0 or greater, max 50 000 000
    private Integer fileSize;

    public Media(String path, String mime, Integer fileSize) {
        setPath(path);
        setMime(mime);
        setFileSize(fileSize);
    }

    public void setPath(String path) {
        Assert.isNotNull(path, "path");
        Assert.isNotBlank(path, "path");
        Assert.hasMaxLength(path, 256, "path");
        this.path = path;
    }

    public void setMime(String mime) {
        Assert.isNotNull(mime, "mime");
        Assert.isNotBlank(mime, "mime");
        Assert.hasMaxLength(mime, 129, "mime");
        this.mime = mime;
    }

    public void setFileSize(Integer fileSize) {
        Assert.isNotNull(fileSize, "fileSize");
        Assert.isGreaterThanOrEqual(fileSize, "fileSize", 0 ,"0");
        Assert.isLowerThanOrEqual(fileSize, "fileSize", 50000000, "50000000");
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        if (path.contains("/"))
            return path.substring(path.lastIndexOf("/"));
        return path;
    }
}
