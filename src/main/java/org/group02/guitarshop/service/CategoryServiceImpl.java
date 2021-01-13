package org.group02.guitarshop.service;

import org.group02.guitarshop.entity.Category;
import org.group02.guitarshop.entity.Product;
import org.group02.guitarshop.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private CategoryRepository repository;

    @Override
    public Category getCategoryById(int id){
        return repository.findById(id).get();
    }

    @Override
    public Category getCategoryByMetadata(String metadata) {
        List<Category> categories = entityManager.createNativeQuery("SELECT * FROM Category WHERE metadata = ?", Category.class)
                                    .setParameter(1, metadata)
                                    .getResultList();

        return categories.get(0);
    }

    @Override
    public int getQuantityByIdCategory(int idCategory) {
        List<Integer> quantities = entityManager.createNativeQuery("SELECT COUNT(category_id) FROM Product WHERE category_id = ?")
                                   .setParameter(1, idCategory)
                                   .getResultList();
        return quantities.get(0);
    }

    @Override
    public List<Category> getAllCategories(){
        return repository.findAll();
    }

    @Override
    public List<Product> getListProduct(int idCategory, String manufacturer, String style, int minPrice, int maxPrice) {
        List<Product> listProduct = entityManager.createNativeQuery("SELECT p.* FROM Product p INNER JOIN Manufacturer m ON p.manufacturer_id = m.id " +
                "WHERE p.category_id = ?1 AND m.name = IIF(?2 IS NULL, m.name, ?2) AND p.style = IIF(?3 IS NULL, p.style, ?3) AND p.price >= IIF(?4 = 0, 0, ?4) AND p.price <= IIF(?5 = 10000000, 10000000, ?5)" +
                "ORDER BY p.id", Product.class)
                .setParameter(1, idCategory)
                .setParameter(2, manufacturer.equals("All") ? null : manufacturer)
                .setParameter(3, style.equals("All") ? null : style)
                .setParameter(4, minPrice)
                .setParameter(5, maxPrice)
                .getResultList();

        return listProduct;
    }

    @Override
    public List<String> getListOfManufacturerNames(int idCategory) {
        List<Object[]> list = entityManager.createNativeQuery("SELECT m.name, COUNT(m.name) FROM Product p INNER JOIN Manufacturer m ON p.manufacturer_id = m.id WHERE category_id = ? GROUP BY m.name ORDER BY m.name")
                              .setParameter(1, idCategory)
                              .getResultList();

        List<String> manufacturerList = new ArrayList<>();

        for (Object[] item : list)
            manufacturerList.add((String) item[0]);

        return manufacturerList;
    }

    @Override
    public List<String> getListOfStyleNames(int idCategory) {
        List<Object[]> list = entityManager.createNativeQuery("SELECT style, COUNT(style) FROM Product WHERE category_id = ? GROUP BY style ORDER BY style")
                              .setParameter(1, idCategory)
                              .getResultList();

        List<String> styleList = new ArrayList<>();

        for (Object[] item : list)
            styleList.add((String) item[0]);

        return styleList;
    }

    @Override
    public void delete(int id){
        repository.deleteById(id);
    }

    @Override
    public void save(Category category){
        repository.save(category);
    }
}
