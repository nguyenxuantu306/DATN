package com.greenfarm.controller;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.greenfarm.entity.Category;
import com.greenfarm.entity.Product;
import com.greenfarm.entity.Report;
import com.greenfarm.entity.User;
import com.greenfarm.service.ProductService;
import com.greenfarm.service.UserService;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import java.text.DecimalFormat;

@RestController
public class PdfController {

	@Autowired
	UserService userService;

	@Autowired
	ProductService productService;

	@GetMapping("/export-to-pdf")
	public ResponseEntity<byte[]> exportToPDF() throws IOException, DocumentException {
		List<User> dataList = getAll(); // Hàm này tạo dữ liệu mẫu

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Document document = new Document();
		PdfWriter.getInstance(document, outputStream);

		// Khởi tạo font Unicode từ tệp font
		BaseFont unicodeFont = BaseFont.createFont("./static/Unicode/arial.ttf",
				BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

		document.open();// Thêm tiêu đề vào tài liệu
		String title = "Danh sách người dùng";
		document.addTitle(title);

		document.open();

		// Thêm tiêu đề chính
		// Tạo font chữ với font Unicode
		Font unicodeFonts = new Font(unicodeFont, 12, Font.NORMAL, BaseColor.BLACK);
		Paragraph titleParagraph = new Paragraph(title, unicodeFonts);

		titleParagraph.setAlignment(Element.ALIGN_CENTER);
		titleParagraph.setSpacingAfter(20); // Khoảng cách dưới tiêu đề chính
		document.add(titleParagraph);

		// Tạo bảng PDF
		PdfPTable table = new PdfPTable(7); // Số cột trong bảng

		// Thiết lập tiêu đề cho từng cột
		
		table.addCell(createCell("STT", true, unicodeFonts));
		table.addCell(createCell("Tên", true, unicodeFonts));
		table.addCell(createCell("Email", true, unicodeFonts));
		table.addCell(createCell("SDT", true, unicodeFonts));
		table.addCell(createCell("Địa chỉ", true, unicodeFonts));
		table.addCell(createCell("Giới tính", true, unicodeFonts));
		table.addCell(createCell("Ngày tạo", true, unicodeFonts));

		// Thêm dữ liệu từ danh sách vào bảng
		for (User data : dataList) {

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			
			String createDateString = dateFormat.format(data.getCreateddate());

			// Chuyển đổi số nguyên thành chuỗi trước khi truyền vào createCell
			table.addCell(createCell(data.getUserid().toString(), false, unicodeFonts));
			table.addCell(createCell(data.getFirstname(), false, unicodeFonts));
			table.addCell(createCell(data.getEmail(), false, unicodeFonts));
			table.addCell(createCell(data.getPhonenumber(), false, unicodeFonts));
			table.addCell(createCell(data.getAddress(), false, unicodeFonts));
			table.addCell(createCell(data.getGender() != null && data.getGender() ? "Nam" : "Nữ", false, unicodeFonts));
			table.addCell(createCell(createDateString, false, unicodeFonts));

		}

		document.add(table);
		document.close();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "exportPDF.pdf");

		return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
	}

	private PdfPCell createCell(String content) {
		return createCell(content, false, null);
	}

