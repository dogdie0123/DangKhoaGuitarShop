package org.group02.guitarshop.service;

import org.group02.guitarshop.entity.Category;
import org.group02.guitarshop.entity.Product;
import java.util.List;

public interface CategoryService {

    Category getCategoryById(int id);
    Category getCategoryByMetadata(String metadata);
    int getQuantityByIdCategory(int id);
    List<Category> getAllCategories();
    List<Product> getListProduct(int id, String manufacturer, String style, int minPrice, int maxPrice);
    List<String> getListOfManufacturerNames(int id);
    List<String> getListOfStyleNames(int id);
    void delete(int id);
    void save(Category category);
}