package domain.common;

import foundation.Assert;

public class TextContent extends Content {
    // not null, not blank, max 1024 characters
    private String text;

    public TextContent(String text) {
        setText(text);
    }

    public void setText(String text) {
        Assert.isNotNull(text, "text");
        Assert.isNotBlank(text, "text");
        Assert.hasMaxLength(text,1025, "text");
        this.text = text;
    }
}