	private PdfPCell createCell(String content, boolean isHeader, Font font) {
		if (font == null) {
			font = new Font(FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
		}

		PdfPCell cell = new PdfPCell(new Phrase(content, font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	public final List<User> getAll() {
		return userService.findAll();
	}


	
	@GetMapping("/pdf-product")
	public ResponseEntity<byte[]> PDFProduct() throws IOException, DocumentException {
		List<Product> dataList = getAllProduct(); // Hàm này tạo dữ liệu mẫu

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Document document = new Document();
		PdfWriter.getInstance(document, outputStream);

		// Khởi tạo font Unicode từ tệp font
		BaseFont unicodeFont = BaseFont.createFont("./static/Unicode/arial.ttf",
				BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		
		document.open();// Thêm tiêu đề vào tài liệu
		String title = "Danh sách sản phẩm";
		
		document.addTitle(title);

		document.open();

		// Thêm tiêu đề chính
		// Tạo font chữ với font Unicode
		Font unicodeFonts = new Font(unicodeFont, 12, Font.NORMAL, BaseColor.BLACK);
		Paragraph titleParagraph = new Paragraph(title, unicodeFonts);

		titleParagraph.setAlignment(Element.ALIGN_CENTER);
		titleParagraph.setSpacingAfter(20); // Khoảng cách dưới tiêu đề chính
		document.add(titleParagraph);

		// Tạo bảng PDF
		PdfPTable table = new PdfPTable(4); // Số cột trong bảng

		// Thiết lập tiêu đề cho từng cột
		table.addCell(createCell("STT", true, unicodeFonts));
		table.addCell(createCell("Tên sản phẩm", true, unicodeFonts));
		table.addCell(createCell("Giá", true, unicodeFonts));
		table.addCell(createCell("Số lượng", true, unicodeFonts));

		// Định dạng giá tiền
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
		symbols.setGroupingSeparator(',');
		symbols.setDecimalSeparator('.');
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00 VNĐ", symbols);

		// Thiết lập dữ liệu cho từng hàng
		for (int i = 0; i < dataList.size(); i++) {
			Product data = dataList.get(i);

			table.addCell(createCell(String.valueOf(i + 1), false, unicodeFonts));
			table.addCell(createCell(data.getProductname(), false, unicodeFonts));
			table.addCell(createCell(decimalFormat.format(data.getPrice()), false, unicodeFonts));
			table.addCell(createCell(String.valueOf(data.getQuantityavailable()), false, unicodeFonts));
		
		}


		document.add(table);
		document.close();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "exportPDF.pdf");

		return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
	}

	public final List<Product> getProduct() {
		return productService.findAll();
	}
	
	
	public final List<Product> getAllProduct() {
		return productService.findAll();
	}
	@GetMapping("/pdf-productstatistics")
	public ResponseEntity<byte[]> PDFProductStatistics() throws IOException, DocumentException {
		List<Report> dataList = getProductStatitics(); // Hàm này tạo dữ liệu mẫu

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Document document = new Document();
		PdfWriter.getInstance(document, outputStream);

		// Khởi tạo font Unicode từ tệp font
		BaseFont unicodeFont = BaseFont.createFont("./static/Unicode/arial.ttf",
				BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		// String fontPath = "classpath:static/Unicode/arial.ttf"; // Đường dẫn tương
		// đối đến tệp font trong thư mục resources
		// BaseFont font = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H,
		// BaseFont.EMBEDDED);

		document.open();// Thêm tiêu đề vào tài liệu
		String title = "Danh sách thống kê sản phẩm";
		document.addTitle(title);

		document.open();

		// Thêm tiêu đề chính
		// Tạo font chữ với font Unicode
		Font unicodeFonts = new Font(unicodeFont, 12, Font.NORMAL, BaseColor.BLACK);
		Paragraph titleParagraph = new Paragraph(title, unicodeFonts);

		titleParagraph.setAlignment(Element.ALIGN_CENTER);
		titleParagraph.setSpacingAfter(20); // Khoảng cách dưới tiêu đề chính
		document.add(titleParagraph);

		// Tạo bảng PDF
		PdfPTable table = new PdfPTable(5); // Số cột trong bảng

		// Thiết lập tiêu đề cho từng cột
		table.addCell(createCell("STT", true, unicodeFonts));
		table.addCell(createCell("Tên sản phẩm", true, unicodeFonts));
		table.addCell(createCell("Giá", true, unicodeFonts));
		table.addCell(createCell("Số lượng sản phẩm đã bán", true, unicodeFonts));
		table.addCell(createCell("Tổng tiền", true, unicodeFonts));

		// Định dạng giá tiền
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
		symbols.setGroupingSeparator(',');
		symbols.setDecimalSeparator('.');
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00 VNĐ", symbols);

		// Thiết lập dữ liệu cho từng hàng
		for (int i = 0; i < dataList.size(); i++) {
		    Report data = dataList.get(i);
		    Product product = (Product) data.getGroup();

		    table.addCell(createCell(String.valueOf(i + 1), false, unicodeFonts));
		    table.addCell(createCell(product.getProductname(), false, unicodeFonts));
		    table.addCell(createCell(decimalFormat.format(product.getPrice()), false, unicodeFonts));
		    table.addCell(createCell(String.valueOf(data.getCount()), false, unicodeFonts));
		    table.addCell(createCell(decimalFormat.format(product.getPrice() * data.getCount()), false, unicodeFonts));
		}

		document.add(table);
		document.close();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "exportPDF.pdf");

		return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
	}

	public final List<Report> getProductStatitics() {
		return productService.getTk_sp();
	}
	
	
	@GetMapping("/pdf-categorytatistics")
	public ResponseEntity<byte[]> PDCAtegoryStatistics() throws IOException, DocumentException {
		List<Report> dataList = getCategoryStatitics(); // Hàm này tạo dữ liệu mẫu

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Document document = new Document();
		PdfWriter.getInstance(document, outputStream);

		// Khởi tạo font Unicode từ tệp font
		BaseFont unicodeFont = BaseFont.createFont("./static/Unicode/arial.ttf",
				BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		// String fontPath = "classpath:static/Unicode/arial.ttf"; // Đường dẫn tương
		

		document.open();// Thêm tiêu đề vào tài liệu
		String title = "Danh sách thống kê loại sản phẩm";
		document.addTitle(title);

		document.open();

		// Thêm tiêu đề chính
		// Tạo font chữ với font Unicode
		Font unicodeFonts = new Font(unicodeFont, 12, Font.NORMAL, BaseColor.BLACK);
		Paragraph titleParagraph = new Paragraph(title, unicodeFonts);

		titleParagraph.setAlignment(Element.ALIGN_CENTER);
		titleParagraph.setSpacingAfter(20); // Khoảng cách dưới tiêu đề chính
		document.add(titleParagraph);

		// Tạo bảng PDF
		PdfPTable table = new PdfPTable(4); // Số cột trong bảng

		// Thiết lập tiêu đề cho từng cột
		table.addCell(createCell("STT", true, unicodeFonts));
		table.addCell(createCell("Tên loại", true, unicodeFonts));
		table.addCell(createCell("Số sản phẩm", true, unicodeFonts));
		table.addCell(createCell("Tổng giá", true, unicodeFonts));

		// Định dạng giá tiền
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
		symbols.setGroupingSeparator(',');
		symbols.setDecimalSeparator('.');
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00 VNĐ", symbols);

		// Thiết lập dữ liệu cho từng hàng
		for (int i = 0; i < dataList.size(); i++) {
			Report data = dataList.get(i);
			Category category = (Category) data.getGroup();

			table.addCell(createCell(String.valueOf(i + 1), false, unicodeFonts));
			table.addCell(createCell(category.getCategoryname(), false, unicodeFonts));
			table.addCell(createCell(String.valueOf(data.getCount()), false, unicodeFonts));
			table.addCell(createCell(decimalFormat.format(data.getSum()), false, unicodeFonts));
		}

		document.add(table);
		document.close();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "exportPDF.pdf");

		return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
	}
	
	public final List<Report> getCategoryStatitics() {
		return productService.getTk_loai();
	}
}
