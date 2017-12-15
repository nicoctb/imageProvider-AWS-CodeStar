package com.aws.codestar.projecttemplates.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aws.codestar.projecttemplates.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@RestController
@RequestMapping("/")
public class ProvideImageController {

    private static final String MESSAGE_FORMAT = "Hello %s!";

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public Response helloWorldGet(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Response(String.format(MESSAGE_FORMAT, name));
    }

    @RequestMapping(method = RequestMethod.POST)
    public Response helloWorldPost(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Response(String.format(MESSAGE_FORMAT, name));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getDirs/{eventName}")
    public Response getDirs(Map<String, List> model, @PathVariable String eventName) {

        List<String> dirs = new ArrayList<String>();
        dirs.add("Originals");
        dirs.add("Animated");
        dirs.add("Prints");
        dirs.add("Thumbnails");
        model.put("dirs", dirs);

        return new Response(model.toString());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getImagesList/{eventName}/{dirName}/{from}/{to}")
    public Response getImagesList(Map<String, List> model,
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

        return new Response(model.toString());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getImageUrl/{eventName}/{dirName}/{imageName}")
    public Response getImageUrl(Map<String, String> model,
                                         @PathVariable String eventName,
                                         @PathVariable String dirName,
                                         @PathVariable String imageName) {

        if ("Thumbnails".equals(dirName)){
            dirName="Prints/thumb";
        }
        String url = "http://events.cabinawtf.ro/"+eventName+"/"+dirName+"/"+imageName+".jpg";
        model.put("imageUrl", url);

        return new Response(model.toString());
    }

}
