package io.github.brenovit.ecommerce.service;

import static io.github.brenovit.ecommerce.mapper.ProductMapper.parse;
import static io.github.brenovit.ecommerce.mapper.ProductMapper.parseCategory;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.brenovit.ecommerce.exception.ResourceNotFoundException;
import io.github.brenovit.ecommerce.models.ProductCategory;
import io.github.brenovit.ecommerce.payload.product.ProductCategoryResponse;
import io.github.brenovit.ecommerce.repository.ProductCategoryRepository;
import lombok.SneakyThrows;

@Service
public class ProductCategoryService {
	
	@Autowired
	private ProductCategoryRepository repository;
	
	public List<ProductCategoryResponse> findAll(){
		return parseCategory(repository.findAll());
	}
	
	@SneakyThrows
	public ProductCategoryResponse findById(Long id){
		Optional<ProductCategory> product = repository.findById(id);
		if(!product.isPresent()) {
			throw new ResourceNotFoundException();
		}
		return parse(product.get());
	}
	
	public ProductCategoryResponse save (ProductCategory request) {
		return parse(repository.save(request));
	}


	public ProductCategoryResponse update(Long id, ProductCategory request) {
		findById(id);
		return parse(repository.save(request));
	}	
	
	@SneakyThrows
	public void delete(Long id) {
		findById(id);
		repository.deleteById(id);
	}
}
