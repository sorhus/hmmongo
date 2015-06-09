Given a Hidden Markov Model and a bunch of observation sequences, compute the most likely path for each observation sequence. 

The project consists of a few different parts. A generic library and some applications. The applicatinos are:

* A standalone scala app
* A hadoop job
* A http server
* A thrift server

In libhmm/src/main/resources there is an HMM that models the T-cell receptor.

### Build it
* `mvn clean package`

### Run the regular scala app
* `scala -cp libhmm/target/libhmm-0.2.0.jar com.github.sorhus.hmmongo.DNAViterbiApp /example_pi.gz /example_A.gz /example_B.gz 41 libhmm/src/main/resources/example_input output`

### Run the http server
* `java -jar scalatra/target/scalatra-0.2.0.jar /example_pi.gz /example_A.gz /example_B.gz 101`
* `curl localhost:8080/acgttgcatcgatcgatcgatcgatcgtacgatcgatcgaacgatgcgactaca`

### Run the thrift server
* `java -jar thrift/target/thrift-0.2.0.jar /example_pi.gz /example_A.gz /example_B.gz 101`
* There is an example client that can be tested as follows
* `java -cp thrift/target/thrift-0.2.0.jar com.github.sorhus.hmmongo.thrift.client.DNAViterbiClient`

The following documentation is for version 0.1.0.

### Run the Scalding job

###### local mode
* `hadoop jar hadoop/target/hadoop-0.2.0.jar com.twitter.scalding.Tool com.github.sorhus.hmmongo.DNAViterbiJob --local --pi hadoop/src/main/resources/example_pi.gz --A hadoop/src/main/resources/example_A.gz --B hadoop/src/main/resources/example_B.gz --T 101 --input hadoop/src/main/resources/example_input --output output`

###### hdfs mode
* If the HMM is big: `export HADOOP_CLIENT_OPT=-Xmx2g`
* Put the input on hdfs
* `hadoop jar hadoop/target/hadoop-0.2.0.jar com.twitter.scalding.Tool com.github.sorhus.hmmongo.DNAViterbiJob --hdfs --pi src/main/resources/tcrb_pi.gz --A src/main/resources/tcrb_A.gz --B src/main/resources/tcrb_B.gz --T 101 --input /user/anton/SRR060692_1.sample --output /user/anton/output`
