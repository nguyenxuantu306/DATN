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
import com.greenfarm.entity.Category;
import com.greenfarm.entity.Product;
import com.greenfarm.entity.ProductImage;
import com.greenfarm.entity.Report;
import com.greenfarm.entity.ReportSP;
import com.greenfarm.service.ProductService;
import com.greenfarm.utils.Log;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/products")
public class ProductRestController {
	@Autowired
	ProductService productService;

	@Autowired
	ModelMapper modelMapper;

	// Lấy ra toàn bộ danh sách sản phẩm
	@GetMapping()
	public ResponseEntity<List<ProductDTO>> getList() {
		try {
			Log.info("Đã nhận được yêu cầu lấy danh sách sản phẩm.");

			List<Product> products = productService.findAll();

			// Sử dụng ModelMapper để ánh xạ từ danh sách Product sang danh sách ProductDTO
			List<ProductDTO> productDTOs = products.stream().map(product -> modelMapper.map(product, ProductDTO.class))
					.collect(Collectors.toList());

			// Trả về danh sách ProductDTO bằng ResponseEntity với mã trạng thái 200 OK
			Log.info("Trả về danh sách sản phẩm {}.", productDTOs.size());
			return new ResponseEntity<>(productDTOs, HttpStatus.OK);
		} catch (Exception e) {
			Log.error("Đã xảy ra lỗi khi lấy danh sách sản phẩm.", e);
			// Trả về ResponseEntity với mã trạng thái 500 INTERNAL SERVER ERROR nếu có lỗi
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// Lấy ra danh sách đã xóa
	@GetMapping("/deleted")
	public ResponseEntity<List<ProductDTO>> getDeletedList() {
		try {
			Log.info("Đã nhận được yêu cầu cho danh sách getDeletedList");
			List<Product> deletedProducts = productService.findAllDeletedProducts();

			// Sử dụng ModelMapper để ánh xạ từ danh sách Product sang danh sách ProductDTO
			List<ProductDTO> productDTOs = deletedProducts.stream()
					.map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());

			// Trả về danh sách ProductDTO bằng ResponseEntity với mã trạng thái 200 OK
			return new ResponseEntity<>(productDTOs, HttpStatus.OK);
		} catch (Exception e) {
			Log.error("Đã xảy ra lỗi trong getDeletedList", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

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

	// Thêm sản phẩm
	@PostMapping()
	public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO productDTO) {
		try {
			Log.info("Nhận yêu cầu tạo sản phẩm mới");
			
			Product product = modelMapper.map(productDTO, Product.class);

			// Tạo đối tượng ProductImage và liên kết chúng với sản phẩm
			List<ProductImage> productImages = new ArrayList<>();
			if (productDTO.getProductimage() != null) {
				for (ProductImageDTO productImageDTO : productDTO.getProductimage()) {
					ProductImage productImage = new ProductImage();
					productImage.setImageurl(productImageDTO.getImageurl());
					productImage.setProduct(product);
					productImages.add(productImage);
				}
			}
			product.setProductimage(productImages);

			Product createdProduct = productService.create(product);

			ProductDTO createdProductDTO = modelMapper.map(createdProduct, ProductDTO.class);

			Log.info("Sản phẩm mới với ID {} đã được tạo thành công", createdProductDTO.getProductid());
			return new ResponseEntity<>(createdProductDTO, HttpStatus.CREATED);
		} catch (Exception e) {
			Log.error("Đã xảy ra lỗi khi tạo sản phẩm", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Cập nhật sản phẩm
	@PutMapping("{productid}")
	public ResponseEntity<ProductDTO> update(@PathVariable("productid") Integer productid,
			@RequestBody Product product) {
		try {
			Log.info("Nhận yêu cầu cập nhật sản phẩm với ID: {}", productid);

			Product updatedProduct = productService.update(product);

			if (updatedProduct == null) {
				// Trả về mã trạng thái 404 Not Found nếu không tìm thấy sản phẩm để cập nhật
				Log.warn("Không tìm thấy sản phẩm với ID {}. Không thể cập nhật.", productid);
				return ResponseEntity.notFound().build();
			}

			// Sử dụng ModelMapper để ánh xạ từ sản phẩm đã cập nhật thành ProductDTO
			ProductDTO productDTO = modelMapper.map(updatedProduct, ProductDTO.class);

			// Trả về ProductDTO bằng ResponseEntity với mã trạng thái 200 OK
			Log.info("Sản phẩm với ID {} đã được cập nhật thành công.", productid);
			return new ResponseEntity<>(productDTO, HttpStatus.OK);
		} catch (Exception e) {
			Log.error("Đã xảy ra lỗi khi cập nhật sản phẩm với ID: {}", productid, e);
			// Trả về ResponseEntity với mã trạng thái 500 INTERNAL SERVER ERROR nếu có lỗi
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// Khôi phục sản phẩm
	@PutMapping("/{productid}/restore")
	public ResponseEntity<String> restoreProduct(@PathVariable("productid") Integer productid) {
		try {
			Log.info("Nhận yêu cầu khôi phục sản phẩm với ID: {}", productid);

			// Tìm kiếm sản phẩm với id tương ứng trong cơ sở dữ liệu
			Product product = productService.findById(productid);

			if (product == null) {
				Log.warn("Không tìm thấy sản phẩm có ID {}. Không thể khôi phục.", productid);
				return new ResponseEntity<>("Sản phẩm không tồn tại", HttpStatus.NOT_FOUND);
			}

			// Khôi phục trạng thái đã xóa của sản phẩm
			product.setIsDeleted(false);

			// Lưu sản phẩm đã khôi phục vào cơ sở dữ liệu
			productService.save(product);

			Log.info("Sản phẩm có ID {} đã được khôi phục thành công", productid);
			return new ResponseEntity<>("Khôi phục sản phẩm thành công", HttpStatus.OK);
		} catch (Exception e) {
			Log.error("Đã xảy ra lỗi khi khôi phục sản phẩm có ID: {}", productid, e);
			return new ResponseEntity<>("Lỗi trong quá trình xử lý", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("{productid}")
	public ResponseEntity<Void> delete(@PathVariable("productid") Integer productid) {
		try {
			Log.info("Đã nhận được yêu cầu xóa sản phẩm với ID: {}", productid);

			Product existingProduct = productService.findById(productid);

			if (existingProduct == null) {
				// Trả về mã trạng thái 404 Not Found nếu không tìm thấy product để xóa
				Log.warn("Không tìm thấy sản phẩm có ID {}. Không thể xóa.", productid);
				return ResponseEntity.notFound().build();
			}

			// Thực hiện xóa trong service
			productService.delete(productid);

			// Trả về mã trạng thái 204 No Content để chỉ ra thành công trong việc xóa
			Log.info("Sản phẩm có ID {} đã được xóa thành công", productid);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			Log.error("Đã xảy ra lỗi khi xóa sản phẩm có ID: {}", productid, e);
			// Trả về ResponseEntity với mã trạng thái 500 INTERNAL SERVER ERROR nếu có lỗi
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
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
