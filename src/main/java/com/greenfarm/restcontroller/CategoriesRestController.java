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
import com.greenfarm.utils.Log;

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
		try {
			Log.info("Nhận yêu cầu lấy danh sách tất cả các danh mục sản phẩm");

			List<Category> categories = categoryService.findAll();

			// Sử dụng modelMapper để ánh xạ danh sách Category sang danh sách CategoryDTO
			ModelMapper modelMapper = new ModelMapper();
			List<CategoryDTO> categoryDtos = categories.stream()
					.map(category -> modelMapper.map(category, CategoryDTO.class)).collect(Collectors.toList());

			// Trả về danh sách CategoryDTO bằng ResponseEntity với mã trạng thái 200 OK
			Log.info("Trả về danh sách tất cả các danh mục sản phẩm thành công.");
			return ResponseEntity.ok(categoryDtos);
		} catch (Exception e) {
			Log.error("Đã xảy ra lỗi khi lấy danh sách danh mục sản phẩm", e);
			// Trả về ResponseEntity với mã trạng thái 500 INTERNAL SERVER ERROR nếu có lỗi
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
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
		try {
			Log.info("Nhận yêu cầu lấy danh sách các danh mục đã bị xóa");

			List<Category> deletedCategory = categoryService.findAllDeletedCategory();

			// Sử dụng modelMapper để ánh xạ danh sách Category sang danh sách CategoryDTO
			ModelMapper modelMapper = new ModelMapper();
			List<CategoryDTO> categoryDTOs = deletedCategory.stream()
					.map(category -> modelMapper.map(category, CategoryDTO.class)).collect(Collectors.toList());

			// Trả về danh sách CategoryDTO bằng ResponseEntity với mã trạng thái 200 OK
			Log.info("Trả về danh sách các danh mục đã bị xóa thành công.");
			return new ResponseEntity<>(categoryDTOs, HttpStatus.OK);
		} catch (Exception e) {
			Log.error("Đã xảy ra lỗi khi lấy danh sách các danh mục đã bị xóa", e);
			// Trả về ResponseEntity với mã trạng thái 500 INTERNAL SERVER ERROR nếu có lỗi
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping()
	public ResponseEntity<CategoryDTO> create(@RequestBody Category category) {
		try {
			Log.info("Nhận yêu cầu tạo mới một danh mục");

			Category createdCategory = categoryService.create(category);

			if (createdCategory == null) {
				// Nếu không thể tạo Category, ghi log và trả về mã trạng thái 500 Internal
				// Server Error
				Log.error("Không thể tạo mới danh mục. Đã xảy ra lỗi.");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}

			// Sử dụng ModelMapper để ánh xạ từ Category sang CategoryDTO
			ModelMapper modelMapper = new ModelMapper();
			CategoryDTO categoryDTO = modelMapper.map(createdCategory, CategoryDTO.class);

			// Trả về CategoryDTO bằng ResponseEntity với mã trạng thái 201 Created
			Log.info("Danh mục đã được tạo mới thành công.");
			return ResponseEntity.status(HttpStatus.CREATED).body(categoryDTO);
		} catch (Exception e) {
			Log.error("Đã xảy ra lỗi khi tạo mới danh mục", e);
			// Trả về ResponseEntity với mã trạng thái 500 INTERNAL SERVER ERROR nếu có lỗi
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("{categoryid}")
	public ResponseEntity<CategoryDTO> update(@PathVariable("categoryid") Integer categoryid,
			@RequestBody Category category) {
		try {
			Log.info("Nhận yêu cầu cập nhật danh mục với ID: {}", categoryid);

			// Thực hiện cập nhật trong service
			Category updatedCategoryResult = categoryService.update(category);

			if (updatedCategoryResult == null) {
				// Trả về mã trạng thái 404 Not Found nếu không tìm thấy danh mục để cập nhật
				Log.warn("Không tìm thấy danh mục với ID {}. Không thể cập nhật.", categoryid);
				return ResponseEntity.notFound().build();
			}

			// Sử dụng ModelMapper để ánh xạ từ Category đã cập nhật thành CategoryDTO
			ModelMapper modelMapper = new ModelMapper();
			CategoryDTO categoryDTO = modelMapper.map(updatedCategoryResult, CategoryDTO.class);

			// Trả về updatedCategoryDTO bằng ResponseEntity với mã trạng thái 200 OK
			Log.info("Danh mục với ID {} đã được cập nhật thành công.", categoryid);
			return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
		} catch (Exception e) {
			Log.error("Đã xảy ra lỗi khi cập nhật danh mục với ID: {}", categoryid, e);
			// Trả về ResponseEntity với mã trạng thái 500 INTERNAL SERVER ERROR nếu có lỗi
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// Khôi phục một danh mục đã bị xóa
	@PutMapping("/{categoryid}/restore")
	public ResponseEntity<String> restoreTour(@PathVariable("categoryid") Integer categoryid) {
		try {
			Log.info("Nhận yêu cầu khôi phục danh mục với ID: {}", categoryid);

			// Tìm kiếm danh mục với ID tương ứng trong cơ sở dữ liệu
			Category category = categoryService.findById(categoryid);

			if (category == null) {
				Log.warn("Không tìm thấy danh mục với ID {}. Không thể khôi phục.", categoryid);
				return new ResponseEntity<>("Danh mục không tồn tại", HttpStatus.NOT_FOUND);
			}

			category.setIsDeleted(false);
			categoryService.save(category);

			Log.info("Danh mục với ID {} đã được khôi phục thành công.", categoryid);
			return new ResponseEntity<>("Khôi phục danh mục thành công", HttpStatus.OK);
		} catch (Exception e) {
			Log.error("Đã xảy ra lỗi khi khôi phục danh mục với ID: {}", categoryid, e);
			// Trả về ResponseEntity với mã trạng thái 500 INTERNAL SERVER ERROR nếu có lỗi
			return new ResponseEntity<>("Đã xảy ra lỗi khi khôi phục danh mục", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{categoryid}")
	public ResponseEntity<Void> deleteBooking(@PathVariable("categoryid") Integer categoryid) {
		try {
			Log.info("Nhận yêu cầu xóa danh mục với ID: {}", categoryid);

			categoryService.deleteCategoryById(categoryid);

			Log.info("Danh mục với ID {} đã được xóa thành công.", categoryid);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			Log.error("Đã xảy ra lỗi khi xóa danh mục với ID: {}", categoryid, e);
			// Trả về ResponseEntity với mã trạng thái 500 INTERNAL SERVER ERROR nếu có lỗi
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/searchkeywordcategory")
	public ResponseEntity<List<CategoryDTO>> getList(@RequestParam(required = false) String keyword) {
		try {
			Log.info("Nhận yêu cầu lấy danh sách danh mục dựa trên từ khóa: {}", keyword);

			List<Category> categories;

			if (keyword != null && !keyword.isEmpty()) {
				// Nếu có từ khóa, thực hiện tìm kiếm
				categories = categoryService.findByKeyword(keyword);
			} else {
				// Nếu không có từ khóa, lấy tất cả danh mục
				categories = categoryService.findAll();
			}

			// Sử dụng modelMapper để ánh xạ danh sách Category sang danh sách CategoryDTO
			ModelMapper modelMapper = new ModelMapper();
			List<CategoryDTO> categoryDtos = categories.stream()
					.map(category -> modelMapper.map(category, CategoryDTO.class)).collect(Collectors.toList());

			Log.info("Trả về danh sách danh mục dựa trên từ khóa thành công.");
			return ResponseEntity.ok(categoryDtos);
		} catch (Exception e) {
			Log.error("Đã xảy ra lỗi khi lấy danh sách danh mục dựa trên từ khóa", e);
			// Trả về ResponseEntity với mã trạng thái 500 INTERNAL SERVER ERROR nếu có lỗi
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
