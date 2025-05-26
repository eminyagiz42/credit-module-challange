package com.eminyagiz.creditmodule.common.mapper;

import com.eminyagiz.creditmodule.model.dto.UserResponse;
import com.eminyagiz.creditmodule.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toUserResponse(User entity);
}
