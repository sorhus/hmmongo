Given a Hidden Markov Model and a bunch of observation sequences, compute the most likely path for each observation sequence. 

The project consists of a few different parts. A generic library and some applications. The applications are:

* A standalone scala app
* A hadoop job
* A http server
* A thrift server

[Javadocs for the library can be found here](http://sorhus.github.io/hmmongo)

Typical usage of the library looks like this.
```
// args = {pi, A, B, T, input, output};
Function<String, InputStream> r = DNAViterbiApp.class::getResourceAsStream;
HMM hmm = new HMM.Builder()
    .fromInputStreams(r.apply(args[0]), r.apply(args[1]), r.apply(args[2]))
    .adjacency()
    .build();
Viterbi<String,FullResult> viterbi =
    new Viterbi.Builder<String,String,FullResult>()
    .withHMM(hmm)
    .withMaxObservationLength(Integer.parseInt(args[3]))
    .withObservationEncoder(new DNAEncoder())
    .withObservationDecoder(new DNADecoder())
    .withPathDecoder(new StringDecoder())
    .withResultFactoryClass("com.github.sorhus.hmmongo.viterbi.result.FullResultFactory")
    .build();
```

The library is written in Java 8.
Note that the `DNAEncoder`, `DNADecoder` and `StringDecoder` requires scala 2.11 to run.

Also, in libhmm/src/main/resources there is an HMM that models the T-cell receptor.

### Build the project
* `mvn clean package`

### Run the scala app
* `scala -cp libhmm/target/libhmm-0.3.0.jar com.github.sorhus.hmmongo.DNAViterbiApp /example_pi.gz /example_A.gz /example_B.gz 41 libhmm/src/main/resources/example_input output`

### Run the http server
* `java -jar scalatra/target/scalatra-0.3.0.jar /example_pi.gz /example_A.gz /example_B.gz 101`
* `curl localhost:8080/acgttgcatcgatcgatcgatcgatcgtacgatcgatcgaacgatgcgactaca`

### Run the thrift server
* `java -jar thrift/target/thrift-0.3.0.jar /example_pi.gz /example_A.gz /example_B.gz 101`
* There is an example client that can be tested as follows
* `java -cp thrift/target/thrift-0.3.0.jar com.github.sorhus.hmmongo.thrift.client.DNAViterbiClient`

### Run the hadoop job

###### local mode
* `hadoop jar hadoop/target/hadoop-0.3.0.jar com.twitter.scalding.Tool com.github.sorhus.hmmongo.DNAViterbiJob --local --pi hadoop/src/main/resources/example_pi.gz --A hadoop/src/main/resources/example_A.gz --B hadoop/src/main/resources/example_B.gz --T 101 --input hadoop/src/main/resources/example_input --output output`

###### hdfs mode
* If the HMM is big: `export HADOOP_CLIENT_OPT=-Xmx2g`
* Put the input on hdfs
* `hadoop jar hadoop/target/hadoop-0.3.0.jar com.twitter.scalding.Tool com.github.sorhus.hmmongo.DNAViterbiJob --hdfs --pi src/main/resources/tcrb_pi.gz --A src/main/resources/tcrb_A.gz --B src/main/resources/tcrb_B.gz --T 101 --input /user/anton/SRR060692_1.sample --output /user/anton/output`
