package org.schabi.newpipe.extractor.services.pornhub.extractors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.grack.nanojson.JsonObject;

import org.junit.jupiter.api.Test;
import org.schabi.newpipe.extractor.exceptions.ParsingException;

public class PornHubStreamExtractorTest {

    @Test
    public void extractsFlashvarsFromSimplePage() throws Exception {
        final String html = "<html><body><script>"
                + "var flashvars_abc123 = {"
                + "\"video_title\": \"Test Video\","
                + "\"video_duration\": 600,"
                + "\"image_url\": \"https://example.com/thumb.jpg\""
                + "};"
                + "</script></body></html>";
        final JsonObject result = PornHubStreamExtractor.extractFlashvars(html, "abc123");
        assertEquals("Test Video", result.getString("video_title"));
        assertEquals(600, result.getInt("video_duration"));
    }

    @Test
    public void handlesNestedBracesInFlashvars() throws Exception {
        final String html = "var flashvars_xyz = {"
                + "\"mediaDefinitions\":[{\"format\":\"hls\",\"videoUrl\":\"a.m3u8\"}],"
                + "\"video_title\":\"Nested\""
                + "};";
        final JsonObject result = PornHubStreamExtractor.extractFlashvars(html, "xyz");
        assertEquals("Nested", result.getString("video_title"));
    }

    @Test
    public void handlesBracesInsideStrings() throws Exception {
        final String html = "var flashvars_k = {"
                + "\"video_title\":\"Weird } string with { braces\","
                + "\"video_duration\":120"
                + "};";
        final JsonObject result = PornHubStreamExtractor.extractFlashvars(html, "k");
        assertEquals("Weird } string with { braces", result.getString("video_title"));
        assertEquals(120, result.getInt("video_duration"));
    }

    @Test
    public void throwsWhenFlashvarsMissing() {
        final String html = "<html><body>no flashvars here</body></html>";
        assertThrows(ParsingException.class,
                () -> PornHubStreamExtractor.extractFlashvars(html, "abc"));
    }
}
