// Created by Fynn Godau 2019, licensed GNU GPL version 3 or later

package org.schabi.newpipe.extractor.services.pornhub;

import static org.schabi.newpipe.extractor.StreamingService.ServiceInfo.MediaCapability.AUDIO;
import static org.schabi.newpipe.extractor.StreamingService.ServiceInfo.MediaCapability.COMMENTS;
import static org.schabi.newpipe.extractor.services.pornhub.extractors.PornHubExtractorHelper.BASE_URL;
import static org.schabi.newpipe.extractor.services.pornhub.extractors.PornHubFeaturedExtractor.FEATURED_API_URL;
import static org.schabi.newpipe.extractor.services.pornhub.extractors.PornHubFeaturedExtractor.KIOSK_FEATURED;
import static org.schabi.newpipe.extractor.services.pornhub.extractors.PornHubRadioExtractor.KIOSK_RADIO;
import static org.schabi.newpipe.extractor.services.pornhub.extractors.PornHubRadioExtractor.RADIO_API_URL;

import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.channel.ChannelExtractor;
import org.schabi.newpipe.extractor.channel.tabs.ChannelTabExtractor;
import org.schabi.newpipe.extractor.comments.CommentsExtractor;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.kiosk.KioskList;
import org.schabi.newpipe.extractor.linkhandler.LinkHandler;
import org.schabi.newpipe.extractor.linkhandler.LinkHandlerFactory;
import org.schabi.newpipe.extractor.linkhandler.ListLinkHandler;
import org.schabi.newpipe.extractor.linkhandler.ListLinkHandlerFactory;
import org.schabi.newpipe.extractor.linkhandler.ReadyChannelTabListLinkHandler;
import org.schabi.newpipe.extractor.linkhandler.SearchQueryHandler;
import org.schabi.newpipe.extractor.linkhandler.SearchQueryHandlerFactory;
import org.schabi.newpipe.extractor.playlist.PlaylistExtractor;
import org.schabi.newpipe.extractor.search.SearchExtractor;
import org.schabi.newpipe.extractor.services.pornhub.extractors.PornHubChannelExtractor;
import org.schabi.newpipe.extractor.services.pornhub.extractors.PornHubChannelTabExtractor;
import org.schabi.newpipe.extractor.services.pornhub.extractors.PornHubCommentsExtractor;
import org.schabi.newpipe.extractor.services.pornhub.extractors.PornHubExtractorHelper;
import org.schabi.newpipe.extractor.services.pornhub.extractors.PornHubFeaturedExtractor;
import org.schabi.newpipe.extractor.services.pornhub.extractors.PornHubPlaylistExtractor;
import org.schabi.newpipe.extractor.services.pornhub.extractors.PornHubRadioExtractor;
import org.schabi.newpipe.extractor.services.pornhub.extractors.PornHubRadioStreamExtractor;
import org.schabi.newpipe.extractor.services.pornhub.extractors.PornHubSearchExtractor;
import org.schabi.newpipe.extractor.services.pornhub.extractors.PornHubStreamExtractor;
import org.schabi.newpipe.extractor.services.pornhub.extractors.PornHubSuggestionExtractor;
import org.schabi.newpipe.extractor.services.pornhub.linkHandler.PornHubChannelLinkHandlerFactory;
import org.schabi.newpipe.extractor.services.pornhub.linkHandler.PornHubChannelTabLinkHandlerFactory;
import org.schabi.newpipe.extractor.services.pornhub.linkHandler.PornHubCommentsLinkHandlerFactory;
import org.schabi.newpipe.extractor.services.pornhub.linkHandler.PornHubFeaturedLinkHandlerFactory;
import org.schabi.newpipe.extractor.services.pornhub.linkHandler.PornHubPlaylistLinkHandlerFactory;
import org.schabi.newpipe.extractor.services.pornhub.linkHandler.PornHubSearchQueryHandlerFactory;
import org.schabi.newpipe.extractor.services.pornhub.linkHandler.PornHubStreamLinkHandlerFactory;
import org.schabi.newpipe.extractor.stream.StreamExtractor;
import org.schabi.newpipe.extractor.subscription.SubscriptionExtractor;
import org.schabi.newpipe.extractor.suggestion.SuggestionExtractor;

