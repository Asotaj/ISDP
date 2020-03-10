package com.telcom.isdp.core.util;

import com.itextpdf.text.pdf.PdfReader;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileInputStream;
import java.io.IOException;


public class OfficePageReaderUtil {

    /**
     * description: 静态方法，用于判断文件类型，并返回页数
     * @param filePath 文件完整路径
     */
    public static int getFilePageNum(String filePath) throws IOException {
        int pageNum = 0;
        String lowerFilePath = filePath.toLowerCase();
        if (lowerFilePath.endsWith(".xls")) {
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(lowerFilePath));
            Integer sheetNums = workbook.getNumberOfSheets();
            if (sheetNums > 0) {
                pageNum = workbook.getSheetAt(0).getRowBreaks().length + 1;
            }
        } else if (lowerFilePath.endsWith(".xlsx")) {
            XSSFWorkbook xwb = new XSSFWorkbook(lowerFilePath);
            Integer sheetNums = xwb.getNumberOfSheets();
            if (sheetNums > 0) {
                pageNum = xwb.getSheetAt(0).getRowBreaks().length + 1;
            }
        } else if (lowerFilePath.endsWith(".docx")) {
            XWPFDocument docx = new XWPFDocument(POIXMLDocument.openPackage(lowerFilePath));
            pageNum = docx.getProperties().getExtendedProperties().getUnderlyingProperties().getPages();
        } else if (lowerFilePath.endsWith(".doc")) {
            //下方的方法不好使，经常只统计出一页
//            HWPFDocument wordDoc = new HWPFDocument(new FileInputStream(lowerFilePath));
//            pageNum = wordDoc.getSummaryInformation().getPageCount();
            //采用如下方法
            pageNum = getDocPageNum(lowerFilePath);
        } else if (lowerFilePath.endsWith(".ppt")) {
            HSLFSlideShow document = new HSLFSlideShow(new FileInputStream(lowerFilePath));
            SlideShow slideShow = new SlideShow(document);
            pageNum = slideShow.getSlides().length;
        } else if (lowerFilePath.endsWith(".pptx")) {
            XMLSlideShow xslideShow = new XMLSlideShow(new FileInputStream(lowerFilePath));
            pageNum = xslideShow.getSlides().length;
        } else if (lowerFilePath.endsWith(".pdf")){
            PdfReader reader = new PdfReader(filePath);
            pageNum = reader.getNumberOfPages();
        }
        return pageNum;
    }


    /**
     * description: 静态方法，专门用于判断Office 2003版本之前的Word（格式为.doc）的页数
     * @param filePath 文件完整路径
     */
    private static int getDocPageNum(String filePath) {
        int pageNum = 0;
        try{
            // 建立ActiveX部件
            ActiveXComponent wordCom = new ActiveXComponent("Word.Application");
            //word应用程序不可见
            wordCom.setProperty("Visible", false);
            // 返回wrdCom.Documents的Dispatch
            Dispatch wrdDocs = wordCom.getProperty("Documents").toDispatch();//Documents表示word的所有文档窗口（word是多文档应用程序）

            // 调用wrdCom.Documents.Open方法打开指定的word文档，返回wordDoc
            Dispatch wordDoc = Dispatch.call(wrdDocs, "Open", filePath, false, true, false).toDispatch();
            Dispatch selection = Dispatch.get(wordCom, "Selection").toDispatch();
            pageNum = Integer.parseInt(Dispatch.call(selection,"information",4).toString());//总页数 //显示修订内容的最终状态

            //关闭文档且不保存
            Dispatch.call(wordDoc, "Close", new Variant(false));
            //退出进程对象
            wordCom.invoke("Quit", new Variant[] {});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageNum;
    }
}
