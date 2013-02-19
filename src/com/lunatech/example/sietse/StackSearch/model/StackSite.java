package com.lunatech.example.sietse.StackSearch.model;

public enum StackSite {
    STACKOVERFLOW("https://www.dropbox.com/s/79j7gciilh2z53b/so-users.xml?dl=1"),
    SERVERFAULT("https://www.dropbox.com/sh/k2mapka5cvbw581/5DExjyZ3e3/sf-users.xml?dl=1"),
    SUPERUSER("https://www.dropbox.com/s/tehzgrejdldatn3/su-users.xml?dl=1");

    public final String url;

    private StackSite(String url) {
        this.url = url;
    }
}
