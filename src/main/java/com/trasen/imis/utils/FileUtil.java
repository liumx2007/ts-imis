package com.trasen.imis.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * 提供一些文件操作方法.
 * <p>
 *
 */
public class FileUtil {

	/** 文件路径分隔符 */
	public final static String FILE_SEPARATOR = File.separator;
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
	/**
	 * 读取文件内容.
	 *
	 * @param name
	 *            文件路径.
	 * @return 文件文本内容
	 * @throws java.io.IOException
	 */
	public static String readFile(String name) throws IOException {
		StringBuffer sb = new StringBuffer();
		BufferedReader in = new BufferedReader(new FileReader(name));
		String s;
		int i = 0;
		while ((s = in.readLine()) != null) {
			if(i++ > 0) {
				sb.append("\n");
			}
			sb.append(s);
		}
		in.close();
		return sb.toString();
	}

	/**
	 * 读取文件内容.
	 *
	 * @param name
	 *            文件路径.
	 * @return 文件文本内容
	 * @throws java.io.IOException
	 */
	public static Set<String> readFileList(String name) throws IOException {
		Set<String> set = Sets.newHashSet();
		BufferedReader in = new BufferedReader(new FileReader(name));
		String s;
		while ((s = in.readLine()) != null) {
			set.add(s);
		}
		in.close();
		return set;
	}

