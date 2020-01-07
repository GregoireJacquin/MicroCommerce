package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.web.exceptions.ProduitIntrouvableException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Api(description = "API pour les opérations CRUD sur les produits.")
@RestController
public class ProductController {
    @Autowired
    private ProductDao productDao;

    @RequestMapping(value = "/Produits",method = RequestMethod.GET)
    public MappingJacksonValue listeProduits() {
        List<Product> produits = productDao.findAll();
        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");
        FilterProvider listeDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltre",monFiltre);
        MappingJacksonValue produitsFiltres = new MappingJacksonValue(produits);
        produitsFiltres.setFilters(listeDeNosFiltres);
        return produitsFiltres;

    }
    @ApiOperation(value = "Récupère un produit grâce à son ID à condition que celui-ci soit en stock !")
    @GetMapping(value = "/Produits/{id}")
    public Product affciherUnProduit(@PathVariable int id) {
        Product produit = productDao.findById(id);
        if(produit == null)
            throw new ProduitIntrouvableException("Le produit avec l'id" + id+ " est INTROUVABLE.");
        return produit;
    }
    @GetMapping(value = "test/produits/{prixLimit}")
    public List<Product> testeDeRequetes(@PathVariable int prixLimit) {
        return productDao.findByPrixGreaterThan(400);
    }
    @GetMapping(value = "test/produits/{recherche}")
    public List<Product> testeDeRequetes(@PathVariable String recherche) {
        return productDao.findByNomLike("%"+recherche+"%");
    }
    @PostMapping(value = "/Produits")
    public ResponseEntity<Void> ajouterProduit(@Valid @RequestBody Product product) {
        Product productAdded = productDao.save(product);
        if(productAdded == null)
            return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productAdded.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
    @DeleteMapping(value = "/Produits/{id}")
    public void supprimerProduit(@PathVariable int id){
        productDao.deleteById(id);
    }
    @PutMapping(value = "/Produits")
    public void updateProduit(@RequestBody Product product)
    {
        productDao.save(product);
    }
}
