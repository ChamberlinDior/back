package com.bus.trans.controller;

import com.bus.trans.model.Terminal;
import com.bus.trans.service.TerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/terminals")
public class TerminalController {

    @Autowired
    private TerminalService terminalService;

    @GetMapping
    public ResponseEntity<List<Terminal>> getAllTerminals() {
        List<Terminal> terminals = terminalService.getAllTerminals();
        return ResponseEntity.ok(terminals);
    }

    @PostMapping("/create")
    public ResponseEntity<Terminal> createTerminal(@RequestBody Terminal terminal) {
        try {
            Terminal newTerminal = terminalService.saveTerminal(terminal);
            return ResponseEntity.ok(newTerminal);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/mac/{macAddress}")
    public ResponseEntity<Terminal> getTerminalByMacAddress(@PathVariable String macAddress) {
        Terminal terminal = terminalService.getTerminalByMacAddress(macAddress);
        if (terminal != null) {
            return ResponseEntity.ok(terminal);
        }
        return ResponseEntity.notFound().build();
    }

    // Nouvelle route pour récupérer un terminal par ID
    @GetMapping("/{id}")
    public ResponseEntity<Terminal> getTerminalById(@PathVariable Long id) {
        Terminal terminal = terminalService.getTerminalById(id);
        if (terminal != null) {
            return ResponseEntity.ok(terminal);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/mac/{macAddress}")
    public ResponseEntity<Terminal> updateTerminal(@PathVariable String macAddress, @RequestBody Terminal terminalDetails) {
        Terminal terminal = terminalService.updateTerminal(macAddress, terminalDetails);
        if (terminal != null) {
            return ResponseEntity.ok(terminal);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTerminal(@PathVariable Long id) {
        terminalService.deleteTerminal(id);
        return ResponseEntity.ok().build();
    }
}
