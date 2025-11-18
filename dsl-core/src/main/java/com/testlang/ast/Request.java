package com.testlang.ast;

public class Request {
    private String method;
    private String path;
    private RequestBlock block;

    public Request(String method, String path, RequestBlock block) {
        this.method = method;
        this.path = path;
        this.block = block;
    }

    public String getMethod() { return method; }
    public String getPath() { return path; }
    public RequestBlock getBlock() { return block; }
}