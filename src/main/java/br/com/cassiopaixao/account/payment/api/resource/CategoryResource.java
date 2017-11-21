package br.com.cassiopaixao.account.payment.api.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cassiopaixao.account.payment.api.event.RecursoCriadoEvent;
import br.com.cassiopaixao.account.payment.api.model.Category;
import br.com.cassiopaixao.account.payment.api.repository.CategoryRepository;

@RestController
@RequestMapping("/categories")
public class CategoryResource {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	public List<Category> getCategories(){
		return categoryRepository.findAll();
	}
	
	@PostMapping
	public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category, HttpServletResponse response) {
		Category categRet = categoryRepository.save(category);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, category.getCode()));
		return ResponseEntity.status(HttpStatus.CREATED).body(categRet);
	}
	
	@GetMapping("/{code}")
	public Category getCategoryById(@PathVariable Long code) {
		return categoryRepository.findOne(code);
	}
	
	
}
