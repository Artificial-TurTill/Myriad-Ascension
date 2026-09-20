package io.github.artificialturtill.myriadascension.organization;

public final class OrganizationStanding {
    private int serviceMerit;
    private boolean member;
    private String rank = "";

    public int serviceMerit() {
        return serviceMerit;
    }

    public void addServiceMerit(int amount) {
        serviceMerit = Math.max(0, serviceMerit + Math.max(0, amount));
    }

    public boolean member() {
        return member;
    }

    public void setMember(boolean member) {
        this.member = member;
    }

    public String rank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank == null ? "" : rank;
    }

    public OrganizationStanding copy() {
        OrganizationStanding copy = new OrganizationStanding();
        copy.serviceMerit = serviceMerit;
        copy.member = member;
        copy.rank = rank;
        return copy;
    }
}
