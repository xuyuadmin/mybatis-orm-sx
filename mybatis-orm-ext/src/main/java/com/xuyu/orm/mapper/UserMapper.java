package com.xuyu.orm.mapper;

import com.xuyu.orm.annotation.ExtInsert;
import com.xuyu.orm.annotation.ExtParam;
import com.xuyu.orm.annotation.ExtSelect;
import com.xuyu.orm.user.User;

public interface UserMapper {

	@ExtInsert("insert into user(userName£¬userAge)values(#{userName},#{userAge})")
	public int insertUser(@ExtParam("userName")String userName,@ExtParam("userAge")Integer userAge);

	@ExtSelect("select * from User where userName=#{userName} and userAge=#{userAge} ")
	User selectUser(@ExtParam("userName") String name, @ExtParam("userAge") Integer userAge);

}
