package com.mycompany.app;

public class TextContent {
    private StringBuilder text;
    
    TextContent() {
    	text = new StringBuilder();
    }
    
    public String getText(){
        return text.toString();
    }
    

    public void append(String content){
        text.append(content);
    }
}
