package org.group02.guitarshop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.group02.guitarshop.entity.*;
import org.group02.guitarshop.repository.CategoryRepository;
import org.group02.guitarshop.repository.ManufacturerRepository;
import org.group02.guitarshop.repository.ProductRepository;
import org.group02.guitarshop.repository.RateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Service
public class ProductServiceImpl implements ProductService {
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    RateRepository rateRepository;

    public List<Product> ListRelatedProducts;
    public int TotalRate;
    public double AverageRate=0;
    public int[] ListCountRate;

    public List<Category> listAllCategory(){
        return (List<Category>)categoryRepository.findAll();
    }

    public List<Manufacturer> listAllManufacturer(){
        return (List<Manufacturer>)manufacturerRepository.findAll();
    }

    public List<Product> getListRelatedProducts() {
        return ListRelatedProducts;
    }

    @Override
    public int getTotalRate() {
        return TotalRate;
    }

    @Override
    public int[] getListCountRate(){
        return ListCountRate;
    }

    public double getAverageRate() {
        return AverageRate;
    }

    @Override
    public List<Product> listAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Product> findById(int id) {
        return repository.findById(id);
    }

    @Override
    public Product save(Product product) {
        repository.save(product);
        return product;
    }

    @Override
    public void deleteById(int id) {
        repository.deleteById(id);
    }

    @Override
    public Product getProductById(Integer id) {
        Product product = repository.findById(id).get();
        return product;
    }

    @Override
    public List<Product> getMostDiscountProducts() {
        Query query = entityManager.createNativeQuery("SELECT TOP(8) * FROM Product as pd ORDER BY pd.Discount_Amount DESC", Product.class);
        return query.getResultList();
    }

    @Override
    public List<Product> getNewestProducts() {
        Query query = entityManager.createNativeQuery("SELECT TOP(8) * FROM Product as pd ORDER BY pd.Created_Time DESC", Product.class);
        return query.getResultList();
    }

    @Override
    public List<Product> searchProducts(String searchString) {
        List<Product> result = new ArrayList<>();
        for (Product p:repository.findAll()) {
            if(p.getName().toLowerCase().contains(searchString.toLowerCase()))
                result.add(p);
        }
        return result;
    }

    @Override
    public void GetProductExtraInfo(Integer id)
    {
        // Lấy list sản phẩm liên quan
        Query query = entityManager.createNativeQuery("SELECT TOP(8) * FROM Product as p1, (SELECT * FROM PRODUCT as p where p.id="+id+") as p2 where p1.id<>p2.id and p1.category_id=p2.category_id", Product.class);
        ListRelatedProducts = query.getResultList();

        // Đếm tổng số lượt xếp hạng sản phẩm
        query = entityManager.createNativeQuery("SELECT * FROM RATE as r where r.product_id="+id, Rate.class);
        List<Rate> listRate = query.getResultList();
        TotalRate = query.getResultList().size();

        // Đếm số lượt của mỗi bậc xếp hạng
        ListCountRate = new int[5];
        for (int i = 0; i < 5; i++)
        {
            query = entityManager.createNativeQuery("select * from RATE as r where r.product_id="+id + " and r.star="+(i+1),Rate.class);
            ListCountRate[i] = query.getResultList().size();
        }
        if (TotalRate > 0)
        {
            int sum=0;
            for (Rate i: listRate) {
                sum += i.getNumberOfStars();
            }
            AverageRate =  Math.round((sum*1.0/TotalRate)*10.0)/10.0;
        }
        else
        {
            AverageRate = 0;
        }

    }

    @Override
    public void updateQuantity(int productId, int quantity) {
        Product product = getProductById(productId);
        int newQuantity = product.getQuantity() - quantity;
        product.setQuantity(newQuantity);
        repository.save(product);
    }

    @Override
    public void updateQuantityWhenCancelOrder(int productId, int quantity) {
        Product product = getProductById(productId);
        int newQuantity = product.getQuantity() + quantity;
        product.setQuantity(newQuantity);
        repository.save(product);
    }

    @Override
    public List<Rate> listRateByProductId(int id) {
        return rateRepository.findByProductId(id);
    }
}
