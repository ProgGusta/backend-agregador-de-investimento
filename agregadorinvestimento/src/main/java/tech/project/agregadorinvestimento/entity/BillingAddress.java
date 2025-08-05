package tech.project.agregadorinvestimento.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_billing_addresses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BillingAddress {

    @Id
    @Column(name = "account_id")
    private UUID id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "street")
    private String street;

    @Column(name = "number")
    private String number;
}
