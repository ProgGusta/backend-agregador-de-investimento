package tech.project.agregadorinvestimento.service;

import org.springframework.stereotype.Service;

import tech.project.agregadorinvestimento.controller.dto.CreateStockDto;
import tech.project.agregadorinvestimento.entity.Stock;
import tech.project.agregadorinvestimento.repository.StockRepository;

@Service
public class StockService {

    private StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void createStock(CreateStockDto createStockDto) {
        // Logic to create a stock
        var stock = new Stock(
            createStockDto.stockId(),
            createStockDto.description()
        );
        
        stockRepository.save(stock);
    }

}
