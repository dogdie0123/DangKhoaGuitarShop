package org.group02.guitarshop.models;

import org.group02.guitarshop.entity.Product;

public class WishListItemModel {

    private Product product;

    public WishListItemModel() {
    }

    public WishListItemModel(WishListItemModel wishListItemModel) {
        this.product = wishListItemModel.product;
    }

    public WishListItemModel(Product product) {
        this.product = product;
    }

    public Product getProduct() {

        return product;
    }

    public void setProduct(Product product) {

        this.product = product;
    }
}
