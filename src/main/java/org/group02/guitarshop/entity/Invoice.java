package org.group02.guitarshop.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "Invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "customer_address")
    private String customerAddress;

    @Column(name = "customer_message")
    private String customerMessage;

    @Column(name = "total")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private Double total;

    @Column(name = "payment_method")
    private String paymentMethod;

    @CreatedDate
    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "person_id")
    private Integer idPerson;

    @Column(name = "discount_id")
    private Integer idDiscountCode;

    @Column(name = "status")
    private int status;

    @ManyToOne
    @JoinColumn(name = "discount_id", referencedColumnName = "id", insertable = false, updatable = false)
    private DiscountCode discountCodeByIdDiscountCode;

    @OneToMany(mappedBy = "invoiceByIdInvoice")
    private List<InvoiceDetail> invoiceDetailsById;

    @ManyToOne
    @JoinColumn(name = "status", referencedColumnName = "id", insertable = false, updatable = false)
    private InvoiceStatus statusByStatusId;

}
