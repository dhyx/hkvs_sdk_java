package com.hkvs.java.utils;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.IntByReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import com.hkvs.java.ClientDemo.HCNetSDK;

public class cama {
    private static  HCNetSDK hcNetSDK = HCNetSDK.INSTANCE;
    private Logger logger = LoggerFactory.getLogger(cama.class);
    private NativeLong userId;//用户句柄

    /**
     * 抓拍图片
     * @param dvr
     */
    public void getDVRPic(Dvr dvr, int chan, String imgpath) {
        NativeLong chanLong = new NativeLong(chan);
        if(!hcNetSDK.NET_DVR_Init()) {
            logger.warn("hksdk(抓图)-海康sdk初始化失败!");
            return ;
        }
        HCNetSDK.NET_DVR_DEVICEINFO_V30 devinfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();// 设备信息
        //注册设备
        userId = hcNetSDK.NET_DVR_Login_V30(dvr.getIp(),(short)dvr.getPort(),dvr.getName(), dvr.getPassword(), devinfo);// 返回一个用户编号，同时将设备信息写入devinfo
        if (userId.intValue() < 0) {
            logger.warn("hksdk(抓图)-设备注册失败,错误码:"+hcNetSDK.NET_DVR_GetLastError());
            return;
        }
        HCNetSDK.NET_DVR_WORKSTATE_V30 devwork = new HCNetSDK.NET_DVR_WORKSTATE_V30();
        if (!hcNetSDK.NET_DVR_GetDVRWorkState_V30(userId, devwork)) {
            // 返回Boolean值，判断是否获取设备能力
            logger.info("hksdk(抓图)-返回设备状态失败");
        }
        //图片质量
        HCNetSDK.NET_DVR_JPEGPARA jpeg = new HCNetSDK.NET_DVR_JPEGPARA();
        //设置图片分辨率
        jpeg.wPicSize=5;
        //设置图片质量
        jpeg.wPicQuality=0;
        IntByReference a = new IntByReference();
        //设置图片大小
        ByteBuffer jpegBuffer = ByteBuffer.allocate(1024 * 1024);
        //String jpegBuffer ="1024 * 1024";
        File file = new File(imgpath);
        // 抓图到内存，单帧数据捕获并保存成JPEG存放在指定的内存空间中
        //需要加入通道
        boolean is = hcNetSDK.NET_DVR_CaptureJPEGPicture_NEW(userId, chanLong, jpeg, jpegBuffer, 1024 * 1024, a);
        if(is) {
            logger.info("hksdk(抓图)-结果状态值(0表示成功):"+hcNetSDK.NET_DVR_GetLastError());
            //存储到本地
            BufferedOutputStream outputStream = null;
            try {
                outputStream = new BufferedOutputStream(new FileOutputStream(file));
                outputStream.write(jpegBuffer.array(),0,a.getValue());
                outputStream.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                if(outputStream!=null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            logger.info("hksdk(抓图)-抓取失败,错误码:"+hcNetSDK.NET_DVR_GetLastError());
        }

        hcNetSDK.NET_DVR_Logout(userId);//退出登录
        //hcNetSDK.NET_DVR_Cleanup();
    }
    public static void main(String[] args) {
        cama camera = new cama();
        Dvr dvr = new Dvr();
        dvr.setNavalA(0);
        dvr.setIp("192.168.1.64");
        dvr.setPort(8000);
        dvr.setName("admin");
        dvr.setPassword("hyperai123");
        dvr.setNavalB(0);
        String imgpath ="E:\\test.jpg";
        int channel = 1;//通道号
        camera.getDVRPic(dvr,channel,imgpath);
    }
}
