package org.group02.guitarshop.service;

import org.group02.guitarshop.entity.InvoiceDetail;
import org.springframework.stereotype.Service;

@Service
public interface InvoiceDetailService {
    void insertInvoiceDetail(InvoiceDetail invoiceDetail);
    InvoiceDetail findById(int iId, int pId);
    void save(InvoiceDetail invoiceDetail);
}
