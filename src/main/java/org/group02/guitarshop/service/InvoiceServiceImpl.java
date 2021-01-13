package org.group02.guitarshop.service;

import org.group02.guitarshop.entity.Invoice;
import org.group02.guitarshop.repository.InvoiceDetailRepository;
import org.group02.guitarshop.repository.InvoiceRepository;
import org.group02.guitarshop.repository.InvoiceStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service("invoiceService")
public class InvoiceServiceImpl implements InvoiceService {
    @Autowired
    InvoiceRepository repository;

    @Autowired
    InvoiceDetailRepository detailRepository;

    @Autowired
    InvoiceStatusRepository statusRepository;

    @Override
    public Integer insertInvoice(Invoice invoice) {
        Invoice returnedInvoice = repository.save(invoice);
        return returnedInvoice.getId();
    }

    @Override
    public Invoice getInvoiceById(Integer id) {
        Invoice invoice = repository.findById(id).get();
        return invoice;
    }

    @Override
    public List<Invoice> listAll(){
        return repository.findAll();
    }

    @Override
    public List<Invoice> listAllByUserID(int id) {
        return repository.findAllByIdPerson(id);
    }

    @Override
    public List<Invoice> listAllByUserIDAndStatus(int id, int status) {
        return repository.findAllByIdPersonAndStatus(id, status);
    }

    @Override
    public List<Invoice> listByStatus23(Date date) {
        return repository.findByStatus2And3(date);
    }

    @Override
    public List<Invoice> listByStatus23FromTo(Date startDate, Date endDate) {
        return repository.findByStatus2And3FromTo(startDate,endDate);
    }

    @Override
    public void delete(int id){
        repository.deleteById(id);
    }

    @Override
    public void save(Invoice invoice) {
        repository.save(invoice);
    }

    @Override
    public List<Invoice> listByStatus(int status) {
        return repository.findAllByStatus(status);
    }

    @Override
    public void setInvoiceStatus(int id, String status) {
    }

    @Override
    public Optional<Float> invoiceSubTotalByDate(Date date) {
        return repository.invoiceSubTotalByDate(date);
    }

    @Override
    public Optional<Float> invoiceSubTotalFromTo(Date startDate, Date endDate) {
        return repository.invoiceSubTotalFromTo(startDate, endDate);
    }

    @Override
    public Invoice findByInvoiceIdAndUserId(int invoiceId, int userId) {
        return repository.findByIdAndIdPerson(invoiceId, userId);
    }


}
