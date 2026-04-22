package org.schabi.newpipe.extractor.services.pornhub.extractors.streaminfoitem;

import java.util.List;

import javax.annotation.Nonnull;

import org.jsoup.nodes.Element;
import org.schabi.newpipe.extractor.Image;
import org.schabi.newpipe.extractor.exceptions.ParsingException;

public class PornHubSearchStreamInfoItemExtractor
        extends PornHubStreamInfoItemExtractor {

    private static final String BASE_URL = "https://www.pornhub.com";

    private final Element card;

    public PornHubSearchStreamInfoItemExtractor(final Element card) {
        super(null);
        this.card = card;
    }

    @Override
    public String getName() throws ParsingException {
        final Element titleLink = card.selectFirst(".title a");
        if (titleLink != null && !titleLink.text().trim().isEmpty()) {
            return titleLink.text().trim();
        }
        return card.attr("title");
    }

    @Override
    public String getUrl() throws ParsingException {
        final String vkey = card.attr("data-video-vkey");
        if (!vkey.isEmpty()) {
            return BASE_URL + "/view_video.php?viewkey=" + vkey;
        }
        final Element link = card.selectFirst("a[href*=view_video]");
        if (link == null) {
            throw new ParsingException("No video URL found in card");
        }
        final String href = link.attr("href");
        return href.startsWith("http") ? href : BASE_URL + href;
    }

    @Override
    public String getUploaderName() {
        final Element el = card.selectFirst(".usernameWrap a");
        return el == null ? "" : el.text().trim();
    }

    @Override
    public String getUploaderUrl() {
        final Element el = card.selectFirst(".usernameWrap a");
        if (el == null) {
            return "";
        }
        final String href = el.attr("href");
        return href.startsWith("http") ? href : BASE_URL + href;
    }

    @Nonnull
    @Override
    public List<Image> getThumbnails() throws ParsingException {
        final Element img = card.selectFirst(".phimage img");
        if (img == null) {
            return List.of();
        }
        String url = img.attr("data-image");
        if (url.isEmpty()) {
            url = img.attr("src");
        }
        if (url.isEmpty()) {
            return List.of();
        }
        return List.of(new Image(url,
                Image.HEIGHT_UNKNOWN, Image.WIDTH_UNKNOWN,
                Image.ResolutionLevel.UNKNOWN));
    }

    @Override
    public long getDuration() {
        final Element dur = card.selectFirst("var.duration");
        return dur == null ? -1 : parseDuration(dur.text());
    }

    /** Parses "M:SS" or "H:MM:SS" into seconds. */
    private static long parseDuration(final String s) {
        if (s == null || s.isEmpty()) {
            return -1;
        }
        final String[] parts = s.trim().split(":");
        long total = 0;
        try {
            for (final String part : parts) {
                total = total * 60 + Long.parseLong(part.trim());
            }
            return total;
        } catch (final NumberFormatException e) {
            return -1;
        }
    }
}
