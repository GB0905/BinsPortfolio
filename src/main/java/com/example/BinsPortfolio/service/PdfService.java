package com.example.BinsPortfolio.service;

import com.example.BinsPortfolio.entity.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.font.FontProvider;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PdfService {
    @Value("${pdfPath}")
    private String defaultPdfPath;
    @Value("${imagePath}")
    private String defaultImagePath;
    @Value("${portfolioImgLocation}")
    private String defaultPath;
    public static final String IMG = "ci_logo.jpg";
    public static final String DESC = "springboot.pdf";

    public final FileService fileService;
    public void deletePdf(String path) {
        fileService.deleteFile(path, "pdf");
    }

    public String createPdf(Project project) throws IOException {

        UUID uuid = UUID.randomUUID();
        String savedFileName = uuid.toString() + project.getTitle()+".pdf";

        String SRC = defaultPdfPath+savedFileName;
        String IMG = defaultImagePath+defaultPath+"/"+project.getImage().getImgName();

        StringBuilder body = new StringBuilder();

        String header = "<!DOCTYPE html>\n" +
                "<html lang=\"ko-KR\">\n" +
                "  <head>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                "    <style>\n" +
                "      body {\n" +
                "        font-family: MalgunGothic;\n" +
                "        font-size: 15px;\n" +
                "        color: #222;\n" +
                "        background: #fff;\n" +
                "        height: 100%;\n" +
                "        display: flex;\n" +
                "        flex-direction: column;\n" +
                "        padding: 0 50px;\n" +
                "        margin: 0 auto;\n" +
                "      }\n" +
                "      .title {\n" +
                "        font-size: 24px;\n" +
                "        margin: 20px 0;\n" +
                "        display: flex;\n" +
                "        justify-content: center;\n" +
                "      }\n" +
                "      .top {\n" +
                "        position: relative;\n" +
                "        display: flex;\n" +
                "        align-items: center;\n" +
                "        margin: 10px 0;\n" +
                "      }\n" +
                "      .date {\n" +
                "        position: absolute;\n" +
                "        right: 0;\n" +
                "        display: flex;\n" +
                "        width: 150px;\n" +
                "      }\n" +
                "      .date .date-element {\n" +
                "        margin: 0 10px;\n" +
                "      }\n" +
                "      .img-box {\n" +
                "        position: relative;\n" +
                "        height: 250px;\n" +
                "        border: #ccc 1px solid;\n" +
                "        margin: 10px 0;\n" +
                "        overflow: hidden;\n" +
                "        display: flex;\n" +
                "        justify-content: center;\n" +
                "        /* align-items: center; */\n" +
                "      }\n" +
                "      img {\n" +
                "        width: 100%;\n" +
                "        height: 100%;\n" +
                "        object-fit: cover;\n" +
                "      }\n" +
                "      .introduction {\n" +
                "        margin: 10px 0;\n" +
                "      }\n" +
                "      .introduction .title {\n" +
                "        font-size: 15px;\n" +
                "        margin: 0 0 10px 0;\n" +
                "        justify-content: flex-start;\n" +
                "      }\n" +
                "      .introduction .content {\n" +
                "        border: #ccc 1px solid;\n" +
                "        height: 100%;\n" +
                "        min-height: 250px;\n" +
                "        display: -webkit-box;\n" +
                "        -webkit-line-clamp: 12;\n" +
                "        overflow: hidden;\n" +
                "        text-overflow: ellipsis;\n" +
                "        -webkit-box-orient: vertical;" +
                "      }\n" +
                "      .skill {\n" +
                "        margin: 10px 0;\n" +
                "      }\n" +
                "      .skill .title {\n" +
                "        font-size: 15px;\n" +
                "        margin: 0 0 10px 0;\n" +
                "        justify-content: flex-start;\n" +
                "      }\n" +
                "      .skill .content {\n" +
                "        border: #ccc 1px solid;\n" +
                "        height: 100%;\n" +
                "        min-height: 250px;\n" +
                "        display: -webkit-box;\n" +
                "        -webkit-line-clamp: 12;\n" +
                "        overflow: hidden;\n" +
                "        text-overflow: ellipsis;\n" +
                "        -webkit-box-orient: vertical;" +
                "      }\n" +
                "    </style>\n" +
                "    <title>Document</title>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <div class='title'>"+project.getTitle()+"</div>\n" +
                "    <div class='top'>\n" +
                "      <div class='link'>"+project.getLink()+"</div>\n" +
                "      <div class='date'>\n" +
                "        <div>"+new SimpleDateFormat("yyyy-MM").format(project.getStartDate())+"</div>\n" +
                "        <div class='date-element'>~</div>\n" +
                "        <div>"+project.getEndDate()+"</div>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "    <div class='img-box'>\n" +
                "      <img src=\""+IMG+"\" />\n" +
                "    </div>\n" +
                "    <div class='introduction'>\n" +
                "      <div class='title'>소개</div>\n" +
                "      <div class='content'>"+project.getIntroduction()+"</div>\n" +
                "    </div>\n" +
                "    <div class='skill'>\n" +
                "      <div class='title'>기술</div>\n" +
                "      <div class='content'>"+project.getSkills()+"</div>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>\n";

        body.append(header);
        System.out.println(header);

        String BODY = body.toString();
        makepdf(BODY, SRC);

        deletePdf(project.getPath());

        return savedFileName;
    }

    //BODY : html string , dest : pdf를 만들 경로(D:\\sample.pdf)
    public void makepdf(String BODY, String dest) throws IOException {
        //한국어를 표시하기 위해 폰트 적용
        String FONT = "static/font/malgun.ttf";
        //ConverterProperties : htmlconverter의 property를 지정하는 메소드인듯
        ConverterProperties properties = new ConverterProperties();
        FontProvider fontProvider = new FontProvider();
//        FontProvider fontProvider = new DefaultFontProvider(false, false, false);
        FontProgram fontProgram = FontProgramFactory.createFont(FONT);
        fontProvider.addFont(fontProgram);
        properties.setFontProvider(fontProvider);

        //pdf 페이지 크기를 조정
        List<IElement> elements = HtmlConverter.convertToElements(BODY, properties);
        PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdf);
        //setMargins 매개변수순서 : 상, 우, 하, 좌
        document.setMargins(50, 50, 50, 50);
        System.out.println(elements);
        for(IElement element:elements) {
            document.add((IBlockElement) element);
        }
        document.close();
    }
}