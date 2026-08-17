package com.tech_fusion.model.admin;

/**
 * A single row in the "Recent Generated Reports" table, tagged by village.
 */
public class GeneratedReport {

    private final String name;
    private final String type;      // "PDF", "XLSX", ...
    private final String village;
    private final String dateGenerated;

    public GeneratedReport(String name, String type, String village, String dateGenerated) {
        this.name = name;
        this.type = type;
        this.village = village;
        this.dateGenerated = dateGenerated;
    }

    public String getName() { return name; }
    public String getType() { return type; }
    public String getVillage() { return village; }
    public String getDateGenerated() { return dateGenerated; }
}