package org.group02.guitarshop.service;

import org.group02.guitarshop.entity.InvoiceDetail;
import org.group02.guitarshop.repository.InvoiceDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("invoiceDetailService")
public class InvoiceDetailServiceImpl implements InvoiceDetailService {
    @Autowired
    private InvoiceDetailRepository repository;

    @Override
    public void insertInvoiceDetail(InvoiceDetail invoiceDetail) { repository.save(invoiceDetail); }

    @Override
    public InvoiceDetail findById(int iId, int pId) {
        return repository.findByInvoiceByIdInvoiceAndProductByIdProduct(iId, pId);
    }

    @Override
    public void save(InvoiceDetail invoiceDetail) {
        repository.save(invoiceDetail);
    }


}
