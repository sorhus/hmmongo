package com.github.sorhus.hmmongo.presto;

import com.facebook.presto.operator.Description;
import com.facebook.presto.operator.scalar.ScalarFunction;
import com.facebook.presto.spi.type.StandardTypes;
import com.facebook.presto.sql.tree.Row;
import com.facebook.presto.type.RowType;
import com.facebook.presto.type.SqlType;
import com.github.sorhus.hmmongo.hmm.HMM;
import com.github.sorhus.hmmongo.viterbi.DNADecoder;
import com.github.sorhus.hmmongo.viterbi.DNAEncoder;
import com.github.sorhus.hmmongo.viterbi.StringDecoder;
import com.github.sorhus.hmmongo.viterbi.Viterbi;
import com.github.sorhus.hmmongo.viterbi.result.FullResult;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.airlift.slice.Slice;

import static io.airlift.slice.Slices.utf8Slice;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class ViterbiFunction {

    private static ViterbiConfig config;
    private final static Logger log = Logger.getLogger(ViterbiFunction.class.getName());

    public static void setConfig(ViterbiConfig config) {
        ViterbiFunction.config = config;
    }

    private static final LoadingCache<String, Viterbi<String, FullResult<String, String>>> cache = CacheBuilder.newBuilder().maximumSize(2)
        .build(new CacheLoader<String, Viterbi<String, FullResult<String, String>>>() {
            @Override
            public Viterbi<String, FullResult<String, String>> load(String name) throws Exception {

                log.info("Loading hmm");

                String pi = config.getPi();
                String A = config.getA();
                String B = config.getB();

                HMM hmm = new HMM.Builder()
                        .fromFiles(pi, A, B)
                        .adjacency()
                        .build();

                log. info("Instantiating viterbi implementation");

                return new Viterbi.Builder<String, String, FullResult<String, String>>()
                        .withHMM(hmm)
                        .withMaxObservationLength(config.getT())
                        .withObservationEncoder(new DNAEncoder())
                        .withObservationDecoder(new DNADecoder())
                        .withPathDecoder(new StringDecoder())
                        .withResultFactoryClass("com.github.sorhus.hmmongo.viterbi.result.FullResultFactory")
                        .withThreadSafety()
                        .build();
            }
        });

    @ScalarFunction("viterbi")
    @Description("Returns the most probable path")
    @SqlType(StandardTypes.VARCHAR)
    public static Slice viterbi(@SqlType(StandardTypes.VARCHAR) Slice implName, @SqlType(StandardTypes.VARCHAR) Slice input) throws ExecutionException {
        Viterbi<String, FullResult<String, String>> impl = cache.get(implName.toString());
        FullResult<String, String> result = impl.apply(input.toStringUtf8());
        return utf8Slice(result.path);
    }

}
