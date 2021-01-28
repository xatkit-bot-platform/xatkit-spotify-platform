package com.xatkit.plugins.wikipedia.platform;

import com.xatkit.core.XatkitBot;
import com.xatkit.core.platform.RuntimePlatform;
import com.xatkit.core.platform.action.RuntimeActionResult;
import com.xatkit.execution.StateContext;
import com.xatkit.plugins.wikipedia.platform.action.GetInfo;
import lombok.NonNull;
import org.apache.commons.configuration2.Configuration;

import static fr.inria.atlanmod.commons.Preconditions.checkArgument;

/**
 * A {@link RuntimePlatform} class that connects and interacts with the Wikipedia API.
 */
public class WikipediaPlatform extends RuntimePlatform {
    /**
     * {@inheritDoc}
     * <p>
     * This method checks that the provided {@code configuration} contains a Wikipedia API token.
     *
     * @throws IllegalArgumentException if the provided {@code configuration} does not contain a Wikipedia API token
     */
    @Override
    public void start(XatkitBot xatkitBot, Configuration configuration) {
        super.start(xatkitBot, configuration);
    }

    /**
     * Returns the URL of the first URL returned by the Wikipedia API and matching the provided {@code searchString}.
     *
     * @param context      the current {@link StateContext}
     * @param searchString the query to send to the Wikipedia API
     * @return the URL of the Wikipedia entry
     */
    public @NonNull String getInfo(@NonNull StateContext context, @NonNull String searchString) {
        GetInfo action = new GetInfo(this, context, searchString);
        RuntimeActionResult result = action.call();
        return (String) result.getResult();
    }
}
