package com.greenfarm.restcontroller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenfarm.dto.CommentDTO;
import com.greenfarm.dto.ReCommentDTO;
import com.greenfarm.entity.Comment;
import com.greenfarm.entity.ReComment;
import com.greenfarm.service.CommentService;
import com.greenfarm.service.ReCommentService;

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
	
	@GetMapping("/comment/{commentid}")
	public ResponseEntity<List<ReCommentDTO>> getListrecommentbycomment(@PathVariable("commentid") Integer commentid){
		
		Comment comment = commentService.findById(commentid);
		List<ReComment> recomments = recommentService.getrecommenComments(comment);
		List<ReCommentDTO> reCommentDTOs = recomments.stream().map(recomment -> modelMapper.map(recomment, ReCommentDTO.class)).collect(Collectors.toList());
		return new ResponseEntity<>(reCommentDTOs, HttpStatus.OK);
		
	}
	
	@GetMapping("{recommentid}")
	public ResponseEntity<ReCommentDTO> getOne(@PathVariable("recommentid") Integer recommentid) {
		
		ReComment reComment = recommentService.findById(recommentid);
		if (reComment == null) {
			// Trả về mã trạng thái 404 Not Found nếu không tìm thấy ReComment
			return ResponseEntity.notFound().build();
		}

		// Sử dụng ModelMapper để ánh xạ từ ReComment sang ReReCommentDTO
		ReCommentDTO ReCommentDTO = modelMapper.map(reComment, ReCommentDTO.class);

		// Trả về ReCommentDTO bằng ResponseEntity với mã trạng thái 200 OK
		return new ResponseEntity<>(ReCommentDTO, HttpStatus.OK);
	}
	
	
	@PostMapping()
	public ResponseEntity<ReCommentDTO> create(@RequestBody ReComment recomment,
			Model model) {
		
			ReComment createdReComment = recommentService.create(recomment);

			// Sử dụng ModelMapper để ánh xạ từ Comment đã tạo thành ReCommentDTO
			ReCommentDTO ReCommentDTO = modelMapper.map(createdReComment, ReCommentDTO.class);

			// Trả về ReCommentDTO bằng ResponseEntity với mã trạng thái 201 Created
			return new ResponseEntity<>(ReCommentDTO, HttpStatus.CREATED);				
	}
}


	//@GetMapping("/tour/{tourid}")
////public ResponseEntity<List<ReCommentDTO>>
//getListcommentbytour(@PathVariable("tourid") Integer tourid){
////Tour tour = tourService.findById(tourid);
////List<Comment> comments = commentService.commentbytourid(tour);
////List<CommentDTO> commenDtos = comments.stream().map(comment ->
//modelMapper.map(comment, CommentDTO.class))
////.collect(Collectors.toList());
////
////return new ResponseEntity<>(commenDtos, HttpStatus.OK);
////
////}

// @GetMapping("/comment/{comment}")
// public ResponseEntity<List<ReCommentDTO>>
// getlisstrecommentbycomment(@RequestBody Comment comment){
// List<ReComment> list = recommentService.ReCommentbyComments(comment);
// List<ReCommentDTO> reCommentDTOs = list.stream().map(recomment ->
// modelMapper.map(recomment, ReCommentDTO.class)).collect(Collectors.toList());
//
