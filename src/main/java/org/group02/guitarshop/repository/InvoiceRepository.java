package org.group02.guitarshop.repository;

import org.group02.guitarshop.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    List<Invoice> findAllByStatus(int id);
    List<Invoice> findAllByIdPerson(int id);
    List<Invoice> findAllByIdPersonAndStatus(int id, int status);

    @Query(value = "select i from Invoice i where (i.status=2 or i.status=3) and i.createdTime=?1")
    List<Invoice> findByStatus2And3(Date date);

    @Query(value = "select i from Invoice i where (i.status=2 or i.status=3) and i.createdTime between ?1 and ?2")
    List<Invoice> findByStatus2And3FromTo(java.sql.Date startDate, Date date);

    @Query(value = "select SUM(i.total) from Invoice i where (i.status=2 or i.status=3) and i.createdTime=?1")
    Optional<Float> invoiceSubTotalByDate(Date date);

    @Query(value = "select SUM(i.total) from Invoice i where (i.status=2 or i.status=3) and i.createdTime between ?1 and ?2")
    Optional<Float> invoiceSubTotalFromTo(Date startDate, Date endDate);

    @Query(value = "select i from Invoice i where i.id =?1 and i.idPerson=?2")
    Invoice findByIdAndIdPerson(int invoiceId, int userId);
}
