package tech.project.agregadorinvestimento.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.project.agregadorinvestimento.entity.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, String> {

}
