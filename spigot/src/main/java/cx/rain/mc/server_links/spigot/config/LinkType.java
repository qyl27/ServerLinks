package cx.rain.mc.server_links.spigot.config;

public enum LinkType {
    CUSTOM(-1),
    BUG_REPORT(0),
    COMMUNITY_GUIDELINES(1),
    SUPPORT(2),
    STATUS(3),
    FEEDBACK(4),
    COMMUNITY(5),
    WEBSITE(6),
    FORUMS(7),
    NEWS(8),
    ANNOUNCEMENTS(9)
    ;

    private final int id;

    LinkType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
