package com.greenfarm.restcontroller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.greenfarm.dto.OrderDTO;
import com.greenfarm.entity.CategorySalesByDate;
import com.greenfarm.entity.FindReportYear;
import com.greenfarm.entity.Order;
import com.greenfarm.entity.Report7day;
import com.greenfarm.entity.ReportRevenue;
import com.greenfarm.entity.ReportYear;
import com.greenfarm.service.OrderService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/orders")
public class OrderRestController {
	@Autowired
	OrderService orderService;

	@Autowired
	ModelMapper modelMapper;

	@GetMapping()
	public ResponseEntity<List<OrderDTO>> getList() {
		List<Order> orders = orderService.findAll();

		// Sử dụng ModelMapper để ánh xạ từ danh sách Product sang danh sách ProductDTO
		List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
				.collect(Collectors.toList());

		// Trả về danh sách ProductDTO bằng ResponseEntity với mã trạng thái 200 OK
		return new ResponseEntity<>(orderDTOs, HttpStatus.OK);
	}

//	public ResponseEntity<String> getAllOrders(
//	        @RequestParam(defaultValue = "0") int page,
//	        @RequestParam(defaultValue = "10") int size,
//	        @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDay,
//	        @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDay) {
//	    List<Order> orders;
//
//	    // Kiểm tra xem người dùng đã cung cấp ngày bắt đầu hay chưa
//	    if (startDay != null) {
//	        // Lấy ngày hiện tại
//	        LocalDate currentDate = LocalDate.now();
//
//	        // Lọc danh sách đơn hàng từ ngày bắt đầu đến ngày hiện tại
//	        LocalDateTime startTime = LocalDateTime.of(startDay, LocalTime.MIN);
//	        LocalDateTime endTime = LocalDateTime.of(currentDate, LocalTime.MIN).plusDays(1);
//	        orders = orderService.findOrdersByDateRange(startTime, endTime, page, size);
//	    } else if (endDay != null) {
//	        // Lọc danh sách đơn hàng từ ngày đầu tiên đến ngày kết thúc
//	        LocalDateTime startTime = LocalDateTime.of(LocalDate.MIN, LocalTime.MIN);
//	        LocalDateTime endTime = LocalDateTime.of(endDay, LocalTime.MIN).plusDays(1);
//	        orders = orderService.findOrdersByDateRange(startTime, endTime, page, size);
//	    } else {
//	        // Lấy toàn bộ danh sách đơn hàng nếu không có ngày bắt đầu và ngày kết thúc
//	        orders = orderService.getAllOrders(page, size);
//	    }
//
//	    // Sắp xếp danh sách đơn hàng theo ngày giảm dần (từ mới nhất đến cũ nhất)
//	    orders.sort(Comparator.comparing(Order::getOrderDateFormatted).reversed());
//
//	    List<OrderDTO> orderDTOs = orders.stream()
//	            .map(order -> modelMapper.map(order, OrderDTO.class))
//	            .collect(Collectors.toList());
//
//	    ObjectMapper objectMapper = new ObjectMapper();
//	    objectMapper.setDateFormat(new SimpleDateFormat("dd-MM-yyyy"));
//
//	    try {
//	        String json = objectMapper.writeValueAsString(orderDTOs);
//	        return ResponseEntity.ok(json);
//	    } catch (JsonProcessingException e) {
//	        // Xử lý lỗi nếu chuyển đổi sang JSON không thành công
//	        e.printStackTrace();
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//	    }
//	}

