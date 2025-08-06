package tech.project.agregadorinvestimento.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.project.agregadorinvestimento.controller.dto.CreateStockDto;
import tech.project.agregadorinvestimento.service.StockService;

@RestController
@RequestMapping("/v1/stocks")
public class StockController {

    private StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    // Define endpoints for stock operations here
    @PostMapping
    public ResponseEntity<Void> createStock(@RequestBody CreateStockDto createStockDto) {
        stockService.createStock(createStockDto);
        return ResponseEntity.ok().build();
    }
}
