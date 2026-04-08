package com.bank.controller;

import com.bank.dto.SimulationRequest;
import com.bank.dto.SimulationResult;
import com.bank.service.SimulationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/simulation")
public class SimulationController {

    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping("/run")
    public ResponseEntity<SimulationResult> runSimulation(@Valid @RequestBody SimulationRequest request) {
        return ResponseEntity.ok(simulationService.run(request));
    }
}
