package com.github.yashx.mit_ocw.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import com.github.yashx.mit_ocw.R;
import com.github.yashx.mit_ocw.util.SpannableStringBuilderTrimmer;
import com.github.yashx.mit_ocw.util.ViewBuilders;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class HtmlRendererCourseFragment extends Fragment {
    private String url;
    private LinearLayout linearLayout;

    public static HtmlRendererCourseFragment newInstance(String url) {

        Bundle args = new Bundle();
        args.putString("url", url);
        HtmlRendererCourseFragment fragment = new HtmlRendererCourseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_htmlrenderercourse, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linearLayout = view.findViewById(R.id.linearLayoutHtmlRendererCourse);
        url = getArguments().getString("url");
        new HtmlAsyncTask().execute(url);
    }

    class HtmlAsyncTask extends AsyncTask<String, String, Document> {
        @Override
        protected Document doInBackground(String... strings) {
            Document doc = null;
            try {
                if (!strings[0].endsWith("/"))
                    strings[0] += "/";
                doc = Jsoup.connect(strings[0]).get();
            } catch (Exception e) {
                Log.e("TAG", "doInBackground: ", e);
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);
            Element eT = document.selectFirst("body main");
            Elements eTs;
            if (eT != null) {
                eTs = eT.select("img");
                if (eTs != null) {
                    for (Element el : eTs) {
                        if(el.attr("alt")!=null)
                            el.replaceWith(new TextNode(el.attr("alt")));
                    }
                }
                eTs = eT.select("a");
                if (eTs != null) {
                    for (Element el : eTs) {
                        if(el.attr("href")!=null)
                            el.attr("href",el.absUrl("href"));
                    }
                }
                eTs = eT.select(">:not(.help)");
                if (eTs != null) {
                    for (Element e : eTs) {
                        if (e.is("h1, h2, h3, h4, h5,h6")) {
                            linearLayout.addView(ViewBuilders.SmallHeadingTextView(getContext(), e.text()));
                        }
//                        else if (e.is("p") && e.select("a") == null e.hasSameValue()) {
//                            linearLayout.addView(ViewBuilders.SmallBodyMidTextView(getContext(), e.text()));
//                        }
                        else {
                              SpannableStringBuilder formattedHtml =
                                    (SpannableStringBuilder) HtmlCompat.fromHtml(e.outerHtml(),
                                            HtmlCompat.FROM_HTML_MODE_COMPACT);
                            formattedHtml = SpannableStringBuilderTrimmer.trimmer(formattedHtml);
                            URLSpan[] currentSpans = formattedHtml.getSpans(0, formattedHtml.length(), URLSpan.class);

                            SpannableString buffer = new SpannableString(formattedHtml);
                            Linkify.addLinks(buffer, Linkify.WEB_URLS);

                            for (URLSpan span : currentSpans) {
                                int end = formattedHtml.getSpanEnd(span);
                                int start = formattedHtml.getSpanStart(span);
                                buffer.setSpan(span, start, end, 0);
                            }
                            TextView t = ViewBuilders.SmallBodyMidTextView(getContext(), "");
                            t.setText(formattedHtml);
                            t.setLinksClickable(true);
                            t.setMovementMethod(LinkMovementMethod.getInstance());
                            linearLayout.addView(t);
                        }
                    }
                    System.out.println(linearLayout.getChildCount());
                }
            }
        }
    }
}
