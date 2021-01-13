package org.group02.guitarshop.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "InvoiceDetail")
public class InvoiceDetail implements Serializable {
    @EmbeddedId
    private InvoiceDetailIdentity invoiceDetailIdentity;

    @Column(name = "quantity", nullable = true)
    private Integer quantity;

    @Column(name="isReviewed", nullable = false)
    private boolean isReviewed;

    public InvoiceDetail() { }

    public InvoiceDetail(InvoiceDetailIdentity invoiceDetailIdentity, Integer quantity, boolean isReviewed) {
        this.invoiceDetailIdentity = invoiceDetailIdentity;
        this.quantity = quantity;
        this.isReviewed = isReviewed;
    }

    public InvoiceDetail(int idInvoice, int idProduct, Integer quantity, boolean isReviewed) {
        InvoiceDetailIdentity invoiceDetailIdentity = new InvoiceDetailIdentity(idInvoice, idProduct);
        this.invoiceDetailIdentity = invoiceDetailIdentity;
        this.quantity = quantity;
        this.isReviewed = isReviewed;
    }

    @MapsId("idInvoice")
    @ManyToOne
    @JoinColumn(name = "invoice_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Invoice invoiceByIdInvoice;

    @MapsId("idProduct")
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Product productByIdProduct;
}
