/*
package com.telcom.isdp.modular.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.telcom.isdp.core.shiro.ShiroKit;
import com.telcom.isdp.core.shiro.ShiroUser;
import com.telcom.isdp.modular.business.entity.CaseMethodLib;
import com.telcom.isdp.modular.business.service.ICaseMethodLibService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/tools")
public class ToolsController {
    private String PREFIX = "/modular/business/tools/";

    @Value("${uploadFilePath}")
    private String uploadFilePath;

    @Value("${trans2PdfPath}")
    private String trans2PdfPath;

    @Autowired
    private ICaseMethodLibService caseMethodLibService;

    @Autowired
    private DocumentConverter converter;

    @RequestMapping("/methods")
    public String methods(){
        return PREFIX + "methods.html";
    }

    @RequestMapping("/cases")
    public String cases(){
        return PREFIX + "cases.html";
    }

    */
/**
     *
     * @param file 文件流
     * @param name  文件名
     * @param summary   摘要
     * @param pageCount 页数
     * @param toolType  case/method
     * @param request
     * @return
     *//*

    @ResponseBody
    @RequestMapping("/uploadFile")
    public Map<String,String> uploadFile(@RequestParam("file") MultipartFile file,
                                         @RequestParam("name") String name,
                                         @RequestParam("summary") String summary,
                                         @RequestParam("pageCount") String pageCount,
                                         @RequestParam("toolType") String toolType,
                                         HttpServletRequest request){
        Map<String,String> map = new HashMap<>();

        if(file!=null){
            name= name.replaceAll("& #","&#");  //英文括号会被转义为& #40; 不知为何中间还有空格
            name= StringEscapeUtils.unescapeHtml4(name);        //转回英文括号

            String fileName = file.getOriginalFilename();// 文件全名称
            if(!fileName.contains(".")){ //文件没有扩展名
                throw new IllegalArgumentException("缺少后缀名");
            }
            //分离后缀名和文件名
            int index = fileName.lastIndexOf(".");
            String preName = fileName.substring(0,index);
            String extName = fileName.substring(index);

            //若没有结尾符,加上
            if (!this.uploadFilePath.endsWith(File.separator)) {
                this.uploadFilePath = this.uploadFilePath + File.separator;
            }
            if (!this.trans2PdfPath.endsWith(File.separator)) {
                this.trans2PdfPath = this.trans2PdfPath + File.separator;
            }

            //若路径不存在，创建路径
            File path = new File(this.uploadFilePath);
            File transPath = new File(this.trans2PdfPath);
            if(!path.exists()){
                path.mkdirs();
            }
            if(!transPath.exists()){
                transPath.mkdirs();
            }

            String newFileName = System.currentTimeMillis() + "-" + name;
            String fullPathName = this.uploadFilePath+newFileName + extName;
            String pdfFileName = this.trans2PdfPath+newFileName + ".pdf";

            File newFile = new File(fullPathName);
            try {
                file.transferTo(newFile);   //生成文件
                //若文件扩展名不是.pdf，则同步创建pdf文件，用于预览
                if(!".pdf".equalsIgnoreCase(extName)){
                    File pfile = new File(pdfFileName);
                    converter.convert(newFile).to(pfile).execute();
                }

                //记录文件信息
                CaseMethodLib caseMethodLib = new CaseMethodLib();
                caseMethodLib.setName(name);
                caseMethodLib.setFilePath(fullPathName);
                caseMethodLib.setUploadTime(LocalDateTime.now());
                caseMethodLib.setReadCount(0L);
                caseMethodLib.setType(toolType);
                Integer pageC;
                try{
                    pageC = Integer.valueOf(pageCount);
                }catch (Exception e){
                    pageC = 1;
                }
                caseMethodLib.setPageCount(pageC);

                String fileType = this.getFileType(extName.toLowerCase());
                caseMethodLib.setFileType(fileType);

                ShiroUser shiroUser = ShiroKit.getUserNotNull();
                caseMethodLib.setUploadMan(shiroUser.getAccount());

                caseMethodLib.setSummary(summary);

                caseMethodLibService.save(caseMethodLib);

                map.put("code", "0");
                map.put("msg","上传成功");
            }catch (Exception e){
                e.printStackTrace();
                map.put("code", "1");
                map.put("msg","上传失败！");
            }
        }else{
            map.put("code", "1");
            map.put("msg","上传失败！文件为null");
        }

        return map;
    }

    @RequestMapping(value = "/showFile",method = {RequestMethod.GET})
    public String showFile(){
        return PREFIX +"showFile.html";
    }

    @ResponseBody
    @RequestMapping(value = "/initFileDetail/{id}",method = {RequestMethod.GET})
    public CaseMethodLib initFileDetail(@PathVariable("id") String id,HttpServletResponse response){
        QueryWrapper queryWrapper = new QueryWrapper(new CaseMethodLib());
        queryWrapper.eq("id",Long.valueOf(id));
        CaseMethodLib caseMethodLib = caseMethodLibService.getOne(queryWrapper);

        //访问数+1
        caseMethodLib.setReadCount(caseMethodLib.getReadCount()+1);
        caseMethodLibService.saveOrUpdate(caseMethodLib);
        return caseMethodLib;
    }

    @ResponseBody
    @RequestMapping(value = "/preViewFile/{id}",method = {RequestMethod.GET})
    public void preViewFile(@PathVariable("id") String id,HttpServletResponse response){
        QueryWrapper queryWrapper = new QueryWrapper(new CaseMethodLib());
        queryWrapper.eq("id",Long.valueOf(id));
        CaseMethodLib caseMethodLib = caseMethodLibService.getOne(queryWrapper);

        try {
            //若没有结尾符,加上
            if (!this.trans2PdfPath.endsWith(File.separator)) {
                this.trans2PdfPath = this.trans2PdfPath + File.separator;
            }

            //使用response,将pdf文件以流的方式发送的前段
            ServletOutputStream outputStream = response.getOutputStream();

            String fileFullPath = "";
            if("pdf".equalsIgnoreCase(caseMethodLib.getFileType())){
                fileFullPath = caseMethodLib.getFilePath();
            }else{
                String path = caseMethodLib.getFilePath();
                int sindex = path.lastIndexOf(File.separator);
                int dindex = path.lastIndexOf(".");
                String fileName = path.substring(sindex+1,dindex);
                fileFullPath = this.trans2PdfPath+fileName+".pdf";
            }

            InputStream in = new FileInputStream(new File(fileFullPath));// 读取文件
            // copy文件
            int i = IOUtils.copy(in, outputStream);
            System.out.println(i);
            in.close();
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    */
/**
     * 获取匹配结果数量
     * @param keyword
     * @return
     *//*

    @ResponseBody
    @RequestMapping("/searchCount")
    public int searchCount(@RequestParam("keyword") String keyword,
                           @RequestParam("type") String type){
        String[] keywords = keyword.split("\\s+");
        QueryWrapper wrapper = new QueryWrapper(new CaseMethodLib());
        for(int i=0;i<keywords.length;i++){
            wrapper.like("name","%"+keywords[i]+"%");
        }
        wrapper.eq("type",type);
        int count = caseMethodLibService.count(wrapper);

        return count;
    }

    */
