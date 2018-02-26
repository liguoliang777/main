package com.ngame.api;


import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * Created by Administrator on 2017/12/4.
 */

public class CharSetEncoder extends BufferedOutputStream {
    private final CharsetEncoder m_encoder; // CharsetDecoder（解码器）和CharsetEncoder（编码器）

    public CharSetEncoder(OutputStream out, String charsetName, int size) {
        super(out, size);
        m_encoder = Charset.forName(URLConnectionMgr.sGetCharsetName(charsetName)).newEncoder();
    }

    public final CharSetEncoder Encoder(String data) throws IOException{
        ByteBuffer byteBuffer = m_encoder.encode(CharBuffer.wrap(data));
        super.write(byteBuffer.array(), 0, byteBuffer.limit());
        return this;
    }
}
