package org.schabi.newpipe.extractor.services.pornhub.linkHandler;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PornHubCommentsLinkHandlerFactoryTest {

    private final PornHubCommentsLinkHandlerFactory factory =
            PornHubCommentsLinkHandlerFactory.getInstance();

    @Test
    void getIdMatchesStreamHandler() throws Exception {
        assertEquals("ph5f8e9a1b2c3d4", factory.getId(
                "https://www.pornhub.com/view_video.php?viewkey=ph5f8e9a1b2c3d4"));
    }

    @Test
    void getUrlMatchesStreamHandler() throws Exception {
        assertEquals("https://www.pornhub.com/view_video.php?viewkey=ph5f8e9a1b2c3d4",
                factory.getUrl("ph5f8e9a1b2c3d4", Collections.emptyList(), ""));
    }

    @Test
    void videoUrlIsAccepted() throws Exception {
        assertTrue(factory.onAcceptUrl(
                "https://www.pornhub.com/view_video.php?viewkey=ph5f8e9a1b2c3d4"));
    }

    @Test
    void channelUrlIsRejected() throws Exception {
        assertFalse(factory.onAcceptUrl(
                "https://www.pornhub.com/model/lana-rhoades"));
    }

    @Test
    void nonPornhubUrlIsRejected() throws Exception {
        assertFalse(factory.onAcceptUrl(
                "https://www.youtube.com/watch?v=abc123"));
    }
}