	@GetMapping("/search")
	public ResponseEntity<String> searchOrdersByDate(
			@RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime startDateTime,
			@RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime endDateTime,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		List<Order> orders = orderService.findByOrderdateBetween(startDateTime, endDateTime, page, size);

		List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
				.collect(Collectors.toList());

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		try {
			String json = objectMapper.writeValueAsString(orderDTOs);
			return ResponseEntity.ok(json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
//	@GetMapping("/search")
//	public ResponseEntity<byte[]> searchOrdersByDate(
//			@RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime startDateTime,
//			@RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime endDateTime,
//			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
//
//		List<Order> orders = orderService.findByOrderdateBetween(startDateTime, endDateTime, page, size);
//
//		List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
//				.collect(Collectors.toList());
//
//		ObjectMapper objectMapper = new ObjectMapper();
//		objectMapper.registerModule(new JavaTimeModule());
//		Workbook workbook = new XSSFWorkbook();
//		Sheet sheet = workbook.createSheet("Danh sách đơn hàng");
//
//		// Đặt tiêu đề cho tài liệu Excel
//		String title = "Danh sách đơn hàng";
//		Row titleRow = sheet.createRow(0);
//		Cell titleCell = titleRow.createCell(2);
//		titleCell.setCellValue(title);
//
//		// Thiết lập font và kiểu chữ cho tiêu đề
//		Font titleFont = workbook.createFont();
//		titleFont.setBold(true);
//		titleFont.setFontHeightInPoints((short) 16);
//		titleFont.setColor(IndexedColors.BLUE.getIndex());
//
//		CellStyle titleCellStyle = workbook.createCellStyle();
//		titleCellStyle.setFont(titleFont);
//		titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
//		titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//
//		// Áp dụng định dạng cho ô tiêu đề
//		titleCell.setCellStyle(titleCellStyle);
//
//		// Tạo hàng tiêu đề và đặt giá trị cho các ô
//		Row headerRow = sheet.createRow(1);
//		headerRow.createCell(0).setCellValue("STT");
//		headerRow.createCell(1).setCellValue("Người mua");
//		headerRow.createCell(2).setCellValue("Ngày tạo");
//		headerRow.createCell(3).setCellValue("Tổng");
//		headerRow.createCell(4).setCellValue("Trạng thái");
//
//		// Thiết lập font, kiểu chữ và màu sắc cho hàng tiêu đề
//		Font headerFont = workbook.createFont();
//		headerFont.setBold(true);
//		headerFont.setFontHeightInPoints((short) 12);
//		headerFont.setColor(IndexedColors.WHITE.getIndex());
//
//		CellStyle headerCellStyle = workbook.createCellStyle();
//		headerCellStyle.setFont(headerFont);
//		headerCellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
//		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
//
//		// Áp dụng định dạng cho hàng tiêu đề
//		for (Cell cell : headerRow) {
//			cell.setCellStyle(headerCellStyle);
//		}
//
//		// Định dạng dữ liệu và tạo các hàng dữ liệu
//		CellStyle dataCellStyle = workbook.createCellStyle();
//		dataCellStyle.setDataFormat(workbook.createDataFormat().getFormat("dd/MM/yyyy"));
//
//		int rowIdx = 2;
//
//		// Định dạng giá tiền
//		CellStyle currencyStyle = workbook.createCellStyle();
//		DataFormat dataFormat = workbook.createDataFormat();
//		currencyStyle.setDataFormat(dataFormat.getFormat("#,##0.00 [$VNĐ]"));
//
////	    // Căn giữa nội dung hàng trong ô
////	    currencyStyle.setAlignment(HorizontalAlignment.CENTER);
////	    currencyStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//
//		// Căn giữa nội dung hàng trong ô theo chiều ngang từ trái qua phải
//		currencyStyle.setAlignment(HorizontalAlignment.LEFT);
//
//		for (int i = 0; i < orders.size(); i++) {
//			Order data = orders.get(i);
//			Row row = sheet.createRow(rowIdx++);
//			row.createCell(0).setCellValue(i + 1);
//			row.createCell(1).setCellValue(data.getUser().getFirstname());
//			row.createCell(2).setCellValue(data.getOrderDateFormatted());
//			List<OrderDetail> orderDetails = data.getOrderDetail();
//			float totalPrice = 0.0f;
//			for (OrderDetail orderDetail : orderDetails) {
//				totalPrice += orderDetail.getTotalPrice();
//			}
//			// row.createCell(3).setCellValue(totalPrice);
//
//			Cell formattedTotalPriceCell = row.createCell(3);
//			formattedTotalPriceCell.setCellValue(totalPrice);
//			formattedTotalPriceCell.setCellStyle(currencyStyle);
//
//			row.createCell(4).setCellValue(data.getStatusOrder().getName());
//
//		}
//
//		// Prepare Excel file for download
//		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//		try {
//			workbook.write(outputStream);
//			workbook.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//		}
//
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//		headers.setContentDispositionFormData("attachment", "search_results.xlsx");
//
//		return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
//	}

	@PostMapping()
	public Order create(@RequestBody JsonNode orderData) {
		return orderService.create(orderData);
	}

	@PutMapping("{id}")
	public ResponseEntity<OrderDTO> update(@PathVariable("id") Integer id, @RequestBody Order order) {
		Order updatedOrder = orderService.update(order);

		if (updatedOrder == null) {
			return ResponseEntity.notFound().build();
		}
		OrderDTO orderDTO = modelMapper.map(updatedOrder, OrderDTO.class);
		return new ResponseEntity<>(orderDTO, HttpStatus.OK);
	}

	@GetMapping("/byStatusName")
	public ResponseEntity<List<OrderDTO>> getOrdersByStatusName(@RequestParam("statusName") String statusName) {
		List<Order> orders = orderService.getOrdersByStatusName(statusName);

		// Sử dụng ModelMapper để ánh xạ từ Order sang OrderDTO
		List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
				.collect(Collectors.toList());

		return new ResponseEntity<>(orderDTOs, HttpStatus.OK);
	}

	@GetMapping("/filter")
	public ResponseEntity<List<OrderDTO>> filterOrdersByNgayTao(
			@RequestParam("ngayTao") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDateTime ngayTao) {
		List<Order> filteredOrders = orderService.filterOrdersByNgayTao(ngayTao);

		// Sử dụng ModelMapper để ánh xạ từ Order sang OrderDTO
		List<OrderDTO> filteredOrderDTOs = filteredOrders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
				.collect(Collectors.toList());

		return ResponseEntity.ok(filteredOrderDTOs);
	}

	// Thống kê số lượng trạng thái
	@GetMapping("/slstatus")
	public ResponseEntity<List<ReportRevenue>> slstatus() {
		return new ResponseEntity<>(orderService.slstatus(), HttpStatus.OK);
	}

	@PutMapping("/cancel/{orderid}")
	public ResponseEntity<String> cancelOrder(@PathVariable("orderid") Integer orderid) {
		// Thực hiện các thao tác hủy đơn hàng
		try {
			orderService.cancelOrder(orderid);
			return new ResponseEntity<>("Đã hủy đơn hàng thành công", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Lỗi khi hủy đơn hàng: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/year-revenue")
	public ResponseEntity<List<ReportYear>> getyearRevenue() {
		List<ReportYear> yearRevenue = orderService.getYearRevenue();
		return new ResponseEntity<>(yearRevenue, HttpStatus.OK);
	}

	@GetMapping("/findyearrevenue/{year}")
	public List<FindReportYear> getYearlyRevenue(@PathVariable Integer year) {
		return orderService.findYearlyRevenue(year);
	}

	@GetMapping("/getCategorySalesByDate")
	public ResponseEntity<List<CategorySalesByDate>> getCategorySalesByDate(
			@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

		List<CategorySalesByDate> result = orderService.getCategorySalesByDate(date);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/last7days")
	public List<Report7day> getRevenueLast7Days() {
		return orderService.getRevenueLast7Days();
	}
}
