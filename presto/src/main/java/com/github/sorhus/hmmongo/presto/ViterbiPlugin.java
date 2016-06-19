package com.github.sorhus.hmmongo.presto;

import com.facebook.presto.metadata.FunctionFactory;
import com.facebook.presto.spi.Plugin;
import com.facebook.presto.spi.type.TypeManager;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.airlift.configuration.ConfigurationLoader;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class ViterbiPlugin implements Plugin {

    private TypeManager typeManager;

    @Inject
    public synchronized void setTypeManager(TypeManager typeManager) {
        this.typeManager = typeManager;
    }

    @Override
    public <T> List<T> getServices(Class<T> type) {
        if(type == FunctionFactory.class) {
            try {
                Map<String, String> properties = new ConfigurationLoader().loadPropertiesFrom("etc/plugin/viterbi.properties");
                ViterbiConfig config = new ViterbiConfig(properties);
                return ImmutableList.of(type.cast(new ViterbiFunctionFactory(typeManager, config)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ImmutableList.of();
    }
}
