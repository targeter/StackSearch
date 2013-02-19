package com.lunatech.example.sietse.StackSearch.model;

public class User {
    public final StackSite site;
    public final Long id;
    public final String displayName;
    public final String about;
    public final Integer reputation;

    public User(StackSite site, Long id, String displayName, String about, Integer reputation) {
        this.site = site;
        this.id = id;
        this.displayName = displayName;
        this.about = about;
        this.reputation = reputation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final User user = (User) o;

        if (!id.equals(user.id)) return false;
        return site == user.site;

    }

    @Override
    public int hashCode() {
        int result = site.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("User");
        sb.append("{site=").append(site);
        sb.append(", id=").append(id);
        sb.append(", displayName='").append(displayName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
