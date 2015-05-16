package com.github.sorhus.hmmongo;

import java.io.Serializable;

public class DNAEncoder implements Serializable {

    public static int encode(char ch) {
        if(ch == 'a' || ch == 'A') {
            return 0;
        } else if(ch == 'c' || ch == 'C') {
            return 1;
        } else if(ch == 'g' || ch == 'G') {
            return 2;
        } else if(ch == 't' || ch == 'T') {
            return 3;
        } else {
            throw new RuntimeException("Corrupt char: " + ch);
        }
    }
}
