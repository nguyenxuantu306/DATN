package com.greenfarm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.ReviewDao;
import com.greenfarm.entity.Product;
import com.greenfarm.entity.ReportRevenue;
import com.greenfarm.entity.Review;
import com.greenfarm.entity.User;
import com.greenfarm.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	private ReviewDao dao;

	@Override
	public Review create(Review review) {
		return dao.save(review);
	}

	@Override
	public List<Review> getReviews() {
		return dao.findAll();
	}

	public Page<Review> findbyproduct(Product product, Pageable pageable) {
		return dao.findByProduct(product, pageable);
	}

	@Override
	public boolean deleteReviewById(Integer reviewid) {
		try {
			// Gọi phương thức xóa đánh giá từ repository hoặc thực hiện logic xóa tại đây
			// Nếu xóa thành công, trả về true, ngược lại trả về false
			dao.deleteById(reviewid);
			return true;
		} catch (Exception e) {
			// Xử lý trường hợp xóa không thành công, ví dụ: ghi log lỗi
			return false;
		}
	}

	@Override
	public List<ReportRevenue> getRatingStats() {
		return dao.getRatingStats();
	}

	@Override
	public boolean hasUserReviewedProduct(User user, Product product) {
		// Sử dụng phương thức findByUserAndProduct trong reviewRepository để kiểm tra
		// đánh giá
		Review existingReview = dao.findReviewByUserAndProduct(user, product);
		return existingReview != null;
	}

}
