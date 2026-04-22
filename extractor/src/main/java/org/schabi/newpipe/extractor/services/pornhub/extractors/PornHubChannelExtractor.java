// Forked from Fynn Godau's NewPipe Bandcamp extractor (2019), GNU GPL v3+.
// Reworked for PornHub by msiuuu, 2026.

package org.schabi.newpipe.extractor.services.pornhub.extractors;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.schabi.newpipe.extractor.Image;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.channel.ChannelExtractor;
import org.schabi.newpipe.extractor.downloader.Downloader;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.linkhandler.ListLinkHandler;

public class PornHubChannelExtractor extends ChannelExtractor {

    private Document document;

    public PornHubChannelExtractor(final StreamingService service,
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
        final Element h1 = document.selectFirst("h1[itemprop=name]");
        return h1 == null ? "" : h1.text().trim();
    }

    @Nonnull
    @Override
    public List<Image> getAvatars() {
        final Element img = document.selectFirst("#getAvatar");
        if (img == null) {
            return List.of();
        }
        final String src = img.attr("src");
        if (src.isEmpty()) {
            return List.of();
        }
        return List.of(new Image(src,
                Image.HEIGHT_UNKNOWN, Image.WIDTH_UNKNOWN,
                Image.ResolutionLevel.UNKNOWN));
    }

    @Nonnull
    @Override
    public List<Image> getBanners() throws ParsingException {
        final Element img = document.selectFirst("#coverPictureDefault");
        if (img == null) {
            return List.of();
        }
        final String src = img.attr("src");
        if (src.isEmpty()) {
            return List.of();
        }
        return List.of(new Image(src,
                Image.HEIGHT_UNKNOWN, Image.WIDTH_UNKNOWN,
                Image.ResolutionLevel.UNKNOWN));
    }

    @Override
    public String getFeedUrl() {
        return null;
    }

    @Override
    public long getSubscriberCount() {
        return -1;
    }

    @Override
    public String getDescription() {
        final Element bio = document.selectFirst(".aboutMeSection");
        return bio == null ? "" : bio.text().trim();
    }

    @Override
    public String getParentChannelName() {
        return null;
    }

    @Override
    public String getParentChannelUrl() {
        return null;
    }

    @Nonnull
    @Override
    public List<Image> getParentChannelAvatars() {
        return List.of();
    }

    @Override
    public boolean isVerified() throws ParsingException {
        return document.selectFirst(".verifiedPornstar, .verifiedIcon")
                != null;
    }

    @Nonnull
    @Override
    public List<ListLinkHandler> getTabs() throws ParsingException {
        // Tabs wired when PornHubChannelTabExtractor is ported.
        return Collections.emptyList();
    }
}
