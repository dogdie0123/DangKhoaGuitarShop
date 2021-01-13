package org.group02.guitarshop.entity;

import javax.persistence.*;
import java.util.Collection;
import lombok.Data;

@Data
@Entity
@Table(name = "Category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "metadata")
    private String metadata;

    @OneToMany(mappedBy = "categoryByIdCategory")
    private Collection<Product> productsById;

}
