package com.tech_fusion.model.gramsevak;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Bill {
    SimpleStringProperty citizenname;
    SimpleStringProperty housename;
    SimpleStringProperty billType;
    SimpleDoubleProperty amount;
    SimpleStringProperty dueDate;
    SimpleStringProperty status;

    public Bill(String citizenName, String houseNo, String billType, double amount, String dueDate, String status) {

        this.citizenname = new SimpleStringProperty(citizenName);

        this.housename = new SimpleStringProperty(houseNo);

        this.billType = new SimpleStringProperty(billType);

        this.amount = new SimpleDoubleProperty(amount);

        this.dueDate = new SimpleStringProperty(dueDate);

        this.status = new SimpleStringProperty(status);
    }

    public String getCitizenname() {
        return citizenname.get();
    }

    public void setCitizenname(SimpleStringProperty citizenname) {
        this.citizenname = citizenname;
    }

    public String getHousename() {
        return housename.get();
    }

    public void setHousename(SimpleStringProperty housename) {
        this.housename = housename;
    }

    public String getBillType() {
        return billType.get();
    }

    public void setBillType(SimpleStringProperty billType) {
        this.billType = billType;
    }

    public double getAmount() {
        return amount.get();
    }

    public void setAmount(SimpleDoubleProperty amount) {
        this.amount = amount;
    }

    public String getDueDate() {
        return dueDate.get();
    }

    public void setDueDate(SimpleStringProperty dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(SimpleStringProperty status) {
        this.status = status;
    }
}