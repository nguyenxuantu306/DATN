//package com.greenfarm.controller;
//
//import com.greenfarm.entity.Report;
//import com.greenfarm.entity.ReportSP;
//import com.greenfarm.entity.Tour;
//import com.greenfarm.entity.TourDateBooking;
//import com.greenfarm.entity.User;
//import com.greenfarm.service.BookingService;
//import com.greenfarm.service.OrderService;
//import com.greenfarm.service.ProductService;
//import com.greenfarm.service.TourDateBookingService;
//import com.greenfarm.service.TourService;
//import com.greenfarm.service.UserService;
//import com.itextpdf.text.BaseColor;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.pdf.PdfPCell;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfWriter;
//
//import java.io.IOException;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class PdfController {
//	@Autowired
//	TourService tourService;
//
//	@Autowired
//	TourDateBookingService toudatebookingService;
//
//	@GetMapping("/export-to-pdf")
//	public ResponseEntity<byte[]> exportToPDF() throws IOException, DocumentException {
//		List<User> dataList = getAll(); // Hàm này tạo dữ liệu mẫu
//				BaseFont.EMBEDDED);
//
//		document.open();// Thêm tiêu đề vào tài liệu
//		String title = "Danh sách người dùng";
//		String title = "DANH SÁCH NGƯỜI DÙNG";
//		document.addTitle(title);
//
//		document.open();
//					.map(address -> address.getStreet() + ", " + address.getDistrict() + ", " + address.getCity())
//					.collect(Collectors.joining("; "));
//			// Thêm thông tin địa chỉ vào cột thứ 4
//			table.addCell(createCell(addressInfo, false, unicodeFonts)); 
//			table.addCell(createCell(addressInfo, false, unicodeFonts));
//			table.addCell(createCell(data.getGender() != null && data.getGender() ? "Nam" : "Nữ", false, unicodeFonts));
//			table.addCell(createCell(createDateString, false, unicodeFonts));
//
//				BaseFont.EMBEDDED);
//
//		document.open();// Thêm tiêu đề vào tài liệu
//		String title = "Danh sách sản phẩm";
//		String title = "DANH SÁCH SẢN PHẨM";
//
//		document.addTitle(title);
//
//		// BaseFont.EMBEDDED);
//
//		document.open();// Thêm tiêu đề vào tài liệu
//		String title = "Danh sách thống kê sản phẩm";
//		String title = "DANH SÁCH THỐNG KÊ SẢN PHẨM";
//		document.addTitle(title);
//
//		document.open();
//		// String fontPath = "classpath:static/Unicode/arial.ttf"; // Đường dẫn tương
//
//		document.open();// Thêm tiêu đề vào tài liệu
//		String title = "Danh sách thống kê loại sản phẩm";
//		String title = "DANH SÁCH THỐNG KÊ LOẠI SẢN PHẨM";
//		document.addTitle(title);
//
//		document.open();
//		// String fontPath = "classpath:static/Unicode/arial.ttf"; // Đường dẫn tương
//
//		document.open();// Thêm tiêu đề vào tài liệu
//		String title = "Danh sách đơn hàng";
//		String title = "DANH SÁCH ĐƠN HÀNG";
//		document.addTitle(title);
//
//		document.open();
//		// String fontPath = "classpath:static/Unicode/arial.ttf"; // Đường dẫn tương
//
//		document.open();// Thêm tiêu đề vào tài liệu
//		String title = "Danh sách Booking tours";
//		String title = "DANH SÁCH ĐẶT TOURS";
//		document.addTitle(title);
//
//		document.open();
//		// BaseFont.EMBEDDED);
//
//		document.open();// Thêm tiêu đề vào tài liệu
//		String title = "Thống kê hàng tồn kho";
//		String title = "THỐNG KÊ HÀNG TỒN KHO";
//		document.addTitle(title);
//
//		document.open();
//				BaseFont.EMBEDDED);
//
//		document.open();// Thêm tiêu đề vào tài liệu
//		String title = "Danh sách Tour";
//		String title = "DANH SÁCH TOURS";
//
//		document.addTitle(title);
//
//		return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
//	}
//
//	@GetMapping("/pdf-tourDateBooking")
//	public ResponseEntity<byte[]> ExcelTourDateBooking() throws IOException, DocumentException {
//		List<TourDateBooking> dataList = getTourDateBooking(); // Hàm này tạo dữ liệu mẫu
//
//		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//		Document document = new Document();
//		PdfWriter.getInstance(document, outputStream);
//
//		// Khởi tạo font Unicode từ tệp font
//		BaseFont unicodeFont = BaseFont.createFont("./static/Unicode/arial.ttf", BaseFont.IDENTITY_H,
//				BaseFont.EMBEDDED);
//		// String fontPath = "classpath:static/Unicode/arial.ttf"; // Đường dẫn tương
//
//		document.open();// Thêm tiêu đề vào tài liệu
//		String title = "DANH SÁCH THỜI GIAN ĐẶT TOURS";
//		document.addTitle(title);
//
//		document.open();
//
//		// Thêm tiêu đề chính
//		// Tạo font chữ với font Unicode
//		Font unicodeFonts = new Font(unicodeFont, 12, Font.NORMAL, BaseColor.BLACK);
//		Paragraph titleParagraph = new Paragraph(title, unicodeFonts);
//
//		titleParagraph.setAlignment(Element.ALIGN_CENTER);
//		titleParagraph.setSpacingAfter(20); // Khoảng cách dưới tiêu đề chính
//		document.add(titleParagraph);
//
//		// Tạo bảng PDF
//		PdfPTable table = new PdfPTable(6); // Số cột trong bảng
//
//		// Thiết lập tiêu đề cho từng cột
//		table.addCell(createCell("STT", true, unicodeFonts));
//		table.addCell(createCell("Tên tour", true, unicodeFonts));
//		table.addCell(createCell("Ngày đặt", true, unicodeFonts));
//		table.addCell(createCell("Tổng chi", true, unicodeFonts));
//		table.addCell(createCell("Ngày khởi hành", true, unicodeFonts));
//		table.addCell(createCell("Số chỗ còn trống", true, unicodeFonts));
//
//		// Định dạng giá tiền
//		DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
//		symbols.setGroupingSeparator(',');
//		symbols.setDecimalSeparator('.');
//		DecimalFormat decimalFormat = new DecimalFormat("#,##0 Đ", symbols);
//
//		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//		// Thiết lập dữ liệu cho từng hàng
//		for (int i = 0; i < dataList.size(); i++) {
//			TourDateBooking data = dataList.get(i);
//
//			table.addCell(createCell(String.valueOf(i + 1), false, unicodeFonts));
//			table.addCell(createCell(data.getBooking().getTour().getTourname(), false, unicodeFonts));
//			// table.addCell(createCell(data.getTourdate().getTourdates().toString(), false,
//			// unicodeFonts)); // Convert LocalDate to String
//
//			// Format the date using DateTimeFormatter
//			String formattedDate = data.getTourdate().getTourdates().format(dateFormatter);
//			table.addCell(createCell(formattedDate, false, unicodeFonts));
//
//			table.addCell(createCell(decimalFormat.format(data.getBooking().getTotalprice()), false, unicodeFonts));
//
//			table.addCell(
//					createCell(String.valueOf(data.getBooking().getTour().getDepartureday()), false, unicodeFonts)); // Convert
//																														// Integer
//																														// to
//																														// String
//			table.addCell(createCell(String.valueOf(data.getTourdate().getAvailableslots()), false, unicodeFonts)); // Convert
//																													// Integer
//																													// to
//																													// String
//		}
//
//		document.add(table);
//		document.close();
//
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//		headers.setContentDispositionFormData("attachment", "exportPDF.pdf");
//
//		return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
//	}
//
//	public final List<Order> getAllOrder() {
//		return orderService.findAll();
//	}
//
//	public final List<Tour> getAllTour() {
//		return tourService.findAll();
//	}
//
//	public final List<TourDateBooking> getTourDateBooking() {
//		return toudatebookingService.findAll();
//	}
//}