package com.greenfarm.restcontroller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.greenfarm.dto.CategoryDTO;
import com.greenfarm.dto.ProductDTO;
import com.greenfarm.entity.Category;
import com.greenfarm.entity.Product;
import com.greenfarm.service.CategoryService;
import org.modelmapper.ModelMapper;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/categories")
public class CategoriesRestController {

	@Autowired
	CategoryService categoryService;

	@Autowired
	ModelMapper modelMapper;

	@GetMapping()
	public ResponseEntity<List<CategoryDTO>> getList() {
		List<Category> categories = categoryService.findAll();

		// Sử dụng modelMapper để ánh xạ danh sách Category sang danh sách CategoryDTO
		ModelMapper modelMapper = new ModelMapper();
		List<CategoryDTO> categoryDtos = categories.stream()
				.map(category -> modelMapper.map(category, CategoryDTO.class)).collect(Collectors.toList());

		return ResponseEntity.ok(categoryDtos); // ResponseEntity.ok() tương đương HttpStatus.OK
	}

	@GetMapping("{categoryid}")
	public ResponseEntity<CategoryDTO> getOne(@PathVariable("categoryid") Integer categoryid) {
		Category category = categoryService.findById(categoryid);

		if (category == null) {
			// Trả về mã trạng thái 404 Not Found nếu không tìm thấy category
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// Sử dụng modelMapper để ánh xạ từ Category sang CategoryDTO
		ModelMapper modelMapper = new ModelMapper();
		CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);

		// Trả về CategoryDTO bằng ResponseEntity với mã trạng thái 200 OK
		return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
	}

	@GetMapping("/deleted")
	public ResponseEntity<List<CategoryDTO>> getDeletedList() {
		List<Category> deletedCategory = categoryService.findAllDeletedCategory();

	
		List<CategoryDTO> CategoryDTOs = deletedCategory.stream()
				.map(category -> modelMapper.map(category, CategoryDTO.class)).collect(Collectors.toList());

		
		return new ResponseEntity<>(CategoryDTOs, HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<CategoryDTO> create(@RequestBody Category category) {
		Category createdCategory = categoryService.create(category);

		if (createdCategory == null) {
			// Nếu không thể tạo Category, trả về mã trạng thái 500 Internal Server Error
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		// Sử dụng ModelMapper để ánh xạ từ Category sang CategoryDTO
		ModelMapper modelMapper = new ModelMapper();
		CategoryDTO categoryDTO = modelMapper.map(createdCategory, CategoryDTO.class);

		// Trả về CategoryDTO bằng ResponseEntity với mã trạng thái 201 Created
		return ResponseEntity.status(HttpStatus.CREATED).body(categoryDTO);
	}

	@PutMapping("{categoryid}")
	public ResponseEntity<CategoryDTO> update(@PathVariable("categoryid") Integer categoryid,
			@RequestBody Category category) {

		// Thực hiện cập nhật trong service
		Category updatedCategoryResult = categoryService.update(category);

		if (updatedCategoryResult == null) {
			// Trả về mã trạng thái 404 Not Found nếu không tìm thấy product để cập nhật
			return ResponseEntity.notFound().build();
		}
		
		
		// Sử dụng ModelMapper để ánh xạ từ Product đã cập nhật thành ProductDTO
		CategoryDTO categoryDTO = modelMapper.map(updatedCategoryResult, CategoryDTO.class);

		// Trả về updatedCategoryDTO bằng ResponseEntity với mã trạng thái 200 OK
		return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
	}

	@PutMapping("/{categoryid}/restore")
	public ResponseEntity<String> restoreTour(@PathVariable("categoryid") Integer categoryid) {
		// Tìm kiếm sản phẩm với id tương ứng trong cơ sở dữ liệu
		Category category = categoryService.findById(categoryid);

		if (category == null) {
			return new ResponseEntity<>("Danh mục không tồn tại", HttpStatus.NOT_FOUND);
		}
		category.setIsDeleted(false);

		categoryService.save(category);

		return new ResponseEntity<>("Khôi phục Danh mục thành công", HttpStatus.OK);
	}
	
	
	@DeleteMapping("/{categoryid}")
	public ResponseEntity<Void> deleteBooking(@PathVariable("categoryid") Integer categoryid) {
		categoryService.deleteCategoryById(categoryid);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/searchkeywordcategory")
	public ResponseEntity<List<CategoryDTO>> getList(@RequestParam(required = false) String keyword) {
		List<Category> categorys;

		if (keyword != null && !keyword.isEmpty()) {
			// Nếu có từ khóa, thực hiện tìm kiếm
			categorys = categoryService.findByKeyword(keyword);
		} else {
			// Nếu không có từ khóa, lấy tất cả người dùng
			categorys = categoryService.findAll();
		}

		List<CategoryDTO> categoryDtos = categorys.stream().map(category -> modelMapper.map(category, CategoryDTO.class))
				.collect(Collectors.toList());

		return ResponseEntity.ok(categoryDtos);
	}
}
