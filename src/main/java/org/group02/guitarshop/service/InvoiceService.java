package org.group02.guitarshop.service;

import org.group02.guitarshop.entity.Invoice;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface InvoiceService {
    Integer insertInvoice(Invoice invoice);
    Invoice getInvoiceById(Integer id);
    List<Invoice> listAll();
    List<Invoice> listAllByUserID(int id);
    List<Invoice> listAllByUserIDAndStatus(int id, int status);
    List<Invoice> listByStatus23(Date date);
    List<Invoice> listByStatus23FromTo(Date startDate, Date endDate);
    void delete(int id);
    void save(Invoice invoice);
    List<Invoice> listByStatus(int status);
    void setInvoiceStatus(int id, String status);
    Optional<Float> invoiceSubTotalByDate(Date date);
    Optional<Float> invoiceSubTotalFromTo(Date startDate, Date endDate);
    Invoice findByInvoiceIdAndUserId(int invoiceId, int userId);
}
