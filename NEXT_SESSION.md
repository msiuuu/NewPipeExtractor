# PornHub — Next Session

## Where we are
- 6 pornhub extractors ported: Stream, Search, Channel, ChannelTab, Playlist (Comments + Featured stubbed).
- 4 unit tests passing on flashvars parser.
- Live HTML tests against saved samples confirm all selectors work.
- NewPipe APK builds cleanly, installs on xiaomi.

## The wall we hit
PornHub serves an "age-wall" interstitial to any non-authenticated request.
Cookie-only bypass is insufficient — they also fingerprint the session
server-side (likely binds session to TLS + IP). Even hardcoding a fresh
captured session cookie into the extractor does not produce valid pages
on the xiaomi because NewPipe's downloader's TLS fingerprint differs
from Chrome's.

## Next session: webview cookie capture (NewPipe app-side)
This is Android UI work in `msiuuu/NewPipe`, NOT extractor work.

### Architecture
1. First time pornhub service is opened, show an embedded `WebView`
   loading `https://www.pornhub.com`.
2. User clicks through pornhub's age-wall inside the webview like a
   normal browser. WebView's cookie jar captures the real session
   cookies.
3. Extract cookies via Android's
   `CookieManager.getInstance().getCookie("https://www.pornhub.com")`.
4. Store those cookies (shared preferences or similar).
5. In `DownloaderImpl.kt`, for any request to pornhub.com, inject the
   stored cookies into the request headers.

### Files to create/touch
- NEW: `app/src/main/java/org/schabi/newpipe/views/PornHubAuthActivity.kt`
  (or a Fragment) — the WebView screen
- NEW: a small cookie-store helper, probably using EncryptedSharedPreferences
- MODIFY: `app/src/main/java/org/schabi/newpipe/DownloaderImpl.kt`
  — inject pornhub cookies into outgoing requests whose host matches
    pornhub.com
- MAYBE: hook into service selection flow so first-time pornhub use
  triggers the auth activity

### Also NOT done (deferred)
- Proper Featured kiosk (currently stub) — needs generic type changed
  from `PlaylistInfoItem` to `StreamInfoItem` and service registration
  update.
- Proper Comments extractor — needs sample of pornhub's comments XHR
  endpoint + JSON schema mapping.
- View count selector on Stream pages (the `.count` probes didn't
  match anything useful; worth another sampling round).
- Like/dislike parsing (`.votesUp` returns "7K" on Stream pages;
  need to parse the K/M suffix into a number).
- PornHubExtractorHelper is still bandcamp-scaffolded; some legacy
  methods (`getArtistDetails`, `getImagesFromImageId`, `parseDate`,
  `isRadioUrl`, etc.) are dead-code-referenced by abandoned
  bandcamp-era InfoItem extractors (`PornHubDiscographStreamInfoItemExtractor`,
  `PornHubAlbumInfoItemExtractor`, `PornHubPlaylistStreamInfoItemExtractor`).
  Cleanup: delete those dead files, strip Helper to pornhub-only utils.

## Local-only files on Misu's machine (not in repo)
- `PornHubAuth.java` in the extractors package — `.gitignore`d. Holds
  dev cookies for quick testing. Can be rebuilt from any fresh cookie
  capture. When proper webview auth ships, delete it.
- `C:\Dev_Code\pornhub_sample.txt` — saved video page HTML for
  `PornHubStreamExtractorLiveTest`.
- `C:\Dev_Code\pornhub_search_sample.txt` — saved search page.
- `C:\Dev_Code\pornhub_channel_sample.txt` — saved channel page.

## Quick-start for Misu
1. `cd C:\Dev_Code\NewPipeExtractor`
2. `git pull origin add-pornhub-extractor`
3. `cd C:\Dev_Code\NewPipe`
4. `git pull origin use-local-pornhub-extractor`
5. Read this file
6. Tell Cali "pick up where we left off — webview auth"
