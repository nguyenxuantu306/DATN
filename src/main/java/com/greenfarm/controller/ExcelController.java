package com.greenfarm.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.http.HttpHeaders;
import java.util.List;
import java.util.Locale.Category;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greenfarm.entity.Address;
import com.greenfarm.entity.Booking;
import com.greenfarm.entity.Order;
import com.greenfarm.entity.OrderDetail;
import com.greenfarm.entity.Product;
import com.greenfarm.entity.ReportSP;
import com.greenfarm.entity.Tour;
import com.greenfarm.entity.TourDateBooking;
import com.greenfarm.entity.User;
import com.greenfarm.service.BookingService;
import com.greenfarm.service.CategoryService;
import com.greenfarm.service.OrderDetailService;
import com.greenfarm.service.OrderService;
import com.greenfarm.service.ProductService;
import com.greenfarm.service.TourDateBookingService;
//import com.greenfarm.service.ReCommentService;
import com.greenfarm.service.TourService;
import com.greenfarm.service.UserService;

@RestController

public class ExcelController {
	@Autowired
	UserService userService;

	@Autowired
	OrderDetailService oderDetailService;

	@Autowired
	ProductService productService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	OrderService orderService;

	@Autowired
	BookingService bookingService;

	@Autowired
	TourService tourService;

	@Autowired
	TourDateBookingService toudatebookingService;
	
	@GetMapping("/print-to-excel")
	public ResponseEntity<byte[]> printToExcel() throws IOException {
		List<User> dataList = getAll(); // Lấy dữ liệu từ hàm getAll()

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Data");

		// Đặt tiêu đề cho tài liệu Excel
		String title = "Danh sách người dùng";
		Row titleRow = sheet.createRow(0);
		Cell titleCell = titleRow.createCell(2);
		titleCell.setCellValue(title);

		// Thiết lập font và kiểu chữ cho tiêu đề
		Font titleFont = workbook.createFont();
		titleFont.setBold(true);
		titleFont.setFontHeightInPoints((short) 16);
		titleFont.setColor(IndexedColors.BLUE.getIndex());

		CellStyle titleCellStyle = workbook.createCellStyle();
		titleCellStyle.setFont(titleFont);
		titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
		titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		// Áp dụng định dạng cho ô tiêu đề
		titleCell.setCellStyle(titleCellStyle);

		// Tạo hàng tiêu đề và đặt giá trị cho các ô
		Row headerRow = sheet.createRow(1);
		headerRow.createCell(0).setCellValue("STT");
		headerRow.createCell(1).setCellValue("Tên");
		headerRow.createCell(2).setCellValue("Email");
		headerRow.createCell(3).setCellValue("SDT");
		headerRow.createCell(3).setCellValue("SĐT");
		headerRow.createCell(4).setCellValue("Địa chỉ");
		headerRow.createCell(5).setCellValue("Giới tính");
		headerRow.createCell(6).setCellValue("Ngày tạo");

		// Thiết lập font, kiểu chữ và màu sắc cho hàng tiêu đề
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 12);
		headerFont.setColor(IndexedColors.WHITE.getIndex());

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		headerCellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

		// Áp dụng định dạng cho hàng tiêu đề
		for (Cell cell : headerRow) {
			cell.setCellStyle(headerCellStyle);
		}

		// Định dạng dữ liệu và tạo các hàng dữ liệu
		CellStyle dataCellStyle = workbook.createCellStyle();
		dataCellStyle.setDataFormat(workbook.createDataFormat().getFormat("dd/MM/yyyy"));

		int rowIdx = 2;

		for (int i = 0; i < dataList.size(); i++) {
			User data = dataList.get(i);

			Row row = sheet.createRow(rowIdx++);
			row.createCell(0).setCellValue(i + 1);
			row.createCell(1).setCellValue(data.getFirstname());
			row.createCell(2).setCellValue(data.getEmail());
			row.createCell(3).setCellValue(data.getPhonenumber());
//	        row.createCell(4).setCellValue(data.getAddress());
			// Lấy danh sách địa chỉ có active = false
			List<Address> inactiveAddresses = data.getAddress().stream().filter(address -> !address.getActive())
					.collect(Collectors.toList());

			// Tạo một chuỗi chứa thông tin địa chỉ có active = false
			String addressInfo = inactiveAddresses.stream()
					.map(address -> address.getStreet() + ", " + address.getDistrict() + ", " + address.getCity())
					.collect(Collectors.joining("; "));

			// Đặt thông tin địa chỉ vào cột thứ 4
			row.createCell(4).setCellValue(addressInfo);
			// Set gender as "Male" or "Female"
			Cell genderCell = row.createCell(5);
			String gender = data.getGender() ? "Nam" : "Nữ";
			genderCell.setCellValue(gender);

			Cell createdayCell = row.createCell(6);
			createdayCell.setCellValue(data.getBirthday());
			createdayCell.setCellStyle(dataCellStyle);

		}

