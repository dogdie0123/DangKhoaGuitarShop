package org.group02.guitarshop.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Table(name = "Product")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "price", nullable = true)
    private Double price;

    @Column(name = "discount_amount")
    private Integer discountAmount;

    @Column(name = "average_rate", nullable = true, precision = 0)
    private Double averageRate;

    @Lob
    @Column(name = "main_img", nullable = true)
    private String imageThumbnail;

    @Column(name = "quantity", nullable = true)
    private Integer quantity;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "style", nullable = true)
    private String style;

    @Column(name = "warranty", nullable = true)
    private String warrantyPeriod;

    @CreationTimestamp
    @Column(name = "created_time", nullable = true)
    private Timestamp createdTime;

    @Column(name = "category_id", nullable = true)
    private Integer idCategory;

    @Column(name = "manufacturer_id", nullable = true)
    private Integer idManufacturer;

    @OneToMany(mappedBy = "productByIdProduct")
    private Collection<InvoiceDetail> invoiceDetailsById;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Category categoryByIdCategory;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Manufacturer manufacturerByIdManufacturer;

    @OneToMany(mappedBy = "productByIdProduct")
    private Collection<Rate> ratesById;

    @OneToMany(mappedBy = "product")
    private Collection<Message> commentCollection;

    @Transient
    public String getImagePath(){
        if (imageThumbnail == "")
            return "/products/0/default_img.jpg";
        else{
            return "/products/" + id + "/" + imageThumbnail;
        }
    }
}
