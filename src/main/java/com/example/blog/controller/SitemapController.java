package com.example.blog.controller;

import com.example.blog.model.Post;
import com.example.blog.model.PostStatus;
import com.example.blog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SitemapController {

    private final PostRepository postRepository;

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String sitemap() {
        var now = java.time.LocalDateTime.now();
        List<Post> posts = postRepository.findAllByStatusAndPublishedAtLessThanEqualOrderByCreatedAtDesc(PostStatus.PUBLISHED, now);
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");
        // Home
        xml.append(url("/", now));
        for (Post p : posts) {
            xml.append(url("/posts/" + p.getId(), p.getUpdatedAt()));
        }
        xml.append("</urlset>");
        return xml.toString();
    }

    private String url(String path, java.time.LocalDateTime lastMod) {
        String loc = path;
        String lastmod = lastMod != null ? lastMod.format(DateTimeFormatter.ISO_DATE) : "";
        return "  <url>\n" +
                "    <loc>" + loc + "</loc>\n" +
                (lastmod.isEmpty() ? "" : ("    <lastmod>" + lastmod + "</lastmod>\n")) +
                "  </url>\n";
    }
}


