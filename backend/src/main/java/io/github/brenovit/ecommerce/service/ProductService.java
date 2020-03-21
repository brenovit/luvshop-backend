package io.github.brenovit.ecommerce.service;

import static io.github.brenovit.ecommerce.mapper.ProductMapper.parse;

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

import io.github.brenovit.ecommerce.exception.ResourceNotFoundException;
import io.github.brenovit.ecommerce.models.Product;
import io.github.brenovit.ecommerce.payload.product.ProductRequest;
import io.github.brenovit.ecommerce.payload.product.ProductResponse;
import io.github.brenovit.ecommerce.repository.ProductRepository;
import io.github.brenovit.ecommerce.util.ErrorCode;
import lombok.SneakyThrows;

@Service
public class ProductService extends InternalService {	
	
	@Autowired
	private ProductRepository repository;
	
	public Page<ProductResponse> findAll(int page, int size, String sort) {
		Page<ProductResponse> response = new PageImpl<>(new ArrayList<>());
		Pageable paging = null;
		
		if(StringUtils.isNotBlank(sort)) {
			paging = PageRequest.of(page, size, Sort.by(sort));
		} else {
			paging = PageRequest.of(page, size);
		}
		Page<Product> pagingResponse = repository.findAll(paging);		
		if(pagingResponse.hasContent()) {			 
			 response = new PageImpl<>(parse(pagingResponse.getContent()), paging, pagingResponse.getTotalElements());
		}
		return response;
	}
	
	public List<ProductResponse> findAll(){
		return parse(repository.findAll());
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
}
