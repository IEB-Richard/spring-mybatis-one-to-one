# Spring and MyBatis - One-To-One Demo 01

This project demostrate how to combine two tables with ResultMap for one-to-one scenario.



### Key Steps

1. First, define the resultMap in RoleMapper.xml first:

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
					"http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="tk.mybatis.simple.mapper.RoleMapper">
	<resultMap id="roleMap"
		type="tk.mybatis.simple.model.SysRole">
		<id property="id" column="id" />
		<result property="roleName" column="role_name" />
		<result property="enabled" column="enabled" />
		<result property="createBy" column="create_by" />
		<result property="createTime" column="create_time"
			jdbcType="TIMESTAMP" />
	</resultMap>
</mapper>
```

> **Please note:**
>
> Here we only define the resultMap and named it as `roleMap`. we will reuse this resultMap in anotherxml mapper file, UserMapper.xml.



2. Let's define the resultMap in UserMapper.xml.

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
					"http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="tk.mybatis.simple.mapper.UserMapper">

	<resultMap id="userMap" type="SysUser">
		<id property="id" column="id" />
		<result property="userName" column="user_name" />
		<result property="userPassword" column="user_password" />
		<result property="userEmail" column="user_email" />
		<result property="userInfo" column="user_info" />
		<result property="headImg" column="head_img" jdbcType="BLOB" />
		<result property="createTime" column="create_time"
			jdbcType="TIMESTAMP" />
	</resultMap>
	<!-- the roleMap was defined from another designated file RoleMapper.xml -->
	<!-- Here we demo how to use it. -->
	<resultMap id="userRoleMap" extends="userMap" type="SysUser">
		<association property="role" columnPrefix="role_"
			resultMap="tk.mybatis.simple.mapper.RoleMapper.roleMap">
		</association>
	</resultMap>

	<select id="selectUserAndRoleById" resultMap="userRoleMap">
		select
		u.id,
		u.user_name userName,
		u.user_password userPassword,
		u.user_email
		userEmail,
		u.user_info userInfo,
		u.head_img headImg,
		u.create_time
		createTime,
		r.id role_id,
		r.role_name role_role_name,
		r.enabled
		role_enabled,
		r.create_by role_create_by,
		r.create_time role_create_time
		from sys_user u
		inner join sys_user_role ur on u.id =
		ur.user_id
		inner
		join sys_role r on ur.role_id = r.id
		where u.id = #{id}
	</select>

</mapper>
```

> **Please note:**
>
> userRoleMap extends userMap and associate roleMap which is defined in RoleMapper.xml file.

3. Define the mapper interface in file `UserMapper`. Please check on the following code:

   ```Java
   package tk.mybatis.simple.mapper;
   
   import tk.mybatis.simple.model.SysUser;
   
   public interface UserMapper {
   	/**
   	 * Select User and its role by user id
   	 * only demo MyBatis one-to-one association solution
   	 * @param id
   	 * @return
   	 */
   	SysUser selectUserAndRoleById(Long id);
   	
   }
   ```

   

4. then write a test case with Junit5.

   ```java
   package tk.mybatis.simple.mapper;
   
   import org.apache.ibatis.session.SqlSession;
   import org.junit.Assert;
   import org.junit.Test;
   
   import tk.mybatis.simple.model.SysUser;
   
   public class UserMapperTest extends BaseMapperTest {
   
   	@Test
   	public void testSelectUserAndRoleById() {
   		// get sql session
   		SqlSession sqlSession = getSqlSession();
   		try {
   			// get UserMapper object do search action
   			UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
   			SysUser user = userMapper.selectUserAndRoleById(1001l);
   			
   			// Check results
   			Assert.assertNotNull(user);
   		    Assert.assertNotNull(user.getRole());
   
   		} finally {
   			// Please don't forget to close the session after reach test
   			sqlSession.close();
   		}
   	}
   
   }
   ```

   