package org.schabi.newpipe.extractor.services.pornhub.linkHandler;

import org.junit.jupiter.api.Test;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PornHubSearchQueryHandlerFactoryTest {

    private final PornHubSearchQueryHandlerFactory factory =
            PornHubSearchQueryHandlerFactory.getInstance();

    @Test
    void basicQueryEmbedded() throws Exception {
        assertEquals("https://www.pornhub.com/video/search?search=lana",
                factory.getUrl("lana", Collections.emptyList(), ""));
    }

    @Test
    void multiWordQueryRoundTrips() throws Exception {
        assertEquals("lana rhoades", decodeQuery(
                factory.getUrl("lana rhoades", Collections.emptyList(), "")));
    }

    @Test
    void specialCharactersRoundTrip() throws Exception {
        assertEquals("a&b c", decodeQuery(
                factory.getUrl("a&b c", Collections.emptyList(), "")));
    }

    private static String decodeQuery(final String url) {
        final String encoded = url.substring(
                url.indexOf("search=") + "search=".length());
        return URLDecoder.decode(encoded, StandardCharsets.UTF_8);
    }
}
