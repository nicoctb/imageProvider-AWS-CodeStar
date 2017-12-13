package com.aws.codestar.projecttemplates.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Controller
public class ProvideImageController {


    @RequestMapping("/getDirs/{eventName}")
    public ResponseEntity<?> getDirs(Map<String, List> model, @PathVariable String eventName) {

        List<String> dirs = new ArrayList<String>();
        dirs.add("Originals");
        dirs.add("Animated");
        dirs.add("Prints");
        dirs.add("Thumbnails");
        model.put("dirs", dirs);

        return new ResponseEntity(model, HttpStatus.OK);
    }

    @RequestMapping("/getImagesList/{eventName}/{dirName}/{from}/{to}")
    public ResponseEntity<?> getImagesList(Map<String, List> model,
                                           @PathVariable String eventName,
                                           @PathVariable String dirName,
                                           @PathVariable String from,
                                           @PathVariable String to) throws IOException{
        List<String> imageList = new ArrayList<String>();

        if ("Thumbnails".equals(dirName)){
            dirName="Prints/thumb";
        }
        String url = "http://events.cabinawtf.ro/"+eventName+"/"+dirName;
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");
        for (Element link : links) {
            String href = link.text();
            if ( "jpg".equals(href.substring(href.length()-3,href.length())) ) {
                imageList.add(href);
            }
        }
        model.put("images", imageList);

        return new ResponseEntity(model, HttpStatus.OK);
    }

    @RequestMapping("/getImageUrl/{eventName}/{dirName}/{imageName}")
    public ResponseEntity<?> getImageUrl(Map<String, String> model,
                                         @PathVariable String eventName,
                                         @PathVariable String dirName,
                                         @PathVariable String imageName) {

        if ("Thumbnails".equals(dirName)){
            dirName="Prints/thumb";
        }
        String url = "http://events.cabinawtf.ro/"+eventName+"/"+dirName+"/"+imageName+".jpg";
        model.put("imageUrl", url);

        return new ResponseEntity(model, HttpStatus.OK);
    }

}
