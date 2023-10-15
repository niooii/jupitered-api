package com.example.niooii.google.searchresults;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@RestController
public class SearchesController { // a more inconvenient way to search google.

    public static final String[] userAgents = {
            "Mozilla/5.0 (Windows NT 10.0; Win64; X64) AppleWebKit/537.36 (KHTML, Like Gecko) Chrome/74.0.3729.169 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, Like Gecko) Chrome/72.0.3626.121 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; X64) AppleWebKit/537.36 (KHTML, Like Gecko) Chrome/74.0.3729.157 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; X64) AppleWebKit/537.36 (KHTML, Like Gecko) Chrome/96.0.4664.110 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; X64) AppleWebKit/537.36 (KHTML, Like Gecko) Chrome/96.0.4664.45 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; X64) AppleWebKit/537.36 (KHTML, Like Gecko) Chrome/97.0.4692.71 Safari/537.36"};

    final String format = "https://www.google.com/search?q=%s";

    @GetMapping("/search")
    public Searches search10(@RequestParam(value = "query", defaultValue = "How do i search with google?") String name, HttpServletRequest request) throws IOException {
        String searchinput;
        if(request.getQueryString() != null) {
            searchinput = request.getQueryString().substring(request.getQueryString().indexOf("query=") + 6);
            System.out.println(searchinput);
            searchinput = searchinput.replaceAll("%20", " ");
        } else {
            searchinput = "How do i search on google?";
        }
        String googleUrl = String.format(format, searchinput);

        // Generate a random num to bypass google block smh
        int rnd = (int)(Math.random()*userAgents.length);
        Document doc = Jsoup.connect(googleUrl).userAgent(userAgents[rnd]).get();
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        Elements results = doc.select("div.g");
        for (Element result : results) {
            String title = result.select("h3").text();
            String link = result.select(".yuRUbf > a").attr("href");
            String snippet = result.select(".VwiC3b").text();
            list.add(new ArrayList<>(Arrays.asList(title, link, snippet)));
        }
        return new Searches(searchinput, list);
    }
}
