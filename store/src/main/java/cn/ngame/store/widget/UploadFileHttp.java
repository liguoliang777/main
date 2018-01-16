package cn.ngame.store.widget;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import cn.ngame.store.bean.UpLoadBean;
import cn.ngame.store.core.utils.FileUtil;

/**
 * Created by jztdzsw on 15-8-24.
 */
public class UploadFileHttp {


    public static UploadFileHttp INSTANCE = new UploadFileHttp();

    private UploadFileHttp() {
    }

    // HttpURLConnection 上传文件
    public UpLoadBean uploadFile(String url, Map<String, File> files) throws IOException {
        boolean isZip = true;
        URL urls = new URL(url + "?" + Math.random());
        HttpURLConnection hc = null;
        try {
            hc = (HttpURLConnection) urls.openConnection();
            String PREFIX = "--", LINEND = "\r\n";
            String CHARSET = "UTF-8";
            String BOUNDARY = java.util.UUID.randomUUID().toString();
            String MULTIPART_FROM_DATA = "multipart/form-data";
            hc.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);
//            IdentityBean identityBean = PreferenceHelper.load(JztApplication.getInstance(), IdentityBean.class);// 取缓存
//            hc.setRequestProperty("Cookie",
//                    "immune=immune;jzt_principal="
//                            + identityBean.getPrincipal());
            // 设定请求的方法为"POST"，默认是GET
            hc.setRequestMethod("POST");
            // 设置连接主机超时（单位：毫秒）
            hc.setConnectTimeout(60000);
            // 设置从主机读取数据超时（单位：毫秒）
            hc.setReadTimeout(60000);
            // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true, 默认情况下是false
            hc.setDoInput(true);
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            hc.setDoOutput(true);
            // Post 请求不能使用缓存
            hc.setUseCaches(false);
            hc.setInstanceFollowRedirects(true);
            hc.setRequestProperty("Charset", "UTF-8");

            DataOutputStream outStream = null;
            try {
                outStream = new DataOutputStream(hc.getOutputStream());
                StringBuilder sb = new StringBuilder();
                // 所有的参数
//                for (Map.Entry<String, String> entry : parmas.entrySet()) {
//                    sb.append(PREFIX);
//                    sb.append(BOUNDARY);
//                    sb.append(LINEND);
//                    sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
//                    sb.append("Content-Type: text/plain; charset=" + "utf-8" + LINEND);
//                    sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
//                    sb.append(LINEND);
//                    sb.append(entry.getValue());
//                    if (entry.toString().indexOf("fileType=2") != -1)
//                        isZip = false;
//                    sb.append(LINEND);
//                }
//                outStream.write(sb.toString().getBytes());

                for (Map.Entry<String, File> entry : files.entrySet()) {

                    StringBuilder sb1 = new StringBuilder();
                    sb1.append(PREFIX);
                    sb1.append(BOUNDARY);
                    sb1.append(LINEND);
                    sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + entry.getValue().getName()
                            + "\"" + LINEND);
                    sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
                    sb1.append(LINEND);
                    outStream.write(sb1.toString().getBytes());

                    // RandomAccessFile oSavedFile = null;
                    ByteArrayOutputStream baos = null;
                    try {
                        // 压缩图片
                        if (isZip) {
                            String path = entry.getValue().getPath();
                            baos = FileUtil.getZipFiles(path);
                        } else {
                            File file = new File(entry.getValue().getPath());
                            FileInputStream fis = new FileInputStream(file);
                            baos = new ByteArrayOutputStream();
                            byte[] b = new byte[1024];
                            int n;
                            while ((n = fis.read(b)) != -1) {
                                baos.write(b, 0, n);
                            }
                        }
                        if (baos != null && baos.toByteArray().length > 0) {
                            outStream.write(baos.toByteArray());
                        }

                    } finally {
                        FileUtil.closeIO(baos);
                        outStream.write(LINEND.getBytes());
                    }
                }
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
                outStream.write(end_data);
                outStream.flush();

            } finally {
                FileUtil.closeIO(outStream);
            }
            int rc = 0;
            rc = hc.getResponseCode();
            if (rc != HttpURLConnection.HTTP_OK) {
                // 请求失败
            } else {
                InputStreamReader reader = null;
                try {
                    reader = new InputStreamReader(hc.getInputStream());
                    Gson gson = new Gson();
                    UpLoadBean res = gson.fromJson(convertStreamToString(hc.getInputStream()), UpLoadBean.class);
                    return res;
                } finally {
                    FileUtil.closeIO(reader);
                }
            }

        } finally {
            // 关闭网络连接
            if (hc != null) {
                hc.disconnect();
            }

        }
        return null;
    }

    public String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StringBuilder sb = new StringBuilder();
        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + " ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