		// Tự động điều chỉnh cỡ các cột
		for (int i = 0; i < 5; i++) {
			sheet.autoSizeColumn(i);
		}

		// Gửi file Excel như là phản hồi HTTP
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		workbook.close();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "userdata.xlsx");

		return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
	}

	@GetMapping("/excel-product")
	public ResponseEntity<byte[]> ExcelProduct() throws IOException {
		List<Product> dataList = getAllProduct(); // Lấy dữ liệu từ hàm getAll()

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Danh sách sản phẩm");

		// Đặt tiêu đề cho tài liệu Excel
		String title = "Danh sách sản phẩm";
		Row titleRow = sheet.createRow(0);
		Cell titleCell = titleRow.createCell(2);
		titleCell.setCellValue(title);

		// Thiết lập font và kiểu chữ cho tiêu đề
		Font titleFont = workbook.createFont();
		titleFont.setBold(true);
		titleFont.setFontHeightInPoints((short) 16);
		titleFont.setColor(IndexedColors.BLUE.getIndex());

		CellStyle titleCellStyle = workbook.createCellStyle();
		titleCellStyle.setFont(titleFont);
		titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
		titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		// Áp dụng định dạng cho ô tiêu đề
		titleCell.setCellStyle(titleCellStyle);

		// Tạo hàng tiêu đề và đặt giá trị cho các ô
		Row headerRow = sheet.createRow(1);
		headerRow.createCell(0).setCellValue("STT");
		headerRow.createCell(1).setCellValue("Tên SP");
		headerRow.createCell(1).setCellValue("Tên sản phẩm");
		headerRow.createCell(2).setCellValue("Giá");
		headerRow.createCell(3).setCellValue("Số lượng");

		// Thiết lập font, kiểu chữ và màu sắc cho hàng tiêu đề
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 12);
		headerFont.setColor(IndexedColors.WHITE.getIndex());

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		headerCellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

		// Áp dụng định dạng cho hàng tiêu đề
		for (Cell cell : headerRow) {
			cell.setCellStyle(headerCellStyle);
		}
		int rowIdx = 2;
		// Định dạng giá tiền
		CellStyle currencyStyle = workbook.createCellStyle();
		DataFormat dataFormat = workbook.createDataFormat();
		currencyStyle.setDataFormat(dataFormat.getFormat("#,##0.00 [$VNĐ]"));
		sheet.setDefaultColumnStyle(3, currencyStyle);

		for (int i = 0; i < dataList.size(); i++) {
			Product data = dataList.get(i);
			Row row = sheet.createRow(rowIdx++);
			row.createCell(0).setCellValue(i + 1);
			row.createCell(1).setCellValue(data.getProductname());

			// Định dạng giá tiền
			Cell formattedSumCell = row.createCell(2);
			formattedSumCell.setCellValue(data.getPrice());
			formattedSumCell.setCellStyle(currencyStyle);

			row.createCell(3).setCellValue(data.getQuantityavailable());
		}

		// Tự động thay đổi độ rộng cột "sum"
		sheet.autoSizeColumn(2);

		// Áp dụng kiểu định dạng giá tiền cho cột "getPrice()" (cột 2)
		sheet.setDefaultColumnStyle(2, currencyStyle);

		// Áp dụng kiểu định dạng giá tiền cho cột "product.getPrice() *
		// data.getCount()" (cột 4)
		sheet.setDefaultColumnStyle(4, currencyStyle);

		// Tự động điều chỉnh cỡ các cột
		for (int i = 0; i < 4; i++) {
			sheet.autoSizeColumn(i);
		}

		// Gửi file Excel như là phản hồi HTTP
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		workbook.close();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "product.xlsx");

		return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
	}

	@GetMapping("/excel-productstatistics")
	public ResponseEntity<byte[]> ExcelCategoryStatistics() throws IOException {
		List<ReportSP> dataList = getProductStatistics(); // Lấy dữ liệu từ hàm getAll()

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Thống kê sản phẩm");

		// Đặt tiêu đề cho tài liệu Excel
		String title = "Danh sách thống kê sản phẩm";
		Row titleRow = sheet.createRow(0);
		Cell titleCell = titleRow.createCell(2);
		titleCell.setCellValue(title);

		// Thiết lập font và kiểu chữ cho tiêu đề
		Font titleFont = workbook.createFont();
		titleFont.setBold(true);
		titleFont.setFontHeightInPoints((short) 16);
		titleFont.setColor(IndexedColors.BLUE.getIndex());

		CellStyle titleCellStyle = workbook.createCellStyle();
		titleCellStyle.setFont(titleFont);
		titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
		titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		// Áp dụng định dạng cho ô tiêu đề
		titleCell.setCellStyle(titleCellStyle);

		// Tạo hàng tiêu đề và đặt giá trị cho các ô
		Row headerRow = sheet.createRow(1);
		headerRow.createCell(0).setCellValue("STT");
		headerRow.createCell(1).setCellValue("Tên SP");
		headerRow.createCell(2).setCellValue("Giá");
		headerRow.createCell(3).setCellValue("SL sản phẩm đã bán ");
		headerRow.createCell(4).setCellValue("Tổng ");
		headerRow.createCell(1).setCellValue("Tên sản phẩm");
		headerRow.createCell(2).setCellValue("Giá");
		headerRow.createCell(3).setCellValue("SL sản phẩm đã bán");
		headerRow.createCell(4).setCellValue("Tổng tiền");

		// Thiết lập font, kiểu chữ và màu sắc cho hàng tiêu đề
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 12);
		headerFont.setColor(IndexedColors.WHITE.getIndex());

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		headerCellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

		// Áp dụng định dạng cho hàng tiêu đề
		for (Cell cell : headerRow) {
			cell.setCellStyle(headerCellStyle);
		}

		// Định dạng dữ liệu và tạo các hàng dữ liệu
		CellStyle dataCellStyle = workbook.createCellStyle();
		dataCellStyle.setDataFormat(workbook.createDataFormat().getFormat("dd/MM/yyyy"));

		int rowIdx = 2;

		// Định dạng giá tiền
		CellStyle currencyStyle = workbook.createCellStyle();
		DataFormat dataFormat = workbook.createDataFormat();
		currencyStyle.setDataFormat(dataFormat.getFormat("#,##0.00 [$VNĐ]"));

		// Áp dụng kiểu định dạng giá tiền cho cột "getPrice()" (cột 2)
		sheet.setDefaultColumnStyle(2, currencyStyle);

		// Tự động thay đổi độ rộng các cột
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(4);

		// Tự động điều chỉnh cỡ các cột
		for (int i = 0; i < 5; i++) {
			sheet.autoSizeColumn(i);
		}

		// Gửi file Excel như là phản hồi HTTP
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		workbook.close();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "product_statistics.xlsx");

		return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
	}

	@GetMapping("/excel-categorystatistics")
	public ResponseEntity<byte[]> ExcelProductStatistics() throws IOException {
		List<ReportSP> dataList = getCategoryStatitics(); // Lấy dữ liệu từ hàm getAll()

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Thống kê loại sản phẩm");

		// Đặt tiêu đề cho tài liệu Excel
		String title = "Danh sách thống kê loại sản phẩm";
		Row titleRow = sheet.createRow(0);
		Cell titleCell = titleRow.createCell(2);
		titleCell.setCellValue(title);

		// Thiết lập font và kiểu chữ cho tiêu đề
		Font titleFont = workbook.createFont();
		titleFont.setBold(true);
		titleFont.setFontHeightInPoints((short) 16);
		titleFont.setColor(IndexedColors.BLUE.getIndex());

		CellStyle titleCellStyle = workbook.createCellStyle();
		titleCellStyle.setFont(titleFont);
		titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
		titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		// Áp dụng định dạng cho ô tiêu đề
		titleCell.setCellStyle(titleCellStyle);

		// Tạo hàng tiêu đề và đặt giá trị cho các ô
		Row headerRow = sheet.createRow(1);
		headerRow.createCell(0).setCellValue("STT");
		headerRow.createCell(1).setCellValue("Loại");
		headerRow.createCell(2).setCellValue("Số sản phẩm");
		headerRow.createCell(3).setCellValue("Tổng giá");

		// Thiết lập font, kiểu chữ và màu sắc cho hàng tiêu đề
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 12);
		headerFont.setColor(IndexedColors.WHITE.getIndex());

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		headerCellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

		// Áp dụng định dạng cho hàng tiêu đề
		for (Cell cell : headerRow) {
			cell.setCellStyle(headerCellStyle);
		}

		// Định dạng dữ liệu và tạo các hàng dữ liệu
		CellStyle dataCellStyle = workbook.createCellStyle();
		dataCellStyle.setDataFormat(workbook.createDataFormat().getFormat("dd/MM/yyyy"));

		int rowIdx = 2;

		// Định dạng giá tiền
		CellStyle currencyStyle = workbook.createCellStyle();
		DataFormat dataFormat = workbook.createDataFormat();

		currencyStyle.setDataFormat(dataFormat.getFormat("#,##0.00 [$VNĐ]"));
		sheet.setDefaultColumnStyle(3, currencyStyle);

		for (int i = 0; i < dataList.size(); i++) {
			ReportSP data = dataList.get(i);
			Row row = sheet.createRow(rowIdx++);
			row.createCell(0).setCellValue(i + 1);
			Category category = (Category) data.getGroup();
			row.createCell(1).setCellValue(category.getCategoryname());
			Cell countCell = row.createCell(2);
			countCell.setCellValue(data.getCount());

			// Định dạng giá tiền
			Cell formattedSumCell = row.createCell(3);
			formattedSumCell.setCellValue(data.getSum());
			formattedSumCell.setCellStyle(currencyStyle);
		}

		// Tự động thay đổi độ rộng cột "sum"
		sheet.autoSizeColumn(3);

		// Tự động điều chỉnh cỡ các cột
		for (int i = 0; i < 4; i++) {
			sheet.autoSizeColumn(i);
		}

		// Gửi file Excel như là phản hồi HTTP
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		workbook.close();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "category_statistics.xlsx");

		return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
	}

	@RequestMapping("/excel-order")
	public ResponseEntity<byte[]> order() throws IOException {
		List<Order> dataList = getAllOrder(); // Lấy dữ liệu từ hàm getAll()

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Danh sách đơn hàng");

		// Đặt tiêu đề cho tài liệu Excel
		String title = "Danh sách đơn hàng";
		Row titleRow = sheet.createRow(0);
		Cell titleCell = titleRow.createCell(2);
		titleCell.setCellValue(title);

		// Thiết lập font và kiểu chữ cho tiêu đề
		Font titleFont = workbook.createFont();
		titleFont.setBold(true);
		titleFont.setFontHeightInPoints((short) 16);
		titleFont.setColor(IndexedColors.BLUE.getIndex());

		CellStyle titleCellStyle = workbook.createCellStyle();
		titleCellStyle.setFont(titleFont);
		titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
		titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		// Áp dụng định dạng cho ô tiêu đề
		titleCell.setCellStyle(titleCellStyle);

		// Tạo hàng tiêu đề và đặt giá trị cho các ô
		Row headerRow = sheet.createRow(1);
		headerRow.createCell(0).setCellValue("STT");
		headerRow.createCell(1).setCellValue("Người mua");
		headerRow.createCell(2).setCellValue("Ngày tạo");
		headerRow.createCell(3).setCellValue("Tổng");
		headerRow.createCell(4).setCellValue("Trạng thái");

		// Thiết lập font, kiểu chữ và màu sắc cho hàng tiêu đề
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 12);
		headerFont.setColor(IndexedColors.WHITE.getIndex());

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		headerCellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

		// Áp dụng định dạng cho hàng tiêu đề
		for (Cell cell : headerRow) {
			cell.setCellStyle(headerCellStyle);
		}

		// Định dạng dữ liệu và tạo các hàng dữ liệu
		CellStyle dataCellStyle = workbook.createCellStyle();
		dataCellStyle.setDataFormat(workbook.createDataFormat().getFormat("dd/MM/yyyy"));

		int rowIdx = 2;

		// Định dạng giá tiền
		CellStyle currencyStyle = workbook.createCellStyle();
		DataFormat dataFormat = workbook.createDataFormat();
		currencyStyle.setDataFormat(dataFormat.getFormat("#,##0.00 [$VNĐ]"));

		// // Căn giữa nội dung hàng trong ô
		// currencyStyle.setAlignment(HorizontalAlignment.CENTER);
		// currencyStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		// Căn giữa nội dung hàng trong ô theo chiều ngang từ trái qua phải
		currencyStyle.setAlignment(HorizontalAlignment.LEFT);

		for (int i = 0; i < dataList.size(); i++) {
			Order data = dataList.get(i);
			Row row = sheet.createRow(rowIdx++);
			row.createCell(0).setCellValue(i + 1);
			row.createCell(1).setCellValue(data.getUser().getFirstname());
			row.createCell(2).setCellValue(data.getOrderDateFormatted());
			List<OrderDetail> orderDetails = data.getOrderDetail();
			float totalPrice = 0.0f;
			for (OrderDetail orderDetail : orderDetails) {
				totalPrice += orderDetail.getTotalPrice();
			}
			// row.createCell(3).setCellValue(totalPrice);

			Cell formattedTotalPriceCell = row.createCell(3);
			formattedTotalPriceCell.setCellValue(totalPrice);
			formattedTotalPriceCell.setCellStyle(currencyStyle);

			row.createCell(4).setCellValue(data.getStatusOrder().getName());

		}

		// Tự động điều chỉnh cỡ các cột
		for (int i = 0; i < 4; i++) {
			sheet.autoSizeColumn(i);
		}

		// Gửi file Excel như là phản hồi HTTP
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		workbook.close();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "order.xlsx");

		return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
	}

	@GetMapping("/excel-booking")
	public ResponseEntity<byte[]> bookings() throws IOException {
		List<Booking> dataList = getAllBookings(); // Lấy dữ liệu từ hàm getAll()

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Danh sách Booking Tours");

		// Đặt tiêu đề cho tài liệu Excel
		String title = "Danh sách Booking Tours";
		Row titleRow = sheet.createRow(0);
		Cell titleCell = titleRow.createCell(2);
		titleCell.setCellValue(title);

		// Thiết lập font và kiểu chữ cho tiêu đề
		Font titleFont = workbook.createFont();
		titleFont.setBold(true);
		titleFont.setFontHeightInPoints((short) 16);
		titleFont.setColor(IndexedColors.BLUE.getIndex());

		CellStyle titleCellStyle = workbook.createCellStyle();
		titleCellStyle.setFont(titleFont);
		titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
		titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		// Áp dụng định dạng cho ô tiêu đề
		titleCell.setCellStyle(titleCellStyle);

		// Tạo hàng tiêu đề và đặt giá trị cho các ô
		Row headerRow = sheet.createRow(1);
		headerRow.createCell(0).setCellValue("STT");
		headerRow.createCell(1).setCellValue("Tên tour");
		headerRow.createCell(2).setCellValue("Người đặt");
		headerRow.createCell(3).setCellValue("Thời gian tạo");
		headerRow.createCell(4).setCellValue("Số lượng người");
		headerRow.createCell(5).setCellValue("Tổng");
		headerRow.createCell(6).setCellValue("Trạng thái");

		// Thiết lập font, kiểu chữ và màu sắc cho hàng tiêu đề
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 12);
		headerFont.setColor(IndexedColors.WHITE.getIndex());

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		headerCellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

		// Áp dụng định dạng cho hàng tiêu đề
		for (Cell cell : headerRow) {
			cell.setCellStyle(headerCellStyle);
		}

		// Định dạng dữ liệu và tạo các hàng dữ liệu
		CellStyle dataCellStyle = workbook.createCellStyle();
		dataCellStyle.setDataFormat(workbook.createDataFormat().getFormat("dd/MM/yyyy"));

		int rowIdx = 2;

		// Định dạng giá tiền
		CellStyle currencyStyle = workbook.createCellStyle();
		DataFormat dataFormat = workbook.createDataFormat();
		currencyStyle.setDataFormat(dataFormat.getFormat("#,##0.00 [$VNĐ]"));

		// Căn giữa nội dung hàng trong ô theo chiều ngang từ trái qua phải
		currencyStyle.setAlignment(HorizontalAlignment.LEFT);

		for (int i = 0; i < dataList.size(); i++) {
			Booking data = dataList.get(i);
			Row row = sheet.createRow(rowIdx++);
			row.createCell(0).setCellValue(i + 1);
			row.createCell(1).setCellValue(data.getTour().getTourname());
			row.createCell(2).setCellValue(data.getUser().getFirstname());
			row.createCell(3).setCellValue(data.getBookingdateFormatted());
			row.createCell(4).setCellValue(data.getAdultticketnumber() + data.getChildticketnumber());
			row.createCell(5).setCellValue(data.getTotalprice());
			row.createCell(6).setCellValue(data.getStatusbooking().getName());

		}

		// Tự động điều chỉnh cỡ các cột
		for (int i = 0; i < 5; i++) {
			sheet.autoSizeColumn(i);
		}

		// Gửi file Excel như là phản hồi HTTP
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		workbook.close();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "category_statistics.xlsx");

		// Tự động điều chỉnh cỡ các cột
		for (int i = 0; i < 5; i++) {
			sheet.autoSizeColumn(i);
		}
		return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
	}

	@GetMapping("/excel-inventorystatistics")
	public ResponseEntity<byte[]> ExcelInventorystatistics() throws IOException {
		List<ReportSP> dataList = Inventorystatistics(); // Lấy dữ liệu từ hàm getAll()

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Thống kê hàng tồn kho");

		// Đặt tiêu đề cho tài liệu Excel
		String title = "Thống kê hàng tồn kho";
		Row titleRow = sheet.createRow(0);
		Cell titleCell = titleRow.createCell(2);
		titleCell.setCellValue(title);

		// Thiết lập font và kiểu chữ cho tiêu đề
		Font titleFont = workbook.createFont();
		titleFont.setBold(true);
		titleFont.setFontHeightInPoints((short) 16);
		titleFont.setColor(IndexedColors.BLUE.getIndex());

		CellStyle titleCellStyle = workbook.createCellStyle();
		titleCellStyle.setFont(titleFont);
		titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
		titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		// Áp dụng định dạng cho ô tiêu đề
		titleCell.setCellStyle(titleCellStyle);

		// Tạo hàng tiêu đề và đặt giá trị cho các ô
		Row headerRow = sheet.createRow(1);
		headerRow.createCell(0).setCellValue("STT");
		headerRow.createCell(1).setCellValue("Tên SP");
		headerRow.createCell(2).setCellValue("SL còn trong kho ");
		headerRow.createCell(3).setCellValue("Số lượng đã bán");

		// Thiết lập font, kiểu chữ và màu sắc cho hàng tiêu đề
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 12);
		headerFont.setColor(IndexedColors.WHITE.getIndex());

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		headerCellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

		// Áp dụng định dạng cho hàng tiêu đề
		for (Cell cell : headerRow) {
			cell.setCellStyle(headerCellStyle);
		}

		// Định dạng dữ liệu và tạo các hàng dữ liệu
		CellStyle dataCellStyle = workbook.createCellStyle();
		dataCellStyle.setDataFormat(workbook.createDataFormat().getFormat("dd/MM/yyyy"));

		int rowIdx = 2;

		for (int i = 0; i < dataList.size(); i++) {
			ReportSP data = dataList.get(i);
			Row row = sheet.createRow(rowIdx++);
			row.createCell(0).setCellValue(i + 1);
			Product product = (Product) data.getGroup();
			row.createCell(1).setCellValue(product.getProductname());
			row.createCell(2).setCellValue(product.getQuantityavailable());
			row.createCell(3).setCellValue(data.getCount());
		}

		// Tự động điều chỉnh cỡ các cột
		for (int i = 0; i < 4; i++) {
			sheet.autoSizeColumn(i);
		}

		// Gửi file Excel như là phản hồi HTTP
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		workbook.close();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "Inventorystatistics.xlsx");

		return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
	}

	@GetMapping("/excel-tour")
	public ResponseEntity<byte[]> ExcelTour() throws IOException {
		List<Tour> dataList = getAllTour(); // Lấy dữ liệu từ hàm getAll()

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Danh sách Tours");

		// Đặt tiêu đề cho tài liệu Excel
		String title = "Danh sách Tours";
		Row titleRow = sheet.createRow(0);
		Cell titleCell = titleRow.createCell(3);
		titleCell.setCellValue(title);

		// Thiết lập font và kiểu chữ cho tiêu đề
		Font titleFont = workbook.createFont();
		titleFont.setBold(true);
		titleFont.setFontHeightInPoints((short) 16);
		titleFont.setColor(IndexedColors.BLUE.getIndex());

		CellStyle titleCellStyle = workbook.createCellStyle();
		titleCellStyle.setFont(titleFont);
		titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
		titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		// Áp dụng định dạng cho ô tiêu đề
		titleCell.setCellStyle(titleCellStyle);

		// Tạo hàng tiêu đề và đặt giá trị cho các ô
		Row headerRow = sheet.createRow(1);
		headerRow.createCell(0).setCellValue("STT");
		headerRow.createCell(1).setCellValue("Tên Tours");
		headerRow.createCell(2).setCellValue("Ngày");
		headerRow.createCell(3).setCellValue("Giá");
		headerRow.createCell(4).setCellValue("Mô tả");
		headerRow.createCell(5).setCellValue("Điều kiện");
		headerRow.createCell(6).setCellValue("Tổng quan");

		// Thiết lập font, kiểu chữ và màu sắc cho hàng tiêu đề
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 12);
		headerFont.setColor(IndexedColors.WHITE.getIndex());

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		headerCellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

		// Áp dụng định dạng cho hàng tiêu đề
		for (Cell cell : headerRow) {
			cell.setCellStyle(headerCellStyle);
		}
		int rowIdx = 2;
		// Định dạng giá tiền
		CellStyle currencyStyle = workbook.createCellStyle();
		DataFormat dataFormat = workbook.createDataFormat();
		currencyStyle.setDataFormat(dataFormat.getFormat("#,##0.00 [$VNĐ]"));
		sheet.setDefaultColumnStyle(3, currencyStyle);

		for (int i = 0; i < dataList.size(); i++) {
			Tour data = dataList.get(i);
			Row row = sheet.createRow(rowIdx++);

			row.createCell(0).setCellValue(i + 1);
			row.createCell(1).setCellValue(data.getTourname());
			row.createCell(2).setCellValue(data.getDepartureday());
			
			// Định dạng giá tiền
			Cell formattedSumCell = row.createCell(3);
			formattedSumCell.setCellValue(data.getPricings().getAdultprice());
			formattedSumCell.setCellStyle(currencyStyle);
//			row.createCell(4).setCellValue(data.getDescription());

			// row.createCell(4).setCellValue(data.getAvailableslots());

			// Kiểm tra và hiển thị "đã có" nếu điều kiện được đáp ứng
			String description = data.getDescription();
			String tourConditions = data.getTourCondition().getConditions();
			String tourOverviewContent = data.getTourOverview().getContent();

			Cell descriptioncell = row.createCell(4);
			if (description != null && !description.isEmpty()) {
				descriptioncell.setCellValue("Đã có");
			} else {
				descriptioncell.setCellValue("");
			}
			
			Cell conditionCell = row.createCell(5);
			if (tourConditions != null && !tourConditions.isEmpty()) {
				conditionCell.setCellValue("Đã có");
			} else {
				conditionCell.setCellValue("");
			}

			Cell overviewCell = row.createCell(6);
			if (tourOverviewContent != null && !tourOverviewContent.isEmpty()) {
				overviewCell.setCellValue("Đã có");
			} else {
				overviewCell.setCellValue("");
			}
		}

		// Tự động thay đổi độ rộng cột "sum"
		sheet.autoSizeColumn(2);

		// Áp dụng kiểu định dạng giá tiền cho cột "product.getPrice() *
		// data.getCount()" (cột 4)
		sheet.setDefaultColumnStyle(3, currencyStyle);

		// Tự động thay đổi độ rộng các cột
		sheet.autoSizeColumn(1);

		// Tự động điều chỉnh cỡ các cột
		for (int i = 0; i < 7; i++) {
			sheet.autoSizeColumn(i);
		}

		// Gửi file Excel như là phản hồi HTTP
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		workbook.close();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "tour.xlsx");

		return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
	}
	
	@GetMapping("/excel-tourDateBooking")
	public ResponseEntity<byte[]> TourDateBooking() throws IOException {
		List<TourDateBooking> dataList = getTourDateBooking(); // Lấy dữ liệu từ hàm getAll()

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Danh sách thời gian đặt tour");

		// Đặt tiêu đề cho tài liệu Excel
		String title = "Danh sách thời gian đặt tour";
		Row titleRow = sheet.createRow(0);
		Cell titleCell = titleRow.createCell(2);
		titleCell.setCellValue(title);

		// Thiết lập font và kiểu chữ cho tiêu đề
		Font titleFont = workbook.createFont();
		titleFont.setBold(true);
		titleFont.setFontHeightInPoints((short) 16);
		titleFont.setColor(IndexedColors.BLUE.getIndex());

		CellStyle titleCellStyle = workbook.createCellStyle();
		titleCellStyle.setFont(titleFont);
		titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
		titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		// Áp dụng định dạng cho ô tiêu đề
		titleCell.setCellStyle(titleCellStyle);

		// Tạo hàng tiêu đề và đặt giá trị cho các ô
		Row headerRow = sheet.createRow(1);
		headerRow.createCell(0).setCellValue("STT");
		headerRow.createCell(1).setCellValue("Tên tour");
		headerRow.createCell(2).setCellValue("Ngày đặt");
		headerRow.createCell(3).setCellValue("Tổng chi");
		headerRow.createCell(4).setCellValue("Ngày khởi hành");
		headerRow.createCell(5).setCellValue("Số còn trống");

		// Thiết lập font, kiểu chữ và màu sắc cho hàng tiêu đề
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 12);
		headerFont.setColor(IndexedColors.WHITE.getIndex());

		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		headerCellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

		// Áp dụng định dạng cho hàng tiêu đề
		for (Cell cell : headerRow) {
			cell.setCellStyle(headerCellStyle);
		}

		CellStyle dateCellStyle = workbook.createCellStyle();
		dateCellStyle.setDataFormat(workbook.createDataFormat().getFormat("dd/MM/yyyy"));

		int rowIdx = 2;

		// Định dạng giá tiền
		CellStyle currencyStyle = workbook.createCellStyle();
		DataFormat dataFormat = workbook.createDataFormat();
		currencyStyle.setDataFormat(dataFormat.getFormat("#,##0 [$Đ]"));

		CellStyle centeredCellStyle = workbook.createCellStyle();
		centeredCellStyle.setAlignment(HorizontalAlignment.CENTER);
		centeredCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		for (int i = 0; i < dataList.size(); i++) {
			TourDateBooking data = dataList.get(i);
			Row row = sheet.createRow(rowIdx++);
			row.createCell(0).setCellValue(i + 1);
			row.createCell(1).setCellValue(data.getBooking().getTour().getTourname());
			// Tạo ô cho ngày và áp dụng định dạng
			Cell dateCell = row.createCell(2);
			dateCell.setCellValue(data.getTourdate().getTourdates());
			dateCell.setCellStyle(dateCellStyle);
			Cell priceCell = row.createCell(3);
			priceCell.setCellValue(data.getBooking().getTotalprice());
			priceCell.setCellStyle(currencyStyle);
			row.createCell(4).setCellValue(data.getBooking().getTour().getDepartureday());
			row.createCell(5).setCellValue(data.getTourdate().getAvailableslots());

		}

		// Tự động điều chỉnh cỡ các cột
		for (int i = 0; i < 5; i++) {
			sheet.autoSizeColumn(i);
		}

		// Gửi file Excel như là phản hồi HTTP
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		workbook.close();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "category_statistics.xlsx");

		return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
	}

	public final List<User> getAll() {
		return userService.findAll();
	}

	public final List<Product> getProduct() {
		return productService.findAll();
	}

	public final List<Product> getAllProduct() {
		return productService.findAll();
	}

	public final List<ReportSP> getCategoryStatitics() {
		return productService.getTk_loai();
	}

	public final List<Order> getAllOrder() {
		return orderService.findAll();
	}

	public final List<Booking> getAllBookings() {
		return bookingService.findAll();
	}

	public final List<Tour> getAllTour() {
		return tourService.findAll();
	}

	public final List<ReportSP> Inventorystatistics() {
		return productService.getTk_sp();
	}

	public final List<ReportSP> getProductStatistics() {
		return productService.getTk_sp();
	}
	public final List<TourDateBooking> getTourDateBooking() {
		return toudatebookingService.findAll();
	}
}
