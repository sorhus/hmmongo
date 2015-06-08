struct com.github.sorhus.hmmongo.thrift.FullResult {
    1:string input,
    2:string path,
    3:double likelihood
}

service com.github.sorhus.hmmongo.thrift.TCRBViterbiService {
    FullResult apply(1:string input)
}