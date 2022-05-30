package com.mobilishop.api.service;

import com.mobilishop.api.model.Category;
import com.mobilishop.api.model.Product;
import com.mobilishop.api.repository.CategoryRepository;
import com.mobilishop.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class AppService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostConstruct
    private void initApp() {
        initCategories();
        initProducts();
    }

    private void initCategories() {
        Category category = new Category();
        category.setName("Electronics");
        category.setDescription("Electronics articles");
        category.setImage("assets/images/electronics.jpg");
        categoryRepository.save(category);

        category = new Category();
        category.setName("Clothing");
        category.setDescription("Clothing articles");
        category.setImage("assets/images/clothing.jpg");
        categoryRepository.save(category);

        category = new Category();
        category.setName("Shoes");
        category.setDescription("Shoes articles");
        category.setImage("assets/images/shoes.jpg");
        categoryRepository.save(category);

        category = new Category();
        category.setName("Accessories");
        category.setDescription("Accessories articles");
        category.setImage("assets/images/accessories.jpg");
        categoryRepository.save(category);
    }

    private void initProducts() {
        Product product = new Product();
        product.setName("Samsung Galaxy S8");
        product.setDescription("Samsung Galaxy S8 smartphone with 4.30-inch 1440x3040 pixels display, " +
                "Octa-core processor, Exynos 9820 Octa, " +
                "64GB of internal memory, " +
                "Android v8.0 (Oreo), " +
                "5.8 inches, " +
                "3250 mAh battery, " +
                "black");
        product.setPrice(849.99);
        product.setImage("assets/images/samsung-galaxy-s8.jpg");
        product.setCategory(categoryRepository.findByName("Electronics"));
        productRepository.save(product);

        product = new Product();
        product.setName("Apple iPhone X");
        product.setDescription("Apple iPhone X smartphone with 4.00-inch 1080x2340 pixels display, " +
                "Apple A12 Bionic chip, " +
                "64GB of internal memory, " +
                "iOS 12, " +
                "12.5 inches, " +
                "3174 mAh battery, " +
                "silver");
        product.setPrice(999.99);
        product.setImage("assets/images/apple-iphone-x.jpg");
        product.setCategory(categoryRepository.findByName("Electronics"));
        productRepository.save(product);

        product = new Product();
        product.setName("Nike Air Max");
        product.setDescription("Nike Air Max smartphone with 4.00-inch 1080x2340 pixels display, " +
                "Apple A12 Bionic chip, " +
                "64GB of internal memory, " +
                "iOS 12, " +
                "12.5 inches, " +
                "3174 mAh battery, " +
                "silver");
        product.setPrice(699.99);
        product.setImage("assets/images/nike-air-max.jpg");
        product.setCategory(categoryRepository.findByName("Clothing"));
        productRepository.save(product);

        product = new Product();
        product.setName("Adidas Yeezy");
        product.setDescription("Adidas Yeezy smartphone with 4.00-inch 1080x2340 pixels display, " +
                "Apple A12 Bionic chip, " +
                "64GB of internal memory, " +
                "iOS 12, " +
                "12.5 inches, " +
                "3174 mAh battery, " +
                "silver");
        product.setPrice(699.99);
        product.setImage("assets/images/adidas-yeezy.jpg");
        product.setCategory(categoryRepository.findByName("Shoes"));
        productRepository.save(product);

        product = new Product();
        product.setName("Gucci Bag");
        product.setDescription("Gucci Bag smartphone with 4.00-inch 1080x2340 pixels display, " +
                "Apple A12 Bionic chip, " +
                "64GB of internal memory, " +
                "iOS 12, " +
                "12.5 inches, " +
                "3174 mAh battery, " +
                "silver");
        product.setPrice(699.99);
        product.setImage("assets/images/gucci-bag.jpg");
        product.setCategory(categoryRepository.findByName("Accessories"));
        productRepository.save(product);

        product = new Product();
        product.setName("Gucci Watch");
        product.setDescription("Gucci Watch smartphone with 4.00-inch 1080x2340 pixels display, " +
                "Apple A12 Bionic chip, " +
                "64GB of internal memory, " +
                "iOS 12, " +
                "12.5 inches, " +
                "3174 mAh battery, " +
                "silver");
        product.setPrice(699.99);
        product.setImage("assets/images/gucci-watch.jpg");
        product.setCategory(categoryRepository.findByName("Accessories"));
        productRepository.save(product);

        product = new Product();
        product.setName("Gucci Sunglasses");
        product.setDescription("Gucci Sunglasses smartphone with 4.00-inch 1080x2340 pixels display, " +
                "Apple A12 Bionic chip, " +
                "64GB of internal memory, " +
                "iOS 12, " +
                "12.5 inches, " +
                "3174 mAh battery, " +
                "silver");
        product.setPrice(699.99);
        product.setImage("assets/images/gucci-sunglasses.jpg");
        product.setCategory(categoryRepository.findByName("Accessories"));
        productRepository.save(product);

        product = new Product();
        product.setName("Gucci Hat");
        product.setDescription("Gucci Hat smartphone with 4.00-inch 1080x2340 pixels display, " +
                "Apple A12 Bionic chip, " +
                "64GB of internal memory, " +
                "iOS 12, " +
                "12.5 inches, " +
                "3174 mAh battery, " +
                "silver");
        product.setPrice(699.99);
        product.setImage("assets/images/gucci-hat.jpg");
        product.setCategory(categoryRepository.findByName("Accessories"));
        productRepository.save(product);
    }
}
