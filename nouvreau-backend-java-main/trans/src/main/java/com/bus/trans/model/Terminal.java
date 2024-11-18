package com.bus.trans.model;

import jakarta.persistence.*;

@Entity
public class Terminal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String terminalId;

    @Column(nullable = false)
    private String typeTerminal;

    @Column(nullable = false, unique = true)
    private String macAddress;

    @Column(nullable = true)
    private String busMatricule;

    // Getters et Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTypeTerminal() {
        return typeTerminal;
    }

    public void setTypeTerminal(String typeTerminal) {
        this.typeTerminal = typeTerminal;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getBusMatricule() {
        return busMatricule;
    }

    public void setBusMatricule(String busMatricule) {
        this.busMatricule = busMatricule;
    }
}