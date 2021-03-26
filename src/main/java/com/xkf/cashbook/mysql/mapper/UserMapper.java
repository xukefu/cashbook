package com.xkf.cashbook.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xkf.cashbook.mysql.model.UserDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 用户信息
 *
 * @author xukf01
 */
public interface UserMapper extends BaseMapper<UserDO> {

    @Update("update c_user set family_id = #{familyId} , status = #{status} where phone_number = #{familyOwner} and status = 0")
    int updateByPhoneNumber(@Param("familyId") Long familyId, @Param("familyOwner") String familyOwner, @Param("status") Integer status);
}
