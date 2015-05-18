Given a Hidden Markov Model and a bunch of observation sequences, compute the most likely path for each observation sequence. Run it on hadoop. In src/main/resources there is an HMM that models the T-cell receptor.

Here's how to run it

* `mvn clean package`
* `export HADOOP_CLIENT_OPT=-Xmx2g`

* local mode:
`hadoop jar target/hmmongo-<version>.jar com.twitter.scalding.Tool com.github.sorhus.hmmongo.ViterbiJob --local --pi src/main/resources/tcrb_pi.gz --A src/main/resources/tcrb_A.gz --B src/main/resources/tcrb_B.gz --T 101 --input src/main/resources/SRR060692_1.sample --output output`

* hdfs mode:
`hadoop jar target/hmmongo-<version>.jar com.twitter.scalding.Tool com.github.sorhus.hmmongo.ViterbiJob --hdfs --pi src/main/resources/tcrb_pi.gz --A src/main/resources/tcrb_A.gz --B src/main/resources/tcrb_B.gz --T 101 --input /user/anton/SRR060692_1.sample --output /user/anton/output`
