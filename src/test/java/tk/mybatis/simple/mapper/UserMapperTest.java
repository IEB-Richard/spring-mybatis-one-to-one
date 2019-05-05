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
