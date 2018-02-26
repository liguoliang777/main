package com.ngame.api;

import android.icu.util.Output;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Created by Administrator on 2017/12/4.
 */

public class URLConnectionMgr {
    private HttpURLConnection m_httpUrlConnection;
    private final URL m_url;
    private final String m_strMethod;
    private int m_nSize;
    private String m_strHostName;
    private int m_nPort;

    class RequestCallable implements Callable<URLConnectionMgr>{
        InputStream m_inputStream;
        OutputStream m_outputStream;
        private final BufferedInputStream bufferedInputStream;
        private final boolean bTry;

        RequestCallable(BufferedInputStream bufferedInputStream, boolean bTry, InputStream input, OutputStream output){
            super();
            this.bufferedInputStream = bufferedInputStream;
            this.bTry = bTry;
            m_inputStream = input;
            m_outputStream = output;
        }

        public final URLConnectionMgr writeData() throws IOException {
            byte[] buf = new byte[m_nSize];
            while(true){
                int nRead = m_inputStream.read(buf);
                if(nRead == -1){
                    break;
                }
                m_outputStream.write(buf, 0, nRead);
            }
            return URLConnectionMgr.this;
        }

        protected final void close() throws IOException {
            if(bufferedInputStream instanceof Flushable){
                ((Flushable) bufferedInputStream).flush();
            }

            if(bTry){
                try {
                    bufferedInputStream.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }else{
                bufferedInputStream.close();
            }
        }

        @Override
        public URLConnectionMgr call() throws Exception {
            URLConnectionMgr result = null;
            try{
                result = writeData();
                close();
            }catch (IOException e){
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            return result;
        }
    }

    private URLConnectionMgr(CharSequence charSequence, String method){
        super();
        m_httpUrlConnection = null;
        m_nSize = 0x2000;
        try {
            m_url = new URL(charSequence.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        m_strMethod = method;
    }

    private HttpURLConnection makeURLConnection(){
        try{
            HttpURLConnection httpURLConnection = (m_strHostName != null) ?
                    (HttpURLConnection)m_url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(m_strHostName, m_nPort)))
                    : (HttpURLConnection)m_url.openConnection();
            httpURLConnection.setRequestMethod(m_strMethod);
            return httpURLConnection;
        }catch(IOException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private HttpURLConnection getHttpUrlConnection(){
        if(m_httpUrlConnection == null){
            m_httpUrlConnection = makeURLConnection();
        }
        return m_httpUrlConnection;
    }

    static String sGetCharsetName(String charsetName){
        if(charsetName == null || charsetName.isEmpty()){
            charsetName = "UTF-8";
        }
        return charsetName;
    }

    public static URLConnectionMgr sGET(CharSequence charSequence){
        return new URLConnectionMgr(charSequence, "GET");
    }

    public final URLConnectionMgr setRequestProperty(String key, String value){
        getHttpUrlConnection().setRequestProperty(key, value);
        return this;
    }

    public final int getResposeCode(){
        try{
            return getHttpUrlConnection().getResponseCode();
        }catch(IOException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private InputStream getInputStream(){
        InputStream result = null;
        if(getResposeCode() < 400){
            try{
                result = getHttpUrlConnection().getInputStream();
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }else{
            result = getHttpUrlConnection().getErrorStream();
            if(result == null){
                try{
                    result = getHttpUrlConnection().getInputStream();
                }catch (IOException e){
                    if(getContentLength() > 0){
                        throw new RuntimeException(e);
                    }
                    result = new ByteArrayInputStream(new byte[0]);
                }
            }
        }
        return result;
    }

    private int getContentLength(){
        return getHttpUrlConnection().getHeaderFieldInt("Content-Length", -1);
    }

    private String request(String type){
        int fieldLen = getContentLength();
        ByteArrayOutputStream out = fieldLen > 0 ? new ByteArrayOutputStream(fieldLen) : new ByteArrayOutputStream();
        try{
            BufferedInputStream in = new BufferedInputStream( getInputStream(), m_nSize);
            new RequestCallable(in, false, in, out).call();
            return out.toString(sGetCharsetName(type));
        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public final String requestData(){
        return request(codingType(getHeaderField("Content-Type"), "charset"));
    }

    private String getHeaderField(String name){
        return getHttpUrlConnection().getHeaderField(name);
    }

    private static String codingType(String field, String type){
        String result = null;
        int nQuoteChar = 34;        //"
        int nSemicolonChar = 59;    //;
        int nEqualChar = 61;        //=
        int nTmp;

        if(field == null || field.isEmpty()){
            result = null;
        }else{
            int nLen = field.length();
            int nIndex = field.indexOf(nSemicolonChar) + 1;
            if(nIndex != 0 && nIndex != nLen){
                int nIndex2 = field.indexOf(nSemicolonChar, nIndex);
                if(nIndex2 == -1){
                    nIndex2 = nIndex;
                    nIndex = nLen;
                }else{
                    nTmp = nIndex2;
                    nIndex2 = nIndex;
                    nIndex = nTmp;
                }

                while(nIndex2 < nIndex){
                    int nEqualIndex = field.indexOf(nEqualChar, nIndex2);
                    if(nEqualIndex != -1 && nEqualIndex < nIndex && (type.equals(field.substring(nIndex2, nEqualIndex).trim()))){
                        result = field.substring(nEqualIndex + 1, nIndex).trim();
                        int nResultLen = result.length();
                        if(nResultLen != 0){
                            if(nResultLen <= 2){
                            }else if(nQuoteChar != result.charAt(0)){
                            }else if(nQuoteChar == result.charAt(nResultLen - 1)){
                                result = result.substring(1, nResultLen - 1);
                            }else{
                            }
                            return result;
                        }
                    }

                    nIndex++;
                    nIndex2 = field.indexOf(nSemicolonChar, nIndex);
                    if(nIndex2 == -1){
                        nIndex2 = nLen;
                    }

                    nTmp = nIndex2;
                    nIndex2 = nIndex;
                    nIndex = nTmp;
                }
                return null;
            }
            result = null;
        }


        return result;
    }
}
