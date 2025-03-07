package edu.uob;

import java.util.ArrayList;

public class TokenBank {
    ArrayList<String> tokens = new ArrayList<String>();
    int currentToken = 0;

    void setTokens(ArrayList<String> tokens) {
        this.tokens = tokens;
    }

    String getNextToken() {
        return tokens.get(currentToken + 1);
    }

    String getCurrentToken() {
        return tokens.get(currentToken);
    }

    void nextToken() {
        currentToken++;
    }

    ArrayList<String> getTokens() {
        return tokens;
    }

//    ArrayList<String> tokenArray = new ArrayList<>();

//    Tokenizer (String query) {
//        tokenArray.addAll(Arrays.asList(query.split(" ")));
////        remove semicolon ';' at the end
//        tokenArray.remove(tokenArray.size() - 1);
////        System.out.println(tokenArray);
//    }

//    String query = "  INSERT  INTO  people   VALUES(  'Simon Lock'  ,35, 'simon@bristol.ac.uk' , 1.8  ) ; ";

//    public static void main(String [] args) {
//        Test t = new Test();
//        t.setup();
//    }

}
