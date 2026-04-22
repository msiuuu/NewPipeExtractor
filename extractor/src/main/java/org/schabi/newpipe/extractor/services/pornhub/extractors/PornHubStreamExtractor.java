// Forked from Fynn Godau's NewPipe Bandcamp extractor (2019), GNU GPL v3+.
// Reworked for PornHub by msiuuu, 2026.

package org.schabi.newpipe.extractor.services.pornhub.extractors;

import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.schabi.newpipe.extractor.Image;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.downloader.Downloader;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.linkhandler.LinkHandler;
import org.schabi.newpipe.extractor.localization.DateWrapper;
import org.schabi.newpipe.extractor.playlist.PlaylistInfoItemsCollector;
import org.schabi.newpipe.extractor.stream.AudioStream;
import org.schabi.newpipe.extractor.stream.Description;
import org.schabi.newpipe.extractor.stream.StreamExtractor;
import org.schabi.newpipe.extractor.stream.StreamType;
import org.schabi.newpipe.extractor.stream.VideoStream;

import java.io.IOException;
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

    public PornHubStreamExtractor(final StreamingService service,
                                  final LinkHandler linkHandler) {
        super(service, linkHandler);
    }

    @Override
    public void onFetchPage(@Nonnull final Downloader downloader)
            throws IOException, ExtractionException {
        html = downloader.get(getLinkHandler().getUrl()).responseBody();
        document = Jsoup.parse(html);
        flashvars = extractFlashvars(html, getId());
    }

    /**
     * PornHub embeds video metadata as a JS object literal named
     * flashvars_(viewkey) inside a script tag. Finds that marker, then
     * walks forward brace-by-brace to extract the JSON.
     */
    static JsonObject extractFlashvars(final String pageHtml,
                                                final String viewkey)
            throws ParsingException {
        final Pattern marker = Pattern.compile(
                "var\\s+flashvars_" + Pattern.quote(viewkey) + "\\s*=\\s*\\{",
                Pattern.CASE_INSENSITIVE);
        final Matcher m = marker.matcher(pageHtml);
        if (!m.find()) {
            throw new ParsingException(
                    "Could not find flashvars_" + viewkey + " block in page HTML");
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
        return "";
    }

    @Nonnull
    @Override
    public String getUploaderName() throws ParsingException {
        return "";
    }

    @Nullable
    @Override
    public String getTextualUploadDate() {
        return null;
    }

    @Nullable
    @Override
    public DateWrapper getUploadDate() throws ParsingException {
        return null;
    }

    @Nonnull
    @Override
    public List<Image> getThumbnails() throws ParsingException {
        return List.of();
    }

    @Nonnull
    @Override
    public List<Image> getUploaderAvatars() {
        return List.of();
    }

    @Nonnull
    @Override
    public Description getDescription() {
        return Description.EMPTY_DESCRIPTION;
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
    public List<VideoStream> getVideoStreams() {
        return Collections.emptyList();
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
        return "";
    }

    @Nonnull
    @Override
    public String getLicence() {
        return "";
    }

    @Nonnull
    @Override
    public List<String> getTags() {
        return List.of();
    }

    /** Legacy stub kept so bandcamp-scaffolded extractors compile.
     * Will be deleted when every other extractor is ported off it. */
    public static JsonObject getAlbumInfoJson(final String pageHtml)
            throws ParsingException {
        throw new UnsupportedOperationException(
                "getAlbumInfoJson is bandcamp legacy; migrate off it");
    }
}
