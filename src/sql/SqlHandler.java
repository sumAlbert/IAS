package sql;

/**
 * @author 98267
 */
public interface SqlHandler {

     /**
      * 将对象插入到数据库中
      * @param object 需要插入到数据库的对象
      * @return 是否插入成功
      */
     boolean insertSQL(Object object);

     /**
      * 将对象从数据库中删除
      * @param object 需要从数据库中删除的对象
      * @return 是否删除成功
      */
     boolean deleteSQL(Object object);

     /**
      * 将对象信息更新到数据库中
      * @param object 需要更新的数据库对象
      * @return 是否更新成功
      */
     boolean updateSQL(Object object);

     /**
      * 查询对象信息
      * @param object 需要查询对象信息
      * @return 查询到的信息
      */
     Object selectSQL(Object object);
}
