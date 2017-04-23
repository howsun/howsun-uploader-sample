/**
 * 
 */
package org.howsun.utils;

/**
 * 说明:
 * 
 * @author howsun ->[howsun.zhang@gmail.com]
 * @version 1.0
 *
 * 2017年4月1日 上午11:32:58
 */
public class Files {

	/**
	 * 取扩展名
	 * @param fileName
	 * @return
	 */
	public static String getExtName(String fileName){
		if(fileName != null){
			int index = fileName.indexOf('.');
			if(index > -1){
				return fileName.substring(index - 1);
			}
		}
		return ".unknow";
	}
}
