package com.greenfarm.controller;
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
<<<<<<< Updated upstream
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
=======
>>>>>>> Stashed changes
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.greenfarm.entity.Category;
import com.greenfarm.entity.Product;
import com.greenfarm.entity.Report;
import com.greenfarm.entity.User;
import com.greenfarm.service.CategoryService;
import com.greenfarm.service.OrderDetailService;
import com.greenfarm.service.ProductService;
import com.greenfarm.service.UserService;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

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
<<<<<<< Updated upstream
	    headerRow.createCell(3).setCellValue("SDT");
=======
	    headerRow.createCell(3).setCellValue("SĐT");
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
	    for (User data : dataList) {
	        Row row = sheet.createRow(rowIdx++);
	        row.createCell(0).setCellValue(data.getUserid());
=======
	    for (int i = 0; i < dataList.size(); i++) {
	        User data = dataList.get(i);
	        
	        Row row = sheet.createRow(rowIdx++);
	        row.createCell(0).setCellValue(i + 1);
>>>>>>> Stashed changes
	        row.createCell(1).setCellValue(data.getFirstname());
	        row.createCell(2).setCellValue(data.getEmail());
	        row.createCell(3).setCellValue(data.getPhonenumber());
	        row.createCell(4).setCellValue(data.getAddress());
	        
	     // Set gender as "Male" or "Female"
	        Cell genderCell = row.createCell(5);
	        String gender = data.getGender() ? "Nam" : "Nữ";
	        genderCell.setCellValue(gender);
	       

	        // Đặt giá trị và định dạng cho ô Birthday
<<<<<<< Updated upstream
	        Cell createdayCell = row.createCell(6);
	        createdayCell.setCellValue(data.getBirthday());
	        createdayCell.setCellStyle(dataCellStyle);
=======
	        Cell birthdayCell = row.createCell(6);
	        birthdayCell.setCellValue(data.getCreateddate());
	        birthdayCell.setCellStyle(dataCellStyle);
>>>>>>> Stashed changes

	        
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
<<<<<<< Updated upstream
	
	
	@GetMapping("/excel-product")
	public ResponseEntity<byte[]> ExcelProduct() throws IOException {
	    List<Product> dataList = getProduct(); // Lấy dữ liệu từ hàm getAll()
=======
	// Sản phẩm
	@GetMapping("/excel-product")
	public ResponseEntity<byte[]> ExcelProduct() throws IOException {
	    List<Product> dataList = getAllProduct(); // Lấy dữ liệu từ hàm getAll()
>>>>>>> Stashed changes

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
<<<<<<< Updated upstream
	    headerRow.createCell(1).setCellValue("Tên SP");
=======
	    headerRow.createCell(1).setCellValue("Tên sản phẩm");
>>>>>>> Stashed changes
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

<<<<<<< Updated upstream

	    int rowIdx = 2;
	    
=======
	    int rowIdx = 2;
>>>>>>> Stashed changes
	 // Định dạng giá tiền
	    CellStyle currencyStyle = workbook.createCellStyle();
	    DataFormat dataFormat = workbook.createDataFormat();
	    currencyStyle.setDataFormat(dataFormat.getFormat("#,##0.00 [$VNĐ]"));
<<<<<<< Updated upstream
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
=======

	    // Áp dụng kiểu định dạng giá tiền cho cột "getPrice()" (cột 2)
	    sheet.setDefaultColumnStyle(2, currencyStyle);

	    // Áp dụng kiểu định dạng giá tiền cho cột "product.getPrice() * data.getCount()" (cột 4)
	    sheet.setDefaultColumnStyle(4, currencyStyle);
	    
	    for (int i = 0; i < dataList.size(); i++) {
	        Product data = dataList.get(i);
	        Row row = sheet.createRow(rowIdx++);

	        row.createCell(0).setCellValue(i + 1);
	        row.createCell(1).setCellValue(data.getProductname());

	        Cell priceCell = row.createCell(2);
	        priceCell.setCellValue(data.getPrice());
	        priceCell.setCellStyle(currencyStyle);

	        row.createCell(3).setCellValue(data.getQuantityavailable());
	    }

	 // Tự động thay đổi độ rộng các cột
	    sheet.autoSizeColumn(1);
>>>>>>> Stashed changes
	    
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
<<<<<<< Updated upstream
	    headers.setContentDispositionFormData("attachment", "categorystatistics.xlsx");
=======
	    headers.setContentDispositionFormData("attachment", "product.xlsx");
>>>>>>> Stashed changes

	    return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
	}
	
<<<<<<< Updated upstream
=======
	
>>>>>>> Stashed changes
	@GetMapping("/excel-productstatistics")
	public ResponseEntity<byte[]> ExcelCategoryStatistics() throws IOException {
	    List<Report> dataList = getProductStatitics(); // Lấy dữ liệu từ hàm getAll()

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
<<<<<<< Updated upstream
	    headerRow.createCell(1).setCellValue("Tên SP");
	    headerRow.createCell(2).setCellValue("Giá");
	    headerRow.createCell(3).setCellValue("SL sản phẩm đã bán ");
	    headerRow.createCell(4).setCellValue("Tổng ");
=======
	    headerRow.createCell(1).setCellValue("Tên sản phẩm");
	    headerRow.createCell(2).setCellValue("Giá");
	    headerRow.createCell(3).setCellValue("SL sản phẩm đã bán");
	    headerRow.createCell(4).setCellValue("Tổng tiền");
>>>>>>> Stashed changes
	    
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

	    // Áp dụng kiểu định dạng giá tiền cho cột "product.getPrice() * data.getCount()" (cột 4)
	    sheet.setDefaultColumnStyle(4, currencyStyle);
	    
	    for (int i = 0; i < dataList.size(); i++) {
	        Report data = dataList.get(i);
	        Row row = sheet.createRow(rowIdx++);
	        row.createCell(0).setCellValue(i + 1);
	        Product product = (Product) data.getGroup();
	        row.createCell(1).setCellValue(product.getProductname());
	        //row.createCell(2).setCellValue(product.getPrice());
	        
	        // Định dạng giá tiền cho "getPrice()"
	        Cell priceCell = row.createCell(2);
	        priceCell.setCellValue(product.getPrice());
	        priceCell.setCellStyle(currencyStyle);
	        
	        row.createCell(3).setCellValue(data.getCount());
<<<<<<< Updated upstream
	      
=======
	        
	        
	        //row.createCell(4).setCellValue(product.getPrice() * data.getCount());
	    
	        // Định dạng giá tiền cho "product.getPrice() * data.getCount()"
>>>>>>> Stashed changes
	        Cell totalPriceCell = row.createCell(4);
	        totalPriceCell.setCellValue(product.getPrice() * data.getCount());
	        totalPriceCell.setCellStyle(currencyStyle);
	    }

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
<<<<<<< Updated upstream
	    headers.setContentDispositionFormData("attachment", "productstatistics.xlsx");
=======
	    headers.setContentDispositionFormData("attachment", "product_statistics.xlsx");
>>>>>>> Stashed changes

	    return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
	}
	
	@GetMapping("/excel-categorystatistics")
	public ResponseEntity<byte[]> ExcelProductStatistics() throws IOException {
	    List<Report> dataList = getCategoryStatitics(); // Lấy dữ liệu từ hàm getAll()

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
	        Report data = dataList.get(i);
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
<<<<<<< Updated upstream
	    headers.setContentDispositionFormData("attachment", "categorystatistics.xlsx");
=======
	    headers.setContentDispositionFormData("attachment", "category_statistics.xlsx");
>>>>>>> Stashed changes

	    return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
	}
	
	public final List<User> getAll() {
		return userService.findAll();
	}
	
<<<<<<< Updated upstream
	public final List<Product> getProduct() {
=======
	public final List<Product> getAllProduct() {
>>>>>>> Stashed changes
		return productService.findAll();
	}
	
	public final List<Report> getProductStatitics() {
		return productService.getTk_sp();
	}
	public final List<Report> getCategoryStatitics() {
		return productService.getTk_loai();
	}
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
}
