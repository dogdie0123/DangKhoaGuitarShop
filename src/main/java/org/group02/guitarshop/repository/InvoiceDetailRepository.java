package org.group02.guitarshop.repository;

import org.group02.guitarshop.entity.InvoiceDetail;
import org.group02.guitarshop.entity.InvoiceDetailIdentity;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
@DynamicUpdate
public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, InvoiceDetailIdentity> {
    @Query("select i from InvoiceDetail i where i.invoiceByIdInvoice.id = ?1 and i.productByIdProduct.id = ?2")
    InvoiceDetail findByInvoiceByIdInvoiceAndProductByIdProduct(int iId, int pId);
}
