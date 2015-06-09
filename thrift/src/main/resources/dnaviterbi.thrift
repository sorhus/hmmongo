struct FullResult {
    1:string input,
    2:string path,
    3:double likelihood
}

service DNAViterbiService {
    FullResult apply(1:string input)
}