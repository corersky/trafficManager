package com.yuandu.erp.common.utils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.yuandu.erp.common.web.Servlets;

/**
 * bootstrap fileinput 文件上传
 * 参数返回
 * @author ivoter
 *
 */
public class FileInptUtil implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private static final String DELETE_FILE_API = "/sys/sysController/deleteFile";

    private List<String> initialPreview = Lists.newArrayList();
	private List<PreviewConfig> initialPreviewConfig = Lists.newArrayList();
	private String path ;//相对路径
	
	public List<String> getInitialPreview() {
		return initialPreview;
	}
	public void setInitialPreview(List<String> initialPreview) {
		this.initialPreview = initialPreview;
	}
	public List<PreviewConfig> getInitialPreviewConfig() {
		return initialPreviewConfig;
	}
	public void setInitialPreviewConfig(List<PreviewConfig> initialPreviewConfig) {
		this.initialPreviewConfig = initialPreviewConfig;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public void addPreview(String path,String img){
		String image = "<img src='"+path+"/"+img+"' class='file-preview-image' alt='"+img+"' title='"+img+"'>";
		initialPreview.add(image);
	}
	
	public void addConfig(String caption,String width,String url,String key){
		PreviewConfig config = new PreviewConfig(caption,width,url,key);
		initialPreviewConfig.add(config);
	}
	
	/**
	 * 公共的ajax文件上传接口
	 * @param file
	 * @param type
	 * @return
	 */
	public static FileInptUtil fileUpload(MultipartFile file, String type) {  
	    
		FileInptUtil result = new FileInptUtil();
		
	    String uploadDirPath = "";//文件上传地址
	    String relativePath = "" ;
	    
	    try {  
	    	FileUtils.createDirectory(FileUtils.path(uploadDirPath));
	    	MultipartFile excel = file;  
	    	
	    	String fileName = FileUtils.getUUIDFileNmae(file.getOriginalFilename());
	    	File destFile = new File(uploadDirPath + "/" + fileName);  
            FileCopyUtils.copy(excel.getBytes(), destFile);  
            
            result.addPreview(uploadDirPath, fileName);
            result.addConfig(file.getOriginalFilename(), "120px", DELETE_FILE_API, fileName);
            
            result.setPath(FileUtils.path(Servlets.getRequest().getContextPath() + relativePath)+fileName);
            
            return result;
	    } catch (IOException e) {  
            e.printStackTrace();  
	    }  
	    return null;
	}
	
}

class PreviewConfig implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String caption;
	private String width;
	private String url;
	private String key;
	
	public PreviewConfig(String caption, String width, String url,
			String key) {
		this.caption = caption;
		this.width = width;
		this.url = url;
		this.key = key;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
}