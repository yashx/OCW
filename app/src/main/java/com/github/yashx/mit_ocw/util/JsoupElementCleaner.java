package com.github.yashx.mit_ocw.util;


import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class JsoupElementCleaner {
    public static Elements elementsCleaner(Elements eTs) {
        for (Element e : eTs)
            e = elementCleaner(e);
        return eTs;
    }

    public static Element elementCleaner(Element eT) {
        Elements eTs;
        //web page is received and processed
        if (eT != null) {
            //removes all images from html and replacing them with their alt text
            eTs = eT.select("img");
            if (eTs != null) {
                for (org.jsoup.nodes.Element el : eTs) {
                    if (el.attr("alt") != null)
                        if (el.attr("alt").contains("screen reader"))
                            el.remove();
                        else
                            el.replaceWith(new TextNode(el.attr("alt")));
                }
            }

            //removes all scripts from html
            eTs = eT.select("script");
            if (eTs != null)
                for (org.jsoup.nodes.Element el : eTs)
                    el.remove();

            //removes all p style attributes from html
            eTs = eT.select("p[style]");
            if (eTs != null)
                for (Element el : eTs)
                    el.removeAttr("style");

            //flattening html
            while (eT.selectFirst("div:not(.help)") != null) {
                Element el = eT.selectFirst("div:not(.help)");
                el.after(el.html());
                el.remove();
            }

            //replaces all relative links with absolute links to allow user open them easily from fragment
            eTs = eT.select("a");
            if (eTs != null) {
                for (Element el : eTs) {
                    if (el.attr("href") != null)
                        if(!el.absUrl("href").isEmpty())
                            el.attr("href", el.absUrl("href"));
                        else
                            el.attr("href", "https://ocw.mit.edu" + el.attr("href"));
                }
            }
        }
        return eT;
    }
}