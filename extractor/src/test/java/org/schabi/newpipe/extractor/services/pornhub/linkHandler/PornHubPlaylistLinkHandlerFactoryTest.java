package org.schabi.newpipe.extractor.services.pornhub.linkHandler;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PornHubPlaylistLinkHandlerFactoryTest {

    private final PornHubPlaylistLinkHandlerFactory factory =
            PornHubPlaylistLinkHandlerFactory.getInstance();

    @Test
    void basicPlaylistUrlYieldsId() throws Exception {
        assertEquals("12345678",
                factory.getId("https://www.pornhub.com/playlist/12345678"));
    }

    @Test
    void trailingQueryIgnored() throws Exception {
        assertEquals("12345678",
                factory.getId("https://www.pornhub.com/playlist/12345678?page=2"));
    }

    @Test
    void canonicalReconstructionMatches() throws Exception {
        assertEquals("https://www.pornhub.com/playlist/12345678",
                factory.getUrl("12345678", Collections.emptyList(), ""));
    }

    @Test
    void playlistUrlIsAccepted() throws Exception {
        assertTrue(factory.onAcceptUrl(
                "https://www.pornhub.com/playlist/12345678"));
    }

    @Test
    void videoUrlIsRejected() throws Exception {
        assertFalse(factory.onAcceptUrl(
                "https://www.pornhub.com/view_video.php?viewkey=abc123"));
    }

    @Test
    void channelUrlIsRejected() throws Exception {
        assertFalse(factory.onAcceptUrl(
                "https://www.pornhub.com/model/lana-rhoades"));
    }

    @Test
    void nonPornhubUrlIsRejected() throws Exception {
        assertFalse(factory.onAcceptUrl(
                "https://www.youtube.com/playlist?list=PLabc"));
    }
}
