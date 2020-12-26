package model.post;

/*CREATE TABLE media (
        media_type varchar2(20),
        url varchar2(1000),
        PRIMARY KEY (url));*/

public class Media {
    private final String mediaType;
    private final String url;

    public Media(String mediaType, String url) {
        this.url = url;
        this.mediaType = mediaType;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getUrl() { return url; }
}

