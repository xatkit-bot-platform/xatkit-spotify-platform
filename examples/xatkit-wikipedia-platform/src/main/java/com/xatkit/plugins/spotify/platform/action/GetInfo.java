package com.xatkit.plugins.wikipedia.platform.action;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequest;
import com.xatkit.core.platform.action.RuntimeAction;
import com.xatkit.execution.StateContext;
import com.xatkit.plugins.wikipedia.platform.WikipediaPlatform;
import lombok.NonNull;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

/**
 * A {@link RuntimeAction} that retrieves info from Wikipedia from a given search string.
 * <p>
 * This class relies on the {@link WikipediaPlatform} to access the Wikipedia API using the token stored in the
 * {@link org.apache.commons.configuration2.Configuration}.
 */
public class GetInfo extends RuntimeAction<WikipediaPlatform> {

    /**
     * The Wikipedia REST endpoint to retrieve info from Wikipedia from search strings.
     */
    private static String SEARCH_URL = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=info&generator=allpages&inprop=url&gaplimit=1";

    /**
     * The {@link String} used to search info from Wikipedia.
     */
    private String searchString;

    /**
     * Constructs a new {@link GetInfo} with the provided {@code platform}, {@code context}, and {@code searchString}.
     * 
     * @param platform     the {@link WikipediaPlatform} containing this action
     * @param context      the {@link StateContext} associated to this action
     * @param searchString the {@link String} used to search info from Wikipedia
     * @throws NullPointerException if the provided {@code runtimePlatform}, {@code session}, or {@code searchString}
     *                              is {@code null}
     * 
     */
    public GetInfo(@NonNull WikipediaPlatform platform, @NonNull StateContext context, @NonNull String searchString) {
        super(platform, context);
        this.searchString = searchString;
    }

    private static String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    @Override
    protected Object compute() throws Exception {
        HttpRequest request = Unirest.get(SEARCH_URL+"&gapfrom=" + encodeValue(this.searchString))
                .header("accept", "application/json")
                .header("content-Type", "application/json");
        JsonNode jsonNode = request.asJson().getBody();       

        String key = jsonNode.getObject().getJSONObject("query").getJSONObject("pages").keySet().iterator().next().toString();
        return jsonNode.getObject().getJSONObject("query").getJSONObject("pages").getJSONObject(key).getString("fullurl");

    }
}

