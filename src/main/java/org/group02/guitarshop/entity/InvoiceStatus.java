package org.group02.guitarshop.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name="InvoiceStatus")
public class InvoiceStatus {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "statusByStatusId")
    private List<Invoice> invoicesByStatus;
}
