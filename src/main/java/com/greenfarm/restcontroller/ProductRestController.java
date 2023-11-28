package com.greenfarm.restcontroller;

import java.util.ArrayList;
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
import com.greenfarm.dto.ProductImageDTO;
import com.greenfarm.dto.UserDTO;
import com.greenfarm.entity.Category;
import com.greenfarm.entity.Product;
import com.greenfarm.entity.ProductImage;
import com.greenfarm.entity.Report;
import com.greenfarm.entity.ReportSP;
import com.greenfarm.entity.Tour;
import com.greenfarm.entity.User;
import com.greenfarm.service.ProductService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/products")
public class ProductRestController {
	@Autowired
	ProductService productService;

	@Autowired
	ModelMapper modelMapper;

	@GetMapping()
	public ResponseEntity<List<ProductDTO>> getList() {
		List<Product> products = productService.findAll();

		// Sử dụng ModelMapper để ánh xạ từ danh sách Product sang danh sách ProductDTO
		List<ProductDTO> productDTOs = products.stream().map(product -> modelMapper.map(product, ProductDTO.class))
				.collect(Collectors.toList());

		// Trả về danh sách ProductDTO bằng ResponseEntity với mã trạng thái 200 OK
		return new ResponseEntity<>(productDTOs, HttpStatus.OK);
	}

	@GetMapping("/deleted")
	public ResponseEntity<List<ProductDTO>> getDeletedList() {
		List<Product> deletedProducts = productService.findAllDeletedProducts();

		// Sử dụng ModelMapper để ánh xạ từ danh sách Product sang danh sách ProductDTO
		List<ProductDTO> productDTOs = deletedProducts.stream()
				.map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());

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

//	@PostMapping()
//	public ResponseEntity<ProductDTO> create(@RequestBody Product product, @RequestParam("file") MultipartFile file,
//			Model model) {
//		try {
//			MinioClient minioClient = MinioClient.builder()
//					.endpoint("http://192.168.1.41:9090")
//					.credentials("minioadmin", "minioadmin")
//					.build();
//
//			String bucketName = "image-shop";
//
//			String imageName = UUID.randomUUID().toString() + file.getOriginalFilename();
//
//			minioClient.uploadObject(
//					UploadObjectArgs.builder()
//							.bucket(bucketName)
//							.object(imageName)
//							.build());
//
//			String image = "http://192.168.1.41:9090/" + bucketName + "/" + imageName;
//
//			product.setImage(image);
//			Product createdProduct = productService.create(product);
//
//			// Sử dụng ModelMapper để ánh xạ từ Product đã tạo thành ProductDTO
//			ProductDTO productDTO = modelMapper.map(createdProduct, ProductDTO.class);
//
//			// Trả về ProductDTO bằng ResponseEntity với mã trạng thái 201 Created
//			return new ResponseEntity<>(productDTO, HttpStatus.CREATED);
//		} catch (MinioException | InvalidKeyException | NoSuchAlgorithmException | IOException e) {
//			// Xử lý ngoại lệ
//			model.addAttribute("error", e.getMessage());
//
//		}
//		return null;		
//	}

//	@PostMapping()
//	public ResponseEntity<ProductDTO> create(@RequestBody Product product, Model model) {
//
//		Product createdProduct = productService.create(product);
//
//		// Sử dụng ModelMapper để ánh xạ từ Product đã tạo thành ProductDTO
//		ProductDTO productDTO = modelMapper.map(createdProduct, ProductDTO.class);
//
//		// Trả về ProductDTO bằng ResponseEntity với mã trạng thái 201 Created
//		return new ResponseEntity<>(productDTO, HttpStatus.CREATED);
//	}
	
	@PostMapping()
	public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO productDTO) {
	    // Create Product object
		Product product = modelMapper.map(productDTO, Product.class);

	    // Create ProductImage objects and associate them with the product
	    List<ProductImage> productImages = new ArrayList<>();
	    if (productDTO.getProductimage() != null) {
	        for (ProductImageDTO productImageDTO : productDTO.getProductimage()) {
	            ProductImage productImage = new ProductImage();
	            productImage.setImageurl(productImageDTO.getImageurl());
	            productImage.setProduct(product);
	            productImages.add(productImage);
	        }
	    }

	    // Set the list of images in the product
	    product.setProductimage(productImages);

	    // Save product and associated images
	    Product createdProduct = productService.create(product);

	    // Map to DTO and return
	    ProductDTO createdProductDTO = modelMapper.map(createdProduct, ProductDTO.class);
	    return new ResponseEntity<>(createdProductDTO, HttpStatus.CREATED);
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

	@PutMapping("/{productid}/restore")
	public ResponseEntity<String> restoreProduct(@PathVariable("productid") Integer productid) {
		// Tìm kiếm sản phẩm với id tương ứng trong cơ sở dữ liệu
		Product product = productService.findById(productid);

		if (product == null) {
			return new ResponseEntity<>("Sản phẩm không tồn tại", HttpStatus.NOT_FOUND);
		}

		// Khôi phục trạng thái đã xóa của sản phẩm
		product.setIsDeleted(false);

		// Lưu sản phẩm đã khôi phục vào cơ sở dữ liệu
		productService.save(product);

		return new ResponseEntity<>("Khôi phục sản phẩm thành công", HttpStatus.OK);
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
	
	@GetMapping("/searchkeywordproduct")
	public ResponseEntity<List<ProductDTO>> getList(@RequestParam(required = false) String keyword) {
		List<Product> products;

		if (keyword != null && !keyword.isEmpty()) {
			// Nếu có từ khóa, thực hiện tìm kiếm
			products = productService.findByKeyword(keyword);
		} else {
			// Nếu không có từ khóa, lấy tất cả người dùng
			products = productService.findAll();
		}

		List<ProductDTO> productDtos = products.stream().map(product -> modelMapper.map(product, ProductDTO.class))
				.collect(Collectors.toList());

		return ResponseEntity.ok(productDtos);
	}




	@GetMapping("/thongke/sp")
	public ResponseEntity<List<ReportSP>> getTK_SP() {
		return new ResponseEntity<>(productService.getTk_sp(), HttpStatus.OK);
	}

	@GetMapping("/thongke/loai")
	public ResponseEntity<List<Report>> getTK_Loai() {
		return new ResponseEntity<>(productService.getTk_loai(), HttpStatus.OK);
	}

	@GetMapping("/thongke/top10tk")
	public List<Product> getProductTK() {
		return productService.getReportSpTk();
	}

	@GetMapping("/thongke/top10spbanchay")
	public List<ReportSP> getProductspbanchay() {
		return productService.getReportspbanchay();
	}

	@GetMapping("/getProductsByCategory/{categoryid}")
	public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Integer categoryid) {
		Category category = new Category();
		category.setCategoryid(categoryid);

		List<Product> products = productService.getProductsByCategory(category);

		if (products.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(products, HttpStatus.OK);
	}

//	@PutMapping("/purchase")
//	public ResponseEntity<?> purchaseProduct(@RequestBody List<ThongkeTK> thongketk) {
//	    try {
//	        for (ThongkeTK tk : thongketk) {
//	            productService.purchaseProduct(tk.getProductid(), tk.getQuantitybought());
//	        }
//
//	        return ResponseEntity.ok("Sản phẩm đã được mua thành công.");
//	    } catch (Exception e) {
//	        return ResponseEntity.badRequest().body("Lỗi khi mua sản phẩm: " + e.getMessage());
//	    }
//	}

}
