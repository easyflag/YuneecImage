package com.yuneec.image;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;
 

import java.io.File;
import java.io.FileOutputStream;

 
public class PdfReport {

	private static PdfReport pdfReport;
	public static PdfReport getInstance() {
		if (pdfReport == null) {
			pdfReport = new PdfReport();
		}
		return pdfReport;
	}
 
    public void creat(String fileName, String imagePath) throws Exception {
        try {
            // 1.新建document对象
            Document document = new Document(PageSize.A4);// 建立一个Document对象
 
            // 2.建立一个书写器(Writer)与document对象关联
            File file = new File(fileName);
            file.createNewFile();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
 
            // 3.打开文档
            document.open();
			document.addTitle("Title@PDF-Java");// 标题
			document.addAuthor("Author@umiz");// 作者
			document.addSubject("Subject@iText pdf sample");// 主题
			document.addKeywords("Keywords@iTextpdf");// 关键字
			document.addCreator("Creator@umiz`s");// 创建者
 
            // 4.向文档中添加内容
            new PdfReport().generatePDF(document,imagePath);
 
            // 5.关闭文档
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    // 定义全局的字体静态变量
	private static Font titlefont,titlefontChinese;
	private static Font headfont;
	private static Font keyfont;
	private static Font textfont;
    // 最大宽度
	private static int maxWidth = 520;
	// 静态代码块
    static {
        try {
            // 不同字体（这里定义为同一种字体：包含不同字号、不同style）
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			BaseFont bfEnglish = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);
			titlefontChinese = new Font(bfChinese, 18, Font.BOLD);
            titlefont = new Font(bfEnglish, 18, Font.BOLD);
            headfont = new Font(bfEnglish, 16, Font.BOLD);
            keyfont = new Font(bfEnglish, 10, Font.BOLD);
            textfont = new Font(bfEnglish, 10, Font.NORMAL);
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    // 生成PDF文件
	public void generatePDF(Document document,String imagePath) throws Exception {
 
    	// 段落
		Paragraph paragraph = new Paragraph("Yuneec Image", titlefont);
		paragraph.add(new Phrase(" 红外相机报告！",titlefontChinese));
		paragraph.setAlignment(1); //设置文字居中 0靠左   1，居中     2，靠右
		paragraph.setIndentationLeft(12); //设置左缩进
		paragraph.setIndentationRight(12); //设置右缩进
		paragraph.setFirstLineIndent(24); //设置首行缩进
		paragraph.setLeading(20f); //行间距
		paragraph.setSpacingBefore(5f); //设置段落上空白
		paragraph.setSpacingAfter(10f); //设置段落下空白

		// 直线
		Paragraph p1 = new Paragraph();
		p1.add(new Chunk(new LineSeparator()));
 
		// 点线
		Paragraph p2 = new Paragraph();
		p2.add(new Chunk(new DottedLineSeparator()));
 
//		// 超链接
//		Anchor anchor = new Anchor("baidu");
//		anchor.setReference("www.baidu.com");
// 
//		// 定位
//		Anchor gotoP = new Anchor("goto");
//		gotoP.setReference("#top");
 
		// 添加图片
		Image image = Image.getInstance(imagePath);
		image.setAlignment(Image.ALIGN_CENTER);
		image.scalePercent(40); //依照比例缩放

		// 表格
		PdfPTable table = createTable(new float[] { 80, 100, 100, 100, 80, 80 });
		PdfPCell imagePathName = createCell(imagePath, headfont, Element.ALIGN_LEFT, 6, false);
		imagePathName.setPaddingBottom(20.0f);
		table.addCell(imagePathName);
		table.addCell(createCell("Model", keyfont, Element.ALIGN_CENTER));
		table.addCell(createCell("Make", keyfont, Element.ALIGN_CENTER));
		table.addCell(createCell("Name/Info", keyfont, Element.ALIGN_CENTER));
		table.addCell(createCell("Time", keyfont, Element.ALIGN_CENTER));
		table.addCell(createCell("F-Number", keyfont, Element.ALIGN_CENTER));
		table.addCell(createCell("Aperture Value", keyfont, Element.ALIGN_CENTER));
		Integer totalQuantity = 0;
		for (int i = 0; i < 5; i++) {
			table.addCell(createCell(""+(i+1), textfont));
			table.addCell(createCell("11", textfont));
			table.addCell(createCell("22", textfont));
			table.addCell(createCell("33", textfont));
			table.addCell(createCell("44", textfont));
			table.addCell(createCell("55", textfont));
			totalQuantity ++;
		}
		table.addCell(createCell("total", keyfont));
		table.addCell(createCell("", textfont));
		table.addCell(createCell("", textfont));
		table.addCell(createCell("", textfont));
		table.addCell(createCell(String.valueOf(totalQuantity) + " image", textfont));
		table.addCell(createCell("end", textfont));

		Paragraph paragraphEnd = new Paragraph("...", textfont);
		paragraphEnd.setSpacingAfter(30f);

		document.add(paragraph);
//		document.add(anchor);
		document.add(p2);
//		document.add(gotoP);
		document.add(p1);
		document.add(table);
		document.add(paragraphEnd);
		document.add(image);
	}
 
 
/**------------------------创建表格单元格的方法start----------------------------*/
    /**
     * 创建单元格(指定字体)
     * @param value
     * @param font
     * @return
     */
    public PdfPCell createCell(String value, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }
    /**
     * 创建单元格（指定字体、水平..）
     * @param value
     * @param font
     * @param align
     * @return
     */
	public PdfPCell createCell(String value, Font font, int align) {
		PdfPCell cell = new PdfPCell();
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(align);
		cell.setPhrase(new Phrase(value, font));
		return cell;
	}
    /**
     * 创建单元格（指定字体、水平居..、单元格跨x列合并）
     * @param value
     * @param font
     * @param align
     * @param colspan
     * @return
     */
    public PdfPCell createCell(String value, Font font, int align, int colspan) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }
    /**
     * 创建单元格（指定字体、水平居..、单元格跨x列合并、设置单元格内边距）
     * @param value
     * @param font
     * @param align
     * @param colspan
     * @param boderFlag
     * @return
     */
    public PdfPCell createCell(String value, Font font, int align, int colspan, boolean boderFlag) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setPhrase(new Phrase(value, font));
        cell.setPadding(3.0f);
        if (!boderFlag) {
            cell.setBorder(0);
            cell.setPaddingTop(15.0f);
            cell.setPaddingBottom(8.0f);
        } else if (boderFlag) {
            cell.setBorder(0);
            cell.setPaddingTop(0.0f);
            cell.setPaddingBottom(15.0f);
        }
        return cell;
    }
    /**
     * 创建单元格（指定字体、水平..、边框宽度：0表示无边框、内边距）
     * @param value
     * @param font
     * @param align
     * @param borderWidth
     * @param paddingSize
     * @param flag
     * @return
     */
	public PdfPCell createCell(String value, Font font, int align, float[] borderWidth, float[] paddingSize, boolean flag) {
		PdfPCell cell = new PdfPCell();
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(align);
		cell.setPhrase(new Phrase(value, font));
		cell.setBorderWidthLeft(borderWidth[0]);
		cell.setBorderWidthRight(borderWidth[1]);
		cell.setBorderWidthTop(borderWidth[2]);
		cell.setBorderWidthBottom(borderWidth[3]);
		cell.setPaddingTop(paddingSize[0]);
		cell.setPaddingBottom(paddingSize[1]);
		if (flag) {
			cell.setColspan(2);
		}
		return cell;
	}
/**------------------------创建表格单元格的方法end----------------------------*/
 
 
/**--------------------------创建表格的方法start------------------- ---------*/
    /**
     * 创建默认列宽，指定列数、水平(居中、右、左)的表格
     * @param colNumber
     * @param align
     * @return
     */
	public PdfPTable createTable(int colNumber, int align) {
		PdfPTable table = new PdfPTable(colNumber);
		try {
			table.setTotalWidth(maxWidth);
			table.setLockedWidth(true);
			table.setHorizontalAlignment(align);
			table.getDefaultCell().setBorder(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return table;
	}
    /**
     * 创建指定列宽、列数的表格
     * @param widths
     * @return
     */
	public PdfPTable createTable(float[] widths) {
		PdfPTable table = new PdfPTable(widths);
		try {
			table.setTotalWidth(maxWidth);
			table.setLockedWidth(true);
			table.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.getDefaultCell().setBorder(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return table;
	}
    /**
     * 创建空白的表格
     * @return
     */
	public PdfPTable createBlankTable() {
		PdfPTable table = new PdfPTable(1);
		table.getDefaultCell().setBorder(0);
		table.addCell(createCell("", keyfont));
		table.setSpacingAfter(20.0f);
		table.setSpacingBefore(20.0f);
		return table;
	}
/**--------------------------创建表格的方法end------------------- ---------*/
 
 
}
