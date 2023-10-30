package com.greenfarm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greenfarm.dao.ReviewDao;
import com.greenfarm.entity.Product;
import com.greenfarm.entity.Report;
import com.greenfarm.entity.Review;
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

	@Override
	public List<Review> findbyproduct(Product product) {
		// TODO Auto-generated method stub
		return dao.findByProduct(product);
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
	    public List<Report> getRatingStats() {
	        return dao.getRatingStats();
	    }

}
