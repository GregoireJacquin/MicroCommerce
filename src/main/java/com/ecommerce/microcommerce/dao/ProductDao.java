package com.ecommerce.microcommerce.dao;

import com.ecommerce.microcommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDao extends JpaRepository<Product,Integer> {
    /*public List<Product> findAll();
    public Product findById(int id);

    public Product save(Product product);*/
    Product findById(int id);
    List<Product> findByPrixGreaterThan(int prixLimit);
    List<Product> findByNomLike(String recherche);
}
