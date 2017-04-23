/**
 * 
 */
package org.howsun.gridfs;

import java.io.InputStream;
import java.util.Map;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;

/**
 * 说明:
 * 
 * @author howsun ->[howsun.zhang@gmail.com]
 * @version 1.0
 *
 * 2017年4月2日 上午10:19:56
 */
@Service
public class MongoGridFsService {

	@Resource
	private GridFsTemplate gridFsTemplate;
	
	
	/**
	 * 上传到MongoDB
	 * @param content
	 * @param filename
	 * @param contentType
	 * @param metadata
	 * @return
	 */
	public GridFSFile upload(InputStream content, String filename, String contentType, Map<String, Object> metadata){
		//DBObject metadata
		GridFSFile gridFSFile = gridFsTemplate.store(content, filename, contentType, metadata);
		return gridFSFile;
	}
	
	/**
	 * 下载
	 * @param id
	 * @return
	 */
	public GridFSDBFile download(String id){
		Query query = Query.query(Criteria.where("_id").is(new ObjectId(id)));
		GridFSDBFile gridFSDBFile = gridFsTemplate.findOne(query);
		return gridFSDBFile;
	}
}
