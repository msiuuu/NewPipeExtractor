// Forked from Fynn Godau's NewPipe Bandcamp extractor (2019), GNU GPL v3+.
// Reworked for PornHub by msiuuu, 2026.

package org.schabi.newpipe.extractor.services.pornhub.extractors;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nonnull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.schabi.newpipe.extractor.Image;
import org.schabi.newpipe.extractor.Page;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.downloader.Downloader;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.linkhandler.ListLinkHandler;
import org.schabi.newpipe.extractor.playlist.PlaylistExtractor;
import org.schabi.newpipe.extractor.services.pornhub.extractors.streaminfoitem.PornHubSearchStreamInfoItemExtractor;
import org.schabi.newpipe.extractor.stream.Description;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;
import org.schabi.newpipe.extractor.stream.StreamInfoItemsCollector;

public class PornHubPlaylistExtractor extends PlaylistExtractor {

    private static final String BASE_URL = "https://www.pornhub.com";

    private Document document;

    public PornHubPlaylistExtractor(final StreamingService service,
                                    final ListLinkHandler linkHandler) {
        super(service, linkHandler);
    }

    @Override
    public void onFetchPage(@Nonnull final Downloader downloader)
            throws IOException, ExtractionException {
        document = Jsoup.parse(
                downloader.get(getLinkHandler().getUrl()).responseBody());
    }

    @Nonnull
    @Override
    public String getName() throws ParsingException {
        final Element h1 = document.selectFirst(
                "h1.title, h1.playlistTitle, .playlistTitle, h1");
        return h1 == null ? "" : h1.text().trim();
    }

    @Nonnull
    @Override
    public List<Image> getThumbnails() throws ParsingException {
        final Element img = document.selectFirst(
                ".pcVideoListItem .phimage img");
        if (img == null) {
            return List.of();
        }
        String src = img.attr("data-image");
        if (src.isEmpty()) {
            src = img.attr("src");
        }
        if (src.isEmpty()) {
            return List.of();
        }
        return List.of(new Image(src,
                Image.HEIGHT_UNKNOWN, Image.WIDTH_UNKNOWN,
                Image.ResolutionLevel.UNKNOWN));
    }

    @Override
    public String getUploaderUrl() throws ParsingException {
        final Element el = document.selectFirst(
                ".usernameWrap a, .userInfo a");
        if (el == null) {
            return "";
        }
        final String href = el.attr("href");
        return href.startsWith("http") ? href : BASE_URL + href;
    }

    @Override
    public String getUploaderName() {
        final Element el = document.selectFirst(
                ".usernameWrap a, .userInfo a");
        return el == null ? "" : el.text().trim();
    }

    @Nonnull
    @Override
    public List<Image> getUploaderAvatars() {
        return List.of();
    }

    @Override
    public boolean isUploaderVerified() throws ParsingException {
        return false;
    }

    @Override
    public long getStreamCount() {
        return document.select(
                "ul.videos li.pcVideoListItem, li.pcVideoListItem").size();
    }

    @Nonnull
    @Override
    public Description getDescription() throws ParsingException {
        return Description.EMPTY_DESCRIPTION;
    }

    @Nonnull
    @Override
    public InfoItemsPage<StreamInfoItem> getInitialPage()
            throws ExtractionException {
        final StreamInfoItemsCollector collector =
                new StreamInfoItemsCollector(getServiceId());
        for (final Element card
                : document.select(
                        "ul.videos li.pcVideoListItem, li.pcVideoListItem")) {
            collector.commit(new PornHubSearchStreamInfoItemExtractor(card));
        }
        return new InfoItemsPage<>(collector, null);
    }

    @Override
    public InfoItemsPage<StreamInfoItem> getPage(final Page page) {
        return null;
    }
}
