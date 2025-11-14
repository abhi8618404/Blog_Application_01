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
public class FeedController {

    private final PostRepository postRepository;

    @GetMapping(value = "/feed.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String rss() {
        var now = java.time.LocalDateTime.now();
        List<Post> posts = postRepository.findAllByStatusAndPublishedAtLessThanEqualOrderByCreatedAtDesc(PostStatus.PUBLISHED, now);
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<rss version=\"2.0\">\n");
        xml.append("  <channel>\n");
        xml.append(tag("title", "Blog Feed"));
        xml.append(tag("link", "/"));
        xml.append(tag("description", "Latest posts"));
        for (Post p : posts) {
            xml.append("    <item>\n");
            xml.append(tag("title", escape(p.getTitle())));
            xml.append(tag("link", "/posts/" + p.getId()));
            xml.append(tag("pubDate", p.getPublishedAt() != null ? p.getPublishedAt().format(DateTimeFormatter.RFC_1123_DATE_TIME) : ""));
            xml.append(tag("description", escape(p.getContent().length() > 160 ? p.getContent().substring(0, 160) + "..." : p.getContent())));
            xml.append("    </item>\n");
        }
        xml.append("  </channel>\n");
        xml.append("</rss>");
        return xml.toString();
    }

    private String tag(String name, String body) {
        return "    <" + name + ">" + body + "</" + name + ">\n";
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}


