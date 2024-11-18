package com.bus.trans.service;

import com.bus.trans.model.Terminal;
import com.bus.trans.repository.TerminalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TerminalService {

    @Autowired
    private TerminalRepository terminalRepository;

    public List<Terminal> getAllTerminals() {
        return terminalRepository.findAll();
    }

    public Terminal saveTerminal(Terminal terminal) {
        return terminalRepository.save(terminal);
    }

    public Terminal getTerminalByMacAddress(String macAddress) {
        return terminalRepository.findByMacAddress(macAddress);
    }

    // Nouvelle méthode pour récupérer un terminal par ID
    public Terminal getTerminalById(Long id) {
        return terminalRepository.findById(id).orElse(null);
    }

    public Terminal updateTerminal(String macAddress, Terminal terminalDetails) {
        Terminal terminal = terminalRepository.findByMacAddress(macAddress);
        if (terminal != null) {
            terminal.setTerminalId(terminalDetails.getTerminalId());
            terminal.setTypeTerminal(terminalDetails.getTypeTerminal());
            terminal.setBusMatricule(terminalDetails.getBusMatricule());
            return terminalRepository.save(terminal);
        }
        return null;
    }

    public void deleteTerminal(Long id) {
        terminalRepository.deleteById(id);
    }
}
