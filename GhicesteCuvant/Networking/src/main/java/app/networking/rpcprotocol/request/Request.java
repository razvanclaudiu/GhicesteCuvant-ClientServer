package app.networking.rpcprotocol.request;

import java.io.Serializable;

public class Request implements Serializable {

    private RequestType type;
    private Object data;

    public RequestType type(){
        return type;
    }

    public Object data(){
        return data;
    }

    public void data(Object data){
        this.data = data;
    }

    public void type(RequestType type){
        this.type = type;
    }

    public String toString(){
        return "Request{" + "type=" + type + ", data=" + data + '}';
    }

    public static class Builder{
        private Request request = new Request();

        public Builder type(RequestType type){
            request.type = type;
            return this;
        }

        public Builder data(Object data){
            request.data = data;
            return this;
        }

        public Request build(){
            return request;
        }
    }


}
