package com.ooush.api.entity.enumerables;

public enum WeightDenomination {
    KG("kg"),
    LBS("lbs");

    private final String label;

    private WeightDenomination(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
