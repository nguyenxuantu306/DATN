package com.greenfarm.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfarm.dao.OrderDAO;
import com.greenfarm.dao.OrderDetailDAO;
import com.greenfarm.dao.StatusOrderDAO;
import com.greenfarm.entity.FindReportYear;
import com.greenfarm.entity.Order;
import com.greenfarm.entity.OrderDetail;
import com.greenfarm.entity.ReportRevenue;
import com.greenfarm.entity.ReportYear;
import com.greenfarm.entity.StatusOrder;
import com.greenfarm.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	OrderDAO dao;

	@Autowired
	OrderDetailDAO ddao;

	@Autowired
	StatusOrderDAO statusOrderDAO;

	@Override
	public List<Order> findOrdersByDateRange(LocalDateTime startDay, LocalDateTime endDay, int page, int size) {
		// Tính toán số lượng bản ghi bỏ qua (offset)
		int offset = page * size;

		// Gọi phương thức findOrdersByDateRange trong OrderDAO
		List<Order> orders = dao.findOrdersByDateRange(startDay, endDay);

		// Kiểm tra và cắt danh sách đơn hàng dựa trên trang và kích thước
		if (offset < orders.size()) {
			int toIndex = Math.min(offset + size, orders.size());
			orders = orders.subList(offset, toIndex);
		} else {
			orders = Collections.emptyList();
		}

		return orders;
	}

	@Override
	public List<Order> getAllOrders(int page, int size) {
		int offset = page * size;
		return dao.findAll(offset, size);
	}

	// @Override
	// public List<Order> findAll() {
	// return dao.findAll();
	// }

	@Override
	public Order create(JsonNode orderData) {
		ObjectMapper mapper = new ObjectMapper();

		Order order = mapper.convertValue(orderData, Order.class);
		dao.save(order);

		TypeReference<List<OrderDetail>> type = new TypeReference<List<OrderDetail>>() {
		};
		List<OrderDetail> details = mapper.convertValue(orderData.get("orderDetail"), type).stream()
				.peek(d -> d.setOrder(order)).collect(Collectors.toList());
		ddao.saveAll(details);

		return order;
	}

	@Override
	public List<Order> findByEfindByIdAccountmail(String email) {
		return dao.findByEfindByIdAccountmail(email);
	}

	@Override
	public List<Order> findByIdAccount(Integer Id) {
		return dao.findByIdAccount(Id);
	}

	@Override
	public Order findById(Integer Id) {
		return dao.findById(Id).get();
	}

	@Override
	public Order update(Order order) {
		return dao.save(order);
	}

	@Override
	public List<Order> getOrdersByStatusName(String statusName) {
		return dao.findByStatusOrder_Name(statusName);
	}

	@Override
	public List<Order> filterOrdersByNgayTao(LocalDateTime ngayTao) {
		// Thực hiện truy vấn để lọc các đơn hàng theo ngày tạo
		return dao.findByNgayTao(ngayTao);
	}

	@Override
	public List<ReportRevenue> slstatus() {
		return dao.countOrdersByStatus();
	}

	public void setStatusOrderDAO(StatusOrderDAO statusOrderDAO) {
		this.statusOrderDAO = statusOrderDAO;
	}

	public StatusOrderDAO getStatusOrderDAO() {
		return statusOrderDAO;
	}
	// Các mã khác trong service

	private StatusOrder getCanceledStatusOrder() {
		int canceledStatusOrderId = 4; // ID của trạng thái "Đã hủy" trong cơ sở dữ liệu
		return statusOrderDAO.getStatusOrderByStatusorderid(canceledStatusOrderId);
	}

	@Override
	public void cancelOrder(Integer orderid) {
		Optional<Order> optionalOrder = dao.findById(orderid);
		if (optionalOrder.isPresent()) {
			Order order = optionalOrder.get();
			// Kiểm tra trạng thái đơn hàng trước khi hủy
			if (order.getStatusOrder().getStatusorderid() == 1) {
				// Cập nhật trạng thái đơn hàng thành "Đã hủy"
				StatusOrder canceledStatus = getCanceledStatusOrder(); // Lấy trạng thái "Đã hủy" từ cơ sở dữ
																		// liệu

				if (canceledStatus != null) {
					// Cập nhật trạng thái của đơn hàng thành "Đã hủy"
					order.setStatusOrder(canceledStatus);
					dao.save(order);
				} else {
					System.out.println(order.getStatusOrder().getStatusorderid());
					System.out.println(canceledStatus.getStatusorderid());
					throw new RuntimeException("Không thể hủy đơn hàng.");
				}
			} else {
				throw new RuntimeException("Không tìm thấy đơn hàng.");
			}
		}
	}

	@Override
	public List<Order> findAll() {
		return dao.findAll();
	}

	// lọc trạng thái trong history_order
	@Override
	public List<Order> findByUserEmailAndStatus(String email, String status) {
		return dao.findByUserEmailAndStatus(email, status);
	}

	@Override
	public List<ReportYear> getYearRevenue() {
		return dao.getYearRevenue();
	}

//	@Override
//	public List<Report> getMonthlyRevenue() {
//		return dao.getMonthlyRevenue();
//	}

	@Override
	public List<FindReportYear> findYearlyRevenue(Integer year) {
		return dao.findYearlyRevenue(year);
	}

	@Override
	public List<Order> findByOrderdateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, int page,
			int size) {
		Pageable pageable = PageRequest.of(page, size);
		return dao.findByOrderdateBetween(startDateTime, endDateTime, pageable);
	}

	@Override
	public List<Order> findByUserEmailAndStatusOrderName(String userEmail, String statusName) {
		return dao.findByUserEmailAndStatusOrder_Name(userEmail, statusName);
	}

}