/**
     * 获取匹配结果
     * @param keyword
     * @param current
     * @param limit
     * @return
     *//*

    @ResponseBody
    @RequestMapping("/search")
    public List<CaseMethodLib> search(@RequestParam("keyword") String keyword,
                                      @RequestParam("current") int current,
                                      @RequestParam("limit") int limit,
                                      @RequestParam("type") String type){
        String[] keywords = keyword.split("\\s+");
        QueryWrapper wrapper = new QueryWrapper(new CaseMethodLib());
        for(int i=0;i<keywords.length;i++){
            wrapper.like("name","%"+keywords[i]+"%");
        }
        wrapper.orderByDesc("read_count");
        wrapper.eq("type",type);
        wrapper.last(" limit "+limit+" offset "+(current-1)*limit);
        List<CaseMethodLib> results = caseMethodLibService.list(wrapper);
        return results;
    }

    @ResponseBody
    @RequestMapping("/downloadFile/{id}")
    public void downloadFile(@PathVariable("id") Long id,
                             HttpServletRequest request,
                             HttpServletResponse response){
        CaseMethodLib caseMethodLib = caseMethodLibService.getById(id);
        if(caseMethodLib!=null){
            String path = caseMethodLib.getFilePath();
            File file = new File(path);
            if(file.exists()){
                try {
                    String extName = path.substring(path.lastIndexOf("."));
                    String fileName = caseMethodLib.getName();

                    InputStream inputStream = new FileInputStream(file);
                    OutputStream outputStream = response.getOutputStream();

                    //指明为下载
                    response.setContentType("application/x-download");
                    //设置文件名
                    response.addHeader("Content-Disposition", "attachment;filename*=UTF-8''" + URLEncoder
                            .encode(fileName, "UTF-8") + extName);

                    IOUtils.copy(inputStream,outputStream);
                    outputStream.flush();
                    inputStream.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private String getFileType(String extName){
        Map<String, List<String>> fileTypeMap = new HashMap<>() ;
        String[] word = {".doc",".docx"};
        String[] excel = {".xls",".xlsx"};
        String[] ppt = {".ppt",".pptx"};
        String[] pdf = {".pdf"};
        String[] txt = {".txt"};

        fileTypeMap.put("WORD", Arrays.asList(word));
        fileTypeMap.put("EXCEL",Arrays.asList(excel));
        fileTypeMap.put("PPT",Arrays.asList(ppt));
        fileTypeMap.put("PDF",Arrays.asList(pdf));
        fileTypeMap.put("TXT",Arrays.asList(txt));
        System.out.println("");
        for(String key:fileTypeMap.keySet()){
            if(fileTypeMap.get(key).indexOf(extName)>=0){
                return key;
            }
        }
        return null;
    }

    @RequestMapping("getTest")
    public String getTest(){
        return PREFIX+"test.html";
    }

    @RequestMapping("test")
    @ResponseBody
    public void test(HttpServletResponse response){
        try {
//            File wfile = new File("D:\\HR ESS操作手册（员工版）.doc");
//            File woutFile = new File("D:\\HR ESS操作手册（员工版）.pdf");
//            converter.convert(wfile).to(woutFile).execute();
//
//            File w2file = new File("D:\\致您的一封信.docx");
//            File w2outFile = new File("D:\\致您的一封信.pdf");
//            converter.convert(w2file).to(w2outFile).execute();
//
//            File xfile = new File("D:\\轻量级数据汇总-模型设计-V0.1.xlsx");
//            File xoutFile = new File("D:\\轻量级数据汇总-模型设计-V0.1.pdf");
//            converter.convert(xfile).to(xoutFile).execute();
//
//            File pfile = new File("D:\\贾云强-试用期员工转正答辩展示.pptx");
//            File poutFile = new File("D:\\贾云强-试用期员工转正答辩展示.pdf");
//            converter.convert(pfile).to(poutFile).execute();
//
//            File tfile = new File("D:\\test.txt");
//            File toutFile = new File("D:\\test.pdf");
//            converter.convert(tfile).to(toutFile).execute();

            //使用response,将pdf文件以流的方式发送的前段
            ServletOutputStream outputStream = response.getOutputStream();
//            InputStream in = new FileInputStream(new File("D:\\轻量级数据汇总-模型设计-V0.1.pdf"));// 读取文件
//            // copy文件
//            int i = IOUtils.copy(in, outputStream);
//            System.out.println(i);
//            in.close();
//            outputStream.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
*/
