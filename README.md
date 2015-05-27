Given a Hidden Markov Model and a bunch of observation sequences, compute the most likely path for each observation sequence. Run it on hadoop. In src/main/resources there is an HMM that models the T-cell receptor.

### Build it
* `mvn clean package`

### Run the regular java app
* `java -cp target/hmmongo-0.1.0.jar com.github.sorhus.hmmongo.DNAViterbiApp src/main/resources/example_pi.gz src/main/resources/example_A.gz src/main/resources/example_B.gz 41 src/main/resources/example_input output`

### Run the Scalding job

###### local mode
* `hadoop jar target/hmmongo-0.1.0.jar com.twitter.scalding.Tool com.github.sorhus.hmmongo.DNAViterbiJob --local --pi src/main/resources/example_pi.gz --A src/main/resources/example_A.gz --B src/main/resources/example_B.gz --T 101 --input src/main/resources/example_input --output outpu`

###### hdfs mode
* If the HMM is big: `export HADOOP_CLIENT_OPT=-Xmx2g`
* Put the input on hdfs
* `hadoop jar target/hmmongo-0.1.0.jar com.twitter.scalding.Tool com.github.sorhus.hmmongo.DNAViterbiJob --hdfs --pi src/main/resources/tcrb_pi.gz --A src/main/resources/tcrb_A.gz --B src/main/resources/tcrb_B.gz --T 101 --input /user/anton/SRR060692_1.sample --output /user/anton/output`