import java.util.EnumSet;

public class PornHubService extends StreamingService {

    public PornHubService(final int id) {
        super(id, "PornHub", EnumSet.of(AUDIO, COMMENTS));
    }

    @Override
    public String getBaseUrl() {
        return BASE_URL;
    }

    @Override
    public LinkHandlerFactory getStreamLHFactory() {
        return PornHubStreamLinkHandlerFactory.getInstance();
    }

    @Override
    public ListLinkHandlerFactory getChannelLHFactory() {
        return PornHubChannelLinkHandlerFactory.getInstance();
    }

    @Override
    public ListLinkHandlerFactory getChannelTabLHFactory() {
        return PornHubChannelTabLinkHandlerFactory.getInstance();
    }

    @Override
    public ListLinkHandlerFactory getPlaylistLHFactory() {
        return PornHubPlaylistLinkHandlerFactory.getInstance();
    }

    @Override
    public SearchQueryHandlerFactory getSearchQHFactory() {
        return PornHubSearchQueryHandlerFactory.getInstance();
    }

    @Override
    public ListLinkHandlerFactory getCommentsLHFactory() {
        return PornHubCommentsLinkHandlerFactory.getInstance();
    }

    @Override
    public SearchExtractor getSearchExtractor(final SearchQueryHandler queryHandler) {
        return new PornHubSearchExtractor(this, queryHandler);
    }

    @Override
    public SuggestionExtractor getSuggestionExtractor() {
        return new PornHubSuggestionExtractor(this);
    }

    @Override
    public SubscriptionExtractor getSubscriptionExtractor() {
        return null;
    }

    @Override
    public KioskList getKioskList() throws ExtractionException {
        final KioskList kioskList = new KioskList(this);
        final ListLinkHandlerFactory h = PornHubFeaturedLinkHandlerFactory.getInstance();

        try {
            kioskList.addKioskEntry(
                    (streamingService, url, kioskId) -> new PornHubFeaturedExtractor(
                            PornHubService.this,
                            h.fromUrl(FEATURED_API_URL),
                            kioskId
                    ),
                    h,
                    KIOSK_FEATURED
            );

            kioskList.addKioskEntry(
                    (streamingService, url, kioskId) -> new PornHubRadioExtractor(
                            PornHubService.this,
                            h.fromUrl(RADIO_API_URL),
                            kioskId
                    ),
                    h,
                    KIOSK_RADIO
            );

            kioskList.setDefaultKiosk(KIOSK_FEATURED);

        } catch (final Exception e) {
            throw new ExtractionException(e);
        }

        return kioskList;
    }

    @Override
    public ChannelExtractor getChannelExtractor(final ListLinkHandler linkHandler) {
        return new PornHubChannelExtractor(this, linkHandler);
    }

    @Override
    public ChannelTabExtractor getChannelTabExtractor(final ListLinkHandler linkHandler) {
        if (linkHandler instanceof ReadyChannelTabListLinkHandler) {
            return ((ReadyChannelTabListLinkHandler) linkHandler).getChannelTabExtractor(this);
        } else {
            return new PornHubChannelTabExtractor(this, linkHandler);
        }
    }

    @Override
    public PlaylistExtractor getPlaylistExtractor(final ListLinkHandler linkHandler) {
        return new PornHubPlaylistExtractor(this, linkHandler);
    }

    @Override
    public StreamExtractor getStreamExtractor(final LinkHandler linkHandler) {
        if (PornHubExtractorHelper.isRadioUrl(linkHandler.getUrl())) {
            return new PornHubRadioStreamExtractor(this, linkHandler);
        }
        return new PornHubStreamExtractor(this, linkHandler);
    }

    @Override
    public CommentsExtractor getCommentsExtractor(final ListLinkHandler linkHandler) {
        return new PornHubCommentsExtractor(this, linkHandler);
    }
}
