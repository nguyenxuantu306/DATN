package com.greenfarm.restcontroller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.greenfarm.dto.ProductDTO;
import com.greenfarm.entity.Product;
import com.greenfarm.entity.Report;
import com.greenfarm.service.FirebaseService;
import com.greenfarm.service.ProductService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/products")
public class ProductRestController {
	@Autowired
	ProductService productService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	FirebaseService firebaseService;

	@GetMapping()
	public ResponseEntity<List<ProductDTO>> getList() {
		List<Product> products = productService.findAll();

		// Sử dụng ModelMapper để ánh xạ từ danh sách Product sang danh sách ProductDTO
		List<ProductDTO> productDTOs = products.stream().map(product -> modelMapper.map(product, ProductDTO.class))
				.collect(Collectors.toList());

		// Trả về danh sách ProductDTO bằng ResponseEntity với mã trạng thái 200 OK
		return new ResponseEntity<>(productDTOs, HttpStatus.OK);
	}

	@GetMapping("{productid}")
	public ResponseEntity<ProductDTO> getOne(@PathVariable("productid") Integer productid) {
		Product product = productService.findById(productid);

		if (product == null) {
			// Trả về mã trạng thái 404 Not Found nếu không tìm thấy product
			return ResponseEntity.notFound().build();
		}

		// Sử dụng ModelMapper để ánh xạ từ Product sang ProductDTO
		ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);

		// Trả về ProductDTO bằng ResponseEntity với mã trạng thái 200 OK
		return new ResponseEntity<>(productDTO, HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<ProductDTO> create(@RequestBody Product product,
			Model model) {
			Product createdProduct = productService.create(product);

			// Sử dụng ModelMapper để ánh xạ từ Product đã tạo thành ProductDTO
			ProductDTO productDTO = modelMapper.map(createdProduct, ProductDTO.class);

			// Trả về ProductDTO bằng ResponseEntity với mã trạng thái 201 Created
			return new ResponseEntity<>(productDTO, HttpStatus.CREATED);
		
	}

	@PutMapping("{productid}")
	public ResponseEntity<ProductDTO> update(@PathVariable("productid") Integer productid,
			@RequestBody Product product) {
		Product updatedProduct = productService.update(product);

		if (updatedProduct == null) {
			// Trả về mã trạng thái 404 Not Found nếu không tìm thấy product để cập nhật
			return ResponseEntity.notFound().build();
		}

		// Sử dụng ModelMapper để ánh xạ từ Product đã cập nhật thành ProductDTO
		ProductDTO productDTO = modelMapper.map(updatedProduct, ProductDTO.class);

		// Trả về ProductDTO bằng ResponseEntity với mã trạng thái 200 OK
		return new ResponseEntity<>(productDTO, HttpStatus.OK);
	}

	@DeleteMapping("{productid}")
	public ResponseEntity<Void> delete(@PathVariable("productid") Integer productid) {
		Product existingProduct = productService.findById(productid);

		if (existingProduct == null) {
			// Trả về mã trạng thái 404 Not Found nếu không tìm thấy product để xóa
			return ResponseEntity.notFound().build();
		}

		// Thực hiện xóa trong service
		productService.delete(productid);

		// Trả về mã trạng thái 204 No Content để chỉ ra thành công trong việc xóa
		return ResponseEntity.noContent().build();
	}

	// Search Name
	@GetMapping("/search")
	public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam("keyword") Optional<String> keyword) {
		String searchKeyword = keyword.orElse("");
		List<Product> productList = productService.findProductByKeyword(searchKeyword);

		List<ProductDTO> productDTOList = productList.stream()
				.map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());

		return ResponseEntity.ok(productDTOList);
	}

	// Lọc theo khoảng giá
	@GetMapping("/filter-by-custom-price-range")
	public ResponseEntity<List<ProductDTO>> filterByCustomPriceRange(Model model,
			@RequestParam("priceRange") String priceRange) {
		List<Product> productList = productService.findProductByPriceRange(priceRange);

		List<ProductDTO> productDTOList = productList.stream()
				.map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());

		return ResponseEntity.ok(productDTOList);
	}

	// Sắp xếp theo A - Z & Z - A
	@GetMapping("/sort")
	public ResponseEntity<List<ProductDTO>> sortProductsByName(@RequestParam("sort") String sort) {
		List<Product> productList = productService.findProductByProductNameSort(sort);
		List<ProductDTO> productDTOList = productList.stream()
				.map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());

		return ResponseEntity.ok(productDTOList);
	}

	@GetMapping("/sortprice")
	public ResponseEntity<List<ProductDTO>> sortProductsByPrice(@RequestParam("sortprice") Integer sortprice) {
		List<Product> productList = productService.findProductByProductPiceSort(sortprice);

		List<ProductDTO> productDTOList = productList.stream()
				.map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());

		return ResponseEntity.ok(productDTOList);
	}

	@GetMapping("/thongke/sp")
	public ResponseEntity<List<Report>> getTK_SP() {
		return new ResponseEntity<>(productService.getTk_sp(), HttpStatus.OK);
	}

	@GetMapping("/thongke/loai")
	public ResponseEntity<List<Report>> getTK_Loai() {
		return new ResponseEntity<>(productService.getTk_loai(), HttpStatus.OK);
	}

}
