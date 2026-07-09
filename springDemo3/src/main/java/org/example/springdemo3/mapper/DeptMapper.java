package org.example.springdemo3.mapper;

import org.apache.ibatis.annotations.*;
import org.example.springdemo3.pojo.Dept;
import org.example.springdemo3.pojo.DeptPageListParams;

import java.util.List;

@Mapper
public interface DeptMapper {
    /**
     * 查询所有部门数据
     * @return
     */
    @Select("select * from dept order by update_time desc")
    @Results({
            @Result(column = "create_time",property = "createTime"),
            @Result(column = "update_time",property = "updateTime")
    })
    List<Dept> findAll();

    @Delete("delete from dept where id = #{id}")
    void delete(Integer id);

    @Select("select * from dept where id = #{id}")
    Dept findById(Integer id);

    @Insert("insert into dept(name,create_time,update_time) values(#{name},#{createTime},#{updateTime})")
    void addDept(Dept dept);

    @Update("update dept set name = #{name},update_time = #{updateTime} where id = #{id}")
    void updateDept(Dept dept);

    /**
     * 分页查询：统计总数
     */
    @Select("select count(*) from dept")
    Integer getDeptCount(DeptPageListParams params);

    /**
     * 分页查询：获取当前页数据
     * 使用 LIMIT offset, size 进行物理分页
     */
    @Select("select id, name, create_time as createTime, update_time as updateTime " +
            "from dept order by update_time desc limit #{offset}, #{size}")
    List<Dept> getDeptListByPage(DeptPageListParams params);
}
