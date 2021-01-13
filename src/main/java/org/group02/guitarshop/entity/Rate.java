package org.group02.guitarshop.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "Rate")
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "star", nullable = true, precision = 0)
    private int numberOfStars;

    @Column(name = "product_id", nullable = true)
    private Integer idProduct;

    @Column(name="name")
    private String name;

    @Column(name="content")
    private String content;

    @CreationTimestamp
    @Column(name="created_time")
    private Date createdTime;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Product productByIdProduct;

}
