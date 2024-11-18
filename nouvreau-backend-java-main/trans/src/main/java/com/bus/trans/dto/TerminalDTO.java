package com.bus.trans.dto;

public class TerminalDTO {

    private Long id;
    private String terminalId;
    private String typeTerminal;
    private String macAddress;
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
    }}