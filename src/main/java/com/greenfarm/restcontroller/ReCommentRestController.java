package com.greenfarm.restcontroller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenfarm.dto.CommentDTO;
import com.greenfarm.dto.ReCommentDTO;
import com.greenfarm.dto.CommentDTO;
import com.greenfarm.entity.Comment;
import com.greenfarm.entity.ReComment;
import com.greenfarm.entity.ReportRevenue;
import com.greenfarm.entity.Tour;
import com.greenfarm.entity.Comment;
import com.greenfarm.service.CommentService;
import com.greenfarm.service.ReCommentService;
import com.greenfarm.service.TourService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/recomment")
public class ReCommentRestController {
	
	@Autowired
	ReCommentService recommentService;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	CommentService commentService;
	
	@GetMapping()
	public ResponseEntity<List<ReCommentDTO>> getListrecomment(){
		List<ReComment> recomments = recommentService.findAll();
		List<ReCommentDTO> reCommentDTOs = recomments.stream().map(recomment -> modelMapper.map(recomment, ReCommentDTO.class)).collect(Collectors.toList());
		return new ResponseEntity<>(reCommentDTOs, HttpStatus.OK);
		
	}
	
	@GetMapping("/comment/{comment}")
	public ResponseEntity<List<ReCommentDTO>> getlisstrecommentbycomment(@RequestBody Comment comment){
		List<ReComment> list = recommentService.ReCommentbyComments(comment);
		List<ReCommentDTO> reCommentDTOs = list.stream().map(recomment -> modelMapper.map(recomment, ReCommentDTO.class)).collect(Collectors.toList());
		
		
		return null;
		
	};
//	
//	@GetMapping("/tour/{tourid}")
//	public ResponseEntity<List<ReCommentDTO>> getListcommentbytour(@PathVariable("tourid") Integer tourid){
//		Tour tour = tourService.findById(tourid);
//		List<Comment> comments = commentService.commentbytourid(tour);
//		List<CommentDTO> commenDtos = comments.stream().map(comment -> modelMapper.map(comment, CommentDTO.class))
//				.collect(Collectors.toList());
//				
//		return new ResponseEntity<>(commenDtos, HttpStatus.OK);
//		
//	}
//	
//	
//	@GetMapping("{commentid}")
//	public ResponseEntity<CommentDTO> getOne(@PathVariable("commentid") Integer commentid) {
//		Comment Comment = commentService.findById(commentid);
//
//		if (Comment == null) {
//			// Trả về mã trạng thái 404 Not Found nếu không tìm thấy Comment
//			return ResponseEntity.notFound().build();
//		}
//
//		// Sử dụng ModelMapper để ánh xạ từ Comment sang CommentDTO
//		CommentDTO CommentDTO = modelMapper.map(Comment, CommentDTO.class);
//
//		// Trả về CommentDTO bằng ResponseEntity với mã trạng thái 200 OK
//		return new ResponseEntity<>(CommentDTO, HttpStatus.OK);
//	}
//	
//	
//	@PostMapping()
//	public ResponseEntity<CommentDTO> create(@RequestBody Comment comment,
//			Model model) {
//		
//			Comment createdComment = commentService.create(comment);
//
//			// Sử dụng ModelMapper để ánh xạ từ Comment đã tạo thành CommentDTO
//			CommentDTO CommentDTO = modelMapper.map(createdComment, CommentDTO.class);
//
//			// Trả về CommentDTO bằng ResponseEntity với mã trạng thái 201 Created
//			return new ResponseEntity<>(CommentDTO, HttpStatus.CREATED);				
//	}
//
//	@PutMapping("{commentid}")
//	public ResponseEntity<CommentDTO> update(@PathVariable("commentid") Integer commentid,
//			@RequestBody Comment comment) {
//		Comment updatedComment = commentService.update(comment);
//
//		if (updatedComment == null) {
//			// Trả về mã trạng thái 404 Not Found nếu không tìm thấy Comment để cập nhật
//			return ResponseEntity.notFound().build();
//		}
//
//		// Sử dụng ModelMapper để ánh xạ từ Comment đã cập nhật thành CommentDTO
//		CommentDTO CommentDTO = modelMapper.map(updatedComment, CommentDTO.class);
//
//		// Trả về CommentDTO bằng ResponseEntity với mã trạng thái 200 OK
//		return new ResponseEntity<>(CommentDTO, HttpStatus.OK);
//	}
//
//	@DeleteMapping("{commentid}")
//	public ResponseEntity<Void> delete(@PathVariable("commentid") Integer commentid) {
//		Comment existingComment = commentService.findById(commentid);
//
//		if (existingComment == null) {
//			// Trả về mã trạng thái 404 Not Found nếu không tìm thấy Comment để xóa
//			return ResponseEntity.notFound().build();
//		}
//
//		// Thực hiện xóa trong service
//		commentService.deleteCommentById(commentid);
//
//		// Trả về mã trạng thái 204 No Content để chỉ ra thành công trong việc xóa
//		return ResponseEntity.noContent().build();
//	}
	
}
