// Forked from Fynn Godau's NewPipe Bandcamp extractor (2019), GNU GPL v3+.
// Reworked for PornHub by msiuuu, 2026.

package org.schabi.newpipe.extractor.services.pornhub.extractors;

import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.schabi.newpipe.extractor.Image;
import org.schabi.newpipe.extractor.MediaFormat;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.downloader.Downloader;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.linkhandler.LinkHandler;
import org.schabi.newpipe.extractor.localization.DateWrapper;
import org.schabi.newpipe.extractor.playlist.PlaylistInfoItemsCollector;
import org.schabi.newpipe.extractor.stream.AudioStream;
import org.schabi.newpipe.extractor.stream.DeliveryMethod;
import org.schabi.newpipe.extractor.stream.Description;
import org.schabi.newpipe.extractor.stream.StreamExtractor;
import org.schabi.newpipe.extractor.stream.StreamType;
import org.schabi.newpipe.extractor.stream.VideoStream;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PornHubStreamExtractor extends StreamExtractor {

    private String html;
    private Document document;
    private JsonObject flashvars;
    private JsonObject jsonLd;

    public PornHubStreamExtractor(final StreamingService service,
                                  final LinkHandler linkHandler) {
        super(service, linkHandler);
    }

    @Override
    public void onFetchPage(@Nonnull final Downloader downloader)
            throws IOException, ExtractionException {
        html = downloader.get(getLinkHandler().getUrl()).responseBody();
        document = Jsoup.parse(html);
        flashvars = extractFlashvars(html);
        jsonLd = extractJsonLd(document);
    }

    /**
     * PornHub embeds video metadata as a JS object literal named
     * flashvars_(internal_video_id). Suffix is numeric video_id,
     * NOT the URL viewkey.
     */
    static JsonObject extractFlashvars(final String pageHtml)
            throws ParsingException {
        final Pattern marker = Pattern.compile(
                "var\\s+flashvars_\\d+\\s*=\\s*\\{");
        final Matcher m = marker.matcher(pageHtml);
        if (!m.find()) {
            throw new ParsingException(
                    "Could not find flashvars block in page HTML");
        }
        final int jsonStart = m.end() - 1;
        final int jsonEnd = findMatchingBrace(pageHtml, jsonStart);
        if (jsonEnd == -1) {
            throw new ParsingException("Unterminated flashvars JSON block");
        }
        try {
            return JsonParser.object()
                    .from(pageHtml.substring(jsonStart, jsonEnd + 1));
        } catch (final JsonParserException e) {
            throw new ParsingException("Failed to parse flashvars JSON", e);
        }
    }

    /**
     * Finds the schema.org VideoObject JSON-LD block. Source of truth for
     * description, uploadDate, and a title fallback.
     */
    private static JsonObject extractJsonLd(final Document doc) {
        for (final Element script
                : doc.select("script[type=application/ld+json]")) {
            try {
                final JsonObject obj = JsonParser.object().from(script.data());
                if ("VideoObject".equals(obj.getString("@type"))) {
                    return obj;
                }
            } catch (final Exception ignored) {
                // try next script tag
            }
        }
        return null;
    }

    private static int findMatchingBrace(final String s, final int openIndex) {
        int depth = 0;
        boolean inString = false;
        boolean escape = false;
        for (int i = openIndex; i < s.length(); i++) {
            final char c = s.charAt(i);
            if (escape) {
                escape = false;
                continue;
            }
            if (c == '\\') {
                escape = true;
                continue;
            }
            if (c == '"') {
                inString = !inString;
                continue;
            }
            if (inString) {
                continue;
            }
            if (c == '{') {
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private List<String> extractAdRollParam(final String paramName) {
        final JsonArray adRoll = flashvars.getArray("adRollGlobalConfig");
        if (adRoll == null || adRoll.isEmpty()) {
            return List.of();
        }
        final String jsonUrl = adRoll.getObject(0).getString("json", "");
        if (jsonUrl.isEmpty()) {
            return List.of();
        }
        final Pattern p = Pattern.compile(
                "channel%5B" + paramName + "%5D=([^&]*)");
        final Matcher m = p.matcher(jsonUrl);
        if (!m.find()) {
            return List.of();
        }
        try {
            final String decoded = URLDecoder.decode(
                    m.group(1), StandardCharsets.UTF_8.name());
            if (decoded.isEmpty()) {
                return List.of();
            }
            return Arrays.asList(decoded.split(","));
        } catch (final Exception e) {
            return List.of();
        }
    }

    @Nonnull
    @Override
    public String getName() throws ParsingException {
        return flashvars.getString("video_title", "");
    }

    @Nonnull
    @Override
    public String getUrl() throws ParsingException {
        return getLinkHandler().getUrl();
    }

    @Nonnull
    @Override
    public String getUploaderUrl() throws ParsingException {
        if (document == null) {
            return "";
        }
        final Element el = document.selectFirst(
                ".video-detailed-info .usernameWrap a");
        if (el == null) {
            return "";
        }
        final String href = el.attr("href");
        return href.startsWith("http")
                ? href
                : "https://www.pornhub.com" + href;
    }

    @Nonnull
    @Override
    public String getUploaderName() throws ParsingException {
        if (document == null) {
            return "";
        }
        final Element el = document.selectFirst(
                ".video-detailed-info .usernameWrap a");
        return el == null ? "" : el.text();
    }

    @Nullable
    @Override
    public String getTextualUploadDate() {
        return jsonLd == null ? null : jsonLd.getString("uploadDate", null);
    }

    @Nullable
    @Override
    public DateWrapper getUploadDate() throws ParsingException {
        final String s = getTextualUploadDate();
        if (s == null || s.isEmpty()) {
            return null;
        }
        try {
            return new DateWrapper(OffsetDateTime.parse(s));
        } catch (final Exception e) {
            throw new ParsingException(
                    "Could not parse uploadDate '" + s + "'", e);
        }
    }

    @Nonnull
    @Override
    public List<Image> getThumbnails() throws ParsingException {
        final String imageUrl = flashvars.getString("image_url", "");
        if (imageUrl.isEmpty()) {
            return List.of();
        }
        return List.of(new Image(imageUrl,
                Image.HEIGHT_UNKNOWN, Image.WIDTH_UNKNOWN,
                Image.ResolutionLevel.UNKNOWN));
    }

    @Nonnull
    @Override
    public List<Image> getUploaderAvatars() {
        return List.of();
    }

    @Nonnull
    @Override
    public Description getDescription() {
        if (jsonLd == null) {
            return Description.EMPTY_DESCRIPTION;
        }
        final String raw = jsonLd.getString("description", "");
        if (raw.isEmpty()) {
            return Description.EMPTY_DESCRIPTION;
        }
        // HTML entity decode (pornhub's description contains &period; &comma; etc)
        final String decoded = Jsoup.parse(raw).text();
        return new Description(decoded, Description.PLAIN_TEXT);
    }

    @Override
    public List<AudioStream> getAudioStreams() {
        return Collections.emptyList();
    }

    @Override
    public long getLength() throws ParsingException {
        return flashvars.getLong("video_duration", 0L);
    }

    @Override
    public List<VideoStream> getVideoStreams() throws ExtractionException {
        final List<VideoStream> streams = new ArrayList<>();
        final JsonArray defs = flashvars.getArray("mediaDefinitions");
        if (defs == null) {
            return streams;
        }
        for (int i = 0; i < defs.size(); i++) {
            final JsonObject def = defs.getObject(i);
            final String format = def.getString("format", "");
            final String videoUrl = def.getString("videoUrl", "");
            final Object qualityRaw = def.get("quality");
            if (videoUrl.isEmpty() || !(qualityRaw instanceof String)) {
                continue;
            }
            final String quality = (String) qualityRaw;
            final DeliveryMethod dm = "hls".equals(format)
                    ? DeliveryMethod.HLS
                    : DeliveryMethod.PROGRESSIVE_HTTP;
            streams.add(new VideoStream.Builder()
                    .setId(quality + "-" + format)
                    .setContent(videoUrl, true)
                    .setIsVideoOnly(false)
                    .setMediaFormat(MediaFormat.MPEG_4)
                    .setDeliveryMethod(dm)
                    .setResolution(quality + "p")
                    .build());
        }
        return streams;
    }

    @Override
    public List<VideoStream> getVideoOnlyStreams() {
        return Collections.emptyList();
    }

    @Override
    public StreamType getStreamType() {
        return StreamType.VIDEO_STREAM;
    }

    @Override
    public PlaylistInfoItemsCollector getRelatedItems() {
        return new PlaylistInfoItemsCollector(getServiceId());
    }

    @Nonnull
    @Override
    public String getCategory() {
        final List<String> cats = extractAdRollParam("context_category");
        return cats.isEmpty() ? "" : cats.get(0);
    }

    @Nonnull
    @Override
    public String getLicence() {
        return "";
    }

    @Nonnull
    @Override
    public List<String> getTags() {
        return extractAdRollParam("context_tag");
    }

    /** Legacy stub kept so bandcamp-scaffolded extractors compile. */
    public static JsonObject getAlbumInfoJson(final String pageHtml)
            throws ParsingException {
        throw new UnsupportedOperationException(
                "getAlbumInfoJson is bandcamp legacy; migrate off it");
    }
}
