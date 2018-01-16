package cn.ngame.store.core.fileload;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import net.lingala.zip4j.exception.ZipException;

import java.io.File;

import cn.ngame.store.core.utils.AppInstallHelper;
import cn.ngame.store.core.utils.DialogHelper;
import cn.ngame.store.exception.NoSDCardException;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.CompressUtil;
import cn.ngame.store.core.utils.FileUtil;

/**
 * 执行解压缩任务
 * Created by zeng on 2016/7/20.
 */
public class UnZipFileTask implements Runnable{

    private static final String TAG = UnZipFileTask.class.getSimpleName();
    private FileLoadInfo fileInfo;
    private Context context;
    private FragmentManager fm;
    private Handler handler;

    public UnZipFileTask(FragmentManager fm,Context context, FileLoadInfo fileInfo,Handler handler) {
        this.fm = fm;
        this.context = context;
        this.fileInfo = fileInfo;
        this.handler = handler;
    }

    @Override
    public void run() {

        if(fileInfo != null){

            try {
                String apkName = fileInfo.getName();
                if(apkName != null && apkName.endsWith(".ngk")){

                    String fileName = apkName.split("\\.")[0];

                    File file = new File(CommonUtil.getFileLoadBasePath(),apkName);
                    File newFile;
                    if(file.exists() && file.isFile()){
                        newFile = new File(CommonUtil.getFileLoadBasePath(),fileName+".zip");
                        file.renameTo(newFile);
                    }else {
                        newFile = new File(CommonUtil.getFileLoadBasePath(),fileName+".zip");
                    }

                    if(newFile.exists() && newFile.isFile()){

                        String dest = newFile.getParentFile().getPath()+File.separator+fileName;

                        File[] files = CompressUtil.unzip(newFile,dest,""); //解压文件
                        /*for(File f : files){
                            if(f.exists() && f.isFile() && f.getName().endsWith(".apk")){
                                Log.d(TAG,f.getAbsolutePath());
                            }
                        }*/

                        //1.将制定文件夹拷贝到指定目录
                        String targetPath = CommonUtil.getSystemAndroidPath();
                        FileUtil.copyDirectory(dest + File.separator+"Android",targetPath);

                        //2.调用安装方法，安装APK文件
                        for(File f : files){
                            if(f.exists() && f.isFile() && f.getName().endsWith(".apk")){
                                DialogHelper.hideWaiting(fm);
                                AppInstallHelper.installApk(context, f);
                            }
                        }
                    }else{
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context,"安装文件丢失，删除记录重新下载",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            } catch (ZipException e) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        DialogHelper.hideWaiting(fm);
                        Toast.makeText(context,"文件损坏无法解压，删除记录重新下载",Toast.LENGTH_LONG).show();
                    }
                });

                e.printStackTrace();
            } catch (NoSDCardException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        DialogHelper.hideWaiting(fm);
                        Toast.makeText(context,"解压失败，请检查SD卡是否异常",Toast.LENGTH_LONG).show();
                    }
                });
                e.printStackTrace();
            }
        }
    }
}
