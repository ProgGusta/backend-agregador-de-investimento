package tech.project.agregadorinvestimento.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.project.agregadorinvestimento.entity.AccountStock;
import tech.project.agregadorinvestimento.entity.AccountStockId;

@Repository
public interface AccountStockRepository extends JpaRepository<AccountStock, AccountStockId> {

}
