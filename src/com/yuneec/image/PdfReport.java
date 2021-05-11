package com.yuneec.image;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.yuneec.image.module.Language;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class PdfReport {

	private String tempImagePath = "C:\\tempYImage.png";
	private static PdfReport pdfReport;
	public static PdfReport getInstance() {
		if (pdfReport == null) {
			pdfReport = new PdfReport();
		}
		return pdfReport;
	}
 
    public void creat(String fileName) throws Exception {
        try {
            Document document = new Document(PageSize.A4);
 
            File file = new File(fileName);
            file.createNewFile();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
 
            document.open();

            generatePDF(document);
 
            document.close();

			new File(tempImagePath).delete();

			ToastUtil.toast(Language.getString("PDF report generated successfully !","PDF报告生成成功!"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
	private static Font titlefont,titlefontCh;
	private static Font headfont,headfontCh;
	private static Font keyfont,keyfontCh;
	private static Font textfont;
	private static Font textfontCh;
	private static Font boxTemperaturefont,boxTemperaturefontCh;
	private static int maxWidth = 520;
    static {
        try {
            // 不同字体（这里定义为同一种字体：包含不同字号、不同style）
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			BaseFont bfEnglish = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);
			titlefontCh = new Font(bfChinese, 18, Font.BOLD);
            titlefont = new Font(bfEnglish, 18, Font.BOLD);
            headfont = new Font(bfEnglish, 16, Font.BOLD);
			headfontCh = new Font(bfChinese, 16, Font.BOLD);
            keyfont = new Font(bfEnglish, 12, Font.BOLD);
			keyfontCh = new Font(bfChinese, 12, Font.BOLD);
            textfont = new Font(bfEnglish, 12, Font.NORMAL);
			textfontCh = new Font(bfChinese, 12, Font.NORMAL);
			boxTemperaturefont = new Font(bfEnglish, 14, Font.BOLD);
			boxTemperaturefontCh = new Font(bfChinese, 14, Font.BOLD);
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
	public void generatePDF(Document document) throws Exception {
    	// 段落
		Paragraph paragraph = new Paragraph(Language.getString("Yuneec Image","Yuneec 图片"), Language.isEnglish()?titlefont:titlefontCh);
		paragraph.add(new Phrase(Language.getString(" Infrared Camera Report ","红外报告"),Language.isEnglish()?titlefont:titlefontCh));
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
		//time
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = formatter.format(date);
		Paragraph paragraphTime = new Paragraph(time, textfont);
		paragraphTime.setAlignment(2);
		// 点线
		Paragraph p2 = new Paragraph();
		p2.add(new Chunk(new DottedLineSeparator()));
//		// 超链接
//		Anchor anchor = new Anchor("baidu");
//		anchor.setReference("www.baidu.com");
//		// 定位
//		Anchor gotoP = new Anchor("goto");
//		gotoP.setReference("#top");
		// 添加图片
		WritableImage showImagePane = CenterPane.getInstance().showImagePane.snapshot(new SnapshotParameters(), null);
		ImageIO.write(SwingFXUtils.fromFXImage(showImagePane, null), "png", new File(tempImagePath));
		Image image = Image.getInstance(tempImagePath);
		image.setAlignment(Image.ALIGN_CENTER);
		image.scalePercent(60); //依照比例缩放
		// 表格
		PdfPTable pointTemperatureTable = getPointTemperature();
		Paragraph boxParagraph = getBoxTemperature();

		Paragraph paragraphEnd = new Paragraph("...", textfont);
		paragraphEnd.setSpacingAfter(30f);

		document.add(paragraph);
		document.add(p2);
		document.add(paragraphTime);
		document.add(p1);
		document.add(pointTemperatureTable);
		if (boxParagraph != null){
			document.add(boxParagraph);
		}
//		document.add(paragraphEnd);
		document.add(image);
	}

	private Paragraph getBoxTemperature() {
		if(CenterPane.getInstance().boxTemperatureNodeMax.isEmpty()){
			return null;
		}
		Paragraph paragraph = new Paragraph(Language.getString("Start Point: (","起点坐标: (") + CenterPane.getInstance().startLineX + "," +  CenterPane.getInstance().startLineY + ") , " +
				Language.getString(" End Point: ("," 终点坐标: (") + CenterPane.getInstance().endLineX + "," +  CenterPane.getInstance().endLineY + ")\n",
				Language.isEnglish()?boxTemperaturefont:boxTemperaturefontCh);
		paragraph.add(new Phrase(Language.getString("Max Temperature: ","最高温度: "),Language.isEnglish()?boxTemperaturefont:boxTemperaturefontCh));
		paragraph.add(new Phrase(Utils.getFormatTemperature((float) CenterPane.getInstance().boxTemperatureNodeMax.get(4)),boxTemperaturefontCh));
		paragraph.add(new Phrase(Language.getString(" , Min Temperature: "," , 最低温度: "),Language.isEnglish()?boxTemperaturefont:boxTemperaturefontCh));
		paragraph.add(new Phrase(Utils.getFormatTemperature((float) CenterPane.getInstance().boxTemperatureNodeMin.get(4)),boxTemperaturefontCh));
		paragraph.setAlignment(0);
		paragraph.setIndentationLeft(20);
		paragraph.setSpacingAfter(10f);
		return paragraph;
	}

	private PdfPTable getPointTemperature() {
		PdfPTable table = createTable(new float[] {60, 100, 100, 120});
		PdfPCell imagePathName = createCell(Global.currentOpenImagePath, headfont, Element.ALIGN_LEFT, 4, false);
		imagePathName.setPaddingBottom(20.0f);
		table.addCell(imagePathName);

		PdfPCell cellTitle = createCell(Language.getString("Point Temperature Data:","点测温数据:"),
				Language.isEnglish()?headfont:headfontCh, Element.ALIGN_LEFT, 4, false);
		cellTitle.setPaddingBottom(20.0f);
		table.addCell(cellTitle);

		table.addCell(createCell("", keyfont, Element.ALIGN_CENTER));
		table.addCell(createCell("X", keyfont, Element.ALIGN_CENTER));
		table.addCell(createCell("Y", keyfont, Element.ALIGN_CENTER));
		table.addCell(createCell(Language.getString("Temperature","温度"), Language.isEnglish()?keyfont:keyfontCh, Element.ALIGN_CENTER));
		Integer totalQuantity = 0;

		for (int i = 0; i < CenterPane.getInstance().pointTemperatureNodeList.size(); i++) {
			ArrayList pointNodeList = (ArrayList) CenterPane.getInstance().pointTemperatureNodeList.get(i);
			float temperature = (float) pointNodeList.get(4);
			table.addCell(createCell(""+(i+1), textfont));
			table.addCell(createCell(""+pointNodeList.get(5), textfont));
			table.addCell(createCell(""+pointNodeList.get(6), textfont));
			table.addCell(createCell(Utils.getFormatTemperature(temperature), textfontCh));
			totalQuantity ++;
		}
//		table.addCell(createCell("total", keyfont));
//		table.addCell(createCell("", textfont));
//		table.addCell(createCell("", textfont));
//		table.addCell(createCell("", textfont));
//		table.addCell(createCell(String.valueOf(totalQuantity) + " image", textfont));
//		table.addCell(createCell("end", textfont));

		if(!CenterPane.getInstance().boxTemperatureNodeMax.isEmpty()){
			PdfPCell boxCellTitle = createCell(Language.getString("Box Temperature Data:","区域温度数据:"),
					Language.isEnglish()?headfont:headfontCh, Element.ALIGN_LEFT, 4, false);
			boxCellTitle.setPaddingBottom(5.0f);
			table.addCell(boxCellTitle);
		}

		return table;
	}


	/**------------------------创建表格单元格的方法start----------------------------*/
    public PdfPCell createCell(String value, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }
    /**
     * 创建单元格（指定字体、水平..）
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