	/**
	 * 读取文件内容.
	 *
	 * @param file
	 *            文件对象
	 * @return 文件字节数组.
	 * @throws java.io.IOException
	 * @see #readFileStream(java.io.InputStream in)
	 */
	public static byte[] readFile(File file) throws IOException {
		byte[] b = null;
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			b = readFileStream(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw e;
				}
				in = null;
			}
		}
		return b;
	}

	/**
	 * 读取文件流内容.
	 *
	 * @param in
	 *            文件输入流.
	 * @return 文件内容.
	 * @throws java.io.IOException
	 */
	public static byte[] readFileStream(InputStream in) throws IOException {
		int size = in.available();
		byte[] bytes = new byte[size];
		// 读取文件
		int offset = 0;
		int numRead = 0;
		while (offset < size) {
			numRead = in.read(bytes, offset, size - offset);
			if (numRead >= 0) {
				offset += numRead;
			} else {
				break;
			}
		}
		in.close();
		// 确认所有的文件已读入.
		if (offset < bytes.length) {
			throw new IOException("Could not read requested " + size
					+ " bytes from input stream");
		}
		return bytes;
	}


    public  static byte[] getByteArray(InputStream iin) throws IOException {
        BufferedInputStream in = new BufferedInputStream(iin);
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        byte[] temp = new byte[1024];
        int size = 0;
        while ((size = in.read(temp)) != -1) {
            out.write(temp, 0, size);
        }
        byte[] content = out.toByteArray();
        out.close();
        in.close();
        return content;
    }




	/**
	 * 写文件. 对文件写入新的内容writemod的方式有2种 1：如果文件存在，新的内容追加到文件。否则创建一个新文件 2:
	 * 如果文件存在，覆盖旧文件的内容。否则创建一个新文件
	 *
	 * @param fileName
	 *            文件路径
	 * @param strInputContent
	 *            文件内容
	 * @param writemod
	 *            写入方式：1 - 追加 2 - 覆盖
	 * @throws java.io.IOException
	 */
	public static boolean writeFile(String fileName, String strInputContent,
                                    int writemod) throws IOException {
		File fso = new File(fileName);
		String strContent = "";
		BufferedWriter writer = new BufferedWriter(new FileWriter(fso));
		switch (writemod) {
		case 1: // 最加新的内容到文件
			String oldContent = "";
			oldContent = readFile(fileName); // 先读取原文件内容.
			strContent = oldContent + "\n" + strInputContent;
			break;
		case 2: // 新内容覆盖旧文件的内容
			strContent = strInputContent;
			break;
		default:
			strContent = strInputContent;
			break;
		}
		writer.write(strContent);
		writer.close();
		return true;

	}

	/**
	 * 创建单个文件夹.
	 *
	 * @param folderPath
	 *            文件夹路径.
	 * @return
	 * @throws java.io.IOException
	 */
	public static String createFolder(String folderPath) throws IOException {
		String txt = folderPath;
		File myFilePath = new File(txt);
		txt = folderPath;
		if (!myFilePath.exists()) {
			myFilePath.mkdir();
		}

		return txt;
	}

	/**
	 * 创建多级文件夹.
	 *
	 * @param folderPath
	 *            准备要在本级目录下创建新目录的目录路径 例如 c:\myf
	 * @param paths
	 *            无限级目录参数，各级目录以单数线区分 例如 a|b|c ,执行完成后为c:\myf\a\b\c
	 * @return 创建后的完整路径
	 * @throws java.io.IOException
	 */
	public static String createFolders(String folderPath, String paths)
			throws IOException {
		String txts = folderPath;
		String txt;
		txts = folderPath;
		StringTokenizer st = new StringTokenizer(paths, "|");
		for (; st.hasMoreTokens();) {
			txt = st.nextToken().trim();
			createFolder(txts + FILE_SEPARATOR + txt);
		}

		return txts;
	}

	/**
	 * 创建一个新文件.
	 *
	 * @param filePathAndName
	 *            文件完整绝对路径及文件名
	 * @param fileContent
	 *            文件内容
	 * @throws java.io.IOException
	 */
	public static void createFile(String filePathAndName, String fileContent)
			throws IOException {
		String filePath = filePathAndName;
		filePath = filePath.toString();
		File myFilePath = new File(filePath);
		if (!myFilePath.exists()) {
			myFilePath.createNewFile();
		}
		FileWriter resultFile = new FileWriter(myFilePath);
		PrintWriter myFile = new PrintWriter(resultFile);
		String strContent = fileContent;
		myFile.println(strContent);
		myFile.close();
		resultFile.close();

	}

	/**
	 * 创建一个含编码格式的文件.
	 *
	 * @param filePathAndName
	 *            文件完整绝对路径及文件名
	 * @param fileContent
	 *            文件内容
	 * @param encoding
	 *            文件编码格式,如GB2312,UTF-8
	 * @throws java.io.IOException
	 */
	public static void createFile(String filePathAndName, String fileContent,
                                  String encoding) throws IOException {
		String filePath = filePathAndName;
		filePath = filePath.toString();
		File myFilePath = new File(filePath);
		if (!myFilePath.exists()) {
			myFilePath.createNewFile();
		}
		PrintWriter myFile = new PrintWriter(myFilePath, encoding);
		if (org.apache.commons.lang3.StringUtils.isNotEmpty(fileContent)){
			String strContent = fileContent;
			myFile.println(strContent);
		}
		myFile.close();
	}

	/**
	 * 删除一个文件.
	 *
	 * @param filePathAndName
	 *            文件完整绝对路径及文件名
	 * @throws java.io.IOException
	 */
	public static void delFile(String filePathAndName) throws IOException {
		String filePath = filePathAndName;
		File myDelFile = new File(filePath);
		if (myDelFile.exists()) {
			myDelFile.delete();
		}
	}


	/**
	 * 删除文件夹.
	 *
	 * @param folderPath
	 *            文件夹路径
	 * @throws java.io.IOException
	 */
	public static void delFolder(String folderPath) throws IOException {
		delAllFile(folderPath); // 删除完里面所有内容
		String filePath = folderPath;
		filePath = filePath.toString();
		File myFilePath = new File(filePath);
		myFilePath.delete(); // 删除空文件夹
	}

	/**
	 * 删除文件夹下所有文件.
	 *
	 * @param path
	 *            文件夹完整路径.
	 * @throws java.io.IOException
	 */
	public static void delAllFile(String path) throws IOException {
		File file = new File(path);
		if (!file.exists() || !file.isDirectory()) {
			return;
		}

		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹

			}
		}
	}

	/**
	 * 拷贝文件.
	 *
	 * @param oldPathFile
	 *            源文件
	 * @param newPathFile
	 *            目标文件
	 * @throws java.io.IOException
	 */
	public static void copyFile(String oldPathFile, String newPathFile)
			throws IOException {
		int bytesum = 0;
		int byteread = 0;
		File oldfile = new File(oldPathFile);
		if (oldfile.exists()) { // 文件存在时
			InputStream inStream = new FileInputStream(oldPathFile); // 读入原文件
			FileOutputStream fs = new FileOutputStream(newPathFile);
			byte[] buffer = new byte[1024];
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread; // 字节数 文件大小
				fs.write(buffer, 0, byteread);
			}
			inStream.close();
		}
	}



	/**
	 * 拷贝文件.
	 *
	 * @param oldFile
	 *            源文件
	 * @param newFile
	 *            目标文件
	 * @throws java.io.IOException
	 */
	public static void copyFile(File oldFile, File newFile) throws IOException {
		if(oldFile==null)
			return ;

		InputStream is = new FileInputStream(oldFile);
		OutputStream os = new FileOutputStream(newFile);
		byte[] buffer = new byte[4000];
		int length = 0;
		while ((length = is.read(buffer)) > 0) {
			os.write(buffer, 0, length);
		}
		is.close();
		os.close();
	}

	public static boolean isHtmlOrZipType(String type){
        List<String> types = Lists.newArrayList();
        types.add("text/html");
        types.add("application/zip");
        return types.contains(type);
    }

	/**
	 * 拷贝文件夹
	 *
	 * @param oldPath
	 * @param newPath
	 * @throws java.io.IOException
	 */
	public static void copyFolder(String oldPath, String newPath)
			throws IOException {
		new File(newPath).mkdirs(); // 如果文件夹不存在 则建立新文件夹
		File a = new File(oldPath);
		String[] file = a.list();
		File temp = null;
		for (int i = 0; i < file.length; i++) {
			if (oldPath.endsWith(File.separator)) {
				temp = new File(oldPath + file[i]);
			} else {
				temp = new File(oldPath + File.separator + file[i]);
			}
			if (temp.isFile()) {
				FileInputStream input = new FileInputStream(temp);
				FileOutputStream output = new FileOutputStream(newPath + "/"
						+ (temp.getName()).toString());
				byte[] b = new byte[1024 * 5];
				int len;
				while ((len = input.read(b)) != -1) {
					output.write(b, 0, len);
				}
				output.flush();
				output.close();
				input.close();
			}
			if (temp.isDirectory()) {// 如果是子文件夹
				copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
			}
		}
	}

	/**
	 * 获取类根路径下的资源流. 实例：如类目录下的某个属性文件 InputStream inputStream =
	 * CommonUtil.getInputStreamBySourceName("pro-config.properties")
	 *
	 * @param sourceName
	 * @return
	 */
	public static InputStream getInputStreamBySourceName(String sourceName) {
		InputStream configFileInputStream = null;
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		configFileInputStream = classLoader.getResourceAsStream(sourceName);
		return configFileInputStream;
	}

	/**
	 *
	 * 获取与类下面的同路径的资源
	 *
	 * @param sourceName
	 * @param aClass
	 * @return
	 */
	public static InputStream getInputStreamBySoureNameInSamePackage(
            String sourceName, Class<?> aClass) {
		InputStream configFileInputStream = null;
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		configFileInputStream = classLoader
				.getResourceAsStream(getPackagePath(aClass) + File.separator
                        + sourceName);
		return configFileInputStream;
	}

	/**
	 * 获取包名
	 *
	 * @param aClass
	 * @return
	 */
	public static String getPackageName(Class<?> aClass) {
		return aClass.getPackage().getName();
	}

	/**
	 * 把包名转换为路径格式
	 *
	 * @param aClass
	 * @return
	 */
	public static String getPackagePath(Class<?> aClass) {
		return getPackageName(aClass).replace('.', File.separatorChar);
	}

	public static void main(String args[]) throws Exception {
		//System.out.println(createFolders("d:\\a\\", "b|c|d"));
		for (int i=0;i<1000000;i++){
			fileChaseFOS("C:\\Users\\Tracy\\Desktop\\visit","abc"+i);
		}
	}

	/**
	 * 文件写入
	 * 文件存在则追加
	 */
	public static synchronized boolean writeFileAppend(String fileName, String strInputContent,
                                                       int writemod, String splitChar) throws IOException {
		if (!new File(fileName).exists()) {
			writeFile(fileName,strInputContent,writemod);
		}else {
			FileWriter fw = new FileWriter(fileName,true);
	        fw.write(splitChar+strInputContent);
	        fw.close();
		}
        return true;
	}

	/**
	 * 获取通道请求的文件路径，该路径为调用端发送请求过来的文件存储路径 若该文件夹不存在，则创建，否则返回
	 *
	 * @return 最后一级日期目录
	 */
	public static String getFolderPath(String type, String date) {
		String path;
		Properties props = System.getProperties();
		String osArch = props.getProperty("os.arch");
		if ("x86".equals(osArch)) {
			path = "c:" + File.separator + "csFiles";
		} else {
			path = System.getProperty("user.dir");
		}
		StringBuilder folderTemp = new StringBuilder();
		folderTemp.append(path);
		folderTemp.append(File.separator);
		folderTemp.append(type);
		folderTemp.append(File.separator);
		folderTemp.append(date);
		folderTemp.append(File.separator);
		String folderPath = folderTemp.toString();
		File file = new File(folderPath);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		return folderPath;
	}

    public static String getFolderPath(String type) {
        String path;
        Properties props = System.getProperties();
        String osArch = props.getProperty("os.arch");
        if ("x86".equals(osArch)) {
            path = "c:" + File.separator + "csFiles";
        } else {
            path = System.getProperty("user.dir");
        }
        StringBuilder folderTemp = new StringBuilder();
        folderTemp.append(path);
        folderTemp.append(File.separator);
        folderTemp.append(type);
        folderTemp.append(File.separator);
        String folderPath = folderTemp.toString();
        File file = new File(folderPath);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        return folderPath;
    }

    /**
     * 追加记录
     * */
    public static  void writerFile(String path, String... reportList){
        File f = new File(path);
        FileWriter fw = null;
        BufferedWriter bw = null;
        try{
            fw = new FileWriter(f, true);
            bw = new BufferedWriter(fw);
            for(int i = 0; i < reportList.length; i++){
                bw.write(reportList[i]);
                bw.newLine();
            }
            bw.flush();
        }catch(Exception ex){
			LOGGER.error("写数据到文件:"+path+"异常=====",ex);
        }finally{
            IOUtils.closeQuietly(bw);
			if (null!=fw){
				try {
					fw.close();
				}catch (Exception e){
				}
			}
        }
    }

	/**
	 * 文本内容追加
	 *
	 * @param filePath
	 * @param reportList
	 */
	public static void fileChaseFOS(String filePath, String... reportList) {
		try {
			//构造函数中的第二个参数true表示以追加形式写文件
			FileOutputStream fos = new FileOutputStream(filePath, true);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
			for(int i = 0; i < reportList.length; i++){
				osw.write(reportList[i]);
				osw.write("\r\n");
			}
			osw.close();
			fos.close();
		} catch (IOException e) {
			LOGGER.error("文件写入失败！" + e);
		}
	}
	public static File createTxtFile(List<Set<String>> dataList, String outPutPath, String filename) {
		File txtFile = null;
		BufferedWriter csvWtriter = null;
		try {
			txtFile = new File(outPutPath + File.separator + filename + ".txt");
			File parent = txtFile.getParentFile();
			if (parent != null && !parent.exists()) {
				parent.mkdirs();
			}
			txtFile.createNewFile();

			// UTF-8
			csvWtriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(txtFile), "utf-8"), 1024);
			// 写入文件内容
			for (Set<String> row : dataList) {
				writeRow(row, csvWtriter);
			}
			csvWtriter.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				csvWtriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return txtFile;
	}
	/**
	 * 写一行数据方法
	 * @param row
	 * @param csvWriter
	 * @throws IOException
	 */
	private static void writeRow(Set<String> row, BufferedWriter csvWriter) throws IOException {
		// 写入文件头部
		for (Object data : row) {
			StringBuffer buf = new StringBuffer();
			String rowStr =  buf.append(data).toString();
			csvWriter.write(rowStr);
		}
		csvWriter.write("\r\n");
	}

	public static List<String> readDirfile(String fileDir, List<String> fileList){
		if(fileList == null){
			fileList = new ArrayList<String>();
		}
		try {
			File file = new File(fileDir);
			if (!file.isDirectory()) {
				fileList.add(file.getAbsolutePath());
			} else if (file.isDirectory()) {
				String[] filelist = file.list();
				for (int i = 0; i < filelist.length; i++) {
					File readfile = new File(fileDir + File.separator + filelist[i]);
					if (!readfile.isDirectory()) {
						fileList.add(readfile.getAbsolutePath());
					} else if (readfile.isDirectory()) {
						readDirfile(fileDir + File.separator + filelist[i], fileList);
					}
				}
			}
		} catch (Exception e) {
			e.getMessage();
		}
		return fileList;
	}

	/**
	 * @Title           getImgeByte
	 * @Description     网络图片转换成二进制字符串
	 * @param URLName   网络图片地址
	 * @param type      图片类型
	 * @return  byte[]  转换结果
	 * @throws
	 */
	public static byte[] getImgeByte(String URLName, String type) {
		byte[] res = null;
		HttpURLConnection httpconn=null;
		try {
			int HttpResult = 0; // 服务器返回的状态
			URL url = new URL(URLName); // 创建URL
			URLConnection urlconn = url.openConnection(); // 试图连接并取得返回状态码
			urlconn.connect();
			httpconn = (HttpURLConnection) urlconn;
			HttpResult = httpconn.getResponseCode();
			if (HttpResult != HttpURLConnection.HTTP_OK) // 不等于HTTP_OK则连接不成功
				LOGGER.info("==无法连接二维码图片网路地址==");
			else {
				BufferedInputStream bis = new BufferedInputStream(urlconn.getInputStream());
				BufferedImage bm = ImageIO.read(bis);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ImageIO.write(bm, type, bos);
				bos.flush();
				res = bos.toByteArray();
				bos.close();
			}
		} catch (Exception e) {
			LOGGER.error("==连接二维码图片网路地址异常==",e);
		}finally {
			if (httpconn != null) {
				httpconn.disconnect();
				httpconn = null;
			}
		}
		return res;
	}



	/**
	 *
	 * 功能描述   保存图片
	 *
	 * @author ：xiaoyu 创建日期 ：2014年2月12日 下午7:37:45
	 *
	 * @param newFileName
	 *            上传照片文件名
	 * @param filedata
	 *           文件数据
	 *
	 *            修改历史 ：(修改人，修改时间，修改原因/内容)
	 */
	public static void saveFile(String newFileName, MultipartFile filedata) {
		// TODO Auto-generated method stub
		// 根据配置文件获取服务器图片存放路径
		String picDir = "";
		picDir = PropertiesUtils.getProperty("savePicUrl");

		if(picDir==null){
			picDir = "/Users/zhangxiahui/java-service/apache-tomcat-6.0.41/bin/PicUrl";
		}
		String saveFilePath = picDir;

        /* 构建文件目录 */
		File fileDir = new File(saveFilePath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}

		try {
			FileOutputStream out = new FileOutputStream(saveFilePath + "\\"
					+ newFileName);
			// 写入文件
			out.write(filedata.getBytes());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 *
	 * 功能描述：删除图片
	 *
	 * @author ： xiaoyu    创建日期 ：2014年2月12日 下午7:36:55
	 *
	 * @param oldPicName
	 *           修改之前的文件名
	 *
	 *         修改历史 ：(修改人，修改时间，修改原因/内容)
	 */
	private void deleteFile(String oldPicName) {
		// TODO Auto-generated method stub
		String picDir = "";
		picDir = PropertiesUtils.getProperty("savePicUrl");

		if(picDir==null){
			picDir = "/Users/zhangxiahui/java-service/apache-tomcat-6.0.41/bin/PicUrl";
		}
        /* 构建文件目录 */
        if(oldPicName!=null&&!"".equals(oldPicName)){
			File fileDir = new File(picDir+"/"+oldPicName);
			if (fileDir.exists()) {
				//把修改之前的图片删除 以免太多没用的图片占据空间
				fileDir.delete();
			}
		}


	}




	/**
	    * 压缩图片方法
	    *
	    * @param oldFile
	    *            将要压缩的图片
	    * @param width
	    *            压缩宽
	    * @param height
	    *            压缩长
	    * @param quality
	    *            压缩清晰度 <b>建议为1.0</b>
	    * @param smallIcon
	    *            压缩图片后,添加的扩展名
	    * @return
	    */
	    /*public static String imageZipProce(String oldFile, int width, int height, float quality) {
	        if (oldFile == null) {
	            return null;
	        }
	        String newImage = null;
	        try {
	            File file = new File(oldFile);
	            //文件不存在时
	            if(!file.exists())return null;
	            *//** 对服务器上的临时文件进行处理 *//*
	            Image srcFile = ImageIO.read(file);
	            int new_w=0,new_h=0;
	            //获取图片的实际大小 高度
	            int h=(int)srcFile.getHeight(null);
	            //获取图片的实际大小 宽度
	            int w=(int)srcFile.getWidth(null);
	            // 为等比缩放计算输出的图片宽度及高度
	            if((((double)w) >(double)width)||(((double)h)>(double) height))
	            {
	                double rate=0;//算出图片比例值
	                //宽度大于等于高度
	                if(w>=h){
	                  rate = ((double) w) / (double) width;
	                }
	                //宽度小于高度
	                else if(h>w) {
	                    rate = ((double) h) / (double) height;
	                }
	                //构造新的比例的图片高度与宽度值
	                new_w = (int) (((double) w) / rate);
	                new_h = (int) (((double) h) / rate);
	                *//** 宽,高设定 *//*
	                BufferedImage tag = new BufferedImage(new_w, new_h,BufferedImage.TYPE_INT_RGB);
	                tag.getGraphics().drawImage(srcFile, 0, 0, new_w, new_h, null);
	                String filePrex = oldFile.substring(0, oldFile.indexOf('.'));
	                *//** 压缩后的文件名 *//*
	                newImage = filePrex + oldFile.substring(filePrex.length());
	                *//** 压缩之后临时存放位置 *//*
	                FileOutputStream out = new FileOutputStream(newImage);
	                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
	                JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);
	                *//** 压缩质量 *//*
	                jep.setQuality(quality, true);
	                encoder.encode(tag, jep);
	                out.close();
	                srcFile.flush();
	            }
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return newImage;
	    }*/




}
