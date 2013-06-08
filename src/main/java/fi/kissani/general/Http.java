package fi.kissani.general;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

public class Http {
    public static String fetch(String string) throws HttpException, IOException {
        HttpClient restClient = new HttpClient();
        GetMethod method = new GetMethod(string);
        int result = restClient.executeMethod(method);
        if (result == 200) {
            return method.getResponseBodyAsString();
        }
        return null;
    }

}
