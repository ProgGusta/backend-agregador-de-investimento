package tech.project.agregadorinvestimento.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import tech.project.agregadorinvestimento.client.BrapiClient;
import tech.project.agregadorinvestimento.controller.dto.AccountStockResponseDto;
import tech.project.agregadorinvestimento.controller.dto.AssociateAccountStockDto;
import tech.project.agregadorinvestimento.entity.AccountStock;
import tech.project.agregadorinvestimento.entity.AccountStockId;
import tech.project.agregadorinvestimento.repository.AccountRepository;
import tech.project.agregadorinvestimento.repository.AccountStockRepository;
import tech.project.agregadorinvestimento.repository.StockRepository;

@Service
public class AccountService {

    @Value("#{environment.TOKEN}")
    private String TOKEN;

    private AccountRepository accountRepository;
    private StockRepository stockRepository;
    private AccountStockRepository accountStockRepository;
    private BrapiClient brapiClient;

    public AccountService(AccountRepository accountRepository, StockRepository stockRepository, AccountStockRepository accountStockRepository, BrapiClient brapiClient) {
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
        this.accountStockRepository = accountStockRepository;
        this.brapiClient = brapiClient;
    }

    public void associateStock(String accountId, AssociateAccountStockDto associateAccountStockDto) {
        var account = accountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var stock = stockRepository.findById(associateAccountStockDto.stockId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // DTO -> ENTITY
        var id = new AccountStockId(account.getAccountId(), stock.getStockId());
        var entity = new AccountStock(
            id,
            account,
            stock,
            associateAccountStockDto.quantity()
        );

        accountStockRepository.save(entity);
    }

    public List<AccountStockResponseDto> listStocks(String accountId) {
        var account = accountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return account.getAccountStocks()
                .stream()
                .map(as -> new AccountStockResponseDto(
                        as.getStock().getStockId(),
                        as.getQuantity(),
                        getTotal(as.getQuantity(), as.getStock().getStockId())
                ))
                .toList();
    }

    private double getTotal(Integer quantity, String stockId) {
        var response = brapiClient.getQuote(TOKEN, stockId);

        var price =  response.results().getFirst().regularMarketPrice();

        return price * quantity;
    }
}
