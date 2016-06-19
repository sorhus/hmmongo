package com.github.sorhus.hmmongo.presto;

import com.facebook.presto.metadata.FunctionFactory;
import com.facebook.presto.metadata.FunctionListBuilder;
import com.facebook.presto.metadata.SqlFunction;
import com.facebook.presto.spi.type.TypeManager;

import java.util.List;

public class ViterbiFunctionFactory implements FunctionFactory {

    private final TypeManager typeManager;
    private final ViterbiConfig viterbiConfig;

    public ViterbiFunctionFactory(TypeManager typeManager, ViterbiConfig viterbiConfig) {
        this.typeManager = typeManager;
        this.viterbiConfig = viterbiConfig;
    }

    @Override
    public List<SqlFunction> listFunctions() {

        ViterbiFunction.setConfig(viterbiConfig);

        return new FunctionListBuilder(typeManager)
            .scalar(ViterbiFunction.class)
            .getFunctions();
    }
}
