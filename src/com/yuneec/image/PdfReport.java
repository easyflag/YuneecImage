package com.yuneec.image;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.yuneec.image.guide.GuideTemperatureAlgorithm;
import com.yuneec.image.module.Language;
import com.yuneec.image.module.box.BoxTemperature;
import com.yuneec.image.module.box.BoxTemperatureManager;
import com.yuneec.image.module.curve.CurveManager;
import com.yuneec.image.module.curve.CurveTemperature;
import com.yuneec.image.module.point.PointManager;
import com.yuneec.image.module.point.PointTemperature;
import com.yuneec.image.utils.ToastUtil;
import com.yuneec.image.utils.Utils;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class PdfReport {

	private String tempImagePath = FileSystemView.getFileSystemView().getDefaultDirectory() + "\\tempYImage.png";
	private static PdfReport pdfReport;
	private Document document;
	public static PdfReport getInstance() {
		if (pdfReport == null) {
			pdfReport = new PdfReport();
		}
		return pdfReport;
	}
 
    public void creat(String fileName) throws Exception {
        try {
			document = new Document(PageSize.A4);
 
            File file = new File(fileName);
            file.createNewFile();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
 
            document.open();

            generatePDF();
 
            document.close();

			new File(tempImagePath).delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
		ToastUtil.toast(Language.getString("PDF report generated successfully !","PDF报告生成成功!"));
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
 
	public void generatePDF() throws Exception {
    	// 段落
		Paragraph paragraph = new Paragraph(Language.getString("Yuneec Image","Yuneec 图片"), Language.isEnglish()?titlefont:titlefontCh);
		paragraph.add(new Phrase(Language.getString(" Infrared Camera Report ","红外报告"),Language.isEnglish()?titlefont:titlefontCh));
		paragraph.setAlignment(1); //设置文字居中 0靠左   1，居中     2，靠右
		paragraph.setIndentationLeft(12); //设置左缩进
		paragraph.setIndentationRight(12); //设置右缩进
		paragraph.setFirstLineIndent(24); //设置首行缩进
		paragraph.setLeading(50f); //行间距
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

		//add 标题 和 时间
		document.add(paragraph);
		document.add(p2);
		document.add(paragraphTime);
		document.add(p1);

//		// 超链接
//		Anchor anchor = new Anchor("baidu");
//		anchor.setReference("www.baidu.com");
//		// 定位
//		Anchor gotoP = new Anchor("goto");
//		gotoP.setReference("#top");

		//1、测试环境
		addTestingEnvironment();
		//5、图片
		addImage();
		//2、点测温数据
		addPointTemperature();
		//3、区域测温数据
		addBoxTemperature();
		//4、曲线测温数据
		addCurveTemperature();

//		Paragraph paragraphEnd = new Paragraph("...", textfont);
//		paragraphEnd.setSpacingAfter(30f);
//
//		Paragraph paragraphImagePath = new Paragraph(Global.currentOpenImagePath, headfont);
//		document.add(paragraphImagePath);
//
	}


	private void addTestingEnvironment(){
		Paragraph title = new Paragraph(Language.getString("Image Info:","图片信息:"), Language.isEnglish()?headfont:headfontCh);
		title.setAlignment(0);
		title.setIndentationLeft(12);
		title.setSpacingBefore(5f);

		PdfPTable table = createTable(new float[] {120, 120, 120, 120},Element.ALIGN_CENTER);
		table.setSpacingBefore(10f);

		table.addCell(createCell(Language.getString("Camera:","相机机型:"), Language.isEnglish()?textfont:textfontCh,Element.ALIGN_CENTER));
		table.addCell(createCell(RightPane.modelLabels[1].getText(), Language.isEnglish()?textfont:textfontCh,Element.ALIGN_CENTER));
		table.addCell(createCell(Language.getString("Image Name:","图片名称:"), Language.isEnglish()?textfont:textfontCh,Element.ALIGN_CENTER));
		table.addCell(createCell(RightPane.imageDescriptionLabels[1].getText(), Language.isEnglish()?textfont:textfontCh,Element.ALIGN_CENTER));
		table.addCell(createCell(Language.getString("Atmospheric Temperature:","大气环境温度:"), Language.isEnglish()?textfont:textfontCh,Element.ALIGN_CENTER));
		table.addCell(createCell(GuideTemperatureAlgorithm.pParamExt.atmosphericTemper/ 10 + "", Language.isEnglish()?textfont:textfontCh,Element.ALIGN_CENTER));
		table.addCell(createCell(Language.getString("Shooting Time:","拍摄时间:"), Language.isEnglish()?textfont:textfontCh,Element.ALIGN_CENTER));
		table.addCell(createCell(RightPane.timeLabels[1].getText(), Language.isEnglish()?textfont:textfontCh,Element.ALIGN_CENTER));
		try {
			document.add(title);
			document.add(table);
		} catch (DocumentException e) {
			e.printStackTrace();
			document.close();
		}
	}

	private void addPointTemperature() {
		if (PointManager.getInstance().pointTemperatureNodeList.size() > 0){
			document.newPage();
			Paragraph title = new Paragraph(Language.getString("Point Temperature Data:","点测温数据:"), Language.isEnglish()?headfont:headfontCh);
			title.setAlignment(0);
//			title.setLeading(30);
			title.setIndentationLeft(12);
			title.setSpacingBefore(15f);

			PdfPTable table = createTable(new float[] {70, 110, 110, 130},Element.ALIGN_CENTER);
			table.setSpacingBefore(10f);
			table.addCell(createCell(Language.getString("No","编号"), Language.isEnglish()?keyfont:keyfontCh, Element.ALIGN_CENTER));
			table.addCell(createCell("X", Language.isEnglish()?keyfont:keyfontCh, Element.ALIGN_CENTER));
			table.addCell(createCell("Y", Language.isEnglish()?keyfont:keyfontCh, Element.ALIGN_CENTER));
			table.addCell(createCell(Language.getString("Temperature","温度"), Language.isEnglish()?keyfont:keyfontCh, Element.ALIGN_CENTER));

			for (int i = 0; i < PointManager.getInstance().pointTemperatureNodeList.size(); i++) {
				PointTemperature pointTemperature = (PointTemperature) PointManager.getInstance().pointTemperatureNodeList.get(i);
				ArrayList pointNodeList = pointTemperature.getPointTemperatureNode();
				float temperature = (float) pointNodeList.get(4);
				table.addCell(createCell(""+(i+1), Language.isEnglish()?textfont:textfontCh));
				table.addCell(createCell(""+pointNodeList.get(5), Language.isEnglish()?textfont:textfontCh));
				table.addCell(createCell(""+pointNodeList.get(6), Language.isEnglish()?textfont:textfontCh));
				table.addCell(createCell(Utils.getFormatTemperature(temperature), textfontCh));
			}
			try {
				document.add(title);
				document.add(table);
			} catch (DocumentException e) {
				e.printStackTrace();
				document.close();
			}
		}
	}

	private void addBoxTemperature() {
		if (BoxTemperatureManager.getInstance().boxTemperatureList.size() > 0){
			Paragraph title = new Paragraph(Language.getString("Box Temperature Data:","区域测温数据:"), Language.isEnglish()?headfont:headfontCh);
			title.setAlignment(0);
			title.setIndentationLeft(12);
			title.setSpacingBefore(15f);

			PdfPTable table = createTable(new float[] {70, 100, 100, 100, 100},Element.ALIGN_CENTER);
			table.setSpacingBefore(10f);
			table.addCell(createCell(Language.getString("No","编号"), Language.isEnglish()?keyfont:keyfontCh, Element.ALIGN_CENTER));
			table.addCell(createCell(Language.getString("Max","最大值"), Language.isEnglish()?keyfont:keyfontCh, Element.ALIGN_CENTER));
			table.addCell(createCell(Language.getString("Min","最小值"), Language.isEnglish()?keyfont:keyfontCh, Element.ALIGN_CENTER));
			table.addCell(createCell(Language.getString("Emissivity","发射率"), Language.isEnglish()?keyfont:keyfontCh, Element.ALIGN_CENTER));
			table.addCell(createCell(Language.getString("Distance","距离"), Language.isEnglish()?keyfont:keyfontCh, Element.ALIGN_CENTER));
			for (int i = 0; i < BoxTemperatureManager.getInstance().boxTemperatureList.size(); i++) {
				BoxTemperature boxTemperature = (BoxTemperature) BoxTemperatureManager.getInstance().boxTemperatureList.get(i);
				float maxTemperature = 0;
				float minTemperature = 0;
				if (!boxTemperature.getBoxTemperatureNodeMax().isEmpty()) {
					ArrayList boxTemperatureNodeMaxList = boxTemperature.getBoxTemperatureNodeMax();
					maxTemperature = (float) boxTemperatureNodeMaxList.get(4);
				}
				if (!boxTemperature.getBoxTemperatureNodeMin().isEmpty()) {
					ArrayList boxTemperatureNodeMinList = boxTemperature.getBoxTemperatureNodeMin();
					minTemperature = (float) boxTemperatureNodeMinList.get(4);
				}
				table.addCell(createCell(""+(i+1), textfont));
				table.addCell(createCell(Utils.getFormatTemperature(maxTemperature), textfontCh));
				table.addCell(createCell(Utils.getFormatTemperature(minTemperature), textfontCh));
				table.addCell(createCell(GuideTemperatureAlgorithm.pParamExt.emiss / 100f + "", Language.isEnglish()?textfont:textfontCh));
				table.addCell(createCell(GuideTemperatureAlgorithm.pParamExt.distance / 10 + "", Language.isEnglish()?textfont:textfontCh));
			}
			try {
				document.add(title);
				document.add(table);
			} catch (DocumentException e) {
				e.printStackTrace();
				document.close();
			}
		}
	}

	private void addCurveTemperature() {
		if (CurveManager.getInstance().curveTemperatureList.size() > 0){
			Paragraph title = new Paragraph(Language.getString("Curve Temperature Data:","曲线测温数据:"), Language.isEnglish()?headfont:headfontCh);
			title.setAlignment(0);
			title.setIndentationLeft(12);
			title.setSpacingBefore(15f);

			PdfPTable table = createTable(new float[] {70, 100, 100, 100, 100},Element.ALIGN_CENTER);
			table.setSpacingBefore(10f);
			table.addCell(createCell(Language.getString("No","编号"), Language.isEnglish()?keyfont:keyfontCh, Element.ALIGN_CENTER));
			table.addCell(createCell(Language.getString("Max","最大值"), Language.isEnglish()?keyfont:keyfontCh, Element.ALIGN_CENTER));
			table.addCell(createCell(Language.getString("Min","最小值"), Language.isEnglish()?keyfont:keyfontCh, Element.ALIGN_CENTER));
			table.addCell(createCell(Language.getString("Emissivity","发射率"), Language.isEnglish()?keyfont:keyfontCh, Element.ALIGN_CENTER));
			table.addCell(createCell(Language.getString("Distance","距离"), Language.isEnglish()?keyfont:keyfontCh, Element.ALIGN_CENTER));
			for (int i = 0; i < CurveManager.getInstance().curveTemperatureList.size(); i++) {
				BoxTemperature boxTemperature = (BoxTemperature) BoxTemperatureManager.getInstance().boxTemperatureList.get(i);
				float maxTemperature = 0;
				float minTemperature = 0;
				CurveTemperature curveTemperature = (CurveTemperature) CurveManager.getInstance().curveTemperatureList.get(i);
				if (!curveTemperature.getCurveTemperatureNodeMax().isEmpty()){
					ArrayList curveTemperatureNodeMax = curveTemperature.getCurveTemperatureNodeMax();
					maxTemperature = (float) curveTemperatureNodeMax.get(4);
				}
				if (!curveTemperature.getCurveTemperatureNodeMin().isEmpty()){
					ArrayList curveTemperatureNodeMin = curveTemperature.getCurveTemperatureNodeMin();
					minTemperature = (float) curveTemperatureNodeMin.get(4);
				}
				table.addCell(createCell(""+(i+1), textfont));
				table.addCell(createCell(Utils.getFormatTemperature(maxTemperature), textfontCh));
				table.addCell(createCell(Utils.getFormatTemperature(minTemperature), textfontCh));
				table.addCell(createCell(GuideTemperatureAlgorithm.pParamExt.emiss / 100f + "", Language.isEnglish()?textfont:textfontCh));
				table.addCell(createCell(GuideTemperatureAlgorithm.pParamExt.distance / 10 + "", Language.isEnglish()?textfont:textfontCh));
			}
			try {
				document.add(title);
				document.add(table);
			} catch (DocumentException e) {
				e.printStackTrace();
				document.close();
			}
		}
	}

	private void addImage() {
		try {
			Paragraph title = new Paragraph(Language.getString("Test Image:","测试图像:"), Language.isEnglish()?headfont:headfontCh);
			title.setAlignment(0);
			title.setIndentationLeft(12);
			title.setSpacingBefore(15f);

			WritableImage showImagePane = CenterPane.getInstance().showImagePane.snapshot(new SnapshotParameters(), null);
			ImageIO.write(SwingFXUtils.fromFXImage(showImagePane, null), "png", new File(tempImagePath));
			Image image = Image.getInstance(tempImagePath);
			image.setAlignment(Image.ALIGN_CENTER);
			image.scalePercent(70); //依照比例缩放

			document.add(title);
			document.add(image);
		}catch (Exception e){
			e.printStackTrace();
			document.close();
		}
	}


	/**------------------------创建表格单元格的方法start----------------------------*/
    public PdfPCell createCell(String value, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPhrase(new Phrase(value, font));
		cell.setPaddingBottom(5.0f);
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
		cell.setPaddingBottom(5.0f);
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
	public PdfPTable createTable(float[] widths,int horizontalAlignment) {
		maxWidth = 0;
		for (int i=0;i<widths.length;i++){
			maxWidth += widths[i];
		}
		PdfPTable table = new PdfPTable(widths);
		try {
			table.setTotalWidth(maxWidth);
			table.setLockedWidth(true);
			table.setHorizontalAlignment(horizontalAlignment);
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
