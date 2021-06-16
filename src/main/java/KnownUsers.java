public enum KnownUsers {
    BALD_MAN_WITH_GLASSES_1("https://cdn.shopify.com/s/files/1/1045/8368/files/Bald-man-wearing-black-glasses-frame.jpg", "Bald man with glasses and beard"),
    STANLEY_TUCCI("https://i.pinimg.com/originals/0a/58/69/0a58691f4bad41c65af87dd58a999ecd.jpg", "Stanley Tucci"),
    BALD_MAN("https://images.westend61.de/0001229178pw/portrait-of-bald-man-with-beard-wearing-glasses-KNSF06215.jpg", "Bald man"),
    RACHEL_GREEN("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRxMOohYpnmKcr1RnoFTz3fSA0CQD4GAQyG5Q&usqp=CAU", "Rachen Green"),
    WEIRED_MAN_FROM_STOCK("https://www.rmf.fm/_files/Short_foto/625/0e4fb6e75a40f3ac328f8fe2e841898e.jpg", "Strange man from the Stock");

    private final String urlToFace;
    private final String name;

    KnownUsers(String urlToFace, String name) {
        this.urlToFace = urlToFace;
        this.name = name;
    }

    public String getUrlToFace() {
        return urlToFace;
    }

    public String getName() {
        return name;
    }
}
