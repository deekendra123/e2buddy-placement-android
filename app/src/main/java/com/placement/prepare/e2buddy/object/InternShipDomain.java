package com.placement.prepare.e2buddy.object;

public class InternShipDomain {
    private int domainId;
    private String domainName;
    private String numberOfOpening;

    public InternShipDomain(int domainId, String domainName, String numberOfOpening) {
        this.domainId = domainId;
        this.domainName = domainName;
        this.numberOfOpening = numberOfOpening;
    }

    public int getDomainId() {
        return domainId;
    }

    public String getDomainName() {
        return domainName;
    }

    public String getNumberOfOpening() {
        return numberOfOpening;
    }
}
