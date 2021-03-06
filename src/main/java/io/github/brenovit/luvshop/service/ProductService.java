package io.github.brenovit.luvshop.service;

import static io.github.brenovit.luvshop.mapper.ProductMapper.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.github.brenovit.luvshop.exception.ResourceNotFoundException;
import io.github.brenovit.luvshop.models.Product;
import io.github.brenovit.luvshop.models.ProductCategory;
import io.github.brenovit.luvshop.payload.product.ProductRequest;
import io.github.brenovit.luvshop.payload.product.ProductResponse;
import io.github.brenovit.luvshop.repository.ProductRepository;
import io.github.brenovit.luvshop.util.ErrorCode;
import lombok.SneakyThrows;

@Service
public class ProductService extends InternalService {	
	
	@Autowired
	private ProductRepository repository;	
	
	public List<ProductResponse> findAll(){
		return parse(repository.findAll());
	}	

	public Page<ProductResponse> findAll(Long categoryId, String name, int page, int size, String sort) {
		Pageable pageable = getPageable(page, size, sort);
		Page<Product> pagingResponse = null;
		Product p = new Product();
		p.setCategory(new ProductCategory());
		p.getCategory().setId(categoryId);
		p.setName(name);
		pagingResponse = repository.findAll(p, pageable);
			
		if(pagingResponse.hasContent()) {			 
			 return new PageImpl<>(parse(pagingResponse.getContent()), pageable, pagingResponse.getTotalElements());
		}
		return new PageImpl<>(new ArrayList<>());
	}
	
	@SneakyThrows
	public ProductResponse findById(Long id){
		Optional<Product> product = repository.findById(id);
		if(!product.isPresent()) {
			throw new ResourceNotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
		}
		return parse(product.get());
	}
	
	public ProductResponse save (ProductRequest request) {
		//product.setUser(getLoggedUser());
		Product product = parse(request);
		return parse(repository.save(product));
	}


	public ProductResponse update(Long id, ProductRequest request) {
		findById(id);
		Product product = parse(request);
		return parse(repository.save(product));
	}	
	
	@SneakyThrows
	public void delete(Long id) {
		findById(id);
		repository.deleteById(id);
	}

	private Pageable getPageable(int page, int size, String sort) {
		if(StringUtils.isNotBlank(sort)) {
			return PageRequest.of(page, size, Sort.by(sort));
		}		
		return PageRequest.of(page, size);
	}
}
