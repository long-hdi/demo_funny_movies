package com.longhdi.component;

import com.longhdi.qualifier.Id;
import com.longhdi.qualifier.Preview;
import com.longhdi.qualifier.Text;
import com.longhdi.qualifier.YouTube;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.net.http.HttpClient;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilityFactory {

    @Produces
    @ApplicationScoped
    @YouTube @Id
    public MayTransform<String> extractYouTubeVideoIdFromUrl() {
        final String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        return (String url) -> {
            Matcher matcher = compiledPattern.matcher(url);
            return matcher.find() ? Optional.of(matcher.group()) : Optional.empty();
        };
    }

    @Produces
    @Preview @Text
    public MayTransform<String> makePreviewTextFromDescription(InjectionPoint injectionPoint) {
        Text text = (Text) injectionPoint.getQualifiers().stream()
                .filter(annotation -> annotation.annotationType().equals(Text.class))
                .findFirst().get();
        return s -> s == null
                ? Optional.empty()
                : s.length() <= text.length()
                            ? Optional.of(s)
                            : Optional.of(s.substring(0, text.length() - 1) + "...");
    }

    @Produces
    @ApplicationScoped
    public HttpClient getClient() {
        return HttpClient.newHttpClient();
    }

}
